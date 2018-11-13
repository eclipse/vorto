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
		
		class «serviceKey.toFirstUpper»Generator implements IVortoCodeGenerator {
		
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