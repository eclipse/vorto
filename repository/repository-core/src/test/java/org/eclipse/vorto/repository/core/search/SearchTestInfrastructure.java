/**
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 * <p>
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 * <p>
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.core.search;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRetrievalService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.impl.InMemoryTemporaryStorage;
import org.eclipse.vorto.repository.core.impl.ModelRepositoryEventListener;
import org.eclipse.vorto.repository.core.impl.ModelRepositoryFactory;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.core.impl.parser.ErrorMessageProvider;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.core.impl.utils.ModelValidationHelper;
import org.eclipse.vorto.repository.core.impl.validation.AttachmentValidator;
import org.eclipse.vorto.repository.domain.AuthenticationProvider;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.TenantUser;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserRole;
import org.eclipse.vorto.repository.importer.Context;
import org.eclipse.vorto.repository.importer.FileUpload;
import org.eclipse.vorto.repository.importer.UploadModelResult;
import org.eclipse.vorto.repository.importer.impl.VortoModelImporter;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.search.IIndexingService;
import org.eclipse.vorto.repository.search.ISearchService;
import org.eclipse.vorto.repository.search.IndexingEventListener;
import org.eclipse.vorto.repository.search.impl.SimpleSearchService;
import org.eclipse.vorto.repository.tenant.TenantService;
import org.eclipse.vorto.repository.tenant.TenantUserService;
import org.eclipse.vorto.repository.tenant.repository.ITenantRepository;
import org.eclipse.vorto.repository.tenant.repository.ITenantUserRepo;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.impl.DefaultWorkflowService;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modeshape.jcr.RepositoryConfiguration;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * This class provides all the infrastructure required to perform tests on the search service. <br/>
 * It bears notable resemblance to {@link org.eclipse.vorto.repository.AbstractIntegrationTest} for
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
  protected final String DATATYPE_MODEL = "Color.type";
  protected final String FUNCTIONBLOCK_MODEL = "Switcher.fbmodel";
  protected final String INFORMATION_MODEL = "ColorLightIM.infomodel";
  protected final String MAPPING_MODEL = "Color_ios.mapping";

  @InjectMocks
  protected ModelSearchUtil modelSearchUtil = new ModelSearchUtil();

  @Mock
  protected IUserRepository userRepository = Mockito.mock(IUserRepository.class);

  @Mock
  protected AttachmentValidator attachmentValidator = Mockito
      .mock(AttachmentValidator.class);

  @Mock
  protected INotificationService notificationService = Mockito
      .mock(INotificationService.class);

  protected DefaultUserAccountService accountService = null;

  protected IModelPolicyManager policyManager;

  protected VortoModelImporter importer = null;

  protected IWorkflowService workflow = null;

  protected ModelParserFactory modelParserFactory = null;

  protected ModelRepositoryFactory repositoryFactory;

  protected TenantService tenantService = Mockito.mock(TenantService.class);

  protected IIndexingService indexingService = Mockito.mock(IIndexingService.class);

  protected TenantUserService tenantUserService = null;

  protected ModelValidationHelper modelValidationHelper = null;

  protected ErrorMessageProvider errorMessageProvider = new ErrorMessageProvider();

  private ITenantRepository tenantRepo = Mockito.mock(ITenantRepository.class);

  private Tenant playgroundTenant = playgroundTenant();

  protected ISearchService searchService = null;

  protected Tenant playgroundTenant() {
    UserRole roleUser = new UserRole(Role.USER);
    UserRole roleCreator = new UserRole(Role.MODEL_CREATOR);
    UserRole rolePromoter = new UserRole(Role.MODEL_PROMOTER);
    UserRole roleReviewer = new UserRole(Role.MODEL_REVIEWER);
    UserRole rolePublisher = new UserRole(Role.MODEL_PUBLISHER);
    UserRole roleTenantAdmin = new UserRole(Role.TENANT_ADMIN);
    UserRole roleSysAdmin = new UserRole(Role.SYS_ADMIN);

    Tenant playground = Tenant.newTenant("playground", "org.eclipse",
        Sets.newHashSet("org.eclipse", "com.mycompany", "com.ipso", "examples.mappings",
            "vorto.private.playground"));

    playground.addUser(createTenantUser("alex",
        Sets.newHashSet(roleUser, roleCreator, rolePromoter, roleReviewer)));
    playground.addUser(createTenantUser("erle",
        Sets.newHashSet(roleUser, roleCreator, rolePromoter, roleReviewer, roleTenantAdmin)));
    playground.addUser(createTenantUser("admin",
        Sets.newHashSet(roleUser, roleCreator, rolePromoter, roleReviewer, roleSysAdmin)));
    playground.addUser(createTenantUser("creator", Sets.newHashSet(roleUser, roleCreator)));
    playground.addUser(createTenantUser("promoter", Sets.newHashSet(roleUser, rolePromoter)));
    playground.addUser(createTenantUser("reviewer", Sets.newHashSet(roleUser, roleReviewer)));
    playground.addUser(createTenantUser("publisher", Sets.newHashSet(roleUser, rolePublisher)));

    return playground;
  }

  private TenantUser createTenantUser(String name, Set<UserRole> roles) {
    User _user = User.create(name, AuthenticationProvider.GITHUB.name(), null);
    TenantUser user = new TenantUser();
    user.setRoles(roles);
    _user.addTenantUser(user);
    return user;
  }

  protected SearchTestInfrastructure() throws Exception {
    when(tenantService.getTenantFromNamespace(Matchers.anyString()))
        .thenReturn(Optional.of(playgroundTenant));

    when(userRepository.findByUsername("alex")).thenReturn(getUser("alex", playgroundTenant));

    when(userRepository.findByUsername("erle")).thenReturn(getUser("erle", playgroundTenant));

    when(userRepository.findByUsername("admin")).thenReturn(getUser("admin", playgroundTenant));

    when(userRepository.findByUsername("creator")).thenReturn(getUser("creator", playgroundTenant));

    when(userRepository.findByUsername("promoter"))
        .thenReturn(getUser("promoter", playgroundTenant));

    when(userRepository.findByUsername("reviewer"))
        .thenReturn(getUser("reviewer", playgroundTenant));

    when(userRepository.findAll()).thenReturn(Lists.newArrayList(getUser("admin", playgroundTenant),
        getUser("erle", playgroundTenant), getUser("alex", playgroundTenant),
        getUser("creator", playgroundTenant), getUser("reviewer", playgroundTenant),
        getUser("promoter", playgroundTenant)));

    when(tenantService.getTenant("playground")).thenReturn(Optional.of(playgroundTenant));
    when(tenantService.getTenants()).thenReturn(Lists.newArrayList(playgroundTenant));
    when(tenantRepo.findByTenantId("playground")).thenReturn(playgroundTenant);
    when(tenantRepo.findAll()).thenReturn(Lists.newArrayList(playgroundTenant));

    ModelRepositoryEventListener supervisor = new ModelRepositoryEventListener();
    IndexingEventListener indexingSupervisor = new IndexingEventListener(indexingService);

    Collection<ApplicationListener<AppEvent>> listeners = new ArrayList<>();
    listeners.add(supervisor);
    listeners.add(indexingSupervisor);

    ApplicationEventPublisher eventPublisher = new MockAppEventPublisher(listeners);

    accountService = new DefaultUserAccountService();
    accountService.setNotificationService(notificationService);
    accountService.setUserRepository(userRepository);
    accountService.setApplicationEventPublisher(eventPublisher);
    accountService.setTenantUserRepo(Mockito.mock(ITenantUserRepo.class));
    accountService.setTenantRepo(tenantRepo);

    modelParserFactory = new ModelParserFactory();
    modelParserFactory.init();

    RepositoryConfiguration config = null;
    config =
        RepositoryConfiguration.read(new ClassPathResource("vorto-repository.json").getPath());

    repositoryFactory = new ModelRepositoryFactory(accountService, modelSearchUtil,
        attachmentValidator, modelParserFactory, null, config, tenantService) {

      @Override
      public IModelRetrievalService getModelRetrievalService() {
        return super.getModelRetrievalService(createUserContext("admin"));
      }

      @Override
      public IModelRepository getRepository(String tenantId) {
        return super.getRepository(createUserContext("admin", tenantId));
      }

      @Override
      public IModelRepository getRepository(String tenant, Authentication user) {
        if (user == null) {
          return getRepository(tenant);
        }
        return super.getRepository(tenant, user);
      }
    };
    repositoryFactory.setApplicationEventPublisher(eventPublisher);
    repositoryFactory.start();

    supervisor.setRepositoryFactory(repositoryFactory);
    modelParserFactory.setModelRepositoryFactory(repositoryFactory);

    tenantUserService = new TenantUserService(tenantService, accountService);

    searchService = new SimpleSearchService(tenantService, repositoryFactory);
    supervisor.setSearchService(searchService);

    modelValidationHelper = new ModelValidationHelper(repositoryFactory, accountService,
        tenantService);

    importer = new VortoModelImporter();
    importer.setUploadStorage(new InMemoryTemporaryStorage());
    importer.setUserRepository(accountService);
    importer.setModelParserFactory(modelParserFactory);
    importer.setModelRepoFactory(repositoryFactory);
    importer.setModelValidationHelper(modelValidationHelper);

    workflow =
        new DefaultWorkflowService(repositoryFactory, accountService, notificationService);

    MockitoAnnotations.initMocks(SearchTestInfrastructure.class);
  }

  public void terminate() throws Exception {
    repositoryFactory.stop();
  }

  private User getUser(String userId, Tenant tenant) {
    return tenant.getUsers().stream().map(TenantUser::getUser)
        .filter(u -> u.getUsername().equals(userId)).findAny().get();
  }

  protected IUserContext createUserContext(String username) {
    return createUserContext(username, "playground");
  }

  protected Authentication createAuthenticationToken(String username) {
    Set<String> userRoles = getTenantUser(username, playgroundTenant).getRoles().stream()
        .map(uRole -> Role.rolePrefix + uRole.getRole().name()).collect(Collectors.toSet());

    Authentication auth = new TestingAuthenticationToken(username, username,
        userRoles.toArray(new String[userRoles.size()]));

    return auth;
  }

  private TenantUser getTenantUser(String userId, Tenant tenant) {
    return tenant.getUsers().stream().filter(tu -> tu.getUser().getUsername().equals(userId))
        .findAny().get();
  }

  protected IUserContext createUserContext(String username, String tenantId) {
    return UserContext.user(createAuthenticationToken(username), tenantId);
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
