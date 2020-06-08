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

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    repositoryServer.perform(get("/api/v1/models/" + testModel.prettyName + "/content").with(userModelCreator))
        .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        .andDo(result -> System.out.println(result.getResponse().getErrorMessage()))
        .andExpect(status().isOk());
    
    assertTrue(true);
  }

  @Test
  public void testModelFileDownloadContent() throws Exception {
    repositoryServer.perform(get("/api/v1/models/" + testModel.prettyName + "/file").with(userModelCreator))
        .andExpect(status().isOk());
    
    assertTrue(true);
  }
  
  /*
   * Download file including dependencies
   */
  @Test
  public void testModelFileDownloadContentWithDependencies() throws Exception {
    repositoryServer.perform(get("/api/v1/models/" + testModel.prettyName + "/file"+"?includeDependencies=true").with(userModelCreator))
        .andExpect(status().isOk());
    
    assertTrue(true);
  }

}
