package org.eclipse.vorto.codegen.template.plugin

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class HelloWorldTemplate implements IFileTemplate<InformationModel> {
	
	private String serviceKey;
	
	new (String serviceKey) {
		this.serviceKey = serviceKey;
	}
	
	override getFileName(InformationModel context) {
		'HelloWorldTemplate.xtend'
	}
	
	override getPath(InformationModel context) {
		'generator-parent/generator-'+serviceKey.toLowerCase+"/src/main/java/com/mycompany/plugin/templates"
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.mycompany.plugin.templates
		
		import org.eclipse.vorto.codegen.api.IFileTemplate
		import org.eclipse.vorto.codegen.api.InvocationContext
		import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
		import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
		import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
		
		class HelloWorldTemplate implements IFileTemplate<InformationModel> {
			
			override getFileName(InformationModel model) {
				return "output.txt"
			}
			
			override getPath(InformationModel model) {
				return null
			}
			
			override getContent(InformationModel model, InvocationContext context) {
				// here goes the generated content
				return 'Hello '+model.name
			}
		}
		'''
	}
	
}