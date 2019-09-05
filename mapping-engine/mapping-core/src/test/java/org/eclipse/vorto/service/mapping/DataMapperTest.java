/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.decoder.CSVDeserializer;
import org.eclipse.vorto.mapping.engine.decoder.IPayloadDeserializer;
import org.eclipse.vorto.mapping.engine.decoder.JSONDeserializer;
import org.eclipse.vorto.model.runtime.EntityPropertyValue;
import org.eclipse.vorto.model.runtime.FunctionblockValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.eclipse.vorto.service.mapping.spec.SpecWithArrayPayload;
import org.eclipse.vorto.service.mapping.spec.SpecWithConditionFunction;
import org.eclipse.vorto.service.mapping.spec.SpecWithConditionalProperties;
import org.eclipse.vorto.service.mapping.spec.SpecWithConditionedRules;
import org.eclipse.vorto.service.mapping.spec.SpecWithNestedEntity;
import org.eclipse.vorto.service.mapping.spec.SpecWithNestedEnum;
import org.eclipse.vorto.service.mapping.spec.SpecWithPropertyConditionXpath;
import org.eclipse.vorto.service.mapping.spec.SpecWithSameFunctionblock;
import org.eclipse.vorto.service.mapping.spec.SpecWithTwoFunctionblocksWithNestedEntity;
import org.junit.Test;

public class DataMapperTest {
  
  @Test
  public void testMapWithSimpleCondition() throws Exception {
    IDataMapper mapper =
        IDataMapper.newBuilder().withSpecification(new SpecWithConditionalProperties()).build();

    String json = "{\"count\" : 2 }";

    IPayloadDeserializer deserializer = new JSONDeserializer();
    
    InfomodelValue mappedOutput = mapper.mapSource(deserializer.deserialize(json));
    assertFalse(mappedOutput.get("button").getStatusProperty("sensor_value").isPresent());
    assertEquals(2.0,
        mappedOutput.get("button").getStatusProperty("sensor_value2").get().getValue());

    json = "{\"count\" : 0 }";

    mappedOutput = mapper.mapSource(deserializer.deserialize(json));
    assertEquals(0.0,
        mappedOutput.get("button").getStatusProperty("sensor_value").get().getValue());
    assertFalse(mappedOutput.get("button").getStatusProperty("sensor_value2").isPresent());


  }

  @Test
  public void testStringArrayWithSimpleCondition() throws Exception {
    IDataMapper mapper =
        IDataMapper.newBuilder().withSpecification(new SpecWithConditionFunction()).build();

    IPayloadDeserializer deserializer = new CSVDeserializer();
    
    InfomodelValue mappedOutput = mapper.mapSource(deserializer.deserialize(",2,3"));

    assertNull(mappedOutput.get("button"));
    
    mappedOutput = mapper.mapSource(deserializer.deserialize("1,2,3"));
    System.out.println(mappedOutput.get("button").getStatusProperty("sensor_value"));

    assertTrue(mappedOutput.get("button").getStatusProperty("sensor_value").isPresent());
  }



  @Test
  public void testMapWithJxpathCondition() throws Exception {
    IDataMapper mapper =
        IDataMapper.newBuilder().withSpecification(new SpecWithPropertyConditionXpath()).build();

    String json = "{\"data\" : [{\"id\": 100,\"value\": \"x\"},{\"id\": 200,\"value\": \"y\"}]}";

    IPayloadDeserializer deserializer = new JSONDeserializer();

    
    InfomodelValue mappedOutput = mapper.mapSource(deserializer.deserialize(json));
    assertEquals(100.0,
        mappedOutput.get("button").getStatusProperty("sensor_value").get().getValue());

  }

  @Test
  public void testMappingUsingListInput() throws Exception {

    IDataMapper mapper =
        IDataMapper.newBuilder().withSpecification(new SpecWithArrayPayload()).build();

    String json = "[{\"clickType\" : \"DOUBLE\" }, {\"clickType\" : \"SINGLE\" }]";

    IPayloadDeserializer deserializer = new JSONDeserializer();

    InfomodelValue mappedOutput = mapper.mapSource(deserializer.deserialize(json));

    FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");

    assertEquals("DOUBLE",
        buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());

    System.out.println(mappedOutput);

  }

