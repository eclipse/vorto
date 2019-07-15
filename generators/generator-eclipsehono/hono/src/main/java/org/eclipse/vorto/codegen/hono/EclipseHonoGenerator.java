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
package org.eclipse.vorto.codegen.hono;

import org.eclipse.vorto.codegen.hono.arduino.ArduinoCodeGenerator;
import org.eclipse.vorto.codegen.hono.java.EclipseHonoJavaGenerator;
import org.eclipse.vorto.codegen.hono.python.PythonGenerator;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.plugin.generator.GeneratorException;
import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.eclipse.vorto.plugin.generator.config.ConfigTemplateBuilder;
import org.eclipse.vorto.plugin.generator.config.ConfigTemplateBuilder.ChoiceItem;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultBuilder;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultZip;

/**
 * Generates source code for various device platforms that sends a JSON to the Hono MQTT Connector.
 * The data is compliant to a Vorto & Ditto format.
 *
 */
public class EclipseHonoGenerator implements ICodeGenerator {

  private static final String KEY_LANGUAGE = "language";

  @Override
  public IGenerationResult generate(InformationModel model, InvocationContext context) throws GeneratorException {
    GenerationResultZip output = new GenerationResultZip(model, "eclipsehono");

    GenerationResultBuilder result = GenerationResultBuilder.from(output);

    String platform = context.getConfigurationProperties().getOrDefault(KEY_LANGUAGE, "java");
    if (platform.equalsIgnoreCase("arduino")) {
      result.append(generateArduino(model, context));
    } else if (platform.equalsIgnoreCase("python")) {
      result.append(generatePython(model, context));
    } else {
      result.append(generateJava(model, context));
    }

    return output;
  }

  private IGenerationResult generateJava(InformationModel infomodel, InvocationContext context) throws GeneratorException {
    EclipseHonoJavaGenerator javaGenerator = new EclipseHonoJavaGenerator();
    return javaGenerator.generate(infomodel, context);
  }

  private IGenerationResult generatePython(InformationModel infomodel, InvocationContext context) throws GeneratorException {
    PythonGenerator pythonGenerator = new PythonGenerator();
    return pythonGenerator.generate(infomodel, context);
  }

  private IGenerationResult generateArduino(InformationModel infomodel, InvocationContext context) throws GeneratorException {
    ArduinoCodeGenerator arduinoGenerator = new ArduinoCodeGenerator();
    return arduinoGenerator.generate(infomodel, context);
  }

  @Override
  public GeneratorPluginInfo getMeta() {
    return GeneratorPluginInfo.Builder("eclipsehono")
        .withConfigurationKey(KEY_LANGUAGE)
        .withConfigurationTemplate(ConfigTemplateBuilder.builder()
            .withChoiceConfigurationItem(KEY_LANGUAGE, "Device Platform", ChoiceItem.of("Arduino (ESP8266)", "Arduino"), ChoiceItem.of("Python (v2)", "Python"), ChoiceItem.of("Java", "Java")).build())
        .withVendor("Eclipse Vorto Team")
        .withName("Eclipse Hono")
        .withDescription("Generates device code (Arduino, Python, Java) that sends device telemetry data to Eclipse Hono MQTT Connector.")
        .withDocumentationUrl("https://www.eclipse.org/hono")
        .build();
  }
}
