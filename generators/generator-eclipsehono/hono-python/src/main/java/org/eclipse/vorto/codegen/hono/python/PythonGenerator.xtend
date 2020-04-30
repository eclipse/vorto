/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.codegen.hono.python

import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo
import org.eclipse.vorto.plugin.generator.ICodeGenerator
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.GenerationResultZip
import org.eclipse.vorto.plugin.generator.utils.GeneratorTaskFromFileTemplate

class PythonGenerator implements ICodeGenerator {

	override generate(InformationModel infomodel, InvocationContext context) {
		var output = new GenerationResultZip(infomodel, "hono-python");

		var imTemplateGen = new GeneratorTaskFromFileTemplate(new PythonImTemplate())
		imTemplateGen.generate(infomodel, context, output)

		var initTemplate = new PythonInitTemplate()
		initTemplate.rootPath = "model"
		new GeneratorTaskFromFileTemplate(initTemplate).generate(infomodel, context, output)
		initTemplate.rootPath = "model/functionblock"
		new GeneratorTaskFromFileTemplate(initTemplate).generate(infomodel, context, output)
		initTemplate.rootPath = "model/infomodel"
		new GeneratorTaskFromFileTemplate(initTemplate).generate(infomodel, context, output)

		new GeneratorTaskFromFileTemplate(new PythonRequirementsTemplate()).generate(infomodel, context, output)

		var sampleTemplateGen = new GeneratorTaskFromFileTemplate(new PythonSampleTemplate())
		sampleTemplateGen.generate(infomodel, context, output)

		var dittoSerializerTemplateGen = new GeneratorTaskFromFileTemplate(new PythonDittoSerializerTemplate())
		dittoSerializerTemplateGen.generate(infomodel, context, output);

		for (FunctionblockProperty fbProperty : infomodel.properties) {
			var fbTemplateGen = new GeneratorTaskFromFileTemplate(new PythonFbTemplate());
			fbTemplateGen.generate(fbProperty.type, context, output)
		}

		return output
	}

	override getMeta() {
		return GeneratorPluginInfo.Builder("hono-python").withVendor("Eclipse Hono Team").withName(
			"Eclipse Hono").withDescription("Generates Python code that connects to Eclipse Hono via MQTT").
			withDocumentationUrl("https://www.eclipse.org/hono").build();
	}

}
