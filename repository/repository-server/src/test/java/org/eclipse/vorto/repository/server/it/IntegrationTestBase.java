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
package org.eclipse.vorto.repository.server.it;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.plugin.generator.adapter.ObjectMapperFactory;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.PolicyEntry;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.services.UserService;
import org.eclipse.vorto.repository.web.VortoRepository;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.eclipse.vorto.repository.web.api.v1.dto.ModelLink;
import org.eclipse.vorto.repository.web.api.v1.dto.OperationResult;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

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
@Sql("classpath:prepare_tables.sql")
public abstract class IntegrationTestBase {

  protected MockMvc repositoryServer;

  protected TestModel testModel;

  /**
   * Since this is set to 2 in test profile for reasons unclear, taking value directly from config
   * to test cases where users create an excessive number of namespaces.
   */
  @Value("${config.privateNamespaceQuota}")
  protected String privateNamespaceQuota;

  @Autowired
  protected WebApplicationContext wac;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected UserService userService;

  @Autowired
  protected INotificationService notificationService;

  @LocalServerPort
  protected int port;

  protected SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userSysadmin;
  protected SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userSysadmin2;
  protected SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userModelCreator;
  protected SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userModelCreator2;
  protected SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userModelCreator3;
  protected SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userModelViewer;

  protected ObjectMapper objectMapper = new ObjectMapper();

  protected static final String USER_SYSADMIN_NAME = "userSysadmin";
  protected static final String USER_SYSADMIN_NAME_2 = "userSysadmin2";
  protected static final String USER_MODEL_CREATOR_NAME = "userModelCreator";
  protected static final String USER_MODEL_CREATOR_NAME_2 = "userModelCreator2";
  protected static final String USER_MODEL_CREATOR_NAME_3 = "userModelCreator3";
  protected static final String USER_MODEL_VIEWER_NAME = "userModelViewer";
  protected static final String BOSCH_IOT_SUITE_AUTH = "BOSCH-IOT-SUITE-AUTH";
  protected static final String GITHUB = "GITHUB";

