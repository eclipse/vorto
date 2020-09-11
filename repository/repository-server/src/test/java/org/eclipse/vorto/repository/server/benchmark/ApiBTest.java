package org.eclipse.vorto.repository.server.benchmark;

import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.server.ui.AuthenticationProviderMock;
import org.eclipse.vorto.repository.web.VortoRepository;
import org.eclipse.vorto.repository.web.api.v1.NamespaceController;
import org.eclipse.vorto.repository.web.core.ModelRepositoryController;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.modeshape.jcr.RepositoryConfiguration;
import org.modeshape.schematic.document.Changes;
import org.modeshape.schematic.document.EditableDocument;
import org.modeshape.schematic.document.Editor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles={"local-benchmark-test"})
@SpringBootTest(classes = VortoRepository.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// https://github.com/spring-projects/spring-boot/issues/12280
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ContextConfiguration(initializers = ApiBTest.Initializer.class, classes = {VortoRepository.class,ApiBTest.ModeshapeTestConfiguration.class})
public class ApiBTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected AuthenticationProviderMock mock;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected NamespaceController namespaceController;

    @Autowired
    protected ModelRepositoryController modelRepositoryController;

    @ClassRule
    public static ElasticsearchContainer container = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:6.7.2");

    @ClassRule
    public static MySQLContainer mySQLContainer = new MySQLContainer("mysql:5.6.44").withDatabaseName("vorto_schema").withPassword("vorto").withUsername("vorto");


    public static void before() {
        System.setProperty("testcontainers.es", container.getMappedPort(9200).toString());
    }

    @BeforeClass
    public static void configureOAuthConfiguration() {
        System.setProperty("eclipse_clientid", "foo");
        System.setProperty("eclipse_clientSecret", "foo");
        System.setProperty("github_clientid", "foo");
        System.setProperty("github_clientSecret", "foo");
        System.setProperty("eidp_clientid", "foo");
        System.setProperty("eidp_clientSecret", "foo");
        System.setProperty("suite_clientid", "foo");
        System.setProperty("suite_clientSecret", "foo");
        System.setProperty("line.separator", "\n");
        before();
    }



    @TestConfiguration
    public static class ModeshapeTestConfiguration {

        @Value("${repo.configFile}")
        String repoFile;

        @Bean
        @Profile("local-benchmark-test")
        public org.modeshape.jcr.RepositoryConfiguration repoConfiguration() throws Exception {
            RepositoryConfiguration configuration = org.modeshape.jcr.RepositoryConfiguration
                    .read(new ClassPathResource(repoFile).getURL());

            Editor editor = configuration.edit();
            editor.setString(RepositoryConfiguration.FieldName.CONNECTION_URL, mySQLContainer.getJdbcUrl());
            EditableDocument modifiedConfig = configuration.edit().clone();
            modifiedConfig.remove("clustering");
            EditableDocument storage = modifiedConfig.getDocument("storage");
            storage.getDocument("persistence").setString("connectionUrl", mySQLContainer.getJdbcUrl());
            storage.getDocument("persistence").setString("username", mySQLContainer.getUsername());
            storage.getDocument("persistence").setString("password", mySQLContainer.getPassword());

            storage.getDocument("binaryStorage").setString("url", mySQLContainer.getJdbcUrl());
            storage.getDocument("binaryStorage").setString("username", mySQLContainer.getUsername());
            storage.getDocument("binaryStorage").setString("password", mySQLContainer.getPassword());
    //TODO jgroups!
            modifiedConfig.setDocument("storage", storage);
            return RepositoryConfiguration.read(modifiedConfig.toString());
        }
    }

    @Test
    @WithMockUser(username = "user1", roles = "sysadmin")
    public void testGetModelApi() throws WorkflowException {
        createNamespaces(200);
        System.err.println("done");
    }

    /**
     * Exposes the repository (random) port on the host system to the test container after initialization.
     */
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext, "spring.datasource.url=" + mySQLContainer.getJdbcUrl());
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext, "spring.datasource.username=" + mySQLContainer.getUsername());
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext, "spring.datasource.password=" + mySQLContainer.getUsername());
//            applicationContext.getEnvironment().
//            TestPropertyValues.of(N
//                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
//                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
//                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
//            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }



    @Before
    public void setUpTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    private void createNamespaces(int numberOfNamespaces) throws WorkflowException {
        for(int i = 0; i< numberOfNamespaces; i++) {
            namespaceController.createNamespace("test" + i);
            createModel(i);
        }
    }

    private void createModel(int i) throws WorkflowException {
        modelRepositoryController.createModel("test"+i+":MyTestModel"+i+":1.1.0", ModelType.InformationModel, new ArrayList<ModelProperty>());
    }

}
