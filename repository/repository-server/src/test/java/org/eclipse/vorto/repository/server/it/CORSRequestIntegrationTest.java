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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.util.List;
import org.eclipse.vorto.model.ModelType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;

/**
 * Tests for CORS requests with regards to any allowed Origin headers
 * (see application yml configuration).
 */
public class CORSRequestIntegrationTest extends AbstractIntegrationTest {

  private static List<String> testValidOrigins;
  private static List<String> testInvalidOrigins;

  @BeforeClass
  public static void beforeClass() throws Exception {
    testValidOrigins = Files
        .readAllLines(new ClassPathResource("origins/valid_origins.txt").getFile().toPath());
    testInvalidOrigins = Files
        .readAllLines(new ClassPathResource("origins/invalid_origins.txt").getFile().toPath());
  }

  @Override
  protected void setUpTest() throws Exception {
    testModel = TestModel.TestModelBuilder.aTestModel().build();
    testModel.createModel(repositoryServer, userCreator);
  }

  @Test
  public void testGETRequestsValid() throws Exception {
    for (String origin : testValidOrigins) {
      repositoryServer.perform(
          get("/api/v1/models/" + testModel.prettyName)
              .header("Origin", origin)
              .with(userCreator))
          .andExpect(status().isOk());
    }
  }

  @Test
  public void testGETRequestsInvalid() throws Exception {
    for (String origin : testInvalidOrigins) {
      repositoryServer.perform(
          get("/api/v1/models/" + testModel.prettyName)
              .header("Origin", origin)
              .with(userCreator))
          .andExpect(status().isForbidden());
    }
  }

  @Test
  public void testDELETERequestsValid() throws Exception {
    TestModel newModel = TestModel.TestModelBuilder.aTestModel().build();
    for (String origin : testValidOrigins) {
      newModel.createModel(repositoryServer, userCreator);
      repositoryServer.perform(
          delete("/rest/models/" + newModel.prettyName)
              .header("Origin", origin)
              .with(userCreator))
          .andExpect(status().isOk());
    }
  }

  @Test
  public void testDELETERequestsInvalid() throws Exception {
    for (String origin : testInvalidOrigins) {
      TestModel newModel = TestModel.TestModelBuilder.aTestModel().build();
      newModel.createModel(repositoryServer, userCreator);
      repositoryServer.perform(
          delete("/rest/models/" + newModel.prettyName)
              .header("Origin", origin)
              .with(userCreator))
          .andExpect(status().isForbidden());
    }
  }

  @Test
  public void testPOSTRequestsValid() throws Exception {
    for (String origin : testValidOrigins) {
      TestModel newModel = TestModel.TestModelBuilder.aTestModel().build();
      repositoryServer.perform(
          post("/rest/models/" + newModel.prettyName + "/InformationModel")
              .header("Origin", origin)
              .with(userCreator)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isCreated());
    }
  }

  @Test
  public void testPOSTRequestsInvalid() throws Exception {
    for (String origin : testInvalidOrigins) {
      TestModel newModel = TestModel.TestModelBuilder.aTestModel().build();
      repositoryServer.perform(
          post("/rest/models/" + newModel.prettyName + "/InformationModel")
              .header("Origin", origin)
              .with(userCreator)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isForbidden());
    }
  }

  @Test
  public void testPUTRequestsValid() throws Exception {
    String fileName = "Location.fbmodel";
    String modelId = "com.test:Location:1.0.0";
    repositoryServer.perform(
        delete("/rest/models/" + modelId)
            .with(userCreator));
    repositoryServer
        .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
            .with(userAdmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    for (String origin : testValidOrigins) {
      repositoryServer.perform(
          put("/rest/models/" + modelId).with(userCreator)
              .header("Origin", origin)
              .contentType(MediaType.APPLICATION_JSON).content(createContent(fileName)))
          .andExpect(status().isOk());
    }
    // tries deleting the model once done
    repositoryServer.perform(
        delete("/rest/models/" + modelId)
            .with(userCreator));
  }

  @Test
  public void testPUTRequestsInvalid() throws Exception {
    String fileName = "Location.fbmodel";
    String modelId = "com.test:Location:1.0.0";
    repositoryServer.perform(
        delete("/rest/models/" + modelId)
            .with(userCreator));
    repositoryServer
        .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
            .with(userAdmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    for (String origin : testInvalidOrigins) {
      repositoryServer.perform(
          put("/rest/models/" + modelId).with(userCreator)
              .header("Origin", origin)
              .contentType(MediaType.APPLICATION_JSON).content(createContent(fileName)))
          .andExpect(status().isForbidden());
    }
    repositoryServer.perform(
        delete("/rest/models/" + modelId)
            .with(userCreator));
  }
}
