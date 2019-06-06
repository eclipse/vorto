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
package org.eclipse.vorto.model.runtime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.PrimitiveType;
import org.junit.Test;

public class ValidationTest {

  @Test
  public void testPrimitiveTypeValidationString() {
    FunctionblockValue data = new FunctionblockValue(createModel(PrimitiveType.STRING));
    assertTrue(data.withStatusProperty("prop", "value").validate().isValid());
    assertFalse(data.withStatusProperty("prop", 2).validate().isValid());
  }

  @Test
  public void testPrimitiveTypeValidationInteger() {
    FunctionblockValue data = new FunctionblockValue(createModel(PrimitiveType.INT));
    assertTrue(data.withStatusProperty("prop", 2).validate().isValid());
    assertFalse(data.withStatusProperty("prop", "2").validate().isValid());
    assertFalse(data.withStatusProperty("prop", 2.2).validate().isValid());

    assertFalse(data.withStatusProperty("prop", 2.0).validate().isValid());
  }

  @Test
  public void testPrimitiveTypeValidationFloat() {
    FunctionblockValue data = new FunctionblockValue(createModel(PrimitiveType.FLOAT));
    assertTrue(data.withStatusProperty("prop", 2.2).validate().isValid());
    assertFalse(data.withStatusProperty("prop", "4.2").validate().isValid());
  }

  private FunctionblockModel createModel(PrimitiveType type) {
    FunctionblockModel fbModel = new FunctionblockModel(
        ModelId.fromPrettyFormat("default:TestFB:1.0.0"));
    List<ModelProperty> properties = new ArrayList<>();
    properties.add(ModelProperty.createPrimitiveProperty("prop", true, type));
    fbModel.setStatusProperties(properties);
    return fbModel;
  }
}
