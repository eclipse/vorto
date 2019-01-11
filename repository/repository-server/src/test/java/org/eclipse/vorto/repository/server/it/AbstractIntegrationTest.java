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
package org.eclipse.vorto.repository.server.it;

import static java.lang.Thread.sleep;
import static org.eclipse.vorto.repository.account.Role.ADMIN;
import static org.eclipse.vorto.repository.account.Role.MODEL_CREATOR;
import static org.eclipse.vorto.repository.account.Role.MODEL_PROMOTER;
import static org.eclipse.vorto.repository.account.Role.MODEL_REVIEWER;
import static org.eclipse.vorto.repository.account.Role.USER;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.eclipse.vorto.repository.web.VortoRepository;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest(classes = VortoRepository.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//https://github.com/spring-projects/spring-boot/issues/12280
@TestPropertySource(properties = { "repo.configFile = vorto-repository-config-h2.json" })
public abstract class AbstractIntegrationTest {

  protected MockMvc repositoryServer;
  protected TestModel testModel;
  
  @Autowired
  protected WebApplicationContext wac;

  @LocalServerPort
  protected int port;
  
  protected SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userAdmin;
  protected SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userStandard;
  protected SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userCreator;

  protected Gson gson = new Gson();

  @BeforeClass
  public static void configureOAuthConfiguration() {
    System.setProperty("github_clientid", "foo");
    System.setProperty("github_clientSecret", "foo");
    System.setProperty("eidp_clientid", "foo");
    System.setProperty("eidp_clientSecret", "foo");
  }

  @Before
  public void startUpServer() throws Exception {
    repositoryServer = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    userAdmin = user("user1").password("pass")
        .authorities(SpringUserUtils.toAuthorityList(Sets.newHashSet(USER, ADMIN, MODEL_CREATOR,MODEL_PROMOTER,MODEL_REVIEWER)));
    userStandard = user("user2").password("pass")
        .authorities(SpringUserUtils.toAuthorityList(Sets.newHashSet(USER)));
    userCreator = user("user3").password("pass")
        .authorities(SpringUserUtils.toAuthorityList(Sets.newHashSet(USER,MODEL_CREATOR)));
    
    setUpTest();
  }

  public void deleteModel(String modelId) throws Exception {
    repositoryServer.perform(delete("/rest/default/models/" + modelId).with(userAdmin)
        .contentType(MediaType.APPLICATION_JSON));
  }
  
  protected abstract void setUpTest() throws Exception;
  
  
  public void createModel(String fileName, String modelId) throws Exception {
    createModel(userAdmin, fileName, modelId);
  }
  
  public void releaseModel(String modelId) throws Exception {
    repositoryServer.perform(put("/rest/default/workflows/" + modelId + "/actions/Release").with(userAdmin)
        .contentType(MediaType.APPLICATION_JSON));
    
    repositoryServer.perform(put("/rest/default/workflows/" + modelId + "/actions/Approve").with(userAdmin)
        .contentType(MediaType.APPLICATION_JSON));
  }
  
  protected void createModel(SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user,
      String fileName, String modelId) throws Exception {
    repositoryServer.perform(post("/rest/default/models/" + modelId + "/"+ModelType.fromFileName(fileName)).with(user)
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    repositoryServer.perform(put("/rest/default/models/" + modelId).with(user)
        .contentType(MediaType.APPLICATION_JSON).content(createContent(fileName))).andExpect(status().isOk());
  }

  protected String createContent(String fileName) throws Exception {
    String dslContent = IOUtils.toString(new ClassPathResource("models/"+fileName).getInputStream());
    
    Map<String,Object> content = new HashMap<>();
    content.put("contentDsl", dslContent);
    content.put("type",ModelType.fromFileName(fileName));

    return new GsonBuilder().create().toJson(content);
  }
  
  public ResultActions addAttachment(String modelId,
      SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user, String fileName,
      MediaType mediaType) throws Exception {
    MockMultipartFile file =
        new MockMultipartFile("file", fileName, mediaType.toString(), "{\"test\":123}".getBytes());
    MockMultipartHttpServletRequestBuilder builder =
        MockMvcRequestBuilders.fileUpload("/api/v1/attachments/" + modelId);
    return repositoryServer.perform(builder.file(file).with(request -> {
      request.setMethod("PUT");
      return request;
    }).contentType(MediaType.MULTIPART_FORM_DATA).with(user));
  }

}
