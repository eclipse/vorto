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
package org.eclipse.vorto.repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.core.*;
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
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.search.IIndexingService;
import org.eclipse.vorto.repository.search.ISearchService;
import org.eclipse.vorto.repository.search.IndexingEventListener;
import org.eclipse.vorto.repository.search.impl.SimpleSearchService;
import org.eclipse.vorto.repository.services.*;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.eclipse.vorto.repository.utils.MockAppEventPublisher;
import org.eclipse.vorto.repository.utils.NamespaceProvider;
import org.eclipse.vorto.repository.utils.RoleProvider;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.eclipse.vorto.repository.workflow.impl.DefaultWorkflowService;
import org.eclipse.vorto.repository.workflow.impl.SimpleWorkflowModel;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.modeshape.jcr.RepositoryConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.*;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public abstract class UnitTestBase {

  @InjectMocks
  protected ModelSearchUtil modelSearchUtil = new ModelSearchUtil();

  @InjectMocks
  protected UserRolesRequestCache userRolesRequestCache;

  @Mock
  protected UserUtil userUtil;

  @Mock
  protected UserRepository userRepository;

  @Mock
  protected AttachmentValidator attachmentValidator;

  @Mock
  protected INotificationService notificationService;

  @Mock
  protected NamespaceRepository namespaceRepository;

  @Mock
  protected NamespaceService namespaceService;

  @Mock
  protected UserNamespaceRoleService userNamespaceRoleService;

  @Mock
  protected UserRepositoryRoleService userRepositoryRoleService;

  @Mock
  protected PrivilegeService privilegeService;

  @Mock
  protected RoleService roleService;

  @Mock
  protected IOAuthProviderRegistry registry = Mockito.mock(IOAuthProviderRegistry.class);;

  @Mock
  protected IOAuthProvider defaultOauthProvider = Mockito.mock(IOAuthProvider.class);

  @Mock
  protected RoleUtil roleUtil;

  @Mock
  protected IIndexingService indexingService;

  protected DefaultUserAccountService accountService = null;

  protected UserService userService = null;

  protected VortoModelImporter importer = null;

  protected IWorkflowService workflow = null;

  protected ModelParserFactory modelParserFactory = null;

  protected ModelRepositoryFactory repositoryFactory;

  protected ModelValidationHelper modelValidationHelper = null;

  protected ISearchService searchService = null;

  @Before
  public void setup() throws Exception {
    NamespaceRole namespace_admin = RoleProvider.namespaceAdmin();
    NamespaceRole model_viewer = RoleProvider.modelViewer();
    NamespaceRole model_creator = RoleProvider.modelCreator();
    NamespaceRole model_promoter = RoleProvider.modelPromoter();
    NamespaceRole model_publisher = RoleProvider.modelPublisher();
    NamespaceRole model_reviewer = RoleProvider.modelReviewer();
    mockNamespaceService();
    mockNamespaceRolesAndPrivileges(namespace_admin, model_viewer, model_creator, model_promoter,
        model_publisher, model_reviewer);
    mockRoles();
    mockUsers(namespace_admin, model_creator, model_promoter, model_publisher, model_reviewer);
    mockNamespaceRepository();
    mockServices();
  }

  @After
  public void after() {
    repositoryFactory.stop();
  }

  protected void mockNamespaceRepository() {
    Namespace n = new Namespace();
    n.setName("com.mycompany");
    n.setWorkspaceId("playground");
    when(namespaceRepository.findAll()).thenReturn(Lists.newArrayList(n));
    when(namespaceRepository.findByName("com.mycompany")).thenReturn(n);
  }

  protected void mockNamespaceService() {
    when(namespaceService.resolveWorkspaceIdForNamespace(anyString()))
        .thenReturn(Optional.of("playground"));
    when(namespaceService.findNamespaceByWorkspaceId(anyString()))
        .thenReturn(NamespaceProvider.mockEclipseNamespace());

    List<String> workspaceIds = new ArrayList<>();
    workspaceIds.add("playground");
    when(namespaceService.findAllWorkspaceIds()).thenReturn(workspaceIds);
  }

  protected void mockServices() throws Exception {
    // mocks a IOAuthProviderRegistry with one provider mocking GITHUB
    when(defaultOauthProvider.getId()).thenReturn("GITHUB");
    when(defaultOauthProvider.canHandle(anyString())).thenReturn(true);
    when(defaultOauthProvider.canHandle(any(Authentication.class))).thenReturn(true);

    when(registry.getByAuthentication(any())).thenReturn(defaultOauthProvider);
    when(registry.list()).thenReturn(Arrays.asList(defaultOauthProvider));

    ModelRepositoryEventListener supervisor = new ModelRepositoryEventListener();
    IndexingEventListener indexingSupervisor = new IndexingEventListener(indexingService);

    Collection<ApplicationListener<AppEvent>> listeners = new ArrayList<>();
    listeners.add(supervisor);
    listeners.add(indexingSupervisor);

    ApplicationEventPublisher eventPublisher = new MockAppEventPublisher(listeners);

    mockAccountService(eventPublisher);
    mockModelParserFactory();
    mockModelRepositoryFactory(eventPublisher);
    searchService = new SimpleSearchService(namespaceRepository, repositoryFactory);
    mockUserService(eventPublisher);

    supervisor.setRepositoryFactory(repositoryFactory);
    modelParserFactory.setModelRepositoryFactory(repositoryFactory);
    supervisor.setSearchService(searchService);
    this.modelValidationHelper = new ModelValidationHelper(repositoryFactory, this.accountService,
        userRepositoryRoleService, userNamespaceRoleService);
    mockImporter(repositoryFactory);
    this.workflow = new DefaultWorkflowService(repositoryFactory, accountService,
        notificationService, namespaceService, userNamespaceRoleService, roleService);
  }

  private void mockModelParserFactory() {
    modelParserFactory = new ModelParserFactory();
    modelParserFactory.init();
  }

  private void mockModelRepositoryFactory(ApplicationEventPublisher eventPublisher)
      throws Exception {
    RepositoryConfiguration config = RepositoryConfiguration
        .read(new ClassPathResource("vorto-repository.json").getPath());

    when(
        userRepositoryRoleService.isSysadmin(
          UserDto.of("admin", "GITHUB")
        )
    )
    .thenReturn(true);

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
            return super.getRepository(workspaceId, createUserContext("admin"));
          }

          @Override
          public IModelRepository getRepository(String workspaceId, Authentication user) {
            return super.getRepository(workspaceId, user);
          }

          @Override
          public IModelRepository getRepositoryWithoutSessionHelper(String workspaceId) {
            return getRepository(workspaceId);
          }
        };
    repositoryFactory.setApplicationEventPublisher(eventPublisher);
    repositoryFactory.start();
  }

  protected void mockAccountService(ApplicationEventPublisher eventPublisher) {
    accountService = new DefaultUserAccountService(userRolesRequestCache, userRepository,
        userNamespaceRoleService, registry);
    accountService.setApplicationEventPublisher(eventPublisher);
  }

  protected void mockUserService(ApplicationEventPublisher eventPublisher) {
    userService = new UserService(userRolesRequestCache, userUtil, userRepository, repositoryFactory,
        userNamespaceRoleService, notificationService, searchService);
    userService.setApplicationEventPublisher(eventPublisher);
  }

  protected void mockImporter(ModelRepositoryFactory modelRepositoryFactory) {
    this.importer = new VortoModelImporter();
    this.importer.setUploadStorage(new InMemoryTemporaryStorage());
    this.importer.setUserAccountService(this.accountService);
    this.importer.setModelParserFactory(modelParserFactory);
    this.importer.setModelRepoFactory(modelRepositoryFactory);
    this.importer.setModelValidationHelper(modelValidationHelper);
    this.importer.setUserNamespaceRoleService(userNamespaceRoleService);
    this.importer.setUserRepositoryRoleService(userRepositoryRoleService);
  }

  private void mockNamespaceRolesAndPrivileges(NamespaceRole namespace_admin,
      NamespaceRole model_viewer, NamespaceRole model_creator, NamespaceRole model_promoter,
      NamespaceRole model_publisher, NamespaceRole model_reviewer) throws DoesNotExistException {
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
                  .newHashSet(namespace_admin, model_viewer, model_creator, model_promoter,
                      model_publisher,
                      model_reviewer);
            }
    );

    Set<Privilege> privileges = new HashSet<>(Arrays.asList(Privilege.DEFAULT_PRIVILEGES));
    when(privilegeService.getPrivileges(anyLong())).thenReturn(privileges);
  }

  private void mockUsers(NamespaceRole namespace_admin, NamespaceRole model_creator,
      NamespaceRole model_promoter, NamespaceRole model_publisher, NamespaceRole model_reviewer)
      throws DoesNotExistException, OperationForbiddenException, InvalidUserException {
    User alex = new UserBuilder().withID(1).withName("alex").withAuthenticationProviderID("GITHUB").build();
    User erle = new UserBuilder().withName("erle").withAuthenticationProviderID("GITHUB").build();
    User admin = new UserBuilder().withName("admin").withAuthenticationProviderID("GITHUB").build();
    User creator = new UserBuilder().withName("creator").withAuthenticationProviderID("GITHUB").build();
    User promoter = new UserBuilder().withName("promoter").withAuthenticationProviderID("GITHUB").build();
    User reviewer = new UserBuilder().withName("reviewer").withAuthenticationProviderID("GITHUB").build();
    User publisher = new UserBuilder().withName("publisher").withAuthenticationProviderID("GITHUB").build();

    mockUserRepository(alex, erle, admin, creator, promoter, reviewer, publisher);
    mockUserNamespaceRoleService(namespace_admin, model_creator, model_promoter, model_publisher,
        model_reviewer, alex, erle, admin, creator, promoter, reviewer, publisher);
  }

  private void mockUserNamespaceRoleService(NamespaceRole namespace_admin,
      NamespaceRole model_creator, NamespaceRole model_promoter, NamespaceRole model_publisher,
      NamespaceRole model_reviewer, User alex, User erle, User admin, User creator, User promoter,
      User reviewer, User publisher) throws DoesNotExistException, OperationForbiddenException {

    when(userNamespaceRoleService.hasRole(any(UserDto.class), anyString(), anyString()))
        .thenReturn(false);

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

    when(userRepositoryRoleService.isSysadmin(any(User.class))).thenReturn(false);
    when(userRepositoryRoleService.isSysadmin(admin)).thenReturn(true);
    when(userRepositoryRoleService.isSysadmin(
        UserDto.of(admin.getUsername(), "GITHUB"))
    )
        .thenReturn(true);

    when(userNamespaceRoleService.getNamespaces(any(User.class), any(User.class)))
        .thenReturn(Sets.newHashSet(
            NamespaceProvider.mockEclipseNamespace(), NamespaceProvider.mockComNamespace()));
  }

  private void mockUserRepository(User alex, User erle, User admin, User creator, User promoter,
      User reviewer, User publisher) {
    when(userRepository.findByUsernameAndAuthenticationProviderId("alex", alex.getAuthenticationProviderId())).thenReturn(Optional.of(alex));
    when(userRepository.exists(alex.getId())).thenReturn(true);
    when(userRepository.findByUsernameAndAuthenticationProviderId("erle", erle.getAuthenticationProviderId())).thenReturn(Optional.of(erle));
    when(userRepository.exists(erle.getId())).thenReturn(true);
    when(userRepository.findByUsernameAndAuthenticationProviderId("admin", admin.getAuthenticationProviderId())).thenReturn(Optional.of(admin));
    when(userRepository.exists(admin.getId())).thenReturn(true);
    when(userRepository.findByUsernameAndAuthenticationProviderId("creator", creator.getAuthenticationProviderId())).thenReturn(Optional.of(creator));
    when(userRepository.exists(creator.getId())).thenReturn(true);
    when(userRepository.findByUsernameAndAuthenticationProviderId("promoter", promoter.getAuthenticationProviderId())).thenReturn(Optional.of(promoter));
    when(userRepository.exists(promoter.getId())).thenReturn(true);
    when(userRepository.findByUsernameAndAuthenticationProviderId("reviewer", reviewer.getAuthenticationProviderId())).thenReturn(Optional.of(reviewer));
    when(userRepository.exists(reviewer.getId())).thenReturn(true);
    when(userRepository.findByUsernameAndAuthenticationProviderId("publisher", publisher.getAuthenticationProviderId())).thenReturn(Optional.of(publisher));
    when(userRepository.exists(publisher.getId())).thenReturn(true);
    when(userRepository.findAll())
        .thenReturn(Lists.newArrayList(alex, erle, admin, creator, promoter, reviewer, publisher));
  }

  private void mockRoles() {
    when(roleService.findAnyByName("model_viewer"))
        .thenReturn(Optional.of(RoleProvider.modelViewer()));
    when(roleService.findAnyByName("model_creator"))
        .thenReturn(Optional.of(RoleProvider.modelCreator()));
    when(roleService.findAnyByName("model_promoter"))
        .thenReturn(Optional.of(RoleProvider.modelPromoter()));
    when(roleService.findAnyByName("model_reviewer"))
        .thenReturn(Optional.of(RoleProvider.modelReviewer()));
    when(roleService.findAnyByName("model_publisher"))
        .thenReturn(Optional.of(RoleProvider.modelPublisher()));
    when(roleService.findAnyByName("namespace_admin"))
        .thenReturn(Optional.of(RoleProvider.namespaceAdmin()));
    when(roleService.findAnyByName("sysadmin")).thenReturn(Optional.of(RepositoryRole.SYS_ADMIN));
  }

  protected ModelInfo importModel(String user, String modelName) {
    return importModel(modelName, createUserContext(user, "playground"));
  }

  protected ModelInfo importModel(String modelName) {
    return importModel(modelName, createUserContext(getCallerId(), "playground"));
  }

  protected String getCallerId() {
    return "alex";
  }

  protected ModelInfo importModel(String modelName, IUserContext userContext) {
    try {
      UploadModelResult uploadResult = this.importer.upload(
          FileUpload.create(modelName,
              IOUtils.toByteArray(
                  new ClassPathResource("sample_models/" + modelName).getInputStream())),
          Context.create(userContext, Optional.empty()));
      return this.importer
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

  protected void releaseModel(ModelId modelId, IUserContext creator) throws WorkflowException {
    workflow.start(modelId, creator);
    workflow.doAction(modelId, createUserContext("promoter"),
        SimpleWorkflowModel.ACTION_RELEASE.getName());
    workflow.doAction(modelId, createUserContext("reviewer"),
        SimpleWorkflowModel.ACTION_APPROVE.getName());
  }

  protected void setReleaseState(ModelInfo model) throws WorkflowException {
    User mockUser = null;
    User mockAdmin = null;
    try {
      mockUser = new UserBuilder()
          .withName(getCallerId())
          .withAuthenticationProviderID("GITHUB").build();
      mockAdmin = new UserBuilder()
          .withName("admin")
          .withAuthenticationProviderID("GITHUB")
          .build();
    } catch (InvalidUserException iue) {
      fail(iue.getMessage());
    }
    when(
        userRepository.findByUsernameAndAuthenticationProviderId(createUserContext(getCallerId(), "playground").getUsername(), "GITHUB"))
        .thenReturn(Optional.of(mockUser));
    workflow.doAction(model.getId(), createUserContext(getCallerId(), "playground"),
        SimpleWorkflowModel.ACTION_RELEASE.getName());
    when(userRepository.findByUsernameAndAuthenticationProviderId(createUserContext("admin", "playground").getUsername(), "GITHUB"))
        .thenReturn(Optional.of(mockAdmin));
    workflow.doAction(model.getId(), createUserContext("admin", "playground"),
        SimpleWorkflowModel.ACTION_APPROVE.getName());
  }

  protected IModelRepository getModelRepository(IUserContext userContext) {
    return repositoryFactory.getRepository(userContext.getWorkspaceId(), userContext);
  }

  protected IRepositoryManager getRepoManager(IUserContext userContext) {
    return repositoryFactory
        .getRepositoryManager(userContext.getWorkspaceId());
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
   * @param workspaceID
   * @return
   */
  @Deprecated
  protected IUserContext createUserContext(String username, String workspaceID) {
    Authentication authentication = createAuthenticationToken(username);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return UserContext.user(authentication, workspaceID);
  }

}
