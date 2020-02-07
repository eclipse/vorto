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
package org.eclipse.vorto.repository.client.generation;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.vorto.model.ModelId;
import org.junit.Test;

import static org.junit.Assert.*;

public class GeneratorUrlBuilderTest {

  private static final String BASE_URL = "https://eclipse.vorto.org";

  @Test
  public void getGeneratorUrl() {
    final String expected = "https://eclipse.vorto.org/api/v1/generators/eclipseditto/models/com.kolotu.test:SomeModel:1.0.0";
    ModelId modelId = ModelId.fromPrettyFormat("com.kolotu.test:SomeModel:1.0.0");
    String generatorKey = "eclipseditto";
    String result = GeneratorUrlBuilder.getGeneratorUrl(BASE_URL, modelId, generatorKey, null);
    assertEquals(expected, result);
  }

  @Test
  public void getGeneratorUrlWithParams() {
    final String expected = "https://eclipse.vorto.org/api/v1/generators/eclipseditto/models/com.kolotu.test:SomeModel:1.0.0?testKey=testValue";
    ModelId modelId = ModelId.fromPrettyFormat("com.kolotu.test:SomeModel:1.0.0");
    String generatorKey = "eclipseditto";
    Map<String, String> params = new HashMap<>();
    params.put("testKey", "testValue");
    String result = GeneratorUrlBuilder.getGeneratorUrl(BASE_URL, modelId, generatorKey, params);
    assertEquals(expected, result);
  }

  @Test
  public void getGeneratorUrlWithMultipleParams() {
    final String expected = "https://eclipse.vorto.org/api/v1/generators/eclipseditto/models/com.kolotu.test:SomeModel:1.0.0?testKey2=testValue2&testKey=testValue";
    ModelId modelId = ModelId.fromPrettyFormat("com.kolotu.test:SomeModel:1.0.0");
    String generatorKey = "eclipseditto";
    Map<String, String> params = new HashMap<>();
    params.put("testKey", "testValue");
    params.put("testKey2", "testValue2");
    String result = GeneratorUrlBuilder.getGeneratorUrl(BASE_URL, modelId, generatorKey, params);
    assertEquals(expected, result);
  }

  @Test
  public void getAllGeneratorsUrl() {
    final String expected = "https://eclipse.vorto.org/api/v1/generators";
    String result = GeneratorUrlBuilder.getAllGeneratorsUrl(BASE_URL);
    assertEquals(expected, result);
  }
}
