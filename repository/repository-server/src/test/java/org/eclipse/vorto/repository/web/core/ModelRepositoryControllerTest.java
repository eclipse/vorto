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
package org.eclipse.vorto.repository.web.core;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.server.it.AbstractIntegrationTest;
import org.eclipse.vorto.repository.server.it.TestModel;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class ModelRepositoryControllerTest extends AbstractIntegrationTest {


    @Override protected void setUpTest() throws Exception {
        //accountService = context.getBean(IUserAccountService.class);
        testModel = TestModel.TestModelBuilder.aTestModel().build();
        testModel.createModel(repositoryServer, userCreator);
    }

    private ResultActions createImage(String filename, String modelId,
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user) throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", filename, MediaType.IMAGE_PNG_VALUE,
            getClass().getClassLoader().getResourceAsStream("models/" + filename));
        MockMultipartHttpServletRequestBuilder builder =
            MockMvcRequestBuilders.fileUpload("/rest/default/models/" + modelId + "/images");
        return repositoryServer.perform(builder.file(file).with(request -> {
            request.setMethod("POST");
            return request;
        }).contentType(MediaType.MULTIPART_FORM_DATA).with(user));
    }

    @Test public void getModelImage() throws Exception {
        createImage("stock_coffee.jpg", testModel.prettyName, userAdmin)
            .andExpect(status().isCreated());

        this.repositoryServer.perform(
            get("/rest/default/models/" + testModel.prettyName + "/images").with(userAdmin))
            .andExpect(status().isOk());

    }

    @Test public void uploadModelImage() throws Exception {
        createImage("stock_coffee.jpg", testModel.prettyName, userAdmin)
            .andExpect(status().isCreated());
        createImage("model_image.png", testModel.prettyName, userAdmin)
            .andExpect(status().isCreated());
    }

    @Test public void saveModel() throws Exception {
        //Test normal save
        String testModelId = "com.test:TrackingDevice:1.0.0";
        String locationModelId = "com.test:Location:1.0.0";
        //making sure the model is not there
        repositoryServer.perform(delete("/rest/default/models/" + testModelId).with(userAdmin));
        repositoryServer.perform(delete("/rest/default/models/" + locationModelId).with(userAdmin));
        this.createModel(userCreator, "Location.fbmodel", locationModelId);
        this.createModel(userCreator, "TrackingDevice.infomodel", testModelId);
        String json = createContent("TrackingDevice.infomodel");

        this.repositoryServer.perform(
            put("/rest/default/models/" + testModelId).contentType(MediaType.APPLICATION_JSON)
                .content(json).with(userAdmin)).andExpect(status().isOk());
        this.repositoryServer.perform(
            put("/rest/default/models/" + testModelId).contentType(MediaType.APPLICATION_JSON)
                .content(json).with(userCreator)).andExpect(status().isOk());
        // test with existing Model but user has no read permission
        this.repositoryServer.perform(
            put("/rest/default/models/" + testModelId).contentType(MediaType.APPLICATION_JSON)
                .content(json).with(userStandard)).andExpect(status().isNotFound());
        // test save with non existitng modelid
        this.repositoryServer.perform(put("/rest/default/models/com.test1:TrackinDevice:0.0.1")
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userAdmin))
            .andExpect(status().isNotFound());
        // test save with broken model
        this.repositoryServer.perform(
            put("/rest/default/models/" + testModelId).contentType(MediaType.APPLICATION_JSON)
                .content(json.replace("infomodel", "tinfomodel")).with(userAdmin))
            .andExpect(status().isBadRequest());
        // test save with changed model id
        this.repositoryServer.perform(
            put("/rest/default/models/" + testModelId).contentType(MediaType.APPLICATION_JSON)
                .content(json.replace("TrackingDevice", "RackingDevice")).with(userAdmin))
            .andExpect(status().isBadRequest());
        repositoryServer.perform(delete("/rest/default/models/" + testModelId).with(userAdmin));
        repositoryServer.perform(delete("/rest/default/models/" + locationModelId).with(userAdmin));
    }

    @Test public void createModelWithAPI() throws Exception {
        String modelId = "com.test.Location:1.0.0";
        String fileName = "Location.fbmodel";
        repositoryServer.perform(
            post("/rest/default/models/" + modelId + "/" + ModelType.fromFileName(fileName))
                .with(userAdmin).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
        repositoryServer.perform(
            post("/rest/default/models/" + modelId + "/" + ModelType.fromFileName(fileName))
                .with(userAdmin).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
        repositoryServer.perform(delete("/rest/default/models/" + modelId).with(userAdmin));
    }

    @Test public void createVersionOfModel() throws Exception {
        TestModel testModel = TestModel.TestModelBuilder.aTestModel().build();
        testModel.createModel(repositoryServer,userAdmin);
        repositoryServer.perform(
            post("/rest/default/models/" + testModel.prettyName + "/versions/1.0.1").with(userAdmin)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        repositoryServer.perform(
            post("/rest/default/models/com.test1:Location:1.0.0/versions/1.0.1").with(userAdmin)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
        repositoryServer.perform(delete("/rest/default/models/" + testModel.prettyName).with(userAdmin));

    }

    @Test public void deleteModelResource() throws Exception {
        String modelId = "com.test.Location:1.0.0";
        String fileName = "Location.fbmodel";
        String json = createContent("Location.fbmodel");
        //making sure the model is not there
        repositoryServer.perform(delete("/rest/default/models/" + modelId).with(userAdmin));
        this.createModel(userCreator, fileName, modelId);
        repositoryServer.perform(delete("/rest/default/models/" + modelId).with(userAdmin))
            .andExpect(status().isOk());
        this.repositoryServer.perform(
            put("/rest/default/models/" + modelId).contentType(MediaType.APPLICATION_JSON)
                .content(json).with(userAdmin)).andExpect(status().isNotFound());

        //delete non existent model
        repositoryServer.perform(delete("/rest/default/models/com.test:ASDASD:0.0.1").with(userAdmin));
    }

    @Test public void getUserModels() throws Exception {
        this.repositoryServer.perform(get("/rest/default/models/mine/download").with(userAdmin))
            .andExpect(status().isOk());
    }

    @Test public void downloadMappingsForPlatform() throws Exception {
        this.repositoryServer.perform(
            get("/rest/default/models/" + testModel.prettyName + "/download/mappings/test")
                .with(userAdmin)).andExpect(status().isOk());
        this.repositoryServer.perform(
            get("/rest/default/models/com.test:Test1:1.0.0/download/mappings/test").with(userAdmin))
            .andExpect(status().isNotFound());
    }

    @Test public void runDiagnostics() throws Exception {
        this.repositoryServer.perform(
            get("/rest/default/models/" + testModel.prettyName + "/diagnostics").with(userAdmin))
            .andExpect(status().isOk());
        this.repositoryServer
            .perform(get("/rest/default/models/test:Test123:1.0.0/diagnostics").with(userAdmin))
            .andExpect(status().isNotFound());
    }

    @Test public void getPolicies() throws Exception {
        this.repositoryServer.perform(
            get("/rest/default/models/" + testModel.prettyName + "/policies").with(userAdmin))
            .andExpect(status().isOk());
        this.repositoryServer.perform(
            get("/rest/default/models/" + testModel.prettyName + "/policies").with(userCreator))
            .andExpect(status().isOk());
        this.repositoryServer
            .perform(get("/rest/default/models/test:Test123:1.0.0/policies").with(userAdmin))
            .andExpect(status().isNotFound());
    }

    @Test public void getUserPolicy() throws Exception {
        this.repositoryServer.perform(
            get("/rest/default/models/" + testModel.prettyName + "/policy").with(userAdmin))
            .andExpect(status().isNotFound());
        this.repositoryServer.perform(
            get("/rest/default/models/" + testModel.prettyName + "/policy").with(userCreator))
            .andExpect(status().isOk());
    }

    @Test public void addValidPolicyEntry() throws Exception {
        String json =
            "{\"principalId\":\"user3\", \"principalType\": \"User\", \"permission\":\"READ\"}";
        //Valid creation of policy
        this.repositoryServer.perform(
            put("/rest/default/models/" + testModel.prettyName + "/policies")
                .contentType(MediaType.APPLICATION_JSON).content(json).with(userAdmin))
            .andExpect(status().isOk());
        this.repositoryServer.perform(
            get("/rest/default/models/" + testModel.prettyName + "/policy").with(userCreator))
            .andExpect(result -> result.getResponse().getContentAsString().contains(
                "{\"principalId\":\"user3\",\"principalType\":\"User\",\"permission\":\"READ\",\"adminPolicy\":false}"));
    }

    @Test public void editOwnPolicyEntry() throws Exception {
        String json =
            "{\"principalId\":\"user3\", \"principalType\": \"User\", \"permission\":\"READ\"}";
        //Try changing current user policy
        this.repositoryServer.perform(
            put("/rest/default/models/" + testModel.prettyName + "/policies")
                .contentType(MediaType.APPLICATION_JSON).content(json).with(userCreator))
            .andExpect(status().isBadRequest());
    }

    @Test public void addInvalidPolicyEntry() throws Exception {
        String json =
            "{\"principalId\":\"user3\", \"principalType\": \"AUser\", \"permission\":\"READ\"}";
        //Try changing current user policy
        this.repositoryServer.perform(
            put("/rest/default/models/" + testModel.prettyName + "/policies")
                .contentType(MediaType.APPLICATION_JSON).content(json).with(userAdmin))
            .andExpect(status().isBadRequest());
    }

    @Test public void removePolicyEntry() throws Exception {
        String json =
            "{\"principalId\":\"user2\", \"principalType\": \"User\", \"permission\":\"READ\"}";
        //Valid creation of policy
        this.repositoryServer.perform(
            put("/rest/default/models/" + testModel.prettyName + "/policies")
                .contentType(MediaType.APPLICATION_JSON).content(json).with(userAdmin))
            .andExpect(status().isOk());
        repositoryServer.perform(
            delete("/rest/default/models/" + testModel.prettyName + "/policies/user2/User")
                .with(userAdmin)).andExpect(status().isOk());
    }
}
