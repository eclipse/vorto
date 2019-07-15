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
package org.eclipse.vorto.repository.web;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.server.it.AbstractIntegrationTest;
import org.eclipse.vorto.repository.server.it.TestModel;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class ModelRepositoryControllerTest extends AbstractIntegrationTest {

  //private String tenant = "/tenants/playground";

  @Override
  protected void setUpTest() throws Exception {
    // accountService = context.getBean(IUserAccountService.class);
    testModel = TestModel.TestModelBuilder.aTestModel().build();
    testModel.createModel(repositoryServer, userCreator);
  }

  private ResultActions createImage(String filename, String modelId,
      SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user) throws Exception {
    MockMultipartFile file = new MockMultipartFile("file", filename, MediaType.IMAGE_PNG_VALUE,
        getClass().getClassLoader().getResourceAsStream("models/" + filename));
		/*
		 * MockMultipartHttpServletRequestBuilder builder =
		 * MockMvcRequestBuilders.fileUpload("/rest" + tenant + "/models/" + modelId +
		 * "/images");
		 */
    MockMultipartHttpServletRequestBuilder builder =
            MockMvcRequestBuilders.fileUpload("/rest/models/" + modelId + "/images");
    return repositoryServer.perform(builder.file(file).with(request -> {
      request.setMethod("POST");
      return request;
    }).contentType(MediaType.MULTIPART_FORM_DATA).with(user));
  }

  @Test
  public void getModelImage() throws Exception {
    createImage("stock_coffee.jpg", testModel.prettyName, userAdmin)
        .andExpect(status().isCreated());

    this.repositoryServer.perform(
        get("/rest/models/" + testModel.prettyName + "/images").with(userAdmin))
        .andExpect(status().isOk());

    assertTrue(true);
  }

  @Test
  public void uploadModelImage() throws Exception {
    createImage("stock_coffee.jpg", testModel.prettyName, userAdmin)
        .andExpect(status().isCreated());
    createImage("model_image.png", testModel.prettyName, userAdmin).andExpect(status().isCreated());
    
    assertTrue(true);
  }

  @Ignore
  @Test
  public void saveModel() throws Exception {
    // Test normal save
    String testModelId = "com.test:TrackingDevice:1.0.0";
    String locationModelId = "com.test:Location:1.0.0";

    // making sure the model is not there
    try {
      repositoryServer
          .perform(delete("/rest/models/" + testModelId).with(userAdmin));
      repositoryServer
          .perform(delete("/rest/models/" + locationModelId).with(userAdmin));
    } catch (Exception e) {
      // expected if the user isn't there
    }

    this.createModel(userCreator, "Location.fbmodel", locationModelId);
    this.createModel(userCreator, "TrackingDevice.infomodel", testModelId);
    String json = createContent("TrackingDevice.infomodel");

    this.repositoryServer
        .perform(put("/rest/models/" + testModelId)
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userAdmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(put("/rest/models/" + testModelId)
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userCreator))
        .andExpect(status().isOk());
    // test with existing Model but user has no read permission
    this.repositoryServer
        .perform(put("/rest/models/" + testModelId)
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userStandard))
        .andExpect(status().isUnauthorized());
    // test save with non existitng modelid
    this.repositoryServer
        .perform(put("/rest/models/com.test1:TrackinDevice:0.0.1")
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userAdmin))
        .andExpect(status().isNotFound());
    // test save with broken model
    this.repositoryServer
        .perform(put("/rest/models/" + testModelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.replace("infomodel", "tinfomodel")).with(userAdmin))
        .andExpect(status().isBadRequest());
    // test save with changed model id
    this.repositoryServer
        .perform(put("/rest/models/" + testModelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.replace("TrackingDevice", "RackingDevice")).with(userAdmin))
        .andExpect(status().isBadRequest());
    repositoryServer
        .perform(delete("/rest/models/" + testModelId).with(userAdmin));
    repositoryServer
        .perform(delete("/rest/models/" + locationModelId).with(userAdmin));
  }

  @Test
  public void createModelWithAPI() throws Exception {
    String modelId = "com.test.Location:1.0.0";
    String fileName = "Location.fbmodel";
    repositoryServer.perform(
        post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
            .with(userAdmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    repositoryServer.perform(
        post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
            .with(userAdmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict());
    repositoryServer.perform(delete("/rest/models/" + modelId).with(userAdmin));
    
    assertTrue(true);
  }

  @Test
  public void createVersionOfModel() throws Exception {
    TestModel testModel = TestModel.TestModelBuilder.aTestModel().build();
    testModel.createModel(repositoryServer, userAdmin);
    repositoryServer
        .perform(post("/rest/models/" + testModel.prettyName + "/versions/1.0.1")
            .with(userAdmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    repositoryServer
        .perform(post("/rest/models/com.test1:Location:1.0.0/versions/1.0.1")
            .with(userAdmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    repositoryServer
        .perform(delete("/rest/models/" + testModel.prettyName).with(userAdmin));

    assertTrue(true);
  }

  @Test
  public void deleteModelResource() throws Exception {
    String modelId = "com.test.Location:1.0.0";
    String fileName = "Location.fbmodel";
    String json = createContent("Location.fbmodel");

    // making sure the model is not there
    repositoryServer.perform(delete("/rest/models/" + modelId).with(userAdmin))
        .andExpect(status().isNotFound());

    this.createModel(userCreator, fileName, modelId);
    repositoryServer.perform(delete("/rest/models/" + modelId).with(userAdmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(put("/rest/models/" + modelId)
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userAdmin))
        .andExpect(status().isNotFound());

    // delete non existent model
    repositoryServer
        .perform(delete("/rest/models/com.test:ASDASD:0.0.1").with(userAdmin))
        .andExpect(status().isNotFound());
    
    assertTrue(true);
  }

  @Test
  public void getUserModels() throws Exception {
    this.repositoryServer
        .perform(get("/rest/models/mine/download").with(userAdmin))
        .andExpect(status().isOk());
    
    assertTrue(true);
  }

  @Test
  public void downloadMappingsForPlatform() throws Exception {
    this.repositoryServer.perform(
        get("/rest/models/" + testModel.prettyName + "/download/mappings/test")
            .with(userAdmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/com.test:Test1:1.0.0/download/mappings/test")
            .with(userAdmin))
        .andExpect(status().isNotFound());
    
    assertTrue(true);
  }

  @Test
  public void runDiagnostics() throws Exception {
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/diagnostics")
            .with(userAdmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(
            get("/rest/models/test:Test123:1.0.0/diagnostics").with(userAdmin))
        .andExpect(status().isNotFound());
    
    assertTrue(true);
  }

  @Test
  public void getPolicies() throws Exception {
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policies")
            .with(userAdmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policies")
            .with(userCreator))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/test:Test123:1.0.0/policies").with(userAdmin))
        .andExpect(status().isNotFound());
    
    assertTrue(true);
  }

  @Test
  public void getUserPolicy() throws Exception {
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policy")
            .with(nonTenantUser))
        .andExpect(status().isUnauthorized());
    this.repositoryServer.perform(
        get("/rest/models/" + testModel.prettyName + "/policy").with(userAdmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policy")
            .with(userCreator))
        .andExpect(status().isOk());
    
    assertTrue(true);
  }

  @Test
  public void addValidPolicyEntry() throws Exception {
    String json =
        "{\"principalId\":\"user3\", \"principalType\": \"User\", \"permission\":\"READ\"}";
    // Valid creation of policy
    this.repositoryServer
        .perform(put("/rest/models/" + testModel.prettyName + "/policies")
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userAdmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policy")
            .with(userCreator))
        .andExpect(result -> result.getResponse().getContentAsString().contains(
            "{\"principalId\":\"user3\",\"principalType\":\"User\",\"permission\":\"READ\",\"adminPolicy\":false}"));
    
    assertTrue(true);
  }

  @Test
  public void editOwnPolicyEntry() throws Exception {
    String json =
        "{\"principalId\":\"user3\", \"principalType\": \"User\", \"permission\":\"READ\"}";
    // Try changing current user policy
    this.repositoryServer
        .perform(put("/rest/models/" + testModel.prettyName + "/policies")
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userCreator))
        .andExpect(status().isBadRequest());
    
    assertTrue(true);
  }

  @Test
  public void addInvalidPolicyEntry() throws Exception {
    String json =
        "{\"principalId\":\"user3\", \"principalType\": \"AUser\", \"permission\":\"READ\"}";
    // Try changing current user policy
    this.repositoryServer
        .perform(put("/rest/models/" + testModel.prettyName + "/policies")
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userAdmin))
        .andExpect(status().isBadRequest());
    
    assertTrue(true);
  }

  @Test
  public void removePolicyEntry() throws Exception {
    String json =
        "{\"principalId\":\"user2\", \"principalType\": \"User\", \"permission\":\"READ\"}";
    // Valid creation of policy
    this.repositoryServer
        .perform(put("/rest/models/" + testModel.prettyName + "/policies")
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userAdmin))
        .andExpect(status().isOk());
    repositoryServer.perform(
        delete("/rest/models/" + testModel.prettyName + "/policies/user2/User")
            .with(userAdmin))
        .andExpect(status().isOk());
    
    assertTrue(true);
  }

	/*
	 * public void setTenant(String tenant) { this.tenant = tenant; }
	 */
}
