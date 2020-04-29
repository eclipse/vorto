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
package org.eclipse.vorto.codegen.bosch.templates;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.vorto.codegen.bosch.BoschIoTSuiteGenerator;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.plugin.AbstractGeneratorTest;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.eclipse.vorto.plugin.generator.utils.Generated;
import org.eclipse.vorto.plugin.generator.utils.SingleGenerationResult;
import org.junit.Test;

public class BoschIoTSuiteGeneratorTest extends AbstractGeneratorTest {

  ICodeGenerator boschIOTSuiteGenerator = new BoschIoTSuiteGenerator();
  List<MappingModel> mappingModels = new ArrayList<MappingModel>();
  Map<String, String> configProperties = new HashMap<>();

  /*
   * Check if cpp file is generated when language is passed as arduino to BoschIOTSuiteGenerator
   */
  @Test
  public void generateArduino() throws Exception {
    configProperties.put("language", "arduino");
    InvocationContext context = new InvocationContext(mappingModels, configProperties);
    IGenerationResult iGenerationResult = boschIOTSuiteGenerator.generate(modelProvider(), context);
    Generated generatedfile =
        zipFileReader(iGenerationResult, "StatusPropertiesFunctionBlock", ".cpp");
    assertEquals("StatusPropertiesFunctionBlock.cpp", generatedfile.getFileName());
  }

  /*
   * Check if python file is generated when language is passed as python to BoschIOTSuiteGenerator
   */
  @Test
  public void generatePython() throws Exception {
    configProperties.put("language", "python");
    InvocationContext context = new InvocationContext(mappingModels, configProperties);
    IGenerationResult iGenerationResult = boschIOTSuiteGenerator.generate(modelProvider(), context);
    Generated generatedfile =
        zipFileReader(iGenerationResult, "StatusPropertiesFunctionBlock", ".py");
    assertEquals("StatusPropertiesFunctionBlock.py", generatedfile.getFileName());
  }

  /*
   * Check if java file is generated when language is passed as java to BoschIOTSuiteGenerator
   */
  @Test
  public void generateJava() throws Exception {
    configProperties.put("language", "python");
    InvocationContext context = new InvocationContext(mappingModels, configProperties);
    IGenerationResult iGenerationResult = boschIOTSuiteGenerator.generate(modelProvider(), context);
    Generated generatedfile =
        zipFileReader(iGenerationResult, "StatusPropertiesFunctionBlock", ".java");
    assertEquals("StatusPropertiesFunctionBlock.java", generatedfile.getFileName());
  }

  /*
   * Check if provisioning script file is returned when config property is passed as provision to
   * BoschIOTSuiteGenerator
   */
  @Test
  public void generateProvisionTrue() throws Exception {
    configProperties.put("provision", "true");
    InvocationContext context = new InvocationContext(mappingModels, configProperties);
    SingleGenerationResult singleGenerationResult =
        (SingleGenerationResult) boschIOTSuiteGenerator.generate(modelProvider(), context);
    assertEquals("Provisioning_MySensor.postman.json", singleGenerationResult.getFileName());

  }
}
