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
package org.eclipse.vorto.repository.search;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRetrievalService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.impl.InMemoryTemporaryStorage;
import org.eclipse.vorto.repository.core.impl.ModelRepositoryEventListener;
import org.eclipse.vorto.repository.core.impl.ModelRepositoryFactory;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.core.impl.cache.UserNamespaceRolesCache;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
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
import org.eclipse.vorto.repository.services.*;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.impl.DefaultWorkflowService;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.mockito.InjectMocks;
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
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.JavaHomeOption;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * This class provides all the infrastructure required to perform tests on the search service. <br/>
 * It bears notable resemblance to {@code org.eclipse.vorto.repository.AbstractIntegrationTest} for
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
 * within the same test class, so the single tests are not co-dependent with one another.<br/>
 * This is a conversion of the homonymous test class in the {@literal repository-core} module. <br/>
 * The main difference with its sibling is that:
 * <ul>
 *   <li>
 *     The search service is {@link ElasticSearchService}
 *   </li>
 *   <li>
 *     The indexing service is not mocked, and is <i>also</i> the same {@link ElasticSearchService}
 *   </li>
 *   <li>
 *     The actual Elasticsearch runtime is spun by the Allegro framework, with ports made to not
 *     conflict with the default Elasticsearch ports
 *   </li>
 * </ul>
 * Other minor modifications include:
 * <ul>
 *   <li>
 *     User management: user context <i>must</i> be injected in the {@link ElasticSearchService},
 *     see {@link SearchTestInfrastructure#getDefaultUser()} and
 *     {@link ElasticSearchService#search(String, IUserContext)}.
 *   </li>
 *   <li>
 *     Minor details such as model file name retrieval, which is a bit more convoluted here than
 *     in the repository-core module, due to schema reasons - see {@link SearchTestInfrastructure#getFileName(ModelInfo)}.
 *   </li>
 * </ul>
 *
 * @author mena-bosch (refactory)
 */
public final class SearchTestInfrastructure {

  /**
   * Filename property not accessible with ElasticSearch, contrary to tests with simple search. <br/>
   * In order to easily compare a given model's file name with static file name resources, this
   * trivial utility concatenates the model's {@link org.eclipse.vorto.model.ModelId#getName()} with the
   * {@link ModelType#getExtension()} from {@link ModelInfo#getType()}.
   *
   * @param model
   * @return
   */
  protected static String getFileName(ModelInfo model) {
    return String
        .format(MODEL_FILENAME_FORMAT, model.getId().getName(), model.getType().getExtension());
  }

  /**
   * File names common for all simple search test classes.
   */
  protected static final String DATATYPE_MODEL = "Color.type";
  protected static final String FUNCTIONBLOCK_MODEL = "Switcher.fbmodel";
  protected static final String INFORMATION_MODEL = "ColorLightIM.infomodel";
  protected static final String MAPPING_MODEL = "Color_ios.mapping";
  protected static final String MODEL_FILENAME_FORMAT = "%s%s";

  private IUserContext defaultUser;

  /**
   * Field initialized last in ctor.
   *
   * @return a "alex" user context, used in most tests.
   */
  protected IUserContext getDefaultUser() {
    return defaultUser;
  }

  @Mock
  protected UserRepository userRepository = Mockito.mock(UserRepository.class);

  @Mock
  protected AttachmentValidator attachmentValidator = Mockito
      .mock(AttachmentValidator.class);

  @Mock
  protected INotificationService notificationService = Mockito
      .mock(INotificationService.class);

  protected DefaultUserAccountService accountService = null;

  protected VortoModelImporter importer = null;

  protected IWorkflowService workflow = null;

  protected ModelParserFactory modelParserFactory = null;

  protected ModelRepositoryFactory repositoryFactory;

  protected IIndexingService indexingService = null;

  protected ModelValidationHelper modelValidationHelper = null;

  NamespaceService namespaceService = Mockito.mock(NamespaceService.class);

  UserNamespaceRoleService userNamespaceRoleService = Mockito.mock(UserNamespaceRoleService.class);

  protected UserNamespaceRolesCache userNamespaceRolesCache = Mockito.mock(UserNamespaceRolesCache.class);

  NamespaceRepository namespaceRepository = Mockito.mock(NamespaceRepository.class);

  UserRepositoryRoleService userRepositoryRoleService = Mockito
      .mock(UserRepositoryRoleService.class);

  RoleService roleService = Mockito.mock(RoleService.class);

  PrivilegeService privilegeService = Mockito.mock(PrivilegeService.class);

  @InjectMocks
  protected ISearchService searchService;

  protected EmbeddedElastic elasticSearch;

  /**
   * When building in a non-isolated environment, concurrent builds may not work while testing
   * ES tests as the same ports would be in use.<br/>
   * Moreover, the default ES installation directory in the {@literal /tmp} folder can be an issue
   * too. <br/>
   * This tiny utility randomizes ports and ES installation directory for Allegro, with a few basic criteria:
   * <ul>
   *  <li>Ports are not production ES ports</li>
   *  <li>Ports are {@literal > 10000} (to broadly avoid collision with other "known" ports)</li>
   *  <li>HTTP port (see {@link PopularProperties#HTTP_PORT}) and TCP port
   *  (see {@link PopularProperties#TRANSPORT_TCP_PORT}) are expressed as a range of {@literal 1}</li>
   *  <li>HTTP port and TCP port have a distance of {@literal 100} (arbitrary rule)</li>
   *  <li>The temporary installation folder is a relative path under the {@literal target} directory</li>
   * </ul>
   */
  private static class ESRandomizer {

    private static final int MAX_PORT = 65535;
    private static final int MIN_PORT = 10000;
    private int httpStart = 19200;
    private int tcpStart = 19300;
    private String installationDirectory = ".";
    private static final String PATH_FORMAT = "target/temporaryESWithHTTP%dTCP%d";

    private ESRandomizer withHTTPStart(int start) {
      this.httpStart = start;
      return this;
    }

    private ESRandomizer withTCPStart(int start) {
      this.tcpStart = start;
      return this;
    }

    private ESRandomizer withInstallationDirectory(String path) {
      this.installationDirectory = path;
      return this;
    }

    String getInstallationDirectory() {
      return installationDirectory;
    }

    int getHTTPStartPort() {
      return httpStart;
    }

    int getHTTPEndPort() {
      return httpStart + 1;
    }

    int getTCPStartPort() {
      return tcpStart;
    }

    int getTCPEndPort() {
      return tcpStart + 1;
    }

    private ESRandomizer() {
    }

    static ESRandomizer newInstance() {
      int httpStart = ThreadLocalRandom.current().nextInt(MIN_PORT, MAX_PORT - 101);
      return new ESRandomizer()
          .withHTTPStart(httpStart)
          .withTCPStart(httpStart + 100)
          .withInstallationDirectory(String.format(PATH_FORMAT, httpStart, httpStart + 100));
    }
  }

  private static final Logger LOGGER = Logger.getLogger(SearchTestInfrastructure.class);
  private static final String RANDOM_ES_LOG_FORMAT = "Initializing Elasticsearch test service port randomizer with HTTP [%d-%d] and TRANSPORT TCP [%d-%d] and installation directory: %s";

  protected SearchTestInfrastructure() throws Exception {

    ESRandomizer rando = ESRandomizer.newInstance();
    LOGGER.info(
        String.format(
            RANDOM_ES_LOG_FORMAT,
            rando.getHTTPStartPort(),
            rando.getHTTPEndPort(),
            rando.getTCPStartPort(),
            rando.getTCPEndPort(),
            rando.getInstallationDirectory()
        )
    );

    elasticSearch = EmbeddedElastic.builder()
        .withElasticVersion("6.8.8")
        .withSetting(PopularProperties.HTTP_PORT, rando.getHTTPStartPort())
        .withSetting(PopularProperties.TRANSPORT_TCP_PORT, rando.getTCPStartPort())
        .withSetting("discovery.type", "single-node")
        .withJavaHome(JavaHomeOption.inheritTestSuite())
        .withInResourceLocation("elasticsearch-6.8.8.zip")
        .withInstallationDirectory(new File(rando.getInstallationDirectory()))
        // defaults to 15 seconds, making it up to 1 minute to try and smoothe down tests in slower machines
        .withStartTimeout(1, TimeUnit.MINUTES)
        .build();
    elasticSearch.start();

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

    User alex = User.create("alex", "GITHUB", null);
    User erle = User.create("erle", "GITHUB", null);
    User admin = User.create("admin", "GITHUB", null);
    User creator = User.create("creator", "GITHUB", null);
    User promoter = User.create("promoter", "GITHUB", null);
    User reviewer = User.create("reviewer", "GITHUB", null);
    User publisher = User.create("publisher", "GITHUB", null);

    when(userRepository.findByUsername("alex")).thenReturn(alex);
    when(userRepository.findByUsername("erle")).thenReturn(erle);
    when(userRepository.findByUsername("admin")).thenReturn(admin);
    when(userRepository.findByUsername("creator")).thenReturn(creator);
    when(userRepository.findByUsername("promoter")).thenReturn(promoter);
    when(userRepository.findByUsername("reviewer")).thenReturn(reviewer);
    when(userRepository.findByUsername("publisher")).thenReturn(publisher);
    when(userRepository.findAll())
        .thenReturn(Lists.newArrayList(alex, erle, admin, creator, promoter, reviewer, publisher));

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

    when(userNamespaceRoleService.getRolesByWorkspaceIdAndUser(anyString(), anyString())).thenAnswer(inv -> {
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
          .newHashSet(namespace_admin, model_viewer, model_creator, model_promoter, model_publisher,
              model_reviewer);
    });

    when(userNamespaceRoleService.getRolesByWorkspaceIdAndUser(anyString(), any(User.class)))
        .thenReturn(roles);
    // disables caching in test as it won't impact on performance
    when(userNamespaceRolesCache.get(anyString())).thenReturn(Optional.empty());

    setupNamespaceMocking();

    modelParserFactory = new ModelParserFactory();
    modelParserFactory.init();

    RepositoryConfiguration config = null;
    config =
        RepositoryConfiguration.read(new ClassPathResource("vorto-repository.json").getPath());

    repositoryFactory = new ModelRepositoryFactory(null,
        attachmentValidator, modelParserFactory, null, config, null, namespaceService,
        userNamespaceRoleService, privilegeService, userRepositoryRoleService, userNamespaceRolesCache) {

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

    ModelRepositoryEventListener supervisor = new ModelRepositoryEventListener();
    RestClientBuilder clientBuilder = RestClient.builder(
        new HttpHost("localhost", rando.getHTTPStartPort(), "http"),
        new HttpHost("localhost", rando.getHTTPEndPort(), "http"),
        new HttpHost("localhost", rando.getTCPStartPort(), "http"),
        new HttpHost("localhost", rando.getTCPEndPort(), "http")
    );
    searchService = new ElasticSearchService(new RestHighLevelClient(clientBuilder),
        repositoryFactory, userNamespaceRoleService, namespaceRepository);

    indexingService = (IIndexingService) searchService;
    IndexingEventListener indexingSupervisor = new IndexingEventListener(indexingService);

    Collection<ApplicationListener<AppEvent>> listeners = new ArrayList<>();
    listeners.add(supervisor);
    listeners.add(indexingSupervisor);

    ApplicationEventPublisher eventPublisher = new MockAppEventPublisher(listeners);

    accountService = new DefaultUserAccountService(userRepository, notificationService, roleService,
        userNamespaceRoleService);
    accountService.setApplicationEventPublisher(eventPublisher);

    repositoryFactory.setApplicationEventPublisher(eventPublisher);
    repositoryFactory.start();

    supervisor.setRepositoryFactory(repositoryFactory);
    modelParserFactory.setModelRepositoryFactory(repositoryFactory);

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

    MockitoAnnotations.initMocks(SearchTestInfrastructure.class);

    defaultUser = createUserContext("alex");
  }

  /**
   * Stops the Elasticsearch service first, then the repository. <br/>
   * Must be invoked on {@link org.junit.AfterClass} on each test class.
   *
   * @throws Exception
   */
  public void terminate() throws Exception {
    elasticSearch.stop();
    repositoryFactory.stop();
  }

  /**
   * Reindexes all models.
   */
  public void reindex() {
    ((IIndexingService) searchService).reindexAllModels();
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
      roles = Sets
          .newHashSet(org.eclipse.vorto.repository.search.utils.RoleProvider.modelReviewer());
    }

    return new TestingAuthenticationToken(username, username,
        roles.stream().map(IRole::getName).toArray(String[]::new));
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

  private void setupNamespaceMocking() throws OperationForbiddenException, DoesNotExistException {
    when(namespaceService.resolveWorkspaceIdForNamespace(anyString()))
        .thenReturn(Optional.of("playground"));
    when(namespaceService.findNamespaceByWorkspaceId(anyString())).thenReturn(mockNamespace());
    when(namespaceRepository.findAll()).thenReturn(Arrays.asList(mockNamespace()));
    when(userNamespaceRoleService.hasRole(anyString(), any(), any())).thenReturn(true);
    when(userNamespaceRoleService.getNamespaces(anyString(), anyString()))
        .thenReturn(Arrays.asList(mockNamespace()));
    List<String> workspaceIds = new ArrayList<>();
    workspaceIds.add("playground");
    when(namespaceService.findAllWorkspaceIds()).thenReturn(workspaceIds);
    NamespaceRole role = new NamespaceRole();
    role.setName("namespace_admin");
    role.setPrivileges(7);
    role.setRole(32);
    Set<IRole> roles = new HashSet<>();
    roles.add(role);
    when(userNamespaceRoleService.getRoles(anyString(), anyString())).thenReturn(roles);
    when(userNamespaceRoleService.getRoles(any(User.class), any(Namespace.class)))
        .thenReturn(roles);
    Set<Privilege> privileges = new HashSet<>(Arrays.asList(Privilege.DEFAULT_PRIVILEGES));
    when(privilegeService.getPrivileges(anyLong())).thenReturn(privileges);
  }

  private Namespace mockNamespace() {
    Namespace namespace = new Namespace();
    namespace.setName("org.eclipse.vorto");
    namespace.setId(1L);
    namespace.setWorkspaceId("playground");
    return namespace;
  }
}
