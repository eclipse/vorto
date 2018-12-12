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

import static org.eclipse.vorto.repository.account.Role.ADMIN;
import static org.eclipse.vorto.repository.account.Role.MODEL_CREATOR;
import static org.eclipse.vorto.repository.account.Role.USER;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.eclipse.vorto.repository.web.VortoRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest(classes = VortoRepository.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//https://github.com/spring-projects/spring-boot/issues/12280
public abstract class AbstractIntegrationTest {

  protected MockMvc mockMvc;
  protected TestModel testModel;
  
  @Autowired
  protected WebApplicationContext wac;
  
  @LocalServerPort
  protected int port;
  
  @BeforeClass
  public static void configureOAuthConfiguration() {
    System.setProperty("github_clientid", "foo");
    System.setProperty("github_clientSecret", "foo");
    System.setProperty("eidp_clientid", "foo");
    System.setProperty("eidp_clientSecret", "foo");
  }
  
  @Before
  public void startUpServer() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    setUpTest();
  }
  
  protected abstract void setUpTest() throws Exception;
  
  
  public void createModel(String fileName, String modelId) throws Exception {
    SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
        user("admin").password("pass").authorities(
            SpringUserUtils.toAuthorityList(Sets.newHashSet(ADMIN, MODEL_CREATOR, USER)));
    
    createModel(user, modelId,fileName);
  }
  
  private void createModel(SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user, String modelId, String fileName) throws Exception {
    mockMvc.perform(put("/rest/default/models/" + modelId).with(user)
        .contentType(MediaType.APPLICATION_JSON).content(createContent(modelId,fileName))).andExpect(status().isCreated());
  }

  private String createContent(String modelId, String fileName) throws Exception {
    String dslContent = IOUtils.toString(new ClassPathResource(fileName).getInputStream());
    
    Map<String,Object> content = new HashMap<>();
    content.put("contentDsl", dslContent);
    content.put("type",ModelType.fromFileName(fileName));

    return new GsonBuilder().create().toJson(content);
  }
}
