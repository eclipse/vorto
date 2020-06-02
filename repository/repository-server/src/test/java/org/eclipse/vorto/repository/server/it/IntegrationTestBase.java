package org.eclipse.vorto.repository.server.it;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.services.UserService;
import org.eclipse.vorto.repository.web.VortoRepository;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceOperationResult;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {"test"})
@SpringBootTest(classes = VortoRepository.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application-test.yml"})
@ContextConfiguration(initializers = {ConfigFileApplicationContextInitializer.class})
public abstract class IntegrationTestBase {

    @Configuration
    @Profile("test")
    public static class TestConfig {

        @Bean
        public static PropertySourcesPlaceholderConfigurer properties() {
            return new PropertySourcesPlaceholderConfigurer();
        }

    }

    protected MockMvc repositoryServer;

    @Autowired
    protected WebApplicationContext wac;

    @LocalServerPort
    protected int port;

    protected SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userSysadmin;
    protected SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userModelCreator;
    protected SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userModelCreator2;

    @BeforeClass
    public static void configureOAuthConfiguration() {
        System.setProperty("github_clientid", "foo");
        System.setProperty("github_clientSecret", "foo");
        System.setProperty("eidp_clientid", "foo");
        System.setProperty("eidp_clientSecret", "foo");
        System.setProperty("line.separator", "\n");
    }

    protected ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Since this is set to 2 in test profile for reasons unclear, taking value directly from config
     * to test cases where users create an excessive number of namespaces.
     */
    @Value("${config.privateNamespaceQuota}")
    protected String privateNamespaceQuota;

    protected static final String USER_SYSADMIN_NAME = "userSysadmin";
    protected static final String USER_MODEL_CREATOR_NAME = "userModelCreator";
    protected static final String USER_MODEL_CREATOR_2_NAME = "userModelCreator2";
    protected static final String BOSCH_IOT_SUITE_AUTH = "BOSCH-IOT-SUITE-AUTH";
    protected static final String GITHUB = "GITHUB";

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserService userService;

    @Before
    public void startUpServer() {
        repositoryServer = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
        userSysadmin = user(USER_SYSADMIN_NAME).password("pass");
        userModelCreator = user(USER_MODEL_CREATOR_NAME).password("pass");
        userModelCreator2 = user(USER_MODEL_CREATOR_2_NAME).password("pass");
    }

    protected void checkCollaboratorRoles(String namespaceName, String collaboratorName,
        String... roles) throws Exception {
        repositoryServer.perform(
            get(String.format("/rest/namespaces/%s/users", namespaceName))
                .with(userSysadmin)
        )
            .andDo(handler -> {
                Collection<Collaborator> collaborators = objectMapper
                    .readValue(handler.getResponse().getContentAsString(),
                        new TypeReference<Collection<Collaborator>>() {
                        });
                Optional<Collaborator> maybeTarget = collaborators.stream()
                    .filter(c -> c.getUserId().equals(collaboratorName)).findAny();
                assertTrue(maybeTarget.isPresent());
                if (maybeTarget.isPresent()) {
                    Collaborator target = maybeTarget.get();
                    assertEquals(target.getRoles(), Arrays.asList(roles));
                }
            });
    }

    protected void createTechnicalUserAndAddToNamespace(String namespace, Collaborator technicalUser)
        throws Exception {
        repositoryServer.perform(
            post(String.format("/rest/namespaces/%s/users", namespace))
                .content(objectMapper.writeValueAsString(technicalUser))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userSysadmin))
            .andExpect(status().isCreated());
    }


    protected void addCollaboratorToNamespace(String namespace, Collaborator collaborator)
        throws Exception {
        repositoryServer.perform(
            put(String.format("/rest/namespaces/%s/users", namespace))
                .content(objectMapper.writeValueAsString(collaborator))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userSysadmin))
            .andExpect(status().isOk());
    }

    protected void createNamespaceSuccessfully(String namespaceName,
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor actingUser) throws Exception {
        // creates namespace first
        repositoryServer
            .perform(
                put(String.format("/rest/namespaces/%s", namespaceName))
                   .with(actingUser)
            )
            .andExpect(status().isCreated())
            .andExpect(
                content().json(
                    objectMapper.writeValueAsString(NamespaceOperationResult.success())
                )
            );
    }
}
