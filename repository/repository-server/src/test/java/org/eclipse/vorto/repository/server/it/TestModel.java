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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

public class TestModel {
  public String namespace = TestUtils.createRandomString(10).toLowerCase();
  public String modelName = TestUtils.createRandomString(10).toUpperCase();
  public String description = "InformationModel for " + modelName;
  public String version = "1.0.0";
  public String prettyName = namespace + ":" + modelName + ":" + version;
  public String targetPlatform = "target";
  public String json = "{" + "\"targetPlatformKey\":" + targetPlatform + "," + "\"stereotypes\":[],"
      + "\"mappingReference\":null," + "\"id\":{" + "\"name\":\"" + modelName
      + "\",\"namespace\":\"" + namespace + "\",\"version\":\"1.0.0\"," + "\"prettyFormat\":\""
      + prettyName + "\"}," + "\"type\":\"InformationModel\"," + "\"displayName\":\"" + modelName
      + "\"," + "\"description\":\"" + description + "\"," + "\"fileName\":null,"
      + "\"references\":[]," + "\"author\":null," + "\"creationDate\":null,"
      + "\"modificationDate\":null," + "\"hasImage\":false," + "\"state\":null,"
      + "\"imported\":false," + "\"referencedBy\":[]," + "\"platformMappings\":{}," + "\"model\":{"
      + "\"name\":\"" + modelName + "\"," + "\"namespace\":\"" + namespace + "\","
      + "\"version\":\"" + version + "\"," + "\"references\":[]," + "\"description\":\""
      + description + "\"," + "\"displayname\":\"" + modelName + "\"," + "\"category\":null,"
      + "\"properties\":[]" + "}," + "\"targetPlatform\":\"" + targetPlatform + "\","
      + "\"released\":false" + "}";

  public TestModel(String namespace, String modelName, String description, String version) {
    this.namespace = namespace;
    this.modelName = modelName;
    this.description = description;
    this.version = version;
  }

  public void createModel(MockMvc mockMvc,
      SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user1) throws Exception {
    mockMvc.perform(post("/rest/default/models/" + prettyName + "/InformationModel").with(user1)
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
  }


  public static final class TestModelBuilder {
    String namespace = TestUtils.createRandomString(10).toLowerCase();
    String modelName = TestUtils.createRandomString(10).toUpperCase();
    String description = "InformationModel for " + modelName;
    String version = "1.0.0";

    private TestModelBuilder() {}

    public static TestModelBuilder aTestModel() {
      return new TestModelBuilder();
    }

    public TestModelBuilder withNamespace(String namespace) {
      this.namespace = namespace;
      return this;
    }

    public TestModelBuilder withModelName(String modelName) {
      this.modelName = modelName;
      return this;
    }

    public TestModelBuilder withDescription(String description) {
      this.description = description;
      return this;
    }

    public TestModelBuilder withVersion(String version) {
      this.version = version;
      return this;
    }

    public TestModel build() {
      return new TestModel(namespace, modelName, description, version);
    }
  }
}
