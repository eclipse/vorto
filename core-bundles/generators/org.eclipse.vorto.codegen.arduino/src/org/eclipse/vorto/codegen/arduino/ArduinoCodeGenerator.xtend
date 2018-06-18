/*******************************************************************************
 *  Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *******************************************************************************/
package org.eclipse.vorto.codegen.arduino

import org.eclipse.vorto.codegen.api.GenerationResultZip
import org.eclipse.vorto.codegen.api.GeneratorInfo
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException
import org.eclipse.vorto.codegen.utils.Utils
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ArduinoCodeGenerator implements IVortoCodeGenerator {

	override generate(InformationModel infomodel, InvocationContext context, IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {

		var output = new GenerationResultZip(infomodel,getServiceKey());
		
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
		
	override getServiceKey() {
		return "arduinocodegenerator";
	}
	
	override getInfo() {
		return GeneratorInfo.basicInfo("Arduino","Generates Arduino C code that connects to Eclipse Hono via MQTT","Vorto Community").production();
	}

}
