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

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorInfo;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException;
import org.eclipse.vorto.codegen.hono.java.model.FunctionblockTemplate;
import org.eclipse.vorto.codegen.hono.java.model.InformationModelTemplate;
import org.eclipse.vorto.codegen.hono.java.model.JavaClassGeneratorTask;
import org.eclipse.vorto.codegen.hono.java.model.JavaEnumGeneratorTask;
import org.eclipse.vorto.codegen.hono.java.service.IDataServiceTemplate;
import org.eclipse.vorto.codegen.hono.java.service.hono.HonoDataService;
import org.eclipse.vorto.codegen.hono.java.service.hono.HonoMqttClientTemplate;
import org.eclipse.vorto.codegen.utils.GenerationResultBuilder;
import org.eclipse.vorto.codegen.utils.Utils;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * Generates source code for various device platforms that sends a JSON to the Hono MQTT Connector.
 * The data is compliant to a Vorto & Ditto format.
 *
 */
public class EclipseHonoJavaGenerator implements IVortoCodeGenerator {

  @Override
  public IGenerationResult generate(InformationModel model, InvocationContext context,
      IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
    GenerationResultZip output = new GenerationResultZip(model, getServiceKey());

    GenerationResultBuilder result = GenerationResultBuilder.from(output);
    result.append(generateJava(model, context, monitor));


    return output;
  }

  private IGenerationResult generateJava(InformationModel infomodel, InvocationContext context,
      IVortoCodeGenProgressMonitor monitor) {
    GenerationResultZip output = new GenerationResultZip(infomodel, getServiceKey());
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

      for (Entity entity : Utils.getReferencedEntities(fb)) {
        generateForEntity(infomodel, entity, output);
      }
      for (Enum en : Utils.getReferencedEnums(fb)) {
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
  public String getServiceKey() {
    return "hono-java";
  }

  @Override
  public GeneratorInfo getInfo() {
    return GeneratorInfo.basicInfo("Eclipse Hono Java Generator",
        "Generates device java source code that integrates with Eclipse Hono and Eclipse Ditto.",
        "Eclipse Vorto Team");
  }
}
