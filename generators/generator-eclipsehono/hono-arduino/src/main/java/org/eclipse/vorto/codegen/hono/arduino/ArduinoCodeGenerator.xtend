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
package org.eclipse.vorto.codegen.hono.arduino

import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.plugin.generator.GeneratorException
import org.eclipse.vorto.plugin.generator.ICodeGenerator
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.GenerationResultZip
import org.eclipse.vorto.plugin.generator.utils.GeneratorTaskFromFileTemplate
import org.eclipse.vorto.plugin.utils.Utils
import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo

class ArduinoCodeGenerator implements ICodeGenerator {

	override generate(InformationModel infomodel, InvocationContext context) throws GeneratorException {

		var output = new GenerationResultZip(infomodel,"arduinocodegenerator");
		
		var imHeaderTemplateGen = new GeneratorTaskFromFileTemplate(new ArduinoImHeaderTemplate())
		imHeaderTemplateGen.generate(infomodel,context,output)
		
		var imSourceTemplateGen = new GeneratorTaskFromFileTemplate(new ArduinoImSourceTemplate())
		imSourceTemplateGen.generate(infomodel,context,output)
		
		var sketchTemplateGen = new GeneratorTaskFromFileTemplate(new ArduinoSketchTemplate())
		sketchTemplateGen.generate(infomodel,context,output)
		
		for (FunctionblockProperty fbProperty : infomodel.properties) {
			var fbHeaderTemplate = new ArduinoFbHeaderTemplate();
			fbHeaderTemplate.rootPath = infomodel.name + "App";
			var fbHeaderTemplateGen = new GeneratorTaskFromFileTemplate(fbHeaderTemplate)
			fbHeaderTemplateGen.generate(fbProperty.type,context,output)
			
			var fbSourceTemplate = new ArduinoFbSourceTemplate();
			fbSourceTemplate.rootPath = infomodel.name + "App";
			var fbSourceTemplateGen = new GeneratorTaskFromFileTemplate(fbSourceTemplate);
			fbSourceTemplateGen.generate(fbProperty.type,context,output)
			
			for(enumProperty : Utils.getReferencedEnums(fbProperty.type.functionblock)) {
				var enumHeaderTemplate = new ArduinoEnumHeaderTemplate;
				enumHeaderTemplate.rootPath = infomodel.name + "App";
				var enumHeaderTemplateGen = new GeneratorTaskFromFileTemplate(enumHeaderTemplate); 
				enumHeaderTemplateGen.generate(enumProperty,context,output)
			}
			
            for(entityProperty : Utils.getReferencedEntities(fbProperty.type.functionblock)) {
                var entityHeaderTemplate = new ArduinoEntityHeaderTemplate;
                entityHeaderTemplate.rootPath = infomodel.name + "App";
                var entityHeaderTemplateGen = new GeneratorTaskFromFileTemplate(entityHeaderTemplate); 
                entityHeaderTemplateGen.generate(entityProperty,context,output)
                
                var entitySourceTemplate = new ArduinoEntitySoureTemplate;
                entitySourceTemplate.rootPath = infomodel.name + "App";
                var entitySourceTemplateGen = new GeneratorTaskFromFileTemplate(entitySourceTemplate); 
                entitySourceTemplateGen.generate(entityProperty,context,output)
            }
		}
		
		return output
	}
			
	override getMeta() {
		return GeneratorPluginInfo.Builder("arduinocodegenerator")
        .withVendor("Eclipse Hono Team")
        .withName("Eclipse Hono")
        .withDescription("Generates Arduino C code that connects to Eclipse Hono via MQTT")
        .withDocumentationUrl("https://www.eclipse.org/hono")
        .build();
	}

}
