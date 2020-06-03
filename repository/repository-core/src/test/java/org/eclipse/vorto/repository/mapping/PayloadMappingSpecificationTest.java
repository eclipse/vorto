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
package org.eclipse.vorto.repository.mapping;

import org.eclipse.vorto.mapping.engine.decoder.JSONDeserializer;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.Stereotype;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.eclipse.vorto.repository.UnitTestBase;
import org.eclipse.vorto.repository.mapping.impl.DefaultPayloadMappingService;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PayloadMappingSpecificationTest extends UnitTestBase {

  private DefaultPayloadMappingService mappingService;
  
  @Override
  @Before
  public void beforeEach() throws Exception {
    super.beforeEach();
    
    this.mappingService = new DefaultPayloadMappingService(this.repositoryFactory,this.workflow);
  }
  
  @Test
  public void testCreateSpecification() {
    importModel("payloadmapping/org.eclipse.vorto_Voltage_1.0.0.fbmodel");
    importModel("payloadmapping/org.eclipse.vorto_Battery_1.0.0.fbmodel");
    importModel("payloadmapping/org.eclipse.vorto_MyDevice_1.0.0.infomodel");
    
    IMappingSpecification specification = mappingService.getOrCreateSpecification(ModelId.fromPrettyFormat("org.eclipse.vorto:MyDevice:1.0.0"));
    
    assertNotNull(specification);
    assertNotNull(specification.getInfoModel().getTargetPlatformKey());
  }
  
  @Test
  public void testRunMappingTest() {
    importModel("payloadmapping/org.eclipse.vorto_Voltage_1.0.0.fbmodel");
    importModel("payloadmapping/org.eclipse.vorto_Battery_1.0.0.fbmodel");
    importModel("payloadmapping/org.eclipse.vorto_MyDevice_1.0.0.infomodel");
    
    IMappingSpecification specification = mappingService.getOrCreateSpecification(ModelId.fromPrettyFormat("org.eclipse.vorto:MyDevice:1.0.0"));
    
    specification.getFunctionBlock("battery").getStatusProperty("remainingCapacity").get().getStereotype(Stereotype.SOURCE).get().setAttributes(createXpathRule("/cap"));
    
    specification.getFunctionBlock("battery").getStatusProperty("value").get().getStereotype(Stereotype.SOURCE).get().setAttributes(createXpathRule("/voltage"));
    
    JSONDeserializer deserializer = new JSONDeserializer();
    InfomodelValue testResult = mappingService.runTest(specification,deserializer.deserialize("{\"cap\": 23 ,\"voltage\": 2242.2 }") );
    assertEquals(23.0,testResult.get("battery").getStatusProperty("remainingCapacity").get().getValue());
    assertEquals(2242.2,testResult.get("battery").getStatusProperty("value").get().getValue());
  }
  
  @Test
  public void testSaveMappingSpecificationWithoutChanges() {
    importModel("payloadmapping/org.eclipse.vorto_Voltage_1.0.0.fbmodel");
    importModel("payloadmapping/org.eclipse.vorto_Battery_1.0.0.fbmodel");
    importModel("payloadmapping/org.eclipse.vorto_MyDevice_1.0.0.infomodel");
    
    final ModelId modelId = ModelId.fromPrettyFormat("org.eclipse.vorto:MyDevice:1.0.0");
    
    IMappingSpecification specification = mappingService.getOrCreateSpecification(modelId);
    
    mappingService.saveSpecification(specification, createUserContext("alex", "playground"));
    
    assertNotNull(mappingService.resolveMappingIdForModelId(modelId).get());
  }
  
  @Test
  public void testSaveMappingSpecificationWithChanges() {
    importModel("payloadmapping/org.eclipse.vorto_Voltage_1.0.0.fbmodel");
    importModel("payloadmapping/org.eclipse.vorto_Battery_1.0.0.fbmodel");
    importModel("payloadmapping/org.eclipse.vorto_MyDevice_1.0.0.infomodel");
    
    final ModelId modelId = ModelId.fromPrettyFormat("org.eclipse.vorto:MyDevice:1.0.0");
    
    IMappingSpecification specification = mappingService.getOrCreateSpecification(modelId);
    
    specification.getFunctionBlock("battery").getStatusProperty("value").get().getStereotype(Stereotype.SOURCE).get().setAttributes(createXpathRule("/voltage"));

    
    mappingService.saveSpecification(specification, createUserContext("alex", "playground"));
  }
  
  private Map<String, String> createXpathRule(String rule) {
    Map<String,String> attributes = new HashMap<>();
    attributes.put(Stereotype.XPATH_ATT,rule);
    return attributes;
  }

}
