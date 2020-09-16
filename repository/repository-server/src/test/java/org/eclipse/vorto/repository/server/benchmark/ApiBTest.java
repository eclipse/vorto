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
import org.eclipse.vorto.model.*;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.oauth.internal.SpringUserUtils;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.server.ui.AuthenticationProviderMock;
import org.eclipse.vorto.repository.web.VortoRepository;
import org.eclipse.vorto.repository.web.api.v1.ModelController;
import org.eclipse.vorto.repository.web.api.v1.NamespaceController;
import org.eclipse.vorto.repository.web.core.ModelRepositoryController;
import org.eclipse.vorto.repository.workflow.WorkflowException;
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
import org.springframework.http.ResponseEntity;
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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.eclipse.vorto.repository.domain.NamespaceRole.DEFAULT_NAMESPACE_ROLES;
import static org.eclipse.vorto.repository.domain.RepositoryRole.DEFAULT_REPOSITORY_ROLES;

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

    @Autowired
    protected ModelController modelController;

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

    @Test
    @WithMockUser(username = "user1", authorities = {"sysadmin","model_viewer","model_creator","namespace_admin"})
    public void testGetModelApi() throws WorkflowException {
        createTestData(200);
//        System.err.println("done");
    }

    /**
     * Set the data source urls
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
        }
    }



    @Before
    public void setUpTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        mock.setAuthorityListForUser(SpringUserUtils.toAuthorityList(
                Sets.newHashSet(DEFAULT_NAMESPACE_ROLES[0], DEFAULT_NAMESPACE_ROLES[5], DEFAULT_NAMESPACE_ROLES[1], DEFAULT_NAMESPACE_ROLES[2], DEFAULT_NAMESPACE_ROLES[3], DEFAULT_NAMESPACE_ROLES[4], DEFAULT_REPOSITORY_ROLES[0])), "user1");
    }

    private void createTestData(int numberOfNamespaces) throws WorkflowException {
        Set<ModelId> fbSet = new HashSet<>();

        for(int i = 0; i< numberOfNamespaces; i++) {
            namespaceController.createNamespace("test" + i);
            fbSet = createModelsAndFunctionBlocks(i, fbSet);
        }
    }

    private Set<ModelId> createModelsAndFunctionBlocks(int i, Set<ModelId> fbSet) throws WorkflowException {



        ResponseEntity<ModelInfo> entResponse = modelRepositoryController.createModel("test"+i+":MyTestEntity"+i+":1.1.0", ModelType.Datatype, new ArrayList<ModelProperty>());
        ArrayList<ModelProperty> propertiesFb = new ArrayList<>();
        propertiesFb.add(ModelProperty.Builder("ent", entResponse.getBody().getId()).multiple().build());
        ResponseEntity<ModelInfo> fbResponse = modelRepositoryController.createModel("test"+i+":MyTestFunctionBlock"+i+":1.1.0", ModelType.Functionblock, propertiesFb);
        fbSet.add(fbResponse.getBody().getId());
        ArrayList<ModelProperty> properties = createPropertyList(fbSet);
        modelRepositoryController.createModel("test"+i+":MyTestModel"+i+":1.1.0", ModelType.InformationModel, properties);

        return fbSet;
    }

    private ArrayList<ModelProperty> createPropertyList(Set<ModelId> fbSet) {
        ArrayList<ModelProperty> propertyList = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);
        fbSet.stream().forEach(modelId -> {
            propertyList.add(ModelProperty.Builder("fb"+counter.getAndIncrement(), modelId).multiple().build());
        });
        return propertyList;
    }

}
