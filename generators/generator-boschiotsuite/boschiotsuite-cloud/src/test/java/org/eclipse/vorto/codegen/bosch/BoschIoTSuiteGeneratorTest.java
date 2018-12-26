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
package org.eclipse.vorto.codegen.bosch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.vorto.codegen.AbstractGeneratorTest;
import org.eclipse.vorto.codegen.testutils.GeneratorTest;
import org.eclipse.vorto.codegen.testutils.ZipDiff;
import org.junit.Test;

public class BoschIoTSuiteGeneratorTest extends AbstractGeneratorTest {

  @Test
  public void testGenerator() {

    try {

      ZipDiff.Result result = GeneratorTest.withGenerator(new BoschIoTSuiteGenerator())
          .andModels(functionBlock("Location.fbmodel"),
              informationModel("TrackingDevice.infomodel"))
          .useModel("TrackingDevice")
          .thenGenerate(withInvocationContext(generatorParameter()))
          .andCompare(fromZipLocation("generated-boschiotsuite.zip"));

      assertEquals(0, result.getAdded().size());
      assertEquals(0, result.getRemoved().size());
      assertEquals(0, result.getChanged().size());

    } catch (Exception e) {
      e.printStackTrace();
      fail("Failed generating from generator");
    }
  }

  private Map<String, String> generatorParameter() {
    Map<String, String> parameter = new HashMap<String, String>();
    parameter.put("language", "java");
    return parameter;
  }

}
