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
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException
import org.eclipse.vorto.codegen.api.GeneratorInfo

class ArduinoCodeGenerator implements IVortoCodeGenerator {

	override generate(InformationModel infomodel, InvocationContext context, IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {

		var output = new GenerationResultZip(infomodel,getServiceKey());
		
		var imHeaderTemplateGen = new GeneratorTaskFromFileTemplate(new org.eclipse.vorto.codegen.arduino.ArduinoImHeaderTemplate())
		imHeaderTemplateGen.generate(infomodel,context,output)
		
		var imSourceTemplateGen = new GeneratorTaskFromFileTemplate(new org.eclipse.vorto.codegen.arduino.ArduinoImSourceTemplate())
		imSourceTemplateGen.generate(infomodel,context,output)
		
		var sketchTemplateGen = new GeneratorTaskFromFileTemplate(new org.eclipse.vorto.codegen.arduino.ArduinoSketchTemplate())
		sketchTemplateGen.generate(infomodel,context,output)
		
		for (FunctionblockProperty fbProperty : infomodel.properties) {
			var fbHeaderTemplate = new org.eclipse.vorto.codegen.arduino.ArduinoFbHeaderTemplate();
			fbHeaderTemplate.rootPath = infomodel.name + "App";
			var fbHeaderTemplateGen = new GeneratorTaskFromFileTemplate(fbHeaderTemplate)
			fbHeaderTemplateGen.generate(fbProperty.type,context,output)
			
			var fbSourceTemplate = new org.eclipse.vorto.codegen.arduino.ArduinoFbSourceTemplate();
			fbSourceTemplate.rootPath = infomodel.name + "App";
			var fbSourceTemplateGen = new GeneratorTaskFromFileTemplate(fbSourceTemplate);
			fbSourceTemplateGen.generate(fbProperty.type,context,output)
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
