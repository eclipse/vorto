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
package org.eclipse.vorto.repository.core;

import static org.junit.Assert.assertEquals;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.model.ModelId;
import org.junit.Test;

public class ModelIdTest {

  @SuppressWarnings("deprecation")
  @Test
  public void testFileName() {
    org.eclipse.vorto.core.api.model.model.ModelId modelId =
        new org.eclipse.vorto.core.api.model.model.ModelId(ModelType.Functionblock, "Location",
            "org.eclipse.vorto", "1.0.0");
    assertEquals("org.eclipse.vorto_Location_1_0_0.fbmodel", modelId.getFileName());
  }

  @Test
  public void testDeprecatedModelId() {
    String modelId = "com.bosch.Car:1.0.0";
    assertEquals(new ModelId("Car", "com.bosch", "1.0.0"), ModelId.fromPrettyFormat(modelId));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidModelId() {
    String modelId = "Test";
    ModelId.fromPrettyFormat(modelId);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidModelId2() {
    String modelId = "com.bosch.Test";
    ModelId.fromPrettyFormat(modelId);
  }

  @Test
  public void testValidModelId() {
    String modelId = "com.bosch:Test:1.0.0";
    assertEquals(new ModelId("Test", "com.bosch", "1.0.0"), ModelId.fromPrettyFormat(modelId));
  }

  @Test
  public void testValidModelId2() {
    String modelId = "com:Test:1.0.0";
    assertEquals(new ModelId("Test", "com", "1.0.0"), ModelId.fromPrettyFormat(modelId));
  }

  @Test
  public void testValidModelId3() {
    String modelId = "com.bosch.si:Test:1.0.2";
    assertEquals(new ModelId("Test", "com.bosch.si", "1.0.2"), ModelId.fromPrettyFormat(modelId));
  }
}
