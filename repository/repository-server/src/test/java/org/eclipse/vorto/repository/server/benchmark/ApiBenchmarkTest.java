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
package org.eclipse.vorto.repository.server.benchmark;

import com.google.common.collect.Sets;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.gui.action.HtmlReportGenerator;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.eclipse.vorto.repository.oauth.internal.SpringUserUtils;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.server.ui.AuthenticationProviderMock;
import org.eclipse.vorto.repository.web.VortoRepository;
import org.eclipse.vorto.repository.web.api.v1.ModelController;
import org.eclipse.vorto.repository.web.api.v1.NamespaceController;
import org.eclipse.vorto.repository.web.core.ModelRepositoryController;
import org.junit.*;
import org.junit.runner.RunWith;
import org.modeshape.jcr.RepositoryConfiguration;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.eclipse.vorto.repository.domain.NamespaceRole.DEFAULT_NAMESPACE_ROLES;
import static org.eclipse.vorto.repository.domain.RepositoryRole.DEFAULT_REPOSITORY_ROLES;

/**
 * Spins up a repository and corresponding testcontainers (mysql and elasticsearch) and runs a jmeter test plan
 * (src/test/resources/benchmarkTests/apiCallGetModel.jmx). The results are stored and an HTML report is generated in:
 * target/test-classes/benchmarkTests/report.
 *
 * If you want to execute the test directly in your IDE you need to add {@code clean resources:testResources jmeter:configure -Pbenchmark-tests}
 * to your run configuration or make sure that you download JMeter manually and adapt the JMeter home path.
 *
 * Since the test data is compressed the testcontainer needs internet connectivity to install decompress-uitls via apt.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles={"local-benchmark-test"})
@SpringBootTest(classes = VortoRepository.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// https://github.com/spring-projects/spring-boot/issues/12280
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ContextConfiguration(initializers = ApiBenchmarkTest.Initializer.class, classes = {VortoRepository.class, ApiBenchmarkTest.ModeshapeTestConfiguration.class})
public class ApiBenchmarkTest {

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

    @Autowired
    protected ModelController modelController;

    @ClassRule
    public static ElasticsearchContainer container = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:6.7.2");

    @ClassRule
    public static JdbcDatabaseContainer mySQLContainer = (JdbcDatabaseContainer) new MySQLContainer("mysql:5.6.44").withDatabaseName("vorto_schema").withPassword("vorto").withUsername("vorto").withCommand("--max-allowed-packet=67108864");

    private JUnitTestResultCollector jUnitTestResultCollector =  new JUnitTestResultCollector();

    // JMeter home dir
    public static final String JMETER_HOME_DIR = "benchmarkTests";
    // JMeter bin dir
    public static final String JMETER_BINARY_DIR = "jmeter";
    // Property name for the Vorto testcontainer (referenced in the testplan)
    public static final String VORTO_PORT_PROPERTY = "vorto.port";
    // Property name for the file name of the summary file
    public static final String VORTO_RESULT_FILE_SUMMARY_PROPERTY = "vorto.result.file.summary";
    // Property name for the result tree file
    public static final String VORTO_RESULT_FILE_TREE_PROPERTY = "vorto.result.file.tree";
    // Path string used for the benchmark tests
    private static String BENCHMARK_PATH_STRING;
    // JMeter engine
    private StandardJMeterEngine myJMeterEngine = new StandardJMeterEngine();
    // Tree representation of the test plan
    private HashTree testPlanTree;

    public ApiBenchmarkTest() {
    }

    @BeforeClass
    public static void configureOAuthConfiguration() throws URISyntaxException, IOException {
        setSystemProperties();
        globalJMeterConfiguration();
    }

    private static void globalJMeterConfiguration() throws URISyntaxException, IOException {
        BENCHMARK_PATH_STRING = Paths.get(ApiBenchmarkTest.class.getClassLoader().getResource(JMETER_HOME_DIR).toURI()).toString();

        // Initialize Properties
        JMeterUtils.loadJMeterProperties(BENCHMARK_PATH_STRING + "/user.properties");
        JMeterUtils.setJMeterHome(determineJMeterHome());
        JMeterUtils.initLocale();
    }

    private static String determineJMeterHome() throws URISyntaxException, IOException {
        try {
            Path jmeterPath = Files.list(Paths.get(BENCHMARK_PATH_STRING)).filter(path -> path.toFile().isDirectory() && !path.toString().contains("report")).findAny().orElse(null);
            return jmeterPath.resolve(JMETER_BINARY_DIR).toString();
        }catch (Exception ex) {
            System.err.println("unable to determine jmeter directory");
        }
        return "opt/JMeter";
    }

    /**
     * Configure JMeter execution. Set vorto repository port and load the testplan.
     *
     * @throws IOException
     */
    private void configureJMeter() throws IOException, URISyntaxException {
        SaveService.loadProperties();
        // set the vorto port for jmeter test execution.
        JMeterUtils.setProperty(VORTO_PORT_PROPERTY, Integer.toString(port));
        // set result tree file path
        JMeterUtils.setProperty(VORTO_RESULT_FILE_TREE_PROPERTY, BENCHMARK_PATH_STRING + "/resulttree.csv");
        // set result summary file path
        JMeterUtils.setProperty(VORTO_RESULT_FILE_SUMMARY_PROPERTY, BENCHMARK_PATH_STRING  + "/resultsummary.csv");
        // load the test plan
        testPlanTree = SaveService.loadTree(Paths.get(ApiBenchmarkTest.class.getClassLoader().getResource(JMETER_HOME_DIR+"/apiCallGetModel.jmx").toURI()).toFile());
        // add custom test result collector which fails the unit test if at least one jmeter sample fails
        testPlanTree.add(testPlanTree.getArray()[0], jUnitTestResultCollector);
    }

    private static void setSystemProperties() {
        System.setProperty("eclipse_clientid", "foo");
        System.setProperty("eclipse_clientSecret", "foo");
        System.setProperty("github_clientid", "foo");
        System.setProperty("github_clientSecret", "foo");
        System.setProperty("eidp_clientid", "foo");
        System.setProperty("eidp_clientSecret", "foo");
        System.setProperty("suite_clientid", "foo");
        System.setProperty("suite_clientSecret", "foo");
        System.setProperty("line.separator", "\n");
        System.setProperty("testcontainers.es", container.getMappedPort(9200).toString());
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
            // edit the existing modeshape configuration and adapt the db connection and ports.
            Editor editor = configuration.edit();
            editor.setString(RepositoryConfiguration.FieldName.CONNECTION_URL, mySQLContainer.getJdbcUrl());
            EditableDocument modifiedConfig = configuration.edit().clone();
            // no clustering
            modifiedConfig.remove("clustering");
            EditableDocument storage = modifiedConfig.getDocument("storage");
            storage.getDocument("persistence").setString("connectionUrl", mySQLContainer.getJdbcUrl());
            storage.getDocument("persistence").setString("username", mySQLContainer.getUsername());
            storage.getDocument("persistence").setString("password", mySQLContainer.getPassword());

            storage.getDocument("binaryStorage").setString("url", mySQLContainer.getJdbcUrl());
            storage.getDocument("binaryStorage").setString("username", mySQLContainer.getUsername());
            storage.getDocument("binaryStorage").setString("password", mySQLContainer.getPassword());

            modifiedConfig.setDocument("storage", storage);
            return RepositoryConfiguration.read(modifiedConfig.toString());
        }
    }

    @Before
    public void setUpTest() throws IOException, URISyntaxException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        mock.setAuthorityListForUser(SpringUserUtils.toAuthorityList(
                Sets.newHashSet(DEFAULT_NAMESPACE_ROLES[0], DEFAULT_NAMESPACE_ROLES[5], DEFAULT_NAMESPACE_ROLES[1], DEFAULT_NAMESPACE_ROLES[2], DEFAULT_NAMESPACE_ROLES[3], DEFAULT_NAMESPACE_ROLES[4], DEFAULT_REPOSITORY_ROLES[0])), "user1");
        configureJMeter();
    }

    @Test
    public void testGetModelApi() {
        myJMeterEngine.configure(testPlanTree);
        myJMeterEngine.run();
        if(!jUnitTestResultCollector.getFailedSamplesSet().isEmpty()) {
            Assert.fail("Test failed because at least one instance of the following JMeter samples failed: " + jUnitTestResultCollector.getFailedSamplesSet().toString());
        }
    }

    @After
    public void createHtmlReport() throws IOException {
        renameJar();
        HtmlReportGenerator generator = new HtmlReportGenerator(JMeterUtils.getProperty(VORTO_RESULT_FILE_SUMMARY_PROPERTY),BENCHMARK_PATH_STRING + "/user.properties",BENCHMARK_PATH_STRING + "/report");
        List<String> errorList = generator.run();
        if(!errorList.isEmpty()) {
            System.err.println("unable to create report due to errors: ");
            for (String error: errorList) {
                System.err.println(error);
            }
        }
    }

    /**
     * Set the data source urls (url of the mysql testcontainer) and load the test data
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
            loadTestData();
        }

        /**
         * Upload the sql file to the container, decompress it and trigger the import.
         */
        private void loadTestData() {
            try {
                mySQLContainer.copyFileToContainer(MountableFile.forClasspathResource("VortoTestcontainer3.tar.xz"), "VortoTestcontainer3.tar.xz");
                mySQLContainer.execInContainer("bash", "-c","apt-get update && apt-get install xz-utils");
                mySQLContainer.execInContainer("bash", "-c","tar -xf VortoTestcontainer3.tar.xz");
                mySQLContainer.execInContainer("bash","-c","usr/bin/mysql -uvorto -pvorto vorto_schema < VortoTestcontainer3.sql");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Assert.fail("Unable to load testdata.");
            }
        }
    }

    /**
     * Special result collector which only collects the names of failed samples in JMeter.
     */
    private class JUnitTestResultCollector extends ResultCollector {

        private HashSet<String> failedSamplesSet = new HashSet<>();

        @Override
        public void sampleOccurred(SampleEvent e) {
            if(e.getResult() != null)
                if(!e.getResult().isSuccessful())
                    failedSamplesSet.add(e.getResult().getSampleLabel());
        }

        Set<String> getFailedSamplesSet() {
            return failedSamplesSet;
        }
    }

    /**
     * Workaround to make the generator usable (jarfile name is hardcoded in
     * org.apache.jmeter.gui.action.{@link HtmlReportGenerator})
     *
     * @throws IOException
     */
    private void renameJar() throws IOException {
        String jMeterBin = JMeterUtils.getJMeterBinDir();
        Path jmeterJar = Files.list(Paths.get(jMeterBin)).filter(fileName -> fileName.toString().contains("ApacheJMeter")).findAny().orElse(null);
        Files.move(jmeterJar, Paths.get(jMeterBin).resolve("ApacheJMeter.jar"));
    }
}
