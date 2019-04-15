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
package org.eclipse.vorto.repository.server.generator.it;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.eclipse.vorto.codegen.api.GeneratorServiceInfo;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import com.google.gson.reflect.TypeToken;

public class RepositoryGeneratorIntegrationTest extends AbstractGeneratorIntegrationTest {

  protected void setUpTest() throws Exception {
    super.setUpTest();
    createAndReleaseModel("Location.fbmodel", "com.test:Location:1.0.0");
    createAndReleaseModel("TrackingDevice.infomodel", "com.test:TrackingDevice:1.0.0");
    createAndReleaseModel("Zone.type", "com.test:Zone:1.0.0");
    createAndReleaseModel("Colour.type", "com.test:Colour:1.0.0");
    createAndReleaseModel("Lamp.fbmodel", "com.test:Lamp:1.0.0");
    createAndReleaseModel("Address.fbmodel", "com.test:Address:1.0.0");
    createAndReleaseModel("StreetLamp.infomodel", "com.test:StreetLamp:1.0.0");
  }

  /** +++++++++++++ GENERAL GENERATOR INFO ENDPOINT TEST CASES ++++++++++++++++++++++++ */

  @Test
  public void testGetRegisteredGeneratorServices() throws Exception {
    repositoryServer.perform(get("/api/v1/generators").with(userAdmin)).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(getGenerators().size())));
    assertTrue(true);
  }

  private Collection<GeneratorServiceInfo> getGenerators() throws Exception {
    MvcResult result = generatorServer.perform(get("/rest/generators")).andReturn();
    return gson.fromJson(new String(result.getResponse().getContentAsByteArray()),
        new TypeToken<List<GeneratorServiceInfo>>() {}.getType());
  }

  @Test
  public void testGetGeneratorInfo() throws Exception {
    for (GeneratorServiceInfo genInfo : getGenerators()) {
      repositoryServer.perform(get("/api/v1/generators/" + genInfo.getKey()))
          .andExpect(status().isOk()).andExpect(jsonPath("$.name", Matchers.is(genInfo.getName())))
          .andExpect(jsonPath("$.description", Matchers.is(genInfo.getDescription())));
    }
    assertTrue(true);
  }


  /** +++++++++++++ ECLIPSE DITTO TEST CASES ++++++++++++++++++++++++ */

  @Test
  public void testGenerateEclipseDitto() throws Exception {
    invokeAndAssertEclipseDittoGenerator("com.test:TrackingDevice:1.0.0",
        "generated-eclipseditto.zip", Optional.empty());
    assertTrue(true);
  }

  private void invokeAndAssertEclipseDittoGenerator(String modelId, String fileNameToCompare,
      Optional<String> paramUrl) throws Exception {
    repositoryServer
        .perform(get("/api/v1/generators/eclipseditto/models/" + modelId
            + (paramUrl.isPresent() ? paramUrl.get() : "")).with(userAdmin))
        .andExpect(status().isOk())
        .andExpect(ZipFileCompare.equals(loadResource(fileNameToCompare)));
  }

  @Test
  public void testGenerateEclipseDittoForStreetLamp() throws Exception {
    invokeAndAssertEclipseDittoGenerator("com.test:StreetLamp:1.0.0",
        "generated-eclipseditto-lampfb.zip", Optional.empty());
    assertTrue(true);
  }
}
