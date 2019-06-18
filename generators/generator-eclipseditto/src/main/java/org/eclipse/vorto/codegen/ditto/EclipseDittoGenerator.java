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

import org.eclipse.vorto.codegen.ditto.schema.SchemaValidatorTask;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.plugin.generator.GeneratorException;
import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.eclipse.vorto.plugin.generator.utils.ChainedCodeGeneratorTask;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultBuilder;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultZip;

/**
 * Vorto Generator which generates JSON Schema files for Eclipse Ditto in order to validate whether
 * properties (state) and message payloads (operations, events) are in expeceted JSON format.
 */
public final class EclipseDittoGenerator implements ICodeGenerator {

  private static final String KEY = "eclipseditto";
  @Override
  public IGenerationResult generate(InformationModel infomodel, InvocationContext invocationContext) throws GeneratorException {

    GenerationResultZip zipOutputter = new GenerationResultZip(infomodel,KEY );

    ChainedCodeGeneratorTask<InformationModel> generator =
        new ChainedCodeGeneratorTask<InformationModel>();
    generator.addTask(new SchemaValidatorTask());
    generator.generate(infomodel, invocationContext, zipOutputter);

    GenerationResultBuilder result = GenerationResultBuilder.from(zipOutputter);
    return result.build();
  }

  @Override
  public GeneratorPluginInfo getMeta() {
    return GeneratorPluginInfo.Builder(KEY)
        .withVendor("Eclipse Ditto Team")
        .withDescription("Creates JSON schema files in order to validate Things managed by Eclipse Ditto.")
        .withDocumentationUrl("https://www.eclipse.org/ditto")
        .build();
  }
}
