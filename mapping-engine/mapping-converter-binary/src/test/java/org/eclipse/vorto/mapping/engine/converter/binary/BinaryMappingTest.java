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
package org.eclipse.vorto.mapping.engine.converter.binary;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.Conversion;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.converter.JavascriptEvalProvider;
import org.eclipse.vorto.mapping.engine.converter.binary.BinaryFunctionFactory;
import org.eclipse.vorto.mapping.engine.converter.string.StringFunctionFactory;
import org.eclipse.vorto.mapping.engine.converter.types.TypeFunctionFactory;
import org.eclipse.vorto.mapping.engine.model.binary.BinaryData;
import org.eclipse.vorto.mapping.engine.model.blegatt.GattCharacteristic;
import org.eclipse.vorto.mapping.engine.model.blegatt.GattDevice;
import org.eclipse.vorto.mapping.engine.model.blegatt.GattService;
import org.eclipse.vorto.model.runtime.FunctionblockValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BinaryMappingTest {

  private static Gson gson = new GsonBuilder().create();

  @Test
  public void testMappingWithBinary() throws Exception {

    IDataMapper mapper =
        IDataMapper.newBuilder().withSpecification(new SpecWithByteArrayConverter())
            .registerConverterFunction(StringFunctionFactory.createFunctions())
            .registerConverterFunction(TypeFunctionFactory.createFunctions())
            .registerConverterFunction(BinaryFunctionFactory.createFunctions())
            .registerScriptEvalProvider(new JavascriptEvalProvider()).build();
    String x = "4f00630063007500700061006e0063007900200002";
    String json = "{\"data\" : \"" + x + "\"}";

    InfomodelValue mappedDittoOutput = mapper.mapSource(gson.fromJson(json, Object.class));

    FunctionblockValue button = mappedDittoOutput.get("button");

    assertEquals(2, button.getStatusProperty("sensor_value").get().getValue());

    System.out.println(mappedDittoOutput);
  }

  @Test
  public void testMapUsingBase64Converter() throws Exception {
    IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithBase64Converter())
        .registerConverterFunction(BinaryFunctionFactory.createFunctions()).build();

    String json = "{\"data\" : \"" + Base64.encodeBase64String("20".getBytes()) + "\"}";

    InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));
    assertEquals("20", new String((byte[]) mappedOutput.get("button")
        .getStatusProperty("digital_input_state").get().getValue()));

  }

  @Test
  public void testMapWithCustomFunctionCondition() throws Exception {
    IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithConditionFunction())
        .registerConverterFunction(BinaryFunctionFactory.createFunctions())
        .registerConverterFunction(StringFunctionFactory.createFunctions())
        .registerConditionFunction(StringFunctionFactory.createFunctions())
        .registerConditionFunction(BinaryFunctionFactory.createFunctions()).build();

    String json = "{\"data\" : \"aGFsbG8=\"}";

    InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));
    assertEquals("hallo",
        mappedOutput.get("button").getStatusProperty("sensor_value").get().getValue());

  }

  @Test
  public void testMappingWithGattStructure() throws Exception {

    IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecGattConverter())
        .registerConverterFunction(BinaryFunctionFactory.createFunctions())
        .registerScriptEvalProvider(new JavascriptEvalProvider()).build();


    GattDevice gattDevice = new GattDevice();

    GattService gattService = new GattService();
    List<GattCharacteristic> characteristics = new ArrayList<GattCharacteristic>();

    byte[] dest = new byte[6];
    byte[] value = Conversion.intToByteArray(2000, 0, dest, 3, 3);

    characteristics
        .add(new GattCharacteristic("23-D1-13-EF-5F-78-23-15-DE-EF-12-12-0D-F0-00-00", value));

    gattService.setCharacteristics(characteristics);

    List<GattService> services = new ArrayList<GattService>();

    services.add(gattService);
    gattDevice.setModelNumber("23-23-23");
    gattDevice.setServices(services);
    gattDevice.setCharacteristics(characteristics);

    String json = new Gson().toJson(gattDevice);

    InfomodelValue mapped = mapper.mapSource(gson.fromJson(json, Object.class));
    assertEquals(20.00, mapped.get("button").getStatusProperty("sensor_value").get().getValue());
    System.out.println(mapped);
  }
  
  @Test
  public void testMappingBinaryContaining2DataPoints() {
    IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecBinaryConverter())
        .registerConverterFunction(BinaryFunctionFactory.createFunctions())
        .registerScriptEvalProvider(new JavascriptEvalProvider()).build();
    
    byte[] dest = new byte[4]; //2 byte temperature (Byte 1-2), 2 byte humidity (Byte 3-4)
    byte[] value = Conversion.intToByteArray(2000, 0, dest, 0, 2);
    value = Conversion.intToByteArray(8819, 0, dest, 2, 2);
 
    
    BinaryData data = new BinaryData(value);
    InfomodelValue mapped = mapper.mapSource(data);
    assertEquals(20.00, mapped.get("temperature").getStatusProperty("value").get().getValue());
    assertEquals(88.19, mapped.get("humidity").getStatusProperty("value").get().getValue());
  }

}
