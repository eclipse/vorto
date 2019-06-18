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

import org.eclipse.vorto.codegen.bosch.templates.BoschGeneratorConfigUI;
import org.eclipse.vorto.codegen.bosch.templates.ProvisionDeviceScriptTemplate;
import org.eclipse.vorto.codegen.hono.EclipseHonoGenerator;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.plugin.generator.GeneratorException;
import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultBuilder;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultZip;
import org.eclipse.vorto.plugin.generator.utils.GeneratorTaskFromFileTemplate;

public class BoschIoTSuiteGenerator implements ICodeGenerator {

  private static final String KEY = "boschiotsuite";
  
  private static final BoschGeneratorConfigUI CONFIG_TEMPLATE = new BoschGeneratorConfigUI();

  public IGenerationResult generate(InformationModel infomodel, InvocationContext invocationContext) throws GeneratorException {

    GenerationResultZip output = new GenerationResultZip(infomodel, KEY);

    GenerationResultBuilder result = GenerationResultBuilder.from(output);

    String platform = invocationContext.getConfigurationProperties().getOrDefault("language", "");
    if (platform.equalsIgnoreCase("arduino")) {
      result.append(generateArduino(infomodel, invocationContext));
    } else if (platform.equalsIgnoreCase("python")) {
      result.append(generatePython(infomodel, invocationContext));
    } else if (platform.equalsIgnoreCase("java")) {
      result.append(generateJava(infomodel, invocationContext));
    } 
    
    String provisionScript = invocationContext.getConfigurationProperties().getOrDefault("provision", "false");
    if ("true".equalsIgnoreCase(provisionScript)) {
        new GeneratorTaskFromFileTemplate<>(new ProvisionDeviceScriptTemplate()).generate(infomodel, invocationContext, output);
    }

    return output;
  }

  private IGenerationResult generateJava(InformationModel infomodel, InvocationContext context) throws GeneratorException {

    EclipseHonoGenerator honoGenerator = new EclipseHonoGenerator();
    return honoGenerator.generate(infomodel, context);
  }

  private IGenerationResult generatePython(InformationModel infomodel, InvocationContext context) throws GeneratorException {
    EclipseHonoGenerator honoGenerator = new EclipseHonoGenerator();
    return honoGenerator.generate(infomodel, context);
  }

  private IGenerationResult generateArduino(InformationModel infomodel, InvocationContext context) throws GeneratorException {
    EclipseHonoGenerator honoGenerator = new EclipseHonoGenerator();
    return honoGenerator.generate(infomodel, context);
  }

  @Override
  public GeneratorPluginInfo getMeta() {
    return GeneratorPluginInfo.Builder(KEY)
        .withConfigurationKey("language","provision")
        .withConfigurationTemplate(CONFIG_TEMPLATE.getContent().toString())
        .withName("Bosch IoT Suite")
        .withVendor("Eclipse Vorto Team")
        .withDescription("Generates source code templates for integrating devices with the Bosch IoT Suite.")
        .withDocumentationUrl("https://github.com/eclipse/vorto/blob/master/generators/generator-boschiotsuite/Readme.md")
        .build();
  }

}
