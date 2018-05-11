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
package org.eclipse.vorto.codegen.mqtt.python

import org.eclipse.vorto.codegen.api.GenerationResultZip
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException

class PythonGenerator implements IVortoCodeGenerator {

	override generate(InformationModel infomodel, InvocationContext context, IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {

		var output = new GenerationResultZip(infomodel,getServiceKey());
		
		var imTemplateGen = new GeneratorTaskFromFileTemplate(new org.eclipse.vorto.codegen.mqtt.python.PythonImTemplate())
		imTemplateGen.generate(infomodel,context,output)		
		
		var initTemplateGen = new GeneratorTaskFromFileTemplate(new org.eclipse.vorto.codegen.mqtt.python.PythonInitTemplate())
       	initTemplateGen.generate(infomodel,context,output) 
		
		var sampleTemplateGen = new GeneratorTaskFromFileTemplate(new org.eclipse.vorto.codegen.mqtt.python.PythonSampleTemplate())
		sampleTemplateGen.generate(infomodel,context,output)
		
		var dittoSerializerTemplateGen = new GeneratorTaskFromFileTemplate(new org.eclipse.vorto.codegen.mqtt.python.PythonDittoSerializerTemplate())
        dittoSerializerTemplateGen.generate(infomodel,context,output)        
		
		for (FunctionblockProperty fbProperty : infomodel.properties) {
			var fbTemplateGen = new GeneratorTaskFromFileTemplate(new org.eclipse.vorto.codegen.mqtt.python.PythonFbTemplate());
			fbTemplateGen.generate(fbProperty.type,context,output)											
		}
		
		return output
	}
		
	override getServiceKey() {
		return "pythonmqttgenerator";
	}

}
