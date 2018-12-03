/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 * 
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.prosystfi;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.emf.ecore.EObject;
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
import org.eclipse.vorto.codegen.prosystfi.tasks.FunctionalItemGeneratorTask;
import org.eclipse.vorto.codegen.prosystfi.tasks.FunctionalItemImplGeneratorTask;
import org.eclipse.vorto.codegen.prosystfi.tasks.JavaClassGeneratorTask;
import org.eclipse.vorto.codegen.prosystfi.tasks.JavaEnumGeneratorTask;
import org.eclipse.vorto.codegen.prosystfi.templates.EclipseClasspathTemplate;
import org.eclipse.vorto.codegen.prosystfi.templates.EclipseProjectFileTemplate;
import org.eclipse.vorto.codegen.prosystfi.templates.FIBuildFileTemplate;
import org.eclipse.vorto.codegen.prosystfi.templates.FIManifestFileTemplate;
import org.eclipse.vorto.codegen.utils.Utils;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class ProSystGenerator implements IVortoCodeGenerator {

  public static final String JAVA_FILE_EXTENSION = ".java";
  public static final String PLUGIN_NATURE_STRING = "org.eclipse.pde.PluginNature";

  public static final String SOURCE = "src";

  public static final String GETTER_PREFIX = "get";
  public static final String SETTER_PREFIX = "set";

  @Override
  public IGenerationResult generate(InformationModel infomodel, InvocationContext ctx,
      IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
    GenerationResultZip zipOutputter = new GenerationResultZip(infomodel, getServiceKey());
    Set<EObject> visited = new HashSet<>();
    Set<String> exports = new HashSet<>();
    for (FunctionblockProperty fbp : infomodel.getProperties()) {
      Set<String> imports = new HashSet<>();
      FunctionBlock fb = fbp.getType().getFunctionblock();
      for (Entity entity : Utils.getReferencedEntities(fb)) {
        if (!visited.contains(entity)) {
          generateForEntity(ctx, entity, zipOutputter);
          visited.add(entity);
        }
        imports.add(entity.getNamespace());
        exports.add(entity.getNamespace());
      }
      for (Enum en : Utils.getReferencedEnums(fb)) {
        if (!visited.contains(en)) {
          generateForEnum(ctx, en, zipOutputter);
          visited.add(en);
        }
        imports.add(en.getNamespace());
        exports.add(en.getNamespace());
      }
      exports.add(infomodel.getNamespace());
      generateForFunctionBlock(ctx, fbp.getType(), zipOutputter,
          imports.toArray(new String[imports.size()]));
    }
    generateEclipseProject(infomodel, ctx, zipOutputter, exports);
    return zipOutputter;
  }

  private void generateEclipseProject(InformationModel infomodel, InvocationContext ctx,
      IGeneratedWriter outputter, Set<String> exports) {
    ChainedCodeGeneratorTask<InformationModel> generator = new ChainedCodeGeneratorTask<>();
    generator.addTask(new GeneratorTaskFromFileTemplate<>(new FIManifestFileTemplate(exports)));
    generator.addTask(new GeneratorTaskFromFileTemplate<>(new FIBuildFileTemplate()));
    generator.addTask(new GeneratorTaskFromFileTemplate<>(new EclipseProjectFileTemplate()));
    generator.addTask(new GeneratorTaskFromFileTemplate<>(new EclipseClasspathTemplate()));
    generator.generate(infomodel, ctx, outputter);
  }

  private void generateForFunctionBlock(InvocationContext ctx,
      FunctionblockModel fbm, IGeneratedWriter outputter, String[] imports) {
    ChainedCodeGeneratorTask<FunctionblockModel> generator =
        new ChainedCodeGeneratorTask<FunctionblockModel>();
    generator
        .addTask(new FunctionalItemGeneratorTask(JAVA_FILE_EXTENSION, SOURCE, fbm.getNamespace()));
    generator.addTask(new FunctionalItemImplGeneratorTask(JAVA_FILE_EXTENSION, SOURCE,
        fbm.getNamespace(), imports));
    generator.generate(fbm, ctx, outputter);
  }

  private void generateForEntity(InvocationContext ctx, Entity entity,
      IGeneratedWriter outputter) {
    ChainedCodeGeneratorTask<Entity> generator = new ChainedCodeGeneratorTask<Entity>();
    generator.addTask(new JavaClassGeneratorTask(JAVA_FILE_EXTENSION, SOURCE, entity.getNamespace(),
        GETTER_PREFIX, SETTER_PREFIX));
    generator.generate(entity, ctx, outputter);
  }

  private void generateForEnum(InvocationContext ctx, Enum en,
      IGeneratedWriter outputter) {
    ChainedCodeGeneratorTask<Enum> generator = new ChainedCodeGeneratorTask<Enum>();
    generator.addTask(new JavaEnumGeneratorTask(JAVA_FILE_EXTENSION, SOURCE, en.getNamespace()));
    generator.generate(en, ctx, outputter);
  }

  @Override
  public String getServiceKey() {
    return "prosyst";
  }

  @Override
  public GeneratorInfo getInfo() {
    return GeneratorInfo
        .basicInfo("ProSyst Functional Items for mBS Gateway",
            "Generates ProSyst Functional Items for the ProSyst mBS Gateway.", "ProSyst")
        .production();
  }
}
