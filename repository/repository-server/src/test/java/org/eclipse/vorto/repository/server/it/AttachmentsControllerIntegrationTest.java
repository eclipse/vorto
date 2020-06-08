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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.impl.validation.AttachmentValidator;
import org.eclipse.vorto.repository.web.api.v1.dto.AttachResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AttachmentsControllerIntegrationTest extends IntegrationTestBase {

  @Autowired
  private AttachmentValidator attachmentValidator;

  private Gson gson = new GsonBuilder().serializeNulls().create();

  @Test
  public void testRandomModelAttachmentController() throws Exception {
    String nonExistingNamespace = TestUtils.createRandomString(10).toLowerCase();
    repositoryServer.perform(get(
        "/api/v1/attachments/" + nonExistingNamespace + ":" + testModel.modelName + ":5000.0.0").with(userModelCreator))
        .andExpect(status().isNotFound());
    
  }

  @Test
  public void testAttachmentUpload() throws Exception {
    addAttachment(testModel.prettyName, userSysadmin, "test.json", MediaType.APPLICATION_JSON)
        .andExpect(status().isOk());
  }

  @Test
  public void testLargeAttachmentUpload() throws Exception {
    // we set the "fake" size of the attachment as a 1MB more than the configured maximum file size
    // setting for the profile, to ensure uploading fails
    addAttachment(
        testModel.prettyName, userSysadmin, "test.json", MediaType.APPLICATION_JSON,
        (attachmentValidator.getMaxFileSizeSetting() + 1) * 1024 * 1024)
        // unfortunately the v1 controller returns ok even when in error and it cannot be changed
        .andExpect(status().isOk())
        .andExpect(
          content().json(
            gson.toJson(
              AttachResult.fail(
                testModel.getId(),
                "test.json",
                String.format(
                  "The attachment is too large. Maximum size allowed is %dMB",
                  attachmentValidator.getMaxFileSizeSetting()
                )
              )
            )
         )
        );
  }

  @Test
  public void testReasonableSizeAttachmentUpload() throws Exception {
    TestModel test = TestModel.TestModelBuilder.aTestModel().withNamespace(testModel.namespace).build();
    test.createModel(repositoryServer, userModelCreator);

    // we set the "fake" size of the attachment as a 1MB less than the configured maximum file size
    // setting for the profile, to ensure uploading fails
    addAttachment(
        test.prettyName, userSysadmin, "test.json", MediaType.APPLICATION_JSON,
        (attachmentValidator.getMaxFileSizeSetting() - 1) * 1024 * 1024)
        // unfortunately the v1 controller returns ok even when in error and it cannot be changed
        .andExpect(status().isOk())
        .andExpect(
            content().json(
                gson.toJson(
                    AttachResult.success(
                        new SerializableModelId(ModelId.fromPrettyFormat(test.prettyName)),
                        "test.json"
                    )
                )
            )
        );
  }

  @Test
  public void testInvalidAttachmentFileTypeUpload() throws Exception {
    TestModel test = TestModel.TestModelBuilder.aTestModel().build();
    test.createModel(repositoryServer, userSysadmin);

    addAttachment(test.prettyName, userSysadmin, "test.bin", MediaType.APPLICATION_JSON)
        .andExpect(result -> {
          JsonParser jsonParser = new JsonParser();
          JsonElement tree = jsonParser.parse(result.getResponse().getContentAsString());
          assert !tree.getAsJsonObject().get("success").getAsBoolean();
        });
   
    

  }

  @Test
  public void testAttachmentDeleteWithPermissions() throws Exception {
    String fileName = "test.json";
    TestModel deleteModel = TestModel.TestModelBuilder.aTestModel().build();
    // Create Model with extra user
    deleteModel.createModel(repositoryServer, userSysadmin);

    // Add Attachment to Model
    addAttachment(deleteModel.prettyName, userSysadmin, fileName, MediaType.APPLICATION_JSON)
        .andExpect(status().isOk());

    // Delete Attachment from model
    repositoryServer.perform(
        delete("/api/v1/attachments/" + deleteModel.prettyName + "/files/" + fileName).with(userSysadmin))
        .andExpect(status().isOk());
    
    
  }

  @Test
  public void testAttachmentDeleteWithOutPermissions() throws Exception {
    String fileName = "test.json";
    TestModel deleteModel = TestModel.TestModelBuilder.aTestModel().build();
    // Create Model with extra user
    deleteModel.createModel(repositoryServer, userModelCreator);

    // Add Attachment to Model

    // Try to delete model
    repositoryServer.perform(
        delete("/api/v1/attachments/" + deleteModel.prettyName + "/files/" + fileName).with(
            userModelViewer))
        .andExpect(status().isForbidden());
    
    
  }

  @Test
  public void testAttachmentGetWithPermissions() throws Exception {
    repositoryServer.perform(get("/api/v1/attachments/" + testModel.prettyName).with(userSysadmin))
        .andExpect(status().isOk());
    
    
  }

}
