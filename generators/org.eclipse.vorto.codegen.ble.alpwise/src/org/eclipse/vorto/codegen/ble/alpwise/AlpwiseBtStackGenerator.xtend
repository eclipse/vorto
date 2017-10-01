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
import org.eclipse.vorto.codegen.ble.alpwise.templates.AlpwiseAppHeaderTemplate
import org.eclipse.vorto.codegen.ble.alpwise.templates.AlpwiseAppCbkHeaderTemplate
import org.eclipse.vorto.codegen.ble.alpwise.templates.AlpwiseAppSourceTemplate
import org.eclipse.vorto.codegen.ble.model.ModelTransformer
import org.eclipse.vorto.codegen.ble.model.BleInvocationContext
import org.eclipse.vorto.codegen.ble.model.blegatt.Device
import org.eclipse.vorto.codegen.ble.model.blegatt.Service
import org.eclipse.vorto.codegen.ble.alpwise.templates.AlpwiseServiceSourceTemplate
import org.eclipse.vorto.codegen.ble.alpwise.templates.AlpwiseServiceHeaderTemplate
import org.eclipse.vorto.codegen.ble.templates.BleGattTemplate

class AlpwiseBtStackGenerator implements IVortoCodeGenerator {

	override generate(InformationModel infomodel, InvocationContext context, IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {

		var Device device = new ModelTransformer(infomodel, context).transform();
		var BleInvocationContext bleContext = new BleInvocationContext(context, device);

		var output = new GenerationResultZip(infomodel,getServiceKey());
		
		BleGattTemplate.rootPath = "ble";
		
		var imHeaderTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseImHeaderTemplate())
		imHeaderTemplateGen.generate(infomodel, bleContext, output)
		
		var imSourceTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseImSourceTemplate())
		imSourceTemplateGen.generate(infomodel, bleContext, output)
		
		var appHeaderTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseAppHeaderTemplate())
		appHeaderTemplateGen.generate(infomodel, bleContext, output)
		
		var appCbkHeaderTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseAppCbkHeaderTemplate())
		appCbkHeaderTemplateGen.generate(infomodel, bleContext, output)
		
		var appSourceTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseAppSourceTemplate())
		appSourceTemplateGen.generate(infomodel, bleContext, output)
		
		var utilsHeaderTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseUtilsHeaderTemplate())
		utilsHeaderTemplateGen.generate(infomodel, bleContext, output)

		var utilsSourceTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseUtilsSourceTemplate())
		utilsSourceTemplateGen.generate(infomodel, bleContext, output)
		
		for (Service service : device.getServices()) {
			var serviceHeaderTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseServiceHeaderTemplate())
			serviceHeaderTemplateGen.generate(service, bleContext, output)
			
			var serviceSourceTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseServiceSourceTemplate());
			serviceSourceTemplateGen.generate(service, bleContext, output)
		}
				
		for (FunctionblockProperty fbProperty : infomodel.properties) {
			if (context.getMappedElement(fbProperty.getType(), "Service").hasAttribute("uuid")) {
				var fbHeaderTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseFbHeaderTemplate())
				fbHeaderTemplateGen.generate(fbProperty.type, bleContext, output)
				
				var fbSourceTemplateGen = new GeneratorTaskFromFileTemplate(new AlpwiseFbSourceTemplate());
				fbSourceTemplateGen.generate(fbProperty.type, bleContext, output)
			}
		}
		
		return output
	}
		
	override getServiceKey() {
		return "blegatt";
	}

}