  @Test
  public void testMapSingleFunctionblockOfInfomodel2() {
    IDataMapper mapper =
        IDataMapper.newBuilder().withSpecification(new SpecWithConditionedRules()).build();

    final String sampleHomeConnectRESTResponse =
        "{\"data\" : { \"key\" : \"DoorState\", \"value\" : \"Locked\"}}";

    IPayloadDeserializer deserializer = new JSONDeserializer();

    
    InfomodelValue mappedOutput =
        mapper.mapSource(deserializer.deserialize(sampleHomeConnectRESTResponse));
    System.out.println(mappedOutput);
    assertNull(mappedOutput.get("operationState"));
    FunctionblockValue doorStateFunctionblockData = mappedOutput.get("doorState");
    assertEquals("Locked",
        (String) doorStateFunctionblockData.getStatusProperty("sensor_value").get().getValue());

  }

  @Test
  public void testMappingWithInfoModelUsingSameFunctionblock() throws Exception {

    IDataMapper mapper =
        IDataMapper.newBuilder().withSpecification(new SpecWithSameFunctionblock()).build();

    String json = "{\"btnvalue1\" : 2, \"btnvalue2\": 10}";

    IPayloadDeserializer deserializer = new JSONDeserializer();

    InfomodelValue mappedOutput = mapper.mapSource(deserializer.deserialize(json));

    FunctionblockValue buttonFunctionblockData = mappedOutput.get("btn1");

    assertEquals(2.0, buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());

    FunctionblockValue button2FunctionblockData = mappedOutput.get("btn2");

    assertEquals(10.0, button2FunctionblockData.getStatusProperty("sensor_value").get().getValue());

    System.out.println(mappedOutput);

  }

  @Test
  public void testMappingWithEntity() throws Exception {
    IDataMapper mapper =
        IDataMapper.newBuilder().withSpecification(new SpecWithNestedEntity()).build();

    final String sampleDeviceData =
        "{\"temperature\" : 20.3 }";

    IPayloadDeserializer deserializer = new JSONDeserializer();

    InfomodelValue mappedOutput =
        mapper.mapSource(deserializer.deserialize(sampleDeviceData));
    
    EntityPropertyValue temperatureValue = (EntityPropertyValue)mappedOutput.get("outdoorTemperature").getStatusProperty("value").get();
    assertEquals(20.3,temperatureValue.getValue().getPropertyValue("value").get().getValue());
  }
  
  @Test
  public void testMappingWithEntityUnmapped() throws Exception {
    IDataMapper mapper =
        IDataMapper.newBuilder().withSpecification(new SpecWithTwoFunctionblocksWithNestedEntity()).build();

    final String sampleDeviceData =
        "{\"temperature\" : 20.3 }";

    IPayloadDeserializer deserializer = new JSONDeserializer();

    InfomodelValue mappedOutput =
        mapper.mapSource(deserializer.deserialize(sampleDeviceData));
    
    EntityPropertyValue temperatureValue = (EntityPropertyValue)mappedOutput.get("outdoorTemperature").getStatusProperty("value").get();    
    assertEquals(20.3,temperatureValue.getValue().getPropertyValue("value").get().getValue());
    
    assertNull(mappedOutput.get("humidity"));
  }
  
  @Test
  public void testMappingWithEnum() throws Exception {
    IDataMapper mapper =
        IDataMapper.newBuilder().withSpecification(new SpecWithNestedEnum()).build();

    final String sampleDeviceData =
        "{\"temperature\" : 20.3 }";

    IPayloadDeserializer deserializer = new JSONDeserializer();

    InfomodelValue mappedOutput =
        mapper.mapSource(deserializer.deserialize(sampleDeviceData));
    
    assertEquals(20.3,mappedOutput.get("outdoorTemperature").getStatusProperty("value").get().getValue());
    assertEquals("Celcius",mappedOutput.get("outdoorTemperature").getStatusProperty("unit").get().getValue());
  }
}
