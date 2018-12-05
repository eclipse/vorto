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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.MappingException;
import org.eclipse.vorto.mapping.engine.functions.ClassFunction;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.eclipse.vorto.model.runtime.ModelValueFactory;
import org.eclipse.vorto.model.runtime.PropertyValue;
import org.eclipse.vorto.service.mapping.spec.SpecWithConfiguration;
import org.eclipse.vorto.service.mapping.spec.SpecWithConfiguration2;
import org.junit.Test;

public class ConfigurationMappingTest {

  @Test
  public void testMapConfigurationSource() throws Exception {
    IMappingSpecification spec = new SpecWithConfiguration2();
    IDataMapper mapper = IDataMapper.newBuilder().withSpecification(spec)
        .registerConverterFunction(new ClassFunction("button", ConfigurationMappingTest.class))
        .build();

    Map<String, Object> source = new HashMap<String, Object>(1);
    source.put("e", true);

    InfomodelValue mapped = mapper.mapSource(source);
    assertEquals(1, mapped.get("button").getConfiguration().size());
    assertEquals(true, mapped.get("button").getConfiguration().get(0).getValue());
  }

  @Test
  public void testMapSimpleConfigValue() throws Exception {
    IMappingSpecification spec = new SpecWithConfiguration();
    IDataMapper mapper = IDataMapper.newBuilder().withSpecification(spec)
        .registerConverterFunction(new ClassFunction("button", ConfigurationMappingTest.class))
        .build();

    PropertyValue newValue =
        ModelValueFactory.createFBPropertyValue(spec.getFunctionBlock("button"), "enable", true);
    PropertyValue oldValue =
        ModelValueFactory.createFBPropertyValue(spec.getFunctionBlock("button"), "enable", false);

    Object mapped = mapper.mapTarget(newValue, Optional.of(oldValue), "button");
    assertEquals("1", mapped);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUnknownConfigProperty() throws Exception {
    IMappingSpecification spec = new SpecWithConfiguration();
    ModelValueFactory.createFBPropertyValue(spec.getFunctionBlock("button"), "notExistProperty",
        true);
  }

  @Test(expected = MappingException.class)
  public void testNotExistFunction() throws Exception {
    IMappingSpecification spec = new SpecWithConfiguration();
    IDataMapper mapper = IDataMapper.newBuilder().withSpecification(spec).build();

    PropertyValue newValue =
        ModelValueFactory.createFBPropertyValue(spec.getFunctionBlock("button"), "enable", true);
    PropertyValue oldValue =
        ModelValueFactory.createFBPropertyValue(spec.getFunctionBlock("button"), "enable", false);

    Object mapped = mapper.mapTarget(newValue, Optional.of(oldValue), "button");
    assertEquals("1", mapped);
  }

  public static Object convertEnable(Map<String, Object> ctx) {
    if (((Boolean) ctx.get("newValue")).booleanValue() == true) {
      return "1";
    } else {
      return "0";
    }
  }

  public static Object convertE(Map<String, Object> ctx) {
    if (((Boolean) ctx.get("newValue")).booleanValue() == true) {
      return "1";
    } else {
      return "0";
    }
  }
}
