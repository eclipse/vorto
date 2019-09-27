/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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

import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRetrievalService;
import org.eclipse.vorto.repository.core.IRepositoryManager;
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
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.eclipse.vorto.repository.workflow.impl.DefaultWorkflowService;
import org.eclipse.vorto.repository.workflow.impl.SimpleWorkflowModel;
import org.junit.After;
import org.junit.Before;
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
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public abstract class AbstractIntegrationTest {

  @InjectMocks
  protected ModelSearchUtil modelSearchUtil = new ModelSearchUtil();

  @Mock
  protected IUserRepository userRepository = Mockito.mock(IUserRepository.class);

  @Mock
  protected AttachmentValidator attachmentValidator = Mockito.mock(AttachmentValidator.class);

  @Mock
  protected INotificationService notificationService = Mockito.mock(INotificationService.class);

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
  
  private ITenantRepository tenantRepo = Mockito.mock(ITenantRepository.class);

  private Tenant playgroundTenant = playgroundTenant();

  protected ISearchService searchService = null;
  
  @Before
  public void beforeEach() throws Exception {
    when(tenantService.getTenantFromNamespace(Matchers.anyString())).thenReturn(Optional.of(playgroundTenant));
    
    when(userRepository.findByUsername("alex")).thenReturn(getUser("alex", playgroundTenant));

    when(userRepository.findByUsername("erle")).thenReturn(getUser("erle", playgroundTenant));

    when(userRepository.findByUsername("admin")).thenReturn(getUser("admin", playgroundTenant));

    when(userRepository.findByUsername("creator")).thenReturn(getUser("creator", playgroundTenant));
    
    when(userRepository.findByUsername("promoter")).thenReturn(getUser("promoter", playgroundTenant));

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
    modelParserFactory.setErrorMessageProvider(new ErrorMessageProvider());
    modelParserFactory.init();

    RepositoryConfiguration config =
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
        if (user == null)
          return getRepository(tenant);
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
    
    this.modelValidationHelper = new ModelValidationHelper(repositoryFactory, this.accountService, tenantService);

    this.importer = new VortoModelImporter();
    this.importer.setUploadStorage(new InMemoryTemporaryStorage());
    this.importer.setUserRepository(this.accountService);
    this.importer.setModelParserFactory(modelParserFactory);
    this.importer.setModelRepoFactory(repositoryFactory);
    this.importer.setModelValidationHelper(modelValidationHelper);

    this.workflow =
        new DefaultWorkflowService(repositoryFactory, accountService, notificationService);

    MockitoAnnotations.initMocks(this);
  }

  @After
  public void after() throws Exception {
    repositoryFactory.stop();
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
          Context.create(userContext,Optional.empty()));
      return this.importer.doImport(uploadResult.getHandleId(), Context.create(userContext,Optional.empty())).get(0);
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

  protected ModelInfo releaseModel(ModelId modelId, IUserContext creator) throws WorkflowException {
    workflow.start(modelId, creator);
    
    workflow.doAction(modelId, createUserContext("promoter"),
        SimpleWorkflowModel.ACTION_RELEASE.getName());
    
    return workflow.doAction(modelId, createUserContext("reviewer"),
        SimpleWorkflowModel.ACTION_APPROVE.getName());
  }
  
  protected ModelInfo setReleaseState(ModelInfo model) throws WorkflowException {
    when(
        userRepository.findByUsername(createUserContext(getCallerId(), "playground").getUsername()))
            .thenReturn(User.create(getCallerId(), new Tenant("playground"), Role.USER));
    workflow.doAction(model.getId(), createUserContext(getCallerId(), "playground"),
        SimpleWorkflowModel.ACTION_RELEASE.getName());
    when(userRepository.findByUsername(createUserContext("admin", "playground").getUsername()))
        .thenReturn(User.create("admin", new Tenant("playground"), Role.SYS_ADMIN));
    return workflow.doAction(model.getId(), createUserContext("admin", "playground"),
        SimpleWorkflowModel.ACTION_APPROVE.getName());
  }

  protected IModelRepository getModelRepository(IUserContext userContext) {
    return repositoryFactory.getRepository(userContext);
  }
  
  protected IRepositoryManager getRepoManager(IUserContext userContext) {
    return repositoryFactory.getRepositoryManager(userContext.getTenant(), userContext.getAuthentication());
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

  protected IUserContext createUserContext(String username, String tenantId) {
    return UserContext.user(createAuthenticationToken(username), tenantId);
  }

  protected Tenant playgroundTenant() {
    UserRole roleUser = new UserRole(Role.USER);
    UserRole roleCreator = new UserRole(Role.MODEL_CREATOR);
    UserRole rolePromoter = new UserRole(Role.MODEL_PROMOTER);
    UserRole roleReviewer = new UserRole(Role.MODEL_REVIEWER);
    UserRole rolePublisher = new UserRole(Role.MODEL_PUBLISHER);
    UserRole roleTenantAdmin = new UserRole(Role.TENANT_ADMIN);
    UserRole roleSysAdmin = new UserRole(Role.SYS_ADMIN);

    Tenant playground = Tenant.newTenant("playground", "org.eclipse",
        Sets.newHashSet("org.eclipse", "com.mycompany", "com.ipso", "examples.mappings", "vorto.private.playground"));

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
    User _user = User.create(name);
    TenantUser user = new TenantUser();
    user.setRoles(roles);
    _user.addTenantUser(user);
    return user;
  }

  private User getUser(String userId, Tenant tenant) {
    return tenant.getUsers().stream().map(TenantUser::getUser)
        .filter(u -> u.getUsername().equals(userId)).findAny().get();
  }

  private TenantUser getTenantUser(String userId, Tenant tenant) {
    return tenant.getUsers().stream().filter(tu -> tu.getUser().getUsername().equals(userId))
        .findAny().get();
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
        for(ApplicationListener<AppEvent> listener : listeners) {
          listener.onApplicationEvent(appEvent);
        }
      }
    }

    @Override
    public void publishEvent(Object event) {
      // implement when need arises
    }
  }
}
