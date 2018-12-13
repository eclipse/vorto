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
package org.eclipse.vorto.codegen.testutils;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipInputStream;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException;
import org.eclipse.vorto.codegen.utils.Utils;
import org.eclipse.vorto.core.api.model.ModelConversionUtils;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;

public class GeneratorTest {
  private IVortoCodeGenerator generatorUnderTest;

  private IModelWorkspace modelWorkspace;

  private InformationModel informationModel;

  private IGenerationResult generatorResult;

  public static GeneratorTest withGenerator(IVortoCodeGenerator generator) {
    return new GeneratorTest(Objects.requireNonNull(generator));
  }

  private GeneratorTest(IVortoCodeGenerator generatorUnderTest) {
    this.generatorUnderTest = generatorUnderTest;
  }

  public GeneratorTest andModels(ModelEntry... modelEntries) throws GeneratorTestException {
    ModelWorkspaceReader modelWorkspaceReader = IModelWorkspace.newReader();
    for (ModelEntry modelEntry : modelEntries) {

      if (modelEntry.modelDsl == null || modelEntry.modelType == null) {
        throw new GeneratorTestException("Cannot read model");
      }

      modelWorkspaceReader.addFile(
          new ByteArrayInputStream(modelEntry.modelDsl.getBytes(StandardCharsets.UTF_8)),
          modelEntry.modelType);
    }
    this.modelWorkspace = modelWorkspaceReader.read();
    return this;
  }

  public GeneratorTest useModel(String modelName) throws GeneratorTestException {
    Optional<Model> maybeModel =
        modelWorkspace.get().stream().filter(p -> p.getName().equals(modelName)).findFirst();
    if (maybeModel.isPresent()) {
      Model model = maybeModel.get();
      if (model instanceof InformationModel) {
        this.informationModel = (InformationModel) model;
      } else if (model instanceof FunctionblockModel) {
        this.informationModel = Utils.wrapFunctionBlock((FunctionblockModel) model);
      } else {
        throw new GeneratorTestException(
            "No information model or functionblock of the name: " + modelName);
      }
    } else {
      throw new GeneratorTestException("No model of the name: " + modelName);
    }

    return this;
  }

  public GeneratorTest thenGenerate(InvocationContext invocationContext)
      throws VortoCodeGeneratorException {
    this.generatorResult = generatorUnderTest.generate(
        ModelConversionUtils.convertToFlatHierarchy(informationModel), invocationContext, null);
    return this;
  }

  public ZipDiff.Result andCompare(ZipInputStream zipComparison) {
    return new ZipDiff().diff(
        new ZipInputStream(new ByteArrayInputStream(generatorResult.getContent())), zipComparison);
  }

  public static class ModelEntry {
    private ModelType modelType;
    private String modelDsl;

    public ModelEntry(ModelType modelType, String modelDsl) {
      this.modelType = modelType;
      this.modelDsl = modelDsl;
    }

  }
}
