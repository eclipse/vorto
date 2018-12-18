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
package org.eclipse.vorto.codegen.ditto;

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorInfo;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException;
import org.eclipse.vorto.codegen.ditto.schema.SchemaValidatorTask;
import org.eclipse.vorto.codegen.utils.GenerationResultBuilder;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * Vorto Generator which generates JSON Schema files for Eclipse Ditto in order to validate whether
 * properties (state) and message payloads (operations, events) are in expeceted JSON format.
 */
public final class EclipseDittoGenerator implements IVortoCodeGenerator {

  @Override
  public IGenerationResult generate(InformationModel infomodel, InvocationContext invocationContext,
      IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {

    GenerationResultZip zipOutputter = new GenerationResultZip(infomodel, getServiceKey());

    ChainedCodeGeneratorTask<InformationModel> generator =
        new ChainedCodeGeneratorTask<InformationModel>();
    generator.addTask(new SchemaValidatorTask());
    generator.generate(infomodel, invocationContext, zipOutputter);

    GenerationResultBuilder result = GenerationResultBuilder.from(zipOutputter);
    return result.build();
  }

  @Override
  public String getServiceKey() {
    return "eclipseditto";
  }

  @Override
  public GeneratorInfo getInfo() {
    return GeneratorInfo.basicInfo("Eclipse Ditto",
        "Creates JSON schema files in order to validate Things managed by Eclipse Ditto.",
        "Eclipse Ditto Team").production();
  }
}
