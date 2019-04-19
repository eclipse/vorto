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
package org.eclipse.vorto.codegen.bosch;

import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorInfo;
import org.eclipse.vorto.codegen.api.GeneratorInfo.ChoiceItem;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException;
import org.eclipse.vorto.codegen.bosch.templates.ProvisionDeviceScriptTemplate;
import org.eclipse.vorto.codegen.hono.EclipseHonoGenerator;
import org.eclipse.vorto.codegen.prosystfi.ProSystGenerator;
import org.eclipse.vorto.codegen.utils.GenerationResultBuilder;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class BoschIoTSuiteGenerator implements IVortoCodeGenerator {

  public IGenerationResult generate(InformationModel infomodel, InvocationContext invocationContext,
      IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {

    GenerationResultZip output = new GenerationResultZip(infomodel, getServiceKey());

    GenerationResultBuilder result = GenerationResultBuilder.from(output);

    String platform = invocationContext.getConfigurationProperties().getOrDefault("language", "");
    if (platform.equalsIgnoreCase("arduino")) {
      result.append(generateArduino(infomodel, invocationContext, monitor));
    } else if (platform.equalsIgnoreCase("python")) {
      result.append(generatePython(infomodel, invocationContext, monitor));
    } else if (platform.equalsIgnoreCase("java")) {
      result.append(generateJava(infomodel, invocationContext, monitor));
    } else if (platform.equalsIgnoreCase("gateway")) {
      result.append(generateGateway(infomodel, invocationContext, monitor));
    } else {
      result.append(generateJava(infomodel, invocationContext, monitor));
    }
    
    String provisionScript = invocationContext.getConfigurationProperties().getOrDefault("provision", "false");
    if ("true".equalsIgnoreCase(provisionScript)) {
        new GeneratorTaskFromFileTemplate<>(new ProvisionDeviceScriptTemplate()).generate(infomodel, invocationContext, output);
    }

    return output;
  }

  private IGenerationResult generateJava(InformationModel infomodel, InvocationContext context,
      IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {

    EclipseHonoGenerator honoGenerator = new EclipseHonoGenerator();
    return honoGenerator.generate(infomodel, context, monitor);
  }

  private IGenerationResult generatePython(InformationModel infomodel, InvocationContext context,
      IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
    EclipseHonoGenerator honoGenerator = new EclipseHonoGenerator();
    return honoGenerator.generate(infomodel, context, monitor);
  }

  private IGenerationResult generateArduino(InformationModel infomodel, InvocationContext context,
      IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
    EclipseHonoGenerator honoGenerator = new EclipseHonoGenerator();
    return honoGenerator.generate(infomodel, context, monitor);
  }

  private IGenerationResult generateGateway(InformationModel infomodel, InvocationContext context,
      IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
    ProSystGenerator generator = new ProSystGenerator();
    return generator.generate(infomodel, context, monitor);

  }

  @Override
  public String getServiceKey() {
    return "boschiotsuite";
  }

  @Override
  public GeneratorInfo getInfo() {
    return GeneratorInfo.basicInfo("Bosch IoT Suite",
        "Generates device code that either runs on the Bosch IoT Gateway SW or connects directly to the Bosch IoT Hub.",
        "Eclipse Vorto Team").production().withChoiceConfigurationItem("language",
            "Device Platform", ChoiceItem.of("Arduino (ESP8266)", "Arduino"),
            ChoiceItem.of("Python (v2)", "Python"), ChoiceItem.of("Java", "Java"),
            ChoiceItem.of("Bosch IoT Gateway Software", "gateway"))
    		.withBinaryConfigurationItem("provision", "Device Provisioning Script (requires Postman)");
  }

}
