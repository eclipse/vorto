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
package org.eclipse.vorto.codegen.hono.java;

import org.eclipse.vorto.codegen.hono.java.model.FunctionblockTemplate;
import org.eclipse.vorto.codegen.hono.java.model.InformationModelTemplate;
import org.eclipse.vorto.codegen.hono.java.model.JavaClassGeneratorTask;
import org.eclipse.vorto.codegen.hono.java.model.JavaEnumGeneratorTask;
import org.eclipse.vorto.codegen.hono.java.service.IDataServiceTemplate;
import org.eclipse.vorto.codegen.hono.java.service.hono.HonoDataService;
import org.eclipse.vorto.codegen.hono.java.service.hono.HonoMqttClientTemplate;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.plugin.generator.GeneratorException;
import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.eclipse.vorto.plugin.generator.utils.ChainedCodeGeneratorTask;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultBuilder;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultZip;
import org.eclipse.vorto.plugin.generator.utils.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.plugin.generator.utils.IGeneratedWriter;

/**
 * Generates source code for various device platforms that sends a JSON to the Hono MQTT Connector.
 * The data is compliant to a Vorto & Ditto format.
 *
 */
public class EclipseHonoJavaGenerator implements ICodeGenerator {

  @Override
  public IGenerationResult generate(InformationModel model, InvocationContext context) throws GeneratorException {
    GenerationResultZip output = new GenerationResultZip(model, "hono-java");

    GenerationResultBuilder result = GenerationResultBuilder.from(output);
    result.append(generateJava(model, context));


    return output;
  }

  private IGenerationResult generateJava(InformationModel infomodel, InvocationContext context) {
    GenerationResultZip output = new GenerationResultZip(infomodel, "hono-java");
    ChainedCodeGeneratorTask<InformationModel> generator =
        new ChainedCodeGeneratorTask<InformationModel>();

    generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new PomFileTemplate()));
    generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new Log4jTemplate()));

    generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new AppTemplate()));
    generator
        .addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new IDataServiceTemplate()));
    generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new HonoDataService()));
    generator
        .addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new HonoMqttClientTemplate()));
    generator.addTask(
        new GeneratorTaskFromFileTemplate<InformationModel>(new InformationModelTemplate()));

    generator.generate(infomodel, context, output);

    for (FunctionblockProperty fbProperty : infomodel.getProperties()) {
      new GeneratorTaskFromFileTemplate<>(new FunctionblockTemplate(infomodel))
          .generate(fbProperty.getType(), context, output);

      FunctionBlock fb = fbProperty.getType().getFunctionblock();

      for (Entity entity : org.eclipse.vorto.plugin.utils.Utils.getReferencedEntities(fb)) {
        generateForEntity(infomodel, entity, output);
      }
      for (Enum en : org.eclipse.vorto.plugin.utils.Utils.getReferencedEnums(fb)) {
        generateForEnum(infomodel, en, output);
      }
    }

    return output;
  }

  private void generateForEntity(InformationModel infomodel, Entity entity,
      IGeneratedWriter outputter) {
    new JavaClassGeneratorTask(infomodel).generate(entity, null, outputter);
  }

  private void generateForEnum(InformationModel infomodel, Enum en, IGeneratedWriter outputter) {
    new JavaEnumGeneratorTask(infomodel).generate(en, null, outputter);

  }

  @Override
  public GeneratorPluginInfo getMeta() {
    return GeneratorPluginInfo.Builder("hono-java")
        .withVendor("Eclipse Hono Team")
        .withName("Eclipse Hono")
        .withDescription("Creates Java code that sends telemetry data to Hono MQTT Connector.")
        .withDocumentationUrl("https://www.eclipse.org/hono")
        .build();
  }
}
