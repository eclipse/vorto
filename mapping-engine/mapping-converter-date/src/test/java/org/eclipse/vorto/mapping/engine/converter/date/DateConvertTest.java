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
package org.eclipse.vorto.mapping.engine.converter.date;

import static org.junit.Assert.assertEquals;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.converter.date.DateFunctionFactory;
import org.eclipse.vorto.model.runtime.FunctionblockValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DateConvertTest {

  private static Gson gson = new GsonBuilder().create();
  private static final DateFormat JSON_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

  @Test
  public void testMappingTimestamp() throws Exception {

    IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithTimestamp())
        .registerConverterFunction(DateFunctionFactory.createFunctions()).build();

    final Date timestamp = new Date();
    String json = "{\"time\" : " + timestamp.getTime() + "}";

    InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));

    FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");

    assertEquals(JSON_DATE_FORMAT.format(timestamp),
        buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());

    System.out.println(mappedOutput);

  }
}
