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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

public class ModelControllerIntegrationTest extends IntegrationTestBase {

  @Test
  public void testModelAccess() throws Exception {
    repositoryServer.perform(get("/api/v1/models/" + testModel.prettyName).with(userModelCreator))
        .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        .andDo(result -> System.out.println(result.getResponse().getErrorMessage()))
        .andExpect(status().isOk());

    assertTrue(true);
  }

  @Test
  public void testGetModelContent() throws Exception {
    repositoryServer
        .perform(get("/api/v1/models/" + testModel.prettyName + "/content").with(userModelCreator))
        .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        .andDo(result -> System.out.println(result.getResponse().getErrorMessage()))
        .andExpect(status().isOk());

    assertTrue(true);
  }

  @Test
  public void testModelFileDownloadContent() throws Exception {
    repositoryServer
        .perform(get("/api/v1/models/" + testModel.prettyName + "/file").with(userModelCreator))
        .andExpect(status().isOk());

    assertTrue(true);
  }

  /*
   * Download file including dependencies
   */
  @Test
  public void testModelFileDownloadContentWithDependencies() throws Exception {
    repositoryServer.perform(
        get("/api/v1/models/" + testModel.prettyName + "/file" + "?includeDependencies=true")
            .with(userModelCreator))
        .andExpect(status().isOk());

    assertTrue(true);
  }

  /**
   * This verifies that an API call for a model is case-insensitive with regards to the part of its
   * ID that represents the namespace.<br/>
   * In other words, namespaces are case-insensitive both when manipulating the namespace itself,
   * and when using the namespace as part of an ID to represent a model.<br/>
   * Tests the API V1 calls for model info and content.
   *
   * @throws Exception
   * @see ModelRepositoryControllerTest#resolveModelWithCaseInsensitiveNamespace() for UI payload
   */
  @Test
  public void resolveModelWithCaseInsensitiveNamespace() throws Exception {
    String namespace = "com.Some_Other_Company.oFFICIA1";
    //String namespace = "com.some_other_company.official";
    createNamespaceSuccessfully(namespace, userSysadmin);
    String id = String.format("%s.ModelIDCaseInsensitiveTest:1.0.0", namespace);
    createModel(
        userSysadmin,
        "ModelIDCaseInsensitiveTest.type",
        id
    );
    // model info
    repositoryServer
        .perform(
            get(String.format("/api/v1/models/%s", id))
                .with(userSysadmin)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id.namespace", equalTo(namespace.toLowerCase())));

    // model content
    repositoryServer
        .perform(
            get(String.format("/api/v1/models/%s/content", id))
                .with(userSysadmin)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.root.namespace", equalTo(namespace.toLowerCase())));

    // cleanup
    repositoryServer
        .perform(
            delete(String.format("/rest/models/%s", id))
                .with(userSysadmin)
        )
        .andExpect(status().isOk());

    repositoryServer
        .perform(
            delete(String.format("/rest/namespaces/%s", namespace))
                .with(userSysadmin)
        )
        .andExpect(status().isNoContent());
  }

}
