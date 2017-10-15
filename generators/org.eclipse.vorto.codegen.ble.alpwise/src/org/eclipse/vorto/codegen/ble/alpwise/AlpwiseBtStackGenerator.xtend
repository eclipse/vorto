/*******************************************************************************
 *  Copyright (c) 2017 Oliver Meili
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Oliver Meili <omi@ieee.org>
 *******************************************************************************/
package org.eclipse.vorto.codegen.ble.alpwise

import org.eclipse.vorto.codegen.api.GenerationResultZip
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException
import org.eclipse.vorto.codegen.ble.alpwise.templates.AlpwiseImHeaderTemplate
import org.eclipse.vorto.codegen.ble.alpwise.templates.AlpwiseImSourceTemplate
import org.eclipse.vorto.codegen.ble.alpwise.templates.AlpwiseFbHeaderTemplate
import org.eclipse.vorto.codegen.ble.alpwise.templates.AlpwiseFbSourceTemplate
import org.eclipse.vorto.codegen.ble.alpwise.templates.AlpwiseUtilsHeaderTemplate
import org.eclipse.vorto.codegen.ble.alpwise.templates.AlpwiseUtilsSourceTemplate
import org.eclipse.vorto.codegen.ble.alpwise.templates.AlpwiseTemplate
import org.eclipse.vorto.codegen.ble.alpwise.templates.AlpwiseAppHeaderTemplate
import org.eclipse.vorto.codegen.ble.alpwise.templates.AlpwiseAppCbkHeaderTemplate
import org.eclipse.vorto.codegen.ble.alpwise.templates.AlpwiseAppSourceTemplate

class AlpwiseBtStackGenerator implements IVortoCodeGenerator {

	override generate(InformationModel infomodel, InvocationContext context, IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {

		var output = new GenerationResultZip(infomodel,getServiceKey());
		
		AlpwiseTemplate.rootPath = "ble";
		
		var imHeaderTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseImHeaderTemplate())
		imHeaderTemplateGen.generate(infomodel,context,output)
		
		var imSourceTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseImSourceTemplate())
		imSourceTemplateGen.generate(infomodel,context,output)
		
		var appHeaderTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseAppHeaderTemplate())
		appHeaderTemplateGen.generate(infomodel,context,output)
		
		var appCbkHeaderTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseAppCbkHeaderTemplate())
		appCbkHeaderTemplateGen.generate(infomodel,context,output)
		
		var appSourceTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseAppSourceTemplate())
		appSourceTemplateGen.generate(infomodel,context,output)
		
		var utilsHeaderTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseUtilsHeaderTemplate())
		utilsHeaderTemplateGen.generate(infomodel,context,output)

		var utilsSourceTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseUtilsSourceTemplate())
		utilsSourceTemplateGen.generate(infomodel,context,output)
				
		for (FunctionblockProperty fbProperty : infomodel.properties) {
			if (context.getMappedElement(fbProperty.getType(), "Service").hasAttribute("uuid")) {
				var fbHeaderTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseFbHeaderTemplate())
				fbHeaderTemplateGen.generate(fbProperty.type,context,output)
				
				var fbSourceTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseFbSourceTemplate());
				fbSourceTemplateGen.generate(fbProperty.type,context,output)
			}
		}
		
		return output
	}
		
	override getServiceKey() {
		return "blegatt";
	}

}
