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
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.eclipse.vorto.repository.web.VortoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.eclipse.vorto.repository.account.Role.ADMIN;
import static org.eclipse.vorto.repository.account.Role.MODEL_CREATOR;
import static org.eclipse.vorto.repository.account.Role.USER;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class) @ContextConfiguration
@SpringBootTest(classes = VortoRepository.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//https://github.com/spring-projects/spring-boot/issues/12280
public class AttachmentsControllerIntegrationTest {

    MockMvc mockMvc;
    TestModel testModel;

    SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user1;
    //Switch user
    SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user2;

    @Autowired protected WebApplicationContext wac;

    @Before public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).
            apply(springSecurity()).build();
        testModel = TestModel.TestModelBuilder.aTestModel().build();
        testModel.createModel(mockMvc);
        user1 = user("user1")
            .password("pass")
            .authorities(SpringUserUtils.toAuthorityList(Sets.newHashSet(USER, ADMIN, MODEL_CREATOR))
            );
        user2 = user("user2")
                .password("pass")
                .authorities(SpringUserUtils.toAuthorityList(Sets.newHashSet(USER))
                );
    }

    @Test public void testRandomModelAttachmentController() throws Exception {
        String nonExistingNamespace = RandomStringUtils.random(10, true, false);
        mockMvc.perform(get("/api/v1/attachments/" + nonExistingNamespace + ":" + testModel.modelName + ":5000.0.0"))
            .andExpect(status().isNotFound());
    }

    @Test public void testAttachmentUpload() throws Exception {
        TestModel test = TestModel.TestModelBuilder.aTestModel().build();
        test.createModel(mockMvc, user1);
        addAttachment(mockMvc,test,user1,"test.json", MediaType.APPLICATION_JSON).andExpect(status().isOk());
    }

    @Test public void testInvalidAttachmentFileTypeUpload() throws Exception {
        TestModel test = TestModel.TestModelBuilder.aTestModel().build();
        test.createModel(mockMvc, user1);

        addAttachment(mockMvc,test,user1,"test.bin", MediaType.APPLICATION_JSON)
            .andExpect(result -> {
                JsonParser jsonParser = new JsonParser();
                JsonElement tree = jsonParser.parse(result.getResponse().getContentAsString());
                assert !tree.getAsJsonObject().get("success").getAsBoolean();
            });

    }

    @Test public void testAttachmentDeleteWithPermissions() throws Exception {
        String fileName = "test.json";
        TestModel deleteModel = TestModel.TestModelBuilder.aTestModel().build();
        // Create Model with extra user
        deleteModel.createModel(mockMvc, user1);

        //Add Attachment to Model
        addAttachment(mockMvc,deleteModel,user1,fileName, MediaType.APPLICATION_JSON).andExpect(status().isOk());

        // Delete Attachment from model
        mockMvc.perform(delete("/api/v1/attachments/" + deleteModel.prettyName + "/files/" + fileName)
            .with(user1))
            .andExpect(status().isOk());
    }

    @Test public void testAttachmentDeleteWithOutPermissions() throws Exception {
        String fileName = "test.json";
        TestModel deleteModel = TestModel.TestModelBuilder.aTestModel().build();
        // Create Model with extra user
        deleteModel.createModel(mockMvc, user1);

        //Add Attachment to Model

        //Try to delete model
        mockMvc.perform(delete("/api/v1/attachments/" + deleteModel.prettyName + "/files/" + fileName)
            .with(user2))
            .andExpect(status().isForbidden());
    }

    @Test public void testAttachmentGetWithPermissions() throws Exception {
        mockMvc.perform(get("/api/v1/attachments/" + testModel.prettyName).with(user1))
            .andExpect(status().isOk());
    }

    private static ResultActions addAttachment(MockMvc mockMvc, TestModel testModel, SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user, String fileName, MediaType mediaType)
        throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", fileName, mediaType.toString(), "{\"test\":123}".getBytes());
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.fileUpload("/api/v1/attachments/" + testModel.prettyName);
        return mockMvc.perform(
            builder.file(file).with(request -> {
                request.setMethod("PUT");
                return request;
            }).contentType(MediaType.MULTIPART_FORM_DATA).with(user));
    }

}
