/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.hono;

import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorInfo;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException;
import org.eclipse.vorto.codegen.api.GeneratorInfo.ChoiceItem;
import org.eclipse.vorto.codegen.hono.arduino.ArduinoCodeGenerator;
import org.eclipse.vorto.codegen.hono.java.EclipseHonoJavaGenerator;
import org.eclipse.vorto.codegen.hono.python.PythonGenerator;
import org.eclipse.vorto.codegen.utils.GenerationResultBuilder;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * Generates source code for various device platforms that sends a JSON to the Hono MQTT Connector.
 * The data is compliant to a Vorto & Ditto format.
 *
 */
public class EclipseHonoGenerator implements IVortoCodeGenerator {

  @Override
  public IGenerationResult generate(InformationModel model, InvocationContext context,
      IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
    GenerationResultZip output = new GenerationResultZip(model, getServiceKey());

    GenerationResultBuilder result = GenerationResultBuilder.from(output);

    String platform = context.getConfigurationProperties().getOrDefault("language", "java");
    if (platform.equalsIgnoreCase("arduino")) {
      result.append(generateArduino(model, context, monitor));
    } else if (platform.equalsIgnoreCase("python")) {
      result.append(generatePython(model, context, monitor));
    } else {
      result.append(generateJava(model, context, monitor));
    }

    return output;
  }

  private IGenerationResult generateJava(InformationModel infomodel, InvocationContext context,
      IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
    EclipseHonoJavaGenerator javaGenerator = new EclipseHonoJavaGenerator();
    return javaGenerator.generate(infomodel, context, monitor);
  }

  private IGenerationResult generatePython(InformationModel infomodel, InvocationContext context,
      IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
    PythonGenerator pythonGenerator = new PythonGenerator();
    return pythonGenerator.generate(infomodel, context, monitor);
  }

  private IGenerationResult generateArduino(InformationModel infomodel, InvocationContext context,
      IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
    ArduinoCodeGenerator arduinoGenerator = new ArduinoCodeGenerator();
    return arduinoGenerator.generate(infomodel, context, monitor);
  }

  @Override
  public String getServiceKey() {
    return "eclipsehono";
  }

  @Override
  public GeneratorInfo getInfo() {
    return GeneratorInfo.basicInfo("Eclipse Hono",
        "Generates device code (Arduino, Python, Java) that integrates with Eclipse Hono and Eclipse Ditto.",
        "Eclipse Vorto Team").production().withChoiceConfigurationItem("language",
            "Device Platform", ChoiceItem.of("Arduino (ESP8266)", "Arduino"),
            ChoiceItem.of("Python (v2)", "Python"), ChoiceItem.of("Java", "Java"));
  }
}