  @Configuration
  @Profile("test")
  public static class TestConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
      return new PropertySourcesPlaceholderConfigurer();
    }

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
    System.setProperty("line.separator", "\n");
  }

  @Before
  public void setUpTest() throws Exception {
    repositoryServer = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    GrantedAuthority sysadminAuthority = new SimpleGrantedAuthority("sysadmin");
    GrantedAuthority modelViewerAuthority = new SimpleGrantedAuthority("model_viewer");
    GrantedAuthority modelCreatorAuthority = new SimpleGrantedAuthority("model_creator");
    userSysadmin = user(USER_SYSADMIN_NAME).password("pass").authorities(sysadminAuthority);
    userSysadmin2 = user(USER_SYSADMIN_NAME_2).password("pass").authorities(sysadminAuthority);
    userModelCreator = user(USER_MODEL_CREATOR_NAME).password("pass")
        .authorities(modelCreatorAuthority);
    userModelCreator2 = user(USER_MODEL_CREATOR_NAME_2).password("pass")
        .authorities(modelCreatorAuthority);
    userModelCreator3 = user(USER_MODEL_CREATOR_NAME_3).password("pass")
        .authorities(modelCreatorAuthority);
    userModelViewer = user(USER_MODEL_VIEWER_NAME).password("pass")
        .authorities(modelViewerAuthority);
    testModel = TestModel.TestModelBuilder.aTestModel().build();
    createNamespaceSuccessfully("com.mycompany", userSysadmin);
    addCollaboratorToNamespace("com.mycompany", userModelCreatorCollaborator());
    testModel.createModel(repositoryServer, userSysadmin);
  }

  protected Collaborator userModelCreatorCollaborator() {
    Collaborator collaborator = new Collaborator();
    collaborator.setUserId(USER_MODEL_CREATOR_NAME);
    collaborator.setRoles(Sets.newHashSet("model_creator", "model_viewer"));
    collaborator.setAuthenticationProviderId("GITHUB");
    collaborator.setTechnicalUser(false);
    return collaborator;
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
            assertEquals(Arrays.asList(roles), target.getRoles());
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
                objectMapper.writeValueAsString(OperationResult.success())
            )
        );
  }

  protected void createModel(SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user,
      String fileName, String modelId) throws Exception {

    repositoryServer
        .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
            .with(user).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    repositoryServer
        .perform(put("/rest/models/" + modelId).with(user)
            .contentType(MediaType.APPLICATION_JSON).content(createContent(fileName)))
        .andExpect(status().isOk());
  }

  protected String createContent(String fileName) throws Exception {
    String dslContent =
        IOUtils.toString(new ClassPathResource("models/" + fileName).getInputStream());

    Map<String, Object> content = new HashMap<>();
    content.put("contentDsl", dslContent);
    content.put("type", ModelType.fromFileName(fileName));

    return new GsonBuilder().create().toJson(content);
  }

  protected ResultActions addAttachment(String modelId,
      SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user, String fileName,
      MediaType mediaType, Integer size) throws Exception {
    MockMultipartFile file =
        new MockMultipartFile("file", fileName, mediaType.toString(),
            size == null ? "{\"test\":123}".getBytes() : new byte[size]);
    MockMultipartHttpServletRequestBuilder builder =
        MockMvcRequestBuilders.fileUpload("/api/v1/attachments/" + modelId);
    return repositoryServer.perform(builder.file(file).with(request -> {
      request.setMethod("PUT");
      return request;
    }).contentType(MediaType.MULTIPART_FORM_DATA).with(user));
  }

  protected ResultActions addLink(String modelId,
      SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user, ModelLink url)
      throws Exception {
    MockHttpServletRequestBuilder builder =
        MockMvcRequestBuilders.put("/api/v1/attachments/" + modelId + "/links");
    builder.content(ObjectMapperFactory.getInstance().writeValueAsString(url));
    return repositoryServer.perform(builder.with(request -> {
      request.setMethod("PUT");
      return request;
    }).contentType(MediaType.APPLICATION_JSON).with(user));
  }

  protected ResultActions deleteLink(String modelId,
      SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user, ModelLink url)
      throws Exception {
    MockHttpServletRequestBuilder builder =
        MockMvcRequestBuilders.delete("/api/v1/attachments/" + modelId + "/links");
    builder.content(ObjectMapperFactory.getInstance().writeValueAsString(url));
    return repositoryServer.perform(builder.with(request -> {
      request.setMethod("DELETE");
      return request;
    }).contentType(MediaType.APPLICATION_JSON).with(user));
  }

  protected ResultActions addAttachment(String modelId,
      SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user, String fileName,
      MediaType mediaType) throws Exception {
    return addAttachment(modelId, user, fileName, mediaType, null);
  }

  protected ResultActions createImage(String filename, String modelId,
      SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user, Integer size)
      throws Exception {
    MockMultipartFile file = null;
    if (size == null) {
      file = new MockMultipartFile("file", filename, MediaType.IMAGE_PNG_VALUE,
          getClass().getClassLoader().getResourceAsStream("models/" + filename));
    } else {
      file = new MockMultipartFile("file", filename, MediaType.IMAGE_PNG_VALUE, new byte[size]);
    }

    MockMultipartHttpServletRequestBuilder builder =
        MockMvcRequestBuilders.fileUpload("/rest/models/" + modelId + "/images");
    return repositoryServer.perform(builder.file(file).with(request -> {
      request.setMethod("POST");
      return request;
    }).contentType(MediaType.MULTIPART_FORM_DATA).with(user));
  }

  protected ResultActions createImage(String filename, String modelId,
      SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user) throws Exception {
    return createImage(filename, modelId, user, null);
  }

  protected void setPublic(String modelId) throws Exception {
    PolicyEntry publicPolicyEntry = new PolicyEntry();
    publicPolicyEntry.setPrincipalId(IModelPolicyManager.ANONYMOUS_ACCESS_POLICY);
    publicPolicyEntry.setPermission(PolicyEntry.Permission.READ);
    publicPolicyEntry.setPrincipalType(PolicyEntry.PrincipalType.User);

    String publicPolicyEntryStr = new Gson().toJson(publicPolicyEntry);

    repositoryServer.perform(put("/rest/models/" + modelId + "/policies")
        .with(userSysadmin).contentType(MediaType.APPLICATION_JSON).content(publicPolicyEntryStr));
  }

  /**
   * This is a wrapper to allow comparison of JSON payloads involving {@link ModelId}, as it features
   * a public method {@link ModelId#getPrettyFormat()} that does not have a correspective field. <br/>
   * Therefore, expecting {@link ModelId} alone will fail when comparing JSON content because the
   * actual response will contain the pretty format string whereas the expectation will not.
   */
  public static class SerializableModelId extends ModelId {

    protected String prettyFormat;

    protected SerializableModelId(ModelId from) {
      this.prettyFormat = from.getPrettyFormat();
      this.setName(from.getName());
      this.setNamespace(from.getNamespace());
      this.setVersion(from.getVersion());
    }
  }
}
