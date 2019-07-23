/**
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.codegen.openapi

import org.eclipse.vorto.codegen.openapi.templates.OpenAPITemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.plugin.generator.GeneratorException
import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo
import org.eclipse.vorto.plugin.generator.ICodeGenerator
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.config.ConfigTemplateBuilder
import org.eclipse.vorto.plugin.generator.config.ConfigTemplateBuilder.ChoiceItem
import org.eclipse.vorto.plugin.generator.utils.GeneratorTaskFromFileTemplate
import org.eclipse.vorto.plugin.generator.utils.SingleGenerationResult

class OpenAPIGenerator implements ICodeGenerator {
	
	val static String KEY = "openapi"

	override generate(InformationModel infomodel, InvocationContext context) throws GeneratorException {
		var output = new SingleGenerationResult("application/vnd.oai.openapi;version=3.0")
		new GeneratorTaskFromFileTemplate(new OpenAPITemplate()).generate(infomodel,context,output);	
		return output
	}
	
	override getMeta() {
		return GeneratorPluginInfo.Builder(KEY)
			.withConfigurationKey("digitaltwin")
			.withConfigurationTemplate(ConfigTemplateBuilder.builder.withChoiceConfigurationItem("digitaltwin","Digital Twin API",ChoiceItem.of("Bosch IoT Things","boschiotthings")).build)
			.withDescription("Generates device specific OpenAPI - Swagger descriptors for Digital Twin Service APIs")
			.withName("OpenAPI")
			.withDocumentationUrl("https://swagger.io/docs/specification/about/")
			.withVendor("Eclipse Vorto Team")
			.build
	}
}
