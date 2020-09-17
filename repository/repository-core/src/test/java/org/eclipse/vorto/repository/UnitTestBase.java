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
import org.eclipse.vorto.repository.core.impl.cache.UserNamespaceRolesCache;
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
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.eclipse.vorto.repository.workflow.impl.DefaultWorkflowService;
import org.eclipse.vorto.repository.workflow.impl.SimpleWorkflowModel;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modeshape.jcr.RepositoryConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.*;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public abstract class UnitTestBase {

  @InjectMocks
  protected ModelSearchUtil modelSearchUtil = new ModelSearchUtil();

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
  protected IIndexingService indexingService;

  @Mock
  protected UserNamespaceRolesCache userNamespaceRolesCache;

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
    ModelRepositoryEventListener supervisor = new ModelRepositoryEventListener();
    IndexingEventListener indexingSupervisor = new IndexingEventListener(indexingService);

    Collection<ApplicationListener<AppEvent>> listeners = new ArrayList<>();
    listeners.add(supervisor);
    listeners.add(indexingSupervisor);

    ApplicationEventPublisher eventPublisher = new MockAppEventPublisher(listeners);

    mockAccountService(eventPublisher);
    mockUserService(eventPublisher);
    mockModelParserFactory();
    mockModelRepositoryFactory(eventPublisher);

    supervisor.setRepositoryFactory(repositoryFactory);
    modelParserFactory.setModelRepositoryFactory(repositoryFactory);
    searchService = new SimpleSearchService(namespaceRepository, repositoryFactory);
    supervisor.setSearchService(searchService);
    this.modelValidationHelper = new ModelValidationHelper(repositoryFactory, this.accountService,
        userRepositoryRoleService, userNamespaceRoleService);
    mockImporter(repositoryFactory);
    this.workflow = new DefaultWorkflowService(repositoryFactory, accountService,
        notificationService, namespaceService, userNamespaceRoleService, roleService);
    mockUserNamespaceRolesCache();
  }

  private void mockModelParserFactory() {
    modelParserFactory = new ModelParserFactory();
    modelParserFactory.init();
  }

  private void mockModelRepositoryFactory(ApplicationEventPublisher eventPublisher)
      throws Exception {
    RepositoryConfiguration config = RepositoryConfiguration
        .read(new ClassPathResource("vorto-repository.json").getPath());

    when(userRepositoryRoleService.isSysadmin("admin")).thenReturn(true);

    repositoryFactory =
        new ModelRepositoryFactory(modelSearchUtil,
            attachmentValidator, modelParserFactory, null, config, null, namespaceService,
            userNamespaceRoleService, privilegeService, userRepositoryRoleService,
            userNamespaceRolesCache, userRepository) {

          @Override
          public IModelRetrievalService getModelRetrievalService() {
            return super.getModelRetrievalService(createUserContext("admin"));
          }

          @Override
          public IModelRepository getRepository(String workspaceId) {
            return super.getRepository(createUserContext("admin", workspaceId));
          }

          @Override
          public IModelRepository getRepository(String workspaceId, Authentication user) {
            if (user == null) {
              return getRepository(workspaceId);
            }
            return super.getRepository(workspaceId, user);
          }
        };
    repositoryFactory.setApplicationEventPublisher(eventPublisher);
    repositoryFactory.start();
  }

  protected void mockAccountService(ApplicationEventPublisher eventPublisher) {
    accountService = new DefaultUserAccountService(userRepository, notificationService, roleService,
        userNamespaceRoleService);
    accountService.setApplicationEventPublisher(eventPublisher);
  }

  protected void mockUserService(ApplicationEventPublisher eventPublisher) {
    userService = new UserService(userUtil, userRepository, userRepositoryRoleService,
        userNamespaceRoleService, notificationService);
    userService.setApplicationEventPublisher(eventPublisher);
  }

  // disables caching in test as it won't impact on performance
  protected void mockUserNamespaceRolesCache() {
    when(userNamespaceRolesCache.get(anyString())).thenReturn(Optional.empty());
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

    when(userNamespaceRoleService.getRoles(anyString(), anyString())).thenAnswer(inv -> {
      if (inv.getArguments()[0].equals("namespace_admin")) {
        return Sets.newHashSet(namespace_admin);
      }

      if (inv.getArguments()[0].equals("viewer")) {
        return Sets.newHashSet(model_viewer);
      }

      if (inv.getArguments()[0].equals("creator")) {
        return Sets.newHashSet(model_creator);
      }

      if (inv.getArguments()[0].equals("promoter")) {
        return Sets.newHashSet(model_promoter);
      }

      if (inv.getArguments()[0].equals("publisher")) {
        return Sets.newHashSet(model_publisher);
      }

      if (inv.getArguments()[0].equals("reviewer")) {
        return Sets.newHashSet(model_reviewer);
      }

      return Sets
          .newHashSet(namespace_admin, model_viewer, model_creator, model_promoter, model_publisher,
              model_reviewer);
    });

    when(userNamespaceRoleService.getRolesByWorkspaceIdAndUser(anyString(), anyString()))
        .thenAnswer(inv -> {
          if (inv.getArguments()[1].equals("namespace_admin")) {
            return Sets.newHashSet(namespace_admin);
          }

          if (inv.getArguments()[1].equals("viewer")) {
            return Sets.newHashSet(model_viewer);
          }

          if (inv.getArguments()[1].equals("creator")) {
            return Sets.newHashSet(model_creator);
          }

          if (inv.getArguments()[1].equals("promoter")) {
            return Sets.newHashSet(model_promoter);
          }

          if (inv.getArguments()[1].equals("publisher")) {
            return Sets.newHashSet(model_publisher);
          }

          if (inv.getArguments()[1].equals("reviewer")) {
            return Sets.newHashSet(model_reviewer);
          }

          return Sets
              .newHashSet(namespace_admin, model_viewer, model_creator, model_promoter,
                  model_publisher,
                  model_reviewer);
        });

    when(userNamespaceRoleService.getRoles(any(User.class), any(Namespace.class)))
        .thenReturn(roles);

    when(userNamespaceRoleService.getRolesByWorkspaceIdAndUser(anyString(), any(User.class)))
        .thenReturn(roles);

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

    when(userNamespaceRoleService.hasRole(anyString(), any(), any())).thenReturn(false);

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

    when(userNamespaceRoleService.getNamespaces(any(User.class), any(User.class)))
        .thenReturn(Sets.newHashSet(
            NamespaceProvider.mockEclipseNamespace(), NamespaceProvider.mockComNamespace()));
  }

  private void mockUserRepository(User alex, User erle, User admin, User creator, User promoter,
      User reviewer, User publisher) {
    when(userRepository.findByUsername("alex")).thenReturn(alex);
    when(userRepository.exists(alex.getId())).thenReturn(true);
    when(userRepository.findByUsername("erle")).thenReturn(erle);
    when(userRepository.exists(erle.getId())).thenReturn(true);
    when(userRepository.findByUsername("admin")).thenReturn(admin);
    when(userRepository.exists(admin.getId())).thenReturn(true);
    when(userRepository.findByUsername("creator")).thenReturn(creator);
    when(userRepository.exists(creator.getId())).thenReturn(true);
    when(userRepository.findByUsername("promoter")).thenReturn(promoter);
    when(userRepository.exists(promoter.getId())).thenReturn(true);
    when(userRepository.findByUsername("reviewer")).thenReturn(reviewer);
    when(userRepository.exists(reviewer.getId())).thenReturn(true);
    when(userRepository.findByUsername("publisher")).thenReturn(publisher);
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
        userRepository.findByUsername(createUserContext(getCallerId(), "playground").getUsername()))
        .thenReturn(mockUser);
    workflow.doAction(model.getId(), createUserContext(getCallerId(), "playground"),
        SimpleWorkflowModel.ACTION_RELEASE.getName());
    when(userRepository.findByUsername(createUserContext("admin", "playground").getUsername()))
        .thenReturn(mockAdmin);
    workflow.doAction(model.getId(), createUserContext("admin", "playground"),
        SimpleWorkflowModel.ACTION_APPROVE.getName());
  }

  protected IModelRepository getModelRepository(IUserContext userContext) {
    return repositoryFactory.getRepository(userContext);
  }

  protected IRepositoryManager getRepoManager(IUserContext userContext) {
    return repositoryFactory
        .getRepositoryManager(userContext.getWorkspaceId(), userContext.getAuthentication());
  }

  protected IUserContext createUserContext(String username) {
    return createUserContext(username, "playground");
  }

  protected Authentication createAuthenticationToken(String username) {
    if (username.equalsIgnoreCase("admin")) {
      return new TestingAuthenticationToken(username, username, RepositoryRole.SYS_ADMIN.getName());
    }
    Collection<IRole> roles;
    try {
      roles = userNamespaceRoleService.getRoles(username, "");
    } catch (DoesNotExistException e) {
      roles = Sets.newHashSet(RoleProvider.modelReviewer());
    }

    return new TestingAuthenticationToken(username, username,
        roles.stream().map(IRole::getName).toArray(String[]::new));
  }

  protected IUserContext createUserContext(String username, String tenantId) {
    return UserContext.user(createAuthenticationToken(username), tenantId);
  }

}
