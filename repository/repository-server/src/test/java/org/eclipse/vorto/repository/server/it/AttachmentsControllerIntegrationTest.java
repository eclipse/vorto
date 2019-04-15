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

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Test;
import org.springframework.http.MediaType;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class AttachmentsControllerIntegrationTest extends AbstractIntegrationTest {

  @Override
  protected void setUpTest() throws Exception {
    testModel = TestModel.TestModelBuilder.aTestModel().build();
    testModel.createModel(repositoryServer,userCreator);
  }
  
  @Test
  public void testRandomModelAttachmentController() throws Exception {
    String nonExistingNamespace = TestUtils.createRandomString(10).toLowerCase();
    repositoryServer.perform(get(
        "/api/v1/attachments/" + nonExistingNamespace + ":" + testModel.modelName + ":5000.0.0").with(userCreator))
        .andExpect(status().isNotFound());
    assertTrue(true);
  }

  @Test
  public void testAttachmentUpload() throws Exception {
    TestModel test = TestModel.TestModelBuilder.aTestModel().build();
    test.createModel(repositoryServer, userCreator);
    addAttachment(test.prettyName, userAdmin, "test.json", MediaType.APPLICATION_JSON)
        .andExpect(status().isOk());
    assertTrue(true);
  }

  @Test
  public void testInvalidAttachmentFileTypeUpload() throws Exception {
    TestModel test = TestModel.TestModelBuilder.aTestModel().build();
    test.createModel(repositoryServer, userAdmin);

    addAttachment(test.prettyName, userAdmin, "test.bin", MediaType.APPLICATION_JSON)
        .andExpect(result -> {
          JsonParser jsonParser = new JsonParser();
          JsonElement tree = jsonParser.parse(result.getResponse().getContentAsString());
          assert !tree.getAsJsonObject().get("success").getAsBoolean();
        });
   
    assertTrue(true);

  }

  @Test
  public void testAttachmentDeleteWithPermissions() throws Exception {
    String fileName = "test.json";
    TestModel deleteModel = TestModel.TestModelBuilder.aTestModel().build();
    // Create Model with extra user
    deleteModel.createModel(repositoryServer, userAdmin);

    // Add Attachment to Model
    addAttachment(deleteModel.prettyName, userAdmin, fileName, MediaType.APPLICATION_JSON)
        .andExpect(status().isOk());

    // Delete Attachment from model
    repositoryServer.perform(
        delete("/api/v1/attachments/" + deleteModel.prettyName + "/files/" + fileName).with(userAdmin))
        .andExpect(status().isOk());
    
    assertTrue(true);
  }

  @Test
  public void testAttachmentDeleteWithOutPermissions() throws Exception {
    String fileName = "test.json";
    TestModel deleteModel = TestModel.TestModelBuilder.aTestModel().build();
    // Create Model with extra user
    deleteModel.createModel(repositoryServer, userCreator);

    // Add Attachment to Model

    // Try to delete model
    repositoryServer.perform(
        delete("/api/v1/attachments/" + deleteModel.prettyName + "/files/" + fileName).with(userStandard))
        .andExpect(status().isForbidden());
    
    assertTrue(true);
  }

  @Test
  public void testAttachmentGetWithPermissions() throws Exception {
    repositoryServer.perform(get("/api/v1/attachments/" + testModel.prettyName).with(userAdmin))
        .andExpect(status().isOk());
    
    assertTrue(true);
  }
  
//  @Test
//  public void testAttachmentNoPermissions() throws Exception {
//	  addAttachment(testModel.prettyName, userAdmin, "test.json", MediaType.APPLICATION_JSON)
//      .andExpect(status().isOk());
//    repositoryServer.perform(get("/api/v1/attachments/" + testModel.prettyName).with(userStandard))
//        .andExpect(status().isForbidden());
//    
//    assertTrue(true);
//  }


}
