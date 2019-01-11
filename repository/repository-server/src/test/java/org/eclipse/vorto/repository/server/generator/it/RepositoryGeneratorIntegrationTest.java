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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.List;

import org.eclipse.vorto.codegen.api.GeneratorServiceInfo;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import com.google.gson.reflect.TypeToken;


public class RepositoryGeneratorIntegrationTest extends AbstractGeneratorIntegrationTest {

  @Test
  public void testGetRegisteredGeneratorServices() throws Exception {
    repositoryServer.perform(get("/api/v1/generators").with(userAdmin)).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(getGenerators().size())));
  }

  @Test
  public void testGetGeneratorInfo() throws Exception {
    for (GeneratorServiceInfo genInfo : getGenerators()) {
      System.out.println("Checking for [" + genInfo.getKey() + "]");
      repositoryServer.perform(get("/api/v1/generators/" + genInfo.getKey()))
          .andExpect(status().isOk()).andExpect(jsonPath("$.name", Matchers.is(genInfo.getName())))
          .andExpect(jsonPath("$.description", Matchers.is(genInfo.getDescription())));
    }
  }

  @Test
  public void testGenerateBoschIoTSuiteForJava() throws Exception {
    createModel("Location.fbmodel", "com.test:Location:1.0.0");
    createModel("TrackingDevice.infomodel", "com.test:TrackingDevice:1.0.0");

    // releasing the test models, otherwise anonymous user cannot generate code
    releaseModel("com.test:Location:1.0.0");
    releaseModel("com.test:TrackingDevice:1.0.0");


    repositoryServer
        .perform(
            get("/api/v1/generators/boschiotsuite/models/com.test:TrackingDevice:1.0.0?language=java")
                .with(userAdmin))
        .andExpect(status().isOk())
        .andExpect(ZipFileCompare.equals(loadResource("generated-boschiotsuite-java.zip")));
    
    // deleting the test models, otherwise anonymous user cannot generate code
    deleteModel("com.test:TrackingDevice:1.0.0");
    deleteModel("com.test:Location:1.0.0");
  }
  
  @Test
  public void testGenerateBoschIoTSuiteForPython() throws Exception {
    createModel("Location.fbmodel", "com.test:Location:1.0.0");
    createModel("TrackingDevice.infomodel", "com.test:TrackingDevice:1.0.0");

    // releasing the test models, otherwise anonymous user cannot generate code
    releaseModel("com.test:Location:1.0.0");
    releaseModel("com.test:TrackingDevice:1.0.0");


    repositoryServer
        .perform(
            get("/api/v1/generators/boschiotsuite/models/com.test:TrackingDevice:1.0.0?language=python")
                .with(userAdmin))
        .andExpect(status().isOk())
        .andExpect(ZipFileCompare.equals(loadResource("generated-boschiotsuite-python.zip")));
    
    // deleting the test models, otherwise anonymous user cannot generate code
    deleteModel("com.test:TrackingDevice:1.0.0");
    deleteModel("com.test:Location:1.0.0");
  }
  
  @Test
  public void testGenerateBoschIoTSuiteForArduino() throws Exception {
    createModel("Location.fbmodel", "com.test:Location:1.0.0");
    createModel("TrackingDevice.infomodel", "com.test:TrackingDevice:1.0.0");

    // releasing the test models, otherwise anonymous user cannot generate code
    releaseModel("com.test:Location:1.0.0");
    releaseModel("com.test:TrackingDevice:1.0.0");


    repositoryServer
        .perform(
            get("/api/v1/generators/boschiotsuite/models/com.test:TrackingDevice:1.0.0?language=arduino")
                .with(userAdmin))
        .andExpect(status().isOk())
        .andExpect(ZipFileCompare.equals(loadResource("generated-boschiotsuite-arduino.zip")));
    
    // deleting the test models, otherwise anonymous user cannot generate code
    deleteModel("com.test:TrackingDevice:1.0.0");
    deleteModel("com.test:Location:1.0.0");
  }
  
  @Test
  public void testGenerateBoschIoTSuiteForGatewaySoftware() throws Exception {
    createModel("Location.fbmodel", "com.test:Location:1.0.0");
    createModel("TrackingDevice.infomodel", "com.test:TrackingDevice:1.0.0");

    // releasing the test models, otherwise anonymous user cannot generate code
    releaseModel("com.test:Location:1.0.0");
    releaseModel("com.test:TrackingDevice:1.0.0");


    repositoryServer
        .perform(
            get("/api/v1/generators/boschiotsuite/models/com.test:TrackingDevice:1.0.0?language=gateway")
                .with(userAdmin))
        .andExpect(status().isOk())
        .andExpect(ZipFileCompare.equals(loadResource("generated-boschiotsuite-gatewaySoftware.zip")));
    
    // deleting the test models, otherwise anonymous user cannot generate code
    deleteModel("com.test:TrackingDevice:1.0.0");
    deleteModel("com.test:Location:1.0.0");
  }
  
  @Test
  public void testGenerateEclipseDitto() throws Exception {
    createModel("Location.fbmodel", "com.test:Location:1.0.0");
    createModel("TrackingDevice.infomodel", "com.test:TrackingDevice:1.0.0");

    // releasing the test models, otherwise anonymous user cannot generate code
    releaseModel("com.test:Location:1.0.0");
    releaseModel("com.test:TrackingDevice:1.0.0");


    repositoryServer
        .perform(
            get("/api/v1/generators/eclipseditto/models/com.test:TrackingDevice:1.0.0")
                .with(userAdmin))
        .andExpect(status().isOk())
        .andExpect(ZipFileCompare.equals(loadResource("generated-eclipseditto.zip")));
    
    // deleting the test models, otherwise anonymous user cannot generate code
    deleteModel("com.test:TrackingDevice:1.0.0");
    deleteModel("com.test:Location:1.0.0");
  }

  @Test
  public void testGenerateEclipseHonoForArduino() throws Exception {
    createModel("Location.fbmodel", "com.test:Location:1.0.0");
    createModel("TrackingDevice.infomodel", "com.test:TrackingDevice:1.0.0");

    // releasing the test models, otherwise anonymous user cannot generate code
    releaseModel("com.test:Location:1.0.0");
    releaseModel("com.test:TrackingDevice:1.0.0");


    repositoryServer
        .perform(
            get("/api/v1/generators/eclipsehono/models/com.test:TrackingDevice:1.0.0/?language=Arduino")
                .with(userAdmin))
        .andExpect(status().isOk())
        .andExpect(ZipFileCompare.equals(loadResource("generated-eclipsehono-arduino.zip")));
    
    // deleting the test models, otherwise anonymous user cannot generate code
    deleteModel("com.test:TrackingDevice:1.0.0");
    deleteModel("com.test:Location:1.0.0");
  }
  
  @Test
  public void testGenerateEclipseHonoForPython() throws Exception {
    createModel("Location.fbmodel", "com.test:Location:1.0.0");
    createModel("TrackingDevice.infomodel", "com.test:TrackingDevice:1.0.0");

    // releasing the test models, otherwise anonymous user cannot generate code
    releaseModel("com.test:Location:1.0.0");
    releaseModel("com.test:TrackingDevice:1.0.0");


    repositoryServer
        .perform(
            get("/api/v1/generators/eclipsehono/models/com.test:TrackingDevice:1.0.0/?language=python")
                .with(userAdmin))
        .andExpect(status().isOk())
        .andExpect(ZipFileCompare.equals(loadResource("generated-eclipsehono-python.zip")));
    
    // deleting the test models, otherwise anonymous user cannot generate code
    deleteModel("com.test:TrackingDevice:1.0.0");
    deleteModel("com.test:Location:1.0.0");
  }
  
  @Test
  public void testGenerateEclipseHonoForJava() throws Exception {
    createModel("Location.fbmodel", "com.test:Location:1.0.0");
    createModel("TrackingDevice.infomodel", "com.test:TrackingDevice:1.0.0");

    // releasing the test models, otherwise anonymous user cannot generate code
    releaseModel("com.test:Location:1.0.0");
    releaseModel("com.test:TrackingDevice:1.0.0");


    repositoryServer
        .perform(
            get("/api/v1/generators/eclipsehono/models/com.test:TrackingDevice:1.0.0/?language=java")
                .with(userAdmin))
        .andExpect(status().isOk())
        .andExpect(ZipFileCompare.equals(loadResource("generated-eclipsehono-java.zip")));
    
    // deleting the test models, otherwise anonymous user cannot generate code
    deleteModel("com.test:TrackingDevice:1.0.0");
    deleteModel("com.test:Location:1.0.0");
  }
  
  @Test
  public void testGenerateEclipseDittoForStreetLamp() throws Exception {
	createModel("Zone.type", "com.test:Zone:1.0.0");
	createModel("Colour.type", "com.test:Colour:1.0.0");
	createModel("Lamp.fbmodel", "com.test:Lamp:1.0.0");
	createModel("Address.fbmodel", "com.test:Address:1.0.0");
	createModel("StreetLamp.infomodel", "com.test:StreetLamp:1.0.0");

    // releasing the test models, otherwise anonymous user cannot generate code
	releaseModel("com.test:Colour:1.0.0");
    releaseModel("com.test:Zone:1.0.0");
    releaseModel("com.test:Address:1.0.0");
    releaseModel("com.test:Lamp:1.0.0");
    releaseModel("com.test:StreetLamp:1.0.0");


    repositoryServer
        .perform(
            get("/api/v1/generators/eclipseditto/models/com.test:StreetLamp:1.0.0")
                .with(userAdmin))
        .andExpect(status().isOk())
        .andExpect(ZipFileCompare.equals(loadResource("generated-eclipseditto-lampfb.zip")));
    
    // deleting the test models, otherwise anonymous user cannot generate code
    deleteModel("com.test:StreetLamp:1.0.0");
    deleteModel("com.test:Lamp:1.0.0");
    deleteModel("com.test:Address:1.0.0");
    deleteModel("com.test:Colour:1.0.0");
    deleteModel("com.test:Zone:1.0.0");
  }

  @Test
  public void testGenerateBoschIoTSuiteForJavaForStreetLamp() throws Exception {
	createModel("Zone.type", "com.test:Zone:1.0.0");
	createModel("Colour.type", "com.test:Colour:1.0.0");
    createModel("Lamp.fbmodel", "com.test:Lamp:1.0.0");
    createModel("Address.fbmodel", "com.test:Address:1.0.0");
    createModel("StreetLamp.infomodel", "com.test:StreetLamp:1.0.0");

    // releasing the test models, otherwise anonymous user cannot generate code
    releaseModel("com.test:Colour:1.0.0");
    releaseModel("com.test:Zone:1.0.0");
    releaseModel("com.test:Address:1.0.0");
    releaseModel("com.test:Lamp:1.0.0");
    releaseModel("com.test:StreetLamp:1.0.0");


    repositoryServer
        .perform(
            get("/api/v1/generators/boschiotsuite/models/com.test:StreetLamp:1.0.0?language=java")
                .with(userAdmin))
        .andExpect(status().isOk())
        .andExpect(ZipFileCompare.equals(loadResource("generated-boschiotsuite-java-lampFb.zip")));
    
    // deleting the test models, otherwise anonymous user cannot generate code
    deleteModel("com.test:StreetLamp:1.0.0");
    deleteModel("com.test:Lamp:1.0.0");
    deleteModel("com.test:Address:1.0.0");
    deleteModel("com.test:Colour:1.0.0");
    deleteModel("com.test:Zone:1.0.0");
  }
  
  @Test
  public void testGenerateBoschIoTSuiteForPythonForStreetLamp() throws Exception {
	createModel("Zone.type", "com.test:Zone:1.0.0");
	createModel("Colour.type", "com.test:Colour:1.0.0");
    createModel("Lamp.fbmodel", "com.test:Lamp:1.0.0");
    createModel("Address.fbmodel", "com.test:Address:1.0.0");
    createModel("StreetLamp.infomodel", "com.test:StreetLamp:1.0.0");

    // releasing the test models, otherwise anonymous user cannot generate code
    releaseModel("com.test:Colour:1.0.0");
    releaseModel("com.test:Zone:1.0.0");
    releaseModel("com.test:Address:1.0.0");
    releaseModel("com.test:Lamp:1.0.0");
    releaseModel("com.test:StreetLamp:1.0.0");


    ResultMatcher loadedResource = ZipFileCompare.equals(loadResource("generated-boschiotsuite-python-lampFb.zip"));
	repositoryServer
        .perform(
            get("/api/v1/generators/boschiotsuite/models/com.test:StreetLamp:1.0.0?language=python")
                .with(userAdmin))
        .andExpect(status().isOk())
        .andExpect(loadedResource);
    
    // deleting the test models, otherwise anonymous user cannot generate code
    deleteModel("com.test:StreetLamp:1.0.0");
    deleteModel("com.test:Lamp:1.0.0");
    deleteModel("com.test:Address:1.0.0");
    deleteModel("com.test:Colour:1.0.0");
    deleteModel("com.test:Zone:1.0.0");
  }
  
  private Collection<GeneratorServiceInfo> getGenerators() throws Exception {
    MvcResult result = generatorServer.perform(get("/rest/generators")).andReturn();
    return gson.fromJson(new String(result.getResponse().getContentAsByteArray()),
        new TypeToken<List<GeneratorServiceInfo>>() {}.getType());
  }

}
