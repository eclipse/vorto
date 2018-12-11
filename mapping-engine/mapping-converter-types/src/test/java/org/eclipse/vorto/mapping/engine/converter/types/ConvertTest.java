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
package org.eclipse.vorto.mapping.engine.converter.types;

import static org.junit.Assert.assertEquals;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.converter.types.TypeFunctionFactory;
import org.eclipse.vorto.model.runtime.FunctionblockValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConvertTest {

  private static Gson gson = new GsonBuilder().create();

  @Test
  public void testMappingTypeConversion() throws Exception {

    IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithTypeConversion())
        .registerConverterFunction(TypeFunctionFactory.createFunctions()).build();

    String json = "[{\"lng\" : 0.002322},{\"lng\" : 0.002222}]";

    InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));

    FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");

    assertEquals("0.002322",
        buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());

    System.out.println(mappedOutput);

  }
}
