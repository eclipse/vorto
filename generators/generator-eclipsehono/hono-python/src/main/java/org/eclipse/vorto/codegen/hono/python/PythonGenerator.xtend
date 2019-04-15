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
package org.eclipse.vorto.codegen.hono.python
import org.eclipse.vorto.codegen.api.GenerationResultZip
import org.eclipse.vorto.codegen.api.GeneratorInfo
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class PythonGenerator implements IVortoCodeGenerator {

	override generate(InformationModel infomodel, InvocationContext context, IVortoCodeGenProgressMonitor monitor) {
		var output = new GenerationResultZip(infomodel,getServiceKey());
 		
		var imTemplateGen = new GeneratorTaskFromFileTemplate(new PythonImTemplate())
 		imTemplateGen.generate(infomodel,context,output)		
 		
		var initTemplate = new PythonInitTemplate()
		initTemplate.rootPath = "model"
        new GeneratorTaskFromFileTemplate(initTemplate).generate(infomodel,context,output)
		initTemplate.rootPath = "model/functionblock"
        new GeneratorTaskFromFileTemplate(initTemplate).generate(infomodel,context,output)
        initTemplate.rootPath = "model/infomodel"
        new GeneratorTaskFromFileTemplate(initTemplate).generate(infomodel,context,output)
        
        new GeneratorTaskFromFileTemplate(new PythonRequirementsTemplate()).generate(infomodel,context,output)
 		
		var sampleTemplateGen = new GeneratorTaskFromFileTemplate(new PythonSampleTemplate())
 		sampleTemplateGen.generate(infomodel,context,output)
 		
		var dittoSerializerTemplateGen = new GeneratorTaskFromFileTemplate(new PythonDittoSerializerTemplate())
 		dittoSerializerTemplateGen.generate(infomodel,context,output);
 		
 		for (FunctionblockProperty fbProperty : infomodel.properties) {
			var fbTemplateGen = new GeneratorTaskFromFileTemplate(new PythonFbTemplate());
			fbTemplateGen.generate(fbProperty.type,context,output)
 		}
 		
 		return output
	}

	override getServiceKey() {
		return "hono-python";
	}

	override GeneratorInfo getInfo() {
		return GeneratorInfo.basicInfo("Eclipse Hono Python Generator",
			"This generator allows for easy implementation of a device sending telemetry data to an MQTT broker based on Eclipse Paho for Python.",
			"Eclipse Vorto Team")
		}
	}
	