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
package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.twin.TwinPayloadFactory;
import org.eclipse.vorto.model.runtime.FunctionblockValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.eclipse.vorto.service.mapping.spec.SpecWithArrayPayload;
import org.eclipse.vorto.service.mapping.spec.SpecWithConditionalProperties;
import org.eclipse.vorto.service.mapping.spec.SpecWithPropertyConditionXpath;
import org.eclipse.vorto.service.mapping.spec.SpecWithConditionedRules;
import org.eclipse.vorto.service.mapping.spec.SpecWithSameFunctionblock;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonMappingTest {

  private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Test
  public void testMapWithSimpleCondition() throws Exception {
    IDataMapper mapper =
        IDataMapper.newBuilder().withSpecification(new SpecWithConditionalProperties()).build();

    String json = "{\"count\" : 2 }";

    InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));
    assertFalse(mappedOutput.get("button").getStatusProperty("sensor_value").isPresent());
    assertEquals(2.0,
        mappedOutput.get("button").getStatusProperty("sensor_value2").get().getValue());

    json = "{\"count\" : 0 }";

    mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));
    assertEquals(0.0,
        mappedOutput.get("button").getStatusProperty("sensor_value").get().getValue());
    assertFalse(mappedOutput.get("button").getStatusProperty("sensor_value2").isPresent());

    System.out.println(
        gson.toJson(TwinPayloadFactory.toDittoProtocol(mappedOutput, "org.eclipse.vorto", "123")));

    System.out.println(gson.toJson(TwinPayloadFactory.toDittoProtocol(mappedOutput.get("button"),
        "button", "org.eclipse.vorto", "123")));

  }



  @Test
  public void testMapWithJxpathCondition() throws Exception {
    IDataMapper mapper =
        IDataMapper.newBuilder().withSpecification(new SpecWithPropertyConditionXpath()).build();

    String json = "{\"data\" : [{\"id\": 100,\"value\": \"x\"},{\"id\": 200,\"value\": \"y\"}]}";

    InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));
    assertEquals(100.0,
        mappedOutput.get("button").getStatusProperty("sensor_value").get().getValue());

  }

  @Test
  public void testMappingUsingListInput() throws Exception {

    IDataMapper mapper =
        IDataMapper.newBuilder().withSpecification(new SpecWithArrayPayload()).build();

    String json = "[{\"clickType\" : \"DOUBLE\" }, {\"clickType\" : \"SINGLE\" }]";

    InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));

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

    InfomodelValue mappedOutput =
        mapper.mapSource(gson.fromJson(sampleHomeConnectRESTResponse, Object.class));
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

    InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));

    FunctionblockValue buttonFunctionblockData = mappedOutput.get("btn1");

    assertEquals(2.0, buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());

    FunctionblockValue button2FunctionblockData = mappedOutput.get("btn2");

    assertEquals(10.0, button2FunctionblockData.getStatusProperty("sensor_value").get().getValue());

    System.out.println(mappedOutput);

  }

}
