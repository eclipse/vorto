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
package org.eclipse.vorto.codegen.template;

import java.util.Optional;
import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorInfo;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.template.plugin.GeneratorMainTemplate;
import org.eclipse.vorto.codegen.template.plugin.HelloWorldTemplate;
import org.eclipse.vorto.codegen.template.runner.GenPropertiesFileTemplate;
import org.eclipse.vorto.codegen.template.runner.GeneratorConfigurationTemplate;
import org.eclipse.vorto.codegen.template.runner.GeneratorRunnerTemplate;
import org.eclipse.vorto.codegen.template.runner.LocalPropertiesFileTemplate;
import org.eclipse.vorto.codegen.template.runner.PomTemplate;
import org.eclipse.vorto.codegen.template.runner.DockerComposeTemplate;
import org.eclipse.vorto.codegen.template.runner.DockerfileTemplate;
import org.eclipse.vorto.codegen.template.runner.DockerWaitForItShTemplate;
import org.eclipse.vorto.codegen.template.runner.DockerRunGeneratorsShTemplate;
import org.eclipse.vorto.codegen.template.runner.RepositoryConfigTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class CodeGeneratorTemplateGenerator implements IVortoCodeGenerator {

  @Override
  public IGenerationResult generate(InformationModel model, InvocationContext context,
      IVortoCodeGenProgressMonitor monitor) {

    final String serviceKey = context.getConfigurationProperties()
        .getOrDefault("serviceKey", "myplatform").replace(" ", "");

    GenerationResultZip output = new GenerationResultZip(model, getServiceKey());
    ChainedCodeGeneratorTask<InformationModel> generator =
        new ChainedCodeGeneratorTask<InformationModel>();

    // Generates the parent project
    generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(
        new org.eclipse.vorto.codegen.template.parent.PomTemplate()));

    // Generates the Vorto Code Generator Runner Project
    generator.addTask(
        new GeneratorTaskFromFileTemplate<InformationModel>(new GeneratorConfigurationTemplate()));
    generator.addTask(
        new GeneratorTaskFromFileTemplate<InformationModel>(new GeneratorRunnerTemplate()));
    generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(
        new GenPropertiesFileTemplate(serviceKey)));
    generator.addTask(
        new GeneratorTaskFromFileTemplate<InformationModel>(new LocalPropertiesFileTemplate()));
    generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new PomTemplate()));
    generator
        .addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new DockerComposeTemplate()));
    generator.addTask(
        new GeneratorTaskFromFileTemplate<InformationModel>(new DockerWaitForItShTemplate()));
    generator.addTask(
        new GeneratorTaskFromFileTemplate<InformationModel>(new DockerRunGeneratorsShTemplate()));
    generator
        .addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new DockerfileTemplate()));
    generator.addTask(
        new GeneratorTaskFromFileTemplate<InformationModel>(new RepositoryConfigTemplate()));

    // Generates the Vorto Code Generator Plugin Project
    generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(
        new org.eclipse.vorto.codegen.template.plugin.PomTemplate(serviceKey)));
    generator.addTask(
        new GeneratorTaskFromFileTemplate<InformationModel>(new GeneratorMainTemplate(serviceKey)));
    generator.addTask(
        new GeneratorTaskFromFileTemplate<InformationModel>(new HelloWorldTemplate(serviceKey)));

    generator.generate(model, context, output);
    return output;
  }

  @Override
  public String getServiceKey() {
    return "generator_template";
  }

  @Override
  public GeneratorInfo getInfo() {
    return GeneratorInfo
        .basicInfo("Vorto Code Generator Template",
            "Generates a Vorto Code Generator template to create custom Vorto Code generators",
            "Vorto Team")
        .withTextConfigurationItem("serviceKey", "Key", Optional.of("MyPlatform")).infra();
  }
}
