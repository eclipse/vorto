/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.core.search;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.UnitTestBase;
import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IModelRetrievalService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.impl.InMemoryTemporaryStorage;
import org.eclipse.vorto.repository.core.impl.ModelRepositoryEventListener;
import org.eclipse.vorto.repository.core.impl.ModelRepositoryFactory;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.core.impl.cache.UserRolesRequestCache;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.core.impl.utils.ModelValidationHelper;
import org.eclipse.vorto.repository.core.impl.validation.AttachmentValidator;
import org.eclipse.vorto.repository.domain.*;
import org.eclipse.vorto.repository.importer.Context;
import org.eclipse.vorto.repository.importer.FileUpload;
import org.eclipse.vorto.repository.importer.UploadModelResult;
import org.eclipse.vorto.repository.importer.impl.VortoModelImporter;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.oauth.IOAuthProvider;
import org.eclipse.vorto.repository.oauth.IOAuthProviderRegistry;
import org.eclipse.vorto.repository.repositories.NamespaceRepository;
import org.eclipse.vorto.repository.repositories.UserNamespaceRoleRepository;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.repositories.UserRepositoryRoleRepository;
import org.eclipse.vorto.repository.search.IIndexingService;
import org.eclipse.vorto.repository.search.ISearchService;
import org.eclipse.vorto.repository.search.IndexingEventListener;
import org.eclipse.vorto.repository.search.impl.SimpleSearchService;
import org.eclipse.vorto.repository.services.*;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.utils.RoleProvider;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.impl.DefaultWorkflowService;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.modeshape.jcr.RepositoryConfiguration;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.*;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * This class provides all the infrastructure required to perform tests on the search service. <br/>
 * It bears notable resemblance to {@link UnitTestBase} for
 * good reason: it has been ported from there. <br/> The main difference in the code is that the
 * {@link org.junit.Before} and {@link org.junit.After} annotations are gone. <br/> This is because
 * this class is not intended to be used in an inheritance mechanism (i.e. actual test class
 * inheriting from this one, hence {@link org.junit.Before} and {@link org.junit.After} methods
 * being invoked in a hierarchical relationship) but rather, as a static field of the actual test
 * class. <br/> In other words:
 * <ul>
 *   <li>
 *     The test class creates an instance of {@link SearchTestInfrastructure} statically on
 *     {@link org.junit.BeforeClass}, then imports / modifies the required model from test
 *     resources.
 *   </li>
 *   <li>
 *     All tests must be performed on the same model, i.e. there should be no modification of the
 *     model by {@link org.junit.Test} methods.
 *   </li>
 *   <li>
 *     On {@link org.junit.AfterClass}, {@link SearchTestInfrastructure#terminate()} is invoked, to
 *     shut down the repository.
 *   </li>
 * </ul>
 * The reason for this is <b>performance</b>. <br/>
 * Tests performed in this fashion all leverage the same model, which is usually fine for search. <br/>
 * Tests performing on different models should be simply moved to different test classes, all
 * using the same mechanism. <br/>
 * The performance gain during test, given this mechanism, is in an order of minutes (previous
 * methodology) to hundreds of milliseconds (current methodology) for a sincle test class. <br/>
 * The reason for this increase in performance is that everything formerly happening on
 * {@link org.junit.Before}, i.e. for each test, including model imports, is now only happening
 * <i>once</i> per test class, on {@link org.junit.BeforeClass}.<br/>
 * To reiterate, the limitation of this approach is that the model should be considered as immutable
 * within the same test class, so the single tests are not co-dependent with one another.
 *
 * @author mena-bosch (refactory)
 */
@RunWith(MockitoJUnitRunner.class)
public final class SearchTestInfrastructure {

  /**
   * Used in child classes to test a generated query against an arbitrary number of fragments. <br/>
   * This is because parts of the generated queries are done so by iterating {@link
   * java.util.HashSet}s, so the order of appearance of {@literal OR}-separated elements cannot be
   * inferred consistently. <br/> As there is no functional need to use linked or sorted sets for
   * those queries, this workaround is used when testing them. <br/> Obviously, this makes for a
   * much weaker (and more convoluted) test of the generated query - luckily it only occurs when the
   * query contains multiple {@literal OR}-separated terms with wildcards, contextually to a FTS
   * search (think: {@literal CONTAINS([vorto:someField], '%someValue OR someOtherValue%')}). The
   * latter implies multiple identical tags, at least one of which tags a value with a
   * wildcard.<br/> On the other hand, non-repeated tags will always be generated in a specific
   * order defined arbitrarily in the business logic of the simple search service instead, hence the
   * query string can be tested as-is. <br/>
   * <p>
   * There is no validation of any of the parameters here.
   *
   * @param text
   * @param fragments
   */
  protected static void assertContains(String text, String... fragments) {
    assertTrue(Arrays.stream(fragments).allMatch(text::contains));
  }

  /**
   * File names common for all simple search test classes.
   */
  protected static final String DATATYPE_MODEL = "Color.type";
  protected static final String FUNCTIONBLOCK_MODEL = "Switcher.fbmodel";
  protected static final String INFORMATION_MODEL = "ColorLightIM.infomodel";
  protected static final String MAPPING_MODEL = "Color_ios.mapping";

  @InjectMocks
  protected ModelSearchUtil modelSearchUtil = new ModelSearchUtil();

  @Mock
  protected UserNamespaceRoleRepository userNamespaceRoleRepository;

  @Mock
  protected UserRepositoryRoleRepository userRepositoryRoleRepository;

  @Mock
  protected IOAuthProviderRegistry registry = Mockito.mock(IOAuthProviderRegistry.class);

  @Mock
  protected IOAuthProvider defaultOauthProvider = Mockito.mock(IOAuthProvider.class);

  @Mock
  protected UserRepository userRepository = Mockito.mock(UserRepository.class);

  protected UserRolesRequestCache userRolesRequestCache = new UserRolesRequestCache(
      userNamespaceRoleRepository, userRepositoryRoleRepository, userRepository, registry);

  @Mock
  protected AttachmentValidator attachmentValidator = Mockito
      .mock(AttachmentValidator.class);

  @Mock
  protected INotificationService notificationService = Mockito
      .mock(INotificationService.class);

  protected NamespaceRepository namespaceRepository = Mockito.mock(NamespaceRepository.class);

  NamespaceService namespaceService = Mockito.mock(NamespaceService.class);

  UserNamespaceRoleService userNamespaceRoleService = Mockito.mock(UserNamespaceRoleService.class);

  UserRepositoryRoleService userRepositoryRoleService = Mockito
      .mock(UserRepositoryRoleService.class);

  PrivilegeService privilegeService = Mockito.mock(PrivilegeService.class);

  RoleService roleService = Mockito.mock(RoleService.class);

  RoleUtil roleUtil = Mockito.mock(RoleUtil.class);

  protected DefaultUserAccountService accountService = null;

  protected VortoModelImporter importer = null;

  protected IWorkflowService workflow = null;

  protected ModelParserFactory modelParserFactory = null;

  protected ModelRepositoryFactory repositoryFactory;

  protected IIndexingService indexingService = Mockito.mock(IIndexingService.class);

  protected ModelValidationHelper modelValidationHelper = null;

  protected ISearchService searchService = null;

  private void setupMocking() throws Exception {

    // mocks a IOAuthProviderRegistry with one provider mocking GITHUB
    when(defaultOauthProvider.getId()).thenReturn("GITHUB");
    when(defaultOauthProvider.canHandle(anyString())).thenReturn(true);
    when(defaultOauthProvider.canHandle((Authentication)any())).thenReturn(true);

    when(registry.getByAuthentication(any())).thenReturn(defaultOauthProvider);
    when(registry.list()).thenReturn(Arrays.asList(defaultOauthProvider));

    when(namespaceService.resolveWorkspaceIdForNamespace(anyString()))
        .thenReturn(Optional.of("playground"));
    when(namespaceService.findNamespaceByWorkspaceId(anyString())).thenReturn(mockNamespace());
    when(namespaceRepository.findAll()).thenReturn(Lists.newArrayList(mockNamespace()));

    List<String> workspaceIds = new ArrayList<>();
    workspaceIds.add("playground");
    when(namespaceService.findAllWorkspaceIds()).thenReturn(workspaceIds);
    NamespaceRole namespace_admin = new NamespaceRole();
    namespace_admin.setName("namespace_admin");
    namespace_admin.setPrivileges(7);
    namespace_admin.setRole(32);

    NamespaceRole model_viewer = new NamespaceRole();
    model_viewer.setName("model_viewer");
    model_viewer.setPrivileges(1);
    model_viewer.setRole(1);

    NamespaceRole model_creator = new NamespaceRole();
    model_creator.setName("model_creator");
    model_creator.setPrivileges(3);
    model_creator.setRole(2);

    NamespaceRole model_promoter = new NamespaceRole();
    model_promoter.setName("model_promoter");
    model_promoter.setPrivileges(3);
    model_promoter.setRole(4);

    NamespaceRole model_publisher = new NamespaceRole();
    model_publisher.setName("model_publisher");
    model_publisher.setPrivileges(3);
    model_publisher.setRole(4);

    NamespaceRole model_reviewer = new NamespaceRole();
    model_reviewer.setName("model_reviewer");
    model_reviewer.setPrivileges(3);
    model_reviewer.setRole(8);

    Set<IRole> roles = new HashSet<>();
    roles.add(namespace_admin);
    roles.add(model_viewer);
    roles.add(model_creator);
    roles.add(model_promoter);
    roles.add(model_publisher);
    roles.add(model_reviewer);

    when(userNamespaceRoleService.getRoles(any(UserDto.class), anyString()))
        .thenAnswer(
            inv -> {
              if (((UserDto)inv.getArguments()[0]).getUsername().equals("namespace_admin")) {
                return Sets.newHashSet(namespace_admin);
              }

              if (((UserDto)inv.getArguments()[0]).getUsername().equals("viewer")) {
                return Sets.newHashSet(model_viewer);
              }

              if (((UserDto)inv.getArguments()[0]).getUsername().equals("creator")) {
                return Sets.newHashSet(model_creator);
              }

              if (((UserDto)inv.getArguments()[0]).getUsername().equals("promoter")) {
                return Sets.newHashSet(model_promoter);
              }

              if (((UserDto)inv.getArguments()[0]).getUsername().equals("publisher")) {
                return Sets.newHashSet(model_publisher);
              }

              if (((UserDto)inv.getArguments()[0]).getUsername().equals("reviewer")) {
                return Sets.newHashSet(model_reviewer);
              }

              return Sets
                  .newHashSet(namespace_admin, model_viewer, model_creator, model_promoter, model_publisher,
                      model_reviewer);
            }
        );

    when(userNamespaceRoleService.getRolesByUserAndWorkspaceId(any(UserDto.class), anyString()))
        .thenAnswer(inv -> {
          if (((UserDto)inv.getArguments()[0]).getUsername().equals("namespace_admin")) {
            return Sets.newHashSet(namespace_admin);
          }

          if (((UserDto)inv.getArguments()[0]).getUsername().equals("viewer")) {
            return Sets.newHashSet(model_viewer);
          }

          if (((UserDto)inv.getArguments()[0]).getUsername().equals("creator")) {
            return Sets.newHashSet(model_creator);
          }

          if (((UserDto)inv.getArguments()[0]).getUsername().equals("promoter")) {
            return Sets.newHashSet(model_promoter);
          }

          if (((UserDto)inv.getArguments()[0]).getUsername().equals("publisher")) {
            return Sets.newHashSet(model_publisher);
          }

          if (((UserDto)inv.getArguments()[0]).getUsername().equals("reviewer")) {
            return Sets.newHashSet(model_reviewer);
          }

          return Sets
              .newHashSet(namespace_admin, model_viewer, model_creator, model_promoter,
                  model_publisher,
                  model_reviewer);
        });

    Set<Privilege> privileges = new HashSet<>(Arrays.asList(Privilege.DEFAULT_PRIVILEGES));
    when(privilegeService.getPrivileges(anyLong())).thenReturn(privileges);

    when(roleService.findAnyByName("model_viewer"))
        .thenReturn(Optional.of(new NamespaceRole(1, "model_viewer", 1)));
    when(roleService.findAnyByName("model_creator"))
        .thenReturn(Optional.of(new NamespaceRole(2, "model_creator", 3)));
    when(roleService.findAnyByName("model_promoter"))
        .thenReturn(Optional.of(new NamespaceRole(4, "model_promoter", 3)));
    when(roleService.findAnyByName("model_reviewer"))
        .thenReturn(Optional.of(new NamespaceRole(8, "model_reviewer", 3)));
    when(roleService.findAnyByName("model_publisher"))
        .thenReturn(Optional.of(new NamespaceRole(16, "model_publisher", 3)));
    when(roleService.findAnyByName("namespace_admin"))
        .thenReturn(Optional.of(new NamespaceRole(32, "namespace_admin", 7)));
    when(roleService.findAnyByName("sysadmin")).thenReturn(Optional.of(RepositoryRole.SYS_ADMIN));

    User alex = new UserBuilder().withName("alex").withAuthenticationProviderID("GITHUB").build();
    User erle = new UserBuilder().withName("erle").withAuthenticationProviderID("GITHUB").build();
    User admin = new UserBuilder().withName("admin").withAuthenticationProviderID("GITHUB").build();
    User creator = new UserBuilder().withName("creator").withAuthenticationProviderID("GITHUB").build();
    User promoter = new UserBuilder().withName("promoter").withAuthenticationProviderID("GITHUB").build();
    User reviewer = new UserBuilder().withName("reviewer").withAuthenticationProviderID("GITHUB").build();
    User publisher = new UserBuilder().withName("publisher").withAuthenticationProviderID("GITHUB").build();

    when(userRepository.findByUsernameAndAuthenticationProviderId("alex", alex.getAuthenticationProviderId())).thenReturn(Optional.of(alex));
    when(userRepository.findByUsernameAndAuthenticationProviderId("erle", erle.getAuthenticationProviderId())).thenReturn(Optional.of(erle));
    when(userRepository.findByUsernameAndAuthenticationProviderId("admin", admin.getAuthenticationProviderId())).thenReturn(Optional.of(admin));
    when(userRepository.findByUsernameAndAuthenticationProviderId("creator", creator.getAuthenticationProviderId())).thenReturn(Optional.of(creator));
    when(userRepository.findByUsernameAndAuthenticationProviderId("promoter", promoter.getAuthenticationProviderId())).thenReturn(Optional.of(promoter));
    when(userRepository.findByUsernameAndAuthenticationProviderId("reviewer", reviewer.getAuthenticationProviderId())).thenReturn(Optional.of(reviewer));
    when(userRepository.findByUsernameAndAuthenticationProviderId("publisher", publisher.getAuthenticationProviderId())).thenReturn(Optional.of(publisher));
    when(userRepository.findAll())
        .thenReturn(Lists.newArrayList(alex, erle, admin, creator, promoter, reviewer, publisher));

    when(userNamespaceRoleService.hasRole(any(UserDto.class), anyString(), anyString())).thenReturn(false);
    when(userNamespaceRoleService.hasRole(eq(alex), any(), eq(model_creator))).thenReturn(true);
    when(userNamespaceRoleService.hasRole(eq(alex), any(), eq(model_promoter))).thenReturn(true);
    when(userNamespaceRoleService.hasRole(eq(alex), any(), eq(model_reviewer))).thenReturn(true);

    when(userNamespaceRoleService.hasRole(eq(erle), any(), eq(model_creator))).thenReturn(true);
    when(userNamespaceRoleService.hasRole(eq(erle), any(), eq(model_promoter))).thenReturn(true);
    when(userNamespaceRoleService.hasRole(eq(erle), any(), eq(model_reviewer))).thenReturn(true);
    when(userNamespaceRoleService.hasRole(eq(erle), any(), eq(namespace_admin))).thenReturn(true);

    when(userNamespaceRoleService.hasRole(eq(admin), any(), eq(model_creator))).thenReturn(true);
    when(userNamespaceRoleService.hasRole(eq(admin), any(), eq(model_promoter))).thenReturn(true);
    when(userNamespaceRoleService.hasRole(eq(admin), any(), eq(model_reviewer))).thenReturn(true);
    when(userNamespaceRoleService.hasRole(eq(admin), any(), eq(namespace_admin))).thenReturn(true);

    when(userNamespaceRoleService.hasRole(eq(creator), any(), eq(model_creator))).thenReturn(true);

    when(userNamespaceRoleService.hasRole(eq(promoter), any(), eq(model_promoter)))
        .thenReturn(true);

    when(userNamespaceRoleService.hasRole(eq(reviewer), any(), eq(model_reviewer)))
        .thenReturn(true);

    when(userNamespaceRoleService.hasRole(eq(publisher), any(), eq(model_publisher)))
        .thenReturn(true);

    ModelRepositoryEventListener supervisor = new ModelRepositoryEventListener();
    IndexingEventListener indexingSupervisor = new IndexingEventListener(indexingService);

    Collection<ApplicationListener<AppEvent>> listeners = new ArrayList<>();
    listeners.add(supervisor);
    listeners.add(indexingSupervisor);

    ApplicationEventPublisher eventPublisher = new MockAppEventPublisher(listeners);

    accountService = new DefaultUserAccountService(userRolesRequestCache, userRepository,
        userNamespaceRoleService, registry);
    accountService.setApplicationEventPublisher(eventPublisher);

    modelParserFactory = new ModelParserFactory();
    modelParserFactory.init();

    RepositoryConfiguration config = null;
    config =
        RepositoryConfiguration.read(new ClassPathResource("vorto-repository.json").getPath());

    repositoryFactory = new ModelRepositoryFactory(
        null,
        attachmentValidator,
        modelParserFactory,
        null,
        config,
        null,
        namespaceService,
        userNamespaceRoleService,
        userRepositoryRoleService,
        privilegeService,
        roleService,
        roleUtil,
        registry
    ) {

      @Override
      public IModelRetrievalService getModelRetrievalService() {
        return super.getModelRetrievalService(createUserContext("admin"));
      }

      @Override
      public IModelRepository getRepository(String workspaceId) {
        return super.getRepository(workspaceId);
      }

      @Override
      public IModelRepository getRepository(String workspaceId, Authentication user) {
        return super.getRepository(workspaceId, user);
      }
    };
    repositoryFactory.setApplicationEventPublisher(eventPublisher);
    repositoryFactory.start();

    supervisor.setRepositoryFactory(repositoryFactory);
    modelParserFactory.setModelRepositoryFactory(repositoryFactory);

    searchService = new SimpleSearchService(namespaceRepository, repositoryFactory);
    supervisor.setSearchService(searchService);

    modelValidationHelper = new ModelValidationHelper(repositoryFactory, accountService,
        userRepositoryRoleService, userNamespaceRoleService);

    importer = new VortoModelImporter();
    importer.setUploadStorage(new InMemoryTemporaryStorage());
    importer.setUserAccountService(accountService);
    importer.setModelParserFactory(modelParserFactory);
    importer.setModelRepoFactory(repositoryFactory);
    importer.setModelValidationHelper(modelValidationHelper);

    workflow =
        new DefaultWorkflowService(repositoryFactory, accountService, notificationService,
            namespaceService, userNamespaceRoleService, roleService);

    MockitoAnnotations.initMocks(this);
  }

  private Namespace mockNamespace() {
    Namespace namespace = new Namespace();
    namespace.setName("org.eclipse.vorto");
    namespace.setId(1L);
    namespace.setWorkspaceId("playground");
    return namespace;
  }

  protected SearchTestInfrastructure() throws Exception {
    setupMocking();
  }

  public void terminate() throws Exception {
    repositoryFactory.stop();
  }

  protected IUserContext createUserContext(String username) {
    return createUserContext(username, "playground");
  }

  protected Authentication createAuthenticationToken(String username) {
    if (username.equalsIgnoreCase("admin")) {
      return new TestingAuthenticationToken(username, username, RepositoryRole.SYS_ADMIN.getName());
    }
    Collection<IRole> roles = Sets.newHashSet(RoleProvider.modelReviewer());

    return new TestingAuthenticationToken(username, username,
        roles.stream().map(IRole::getName).toArray(String[]::new));
  }

  /**
   * Creates and returns an instance of {@link IUserContext} with the given username and workspace
   * ID (the latter being the cause for deprecation since it should not be associated with the
   * {@link IUserContext} and be removed as a field of that class in the future).<br/>
   * Before returning, injects the authentication into the
   * {@link org.springframework.security.core.context.SecurityContextHolder} so that the
   * {@link IModelRepositoryFactory} functionality in charge of inferring the authentication when
   * not provided can retrieve it.
   * @param username
   * @param workspaceId
   * @return
   */
  @Deprecated
  protected IUserContext createUserContext(String username, String workspaceId) {
    Authentication authentication = createAuthenticationToken(username);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return UserContext.user(authentication, workspaceId);
  }

  protected ModelInfo importModel(String user, String modelName) {
    return importModel(modelName, createUserContext(user, "playground"));
  }

  protected ModelInfo importModel(String modelName) {
    return importModel(modelName, createUserContext(getCallerId(), "playground"));
  }

  protected ModelInfo importModel(String modelName, IUserContext userContext) {
    try {
      UploadModelResult uploadResult = importer.upload(
          FileUpload.create(modelName,
              IOUtils.toByteArray(
                  new ClassPathResource("sample_models/" + modelName).getInputStream())),
          Context.create(userContext, Optional.empty()));
      return importer
          .doImport(uploadResult.getHandleId(), Context.create(userContext, Optional.empty()))
          .get(0);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    } finally {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  protected String getCallerId() {
    return "alex";
  }

  protected class MockAppEventPublisher implements ApplicationEventPublisher {

    private Collection<ApplicationListener<AppEvent>> listeners;

    public MockAppEventPublisher(Collection<ApplicationListener<AppEvent>> listeners) {
      this.listeners = listeners;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
      if (event instanceof AppEvent) {
        AppEvent appEvent = (AppEvent) event;
        for (ApplicationListener<AppEvent> listener : listeners) {
          listener.onApplicationEvent(appEvent);
        }
      }
    }

    @Override
    public void publishEvent(Object event) {
      // implement when need arises
    }
  }

  /*
  Simple accessors for test classes below.
   */

  public ISearchService getSearchService() {
    return searchService;
  }

  public ModelRepositoryFactory getRepositoryFactory() {
    return repositoryFactory;
  }
}
