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
package org.eclipse.vorto.codegen.template.plugin

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class GeneratorMainTemplate implements IFileTemplate<InformationModel> {
	
	private String serviceKey;
	
	new (String serviceKey) {
		this.serviceKey = serviceKey;
	}
	
	override getFileName(InformationModel context) {
		serviceKey.toFirstUpper+'Generator.xtend'
	}
	
	override getPath(InformationModel context) {
		'generator-parent/generator-'+serviceKey.toLowerCase+"/src/main/java/com/mycompany/plugin"
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.mycompany.plugin
		
		import com.mycompany.plugin.templates.HelloWorldTemplate
		
		import org.eclipse.vorto.codegen.api.GenerationResultZip
		import org.eclipse.vorto.codegen.api.GeneratorInfo
		import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate
		import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor
		import org.eclipse.vorto.codegen.api.IVortoCodeGenerator
		import org.eclipse.vorto.codegen.api.InvocationContext
		import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException
		import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
		
		class «serviceKey.toLowerCase.toFirstUpper»Generator implements IVortoCodeGenerator {
		
			override generate(InformationModel infomodel, InvocationContext context,
					IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
				var output = new GenerationResultZip(infomodel,getServiceKey());
				
				var helloworldTemplate = new GeneratorTaskFromFileTemplate(new HelloWorldTemplate())
				helloworldTemplate.generate(infomodel,context,output)
				
				return output
			}
			
			override getServiceKey() {
				return "«serviceKey.toLowerCase»";
			}
			
			override getInfo() {
				return GeneratorInfo.basicInfo("«serviceKey»","«serviceKey.toFirstUpper» Generator description","Vendor XY");
			}
			
		}
		'''
	}
	
}