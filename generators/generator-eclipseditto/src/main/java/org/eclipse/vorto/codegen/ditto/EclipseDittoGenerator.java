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
import org.eclipse.vorto.codegen.ditto.schema.tasks.template.DittoStructureTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.plugin.generator.GeneratorException;
import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.eclipse.vorto.plugin.generator.config.ConfigTemplateBuilder;
import org.eclipse.vorto.plugin.generator.config.ConfigTemplateBuilder.ChoiceItem;
import org.eclipse.vorto.plugin.generator.utils.ChainedCodeGeneratorTask;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultBuilder;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultZip;
import org.eclipse.vorto.plugin.generator.utils.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.plugin.generator.utils.SingleGenerationResult;


/**
 * Vorto Generator which generates JSON Schema files for Eclipse Ditto in order to validate whether
 * properties (state) and message payloads (operations, events) are in expeceted JSON format.
 */
public final class EclipseDittoGenerator implements ICodeGenerator {

  private static final DittoStructureTemplate DITTO_THING_JSON_TEMPLATE = new DittoStructureTemplate();
  private static final String GENERATOR_KEY = "eclipseditto";
  private static final String THING_JSON = "thingJson";

  @Override
  public IGenerationResult generate(InformationModel infomodel, InvocationContext invocationContext) throws GeneratorException {
    String target = invocationContext.getConfigurationProperties().getOrDefault("target", "");
    if (THING_JSON.equalsIgnoreCase(target)) {
      SingleGenerationResult output = new SingleGenerationResult("application/json");
      new GeneratorTaskFromFileTemplate<>(DITTO_THING_JSON_TEMPLATE).generate(infomodel, invocationContext, output);
      return output;
    }

    GenerationResultZip zipOutput = new GenerationResultZip(infomodel, GENERATOR_KEY);
    ChainedCodeGeneratorTask<InformationModel> generator = new ChainedCodeGeneratorTask<>();
    generator.addTask(new SchemaValidatorTask());
    generator.generate(infomodel, invocationContext, zipOutput);
    GenerationResultBuilder result = GenerationResultBuilder.from(zipOutput);
    return result.build();
  }

  @Override
  public GeneratorPluginInfo getMeta() {
    return GeneratorPluginInfo.Builder(GENERATOR_KEY)
        .withConfigurationTemplate(ConfigTemplateBuilder.builder().withChoiceConfigurationItem(
            "target", "Output format",
            ChoiceItem.of("Ditto Thing JSON", THING_JSON),
            ChoiceItem.of("JSON Schema", ""))
            .build())
        .withVendor("Eclipse Ditto Team")
        .withName("Eclipse Ditto")
        .withDescription("Creates JSON schema files in order to validate Things managed by Eclipse "
            + "Ditto. With the Ditto Thing JSON Option, the generator creates a Thing JSON, "
            + "which can be send to Ditto to create a Thing.")
        .withDocumentationUrl(
            "https://github.com/eclipse/vorto/blob/master/generators/generator-eclipseditto/Readme.md")
        .build();
  }
}
