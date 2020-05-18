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
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelValidationHelper;
import org.eclipse.vorto.repository.core.impl.validation.AttachmentValidator;
import org.eclipse.vorto.repository.domain.*;
import org.eclipse.vorto.repository.importer.Context;
import org.eclipse.vorto.repository.importer.FileUpload;
import org.eclipse.vorto.repository.importer.UploadModelResult;
import org.eclipse.vorto.repository.importer.impl.VortoModelImporter;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.tenant.TenantService;
import org.eclipse.vorto.repository.tenant.TenantUserService;
import org.eclipse.vorto.repository.tenant.repository.ITenantRepository;
import org.eclipse.vorto.repository.tenant.repository.ITenantUserRepo;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.impl.DefaultWorkflowService;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.mockito.*;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
   * @param model
   * @return
   */
  protected static String getFileName(ModelInfo model) {
    return String.format(MODEL_FILENAME_FORMAT, model.getId().getName(), model.getType().getExtension());
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

  protected TenantService tenantService = Mockito.mock(TenantService.class);

  protected IIndexingService indexingService = null;

  protected TenantUserService tenantUserService = null;

  protected ModelValidationHelper modelValidationHelper = null;

  private ITenantRepository tenantRepo = Mockito.mock(ITenantRepository.class);

  private Tenant playgroundTenant = playgroundTenant();

  @InjectMocks
  protected ISearchService searchService = null;

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
    private ESRandomizer(){}

    static ESRandomizer newInstance() {
      int httpStart = ThreadLocalRandom.current().nextInt(MIN_PORT, MAX_PORT - 101);
      return new ESRandomizer()
          .withHTTPStart(httpStart)
          .withTCPStart(httpStart + 100)
          .withInstallationDirectory(String.format(PATH_FORMAT, httpStart, httpStart+100));
    }
  }

  private static final Logger LOGGER = Logger.getLogger(SearchTestInfrastructure.class);
  private static final String RANDOM_ES_LOG_FORMAT = "Initializing Elasticsearch test service port randomizer with HTTP [%d-%d] and TRANSPORT TCP [%d-%d] and installation directory: %s";

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
    User _user = User.create(name, "GITHUB", null);
    TenantUser user = new TenantUser();
    user.setRoles(roles);
    _user.addTenantUser(user);
    return user;
  }

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
        .withElasticVersion("6.7.2")
        .withSetting(PopularProperties.HTTP_PORT, rando.getHTTPStartPort())
        .withSetting(PopularProperties.TRANSPORT_TCP_PORT, rando.getTCPStartPort())
        .withSetting("discovery.type", "single-node")
        .withJavaHome(JavaHomeOption.inheritTestSuite())
        .withInResourceLocation("elasticsearch-6.7.2.zip")
        .withInstallationDirectory(new File(rando.getInstallationDirectory()))
        // defaults to 15 seconds, making it up to 1 minute to try and smoothe down tests in slower machines
        .withStartTimeout(1, TimeUnit.MINUTES)
        .build();
    elasticSearch.start();

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

    modelParserFactory = new ModelParserFactory();
    modelParserFactory.init();

    RepositoryConfiguration config = null;
    config =
        RepositoryConfiguration.read(new ClassPathResource("vorto-repository.json").getPath());

    repositoryFactory = new ModelRepositoryFactory(accountService, null,
        attachmentValidator, modelParserFactory, null, config, null, null, null, null) {

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
    searchService = new ElasticSearchService(new RestHighLevelClient(clientBuilder), repositoryFactory, tenantService);

    indexingService = (IIndexingService)searchService;
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

    repositoryFactory.setApplicationEventPublisher(eventPublisher);
    repositoryFactory.start();

    supervisor.setRepositoryFactory(repositoryFactory);
    modelParserFactory.setModelRepositoryFactory(repositoryFactory);

    tenantUserService = new TenantUserService(tenantService, accountService);


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

    defaultUser = createUserContext("alex");
  }

  /**
   * Stops the Elasticsearch service first, then the repository. <br/>
   * Must be invoked on {@link org.junit.AfterClass} on each test class.
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
    ((IIndexingService)searchService).reindexAllModels();
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
