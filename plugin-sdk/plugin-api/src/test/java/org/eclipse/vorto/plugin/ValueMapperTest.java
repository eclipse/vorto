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
package org.eclipse.vorto.plugin;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.plugin.generator.utils.javatemplates.ValueMapper;
import org.junit.Test;

public class ValueMapperTest {
  PrimitiveType primitiveTypeString = PrimitiveType.STRING;
  PrimitiveType primitiveTypeBoolean = PrimitiveType.BOOLEAN;
  PrimitiveType primitiveTypeInt = PrimitiveType.INT;
  PrimitiveType primitiveTypeFloat = PrimitiveType.FLOAT;
  PrimitiveType primitiveTypeDouble = PrimitiveType.DOUBLE;
  PrimitiveType primitiveTypeDateTime = PrimitiveType.DATETIME;
  PrimitiveType primitiveTypeShort = PrimitiveType.SHORT;
  PrimitiveType primitiveTypeBase64 = PrimitiveType.BASE64_BINARY;

  /*
   * Check simple mapping primitive type
   */
  @Test
  public void mapSimpleDatatype() throws Exception {
    String primitiveStr = "";

    primitiveStr = ValueMapper.mapSimpleDatatype(primitiveTypeString);
    assertEquals(true, primitiveStr.equalsIgnoreCase("String"));

    primitiveStr = ValueMapper.mapSimpleDatatype(primitiveTypeDateTime);
    assertEquals(true, primitiveStr.equalsIgnoreCase("java.util.Date"));

    primitiveStr = ValueMapper.mapSimpleDatatype(primitiveTypeBase64);
    assertEquals(true, primitiveStr.equalsIgnoreCase("byte[]"));

    primitiveStr = ValueMapper.mapSimpleDatatype(primitiveTypeShort);
    assertEquals(true, primitiveStr.equalsIgnoreCase(primitiveTypeShort.getLiteral()));
  }

  /*
   * check returning initial values with corresponding primitive type
   */
  @Test
  public void getInitialValue() throws Exception {
    String primitiveStr = "";

    primitiveStr = ValueMapper.getInitialValue(primitiveTypeString);
    assertEquals(true, primitiveStr.equalsIgnoreCase("\"\""));

    primitiveStr = ValueMapper.getInitialValue(primitiveTypeDateTime);
    assertEquals(true, primitiveStr.equalsIgnoreCase("new java.util.Date()"));

    primitiveStr = ValueMapper.getInitialValue(primitiveTypeBase64);
    assertEquals(true, primitiveStr.equalsIgnoreCase("null"));

    primitiveStr = ValueMapper.getInitialValue(primitiveTypeShort);
    assertEquals(true, primitiveStr.equalsIgnoreCase("0"));

    primitiveStr = ValueMapper.getInitialValue(primitiveTypeDouble);
    assertEquals(true, primitiveStr.equalsIgnoreCase("0.0d"));

    primitiveStr = ValueMapper.getInitialValue(primitiveTypeFloat);
    assertEquals(true, primitiveStr.equalsIgnoreCase("0.0f"));

    primitiveStr = ValueMapper.getInitialValue(primitiveTypeInt);
    assertEquals(true, primitiveStr.equalsIgnoreCase("0"));
  }
}
