package org.eclipse.vorto.codegen.ui.wizard.generation.templates.server

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.ui.context.IGeneratorProjectContext

class ApplicationPropertiesTemplate implements IFileTemplate<IGeneratorProjectContext> {
	
	override getFileName(IGeneratorProjectContext context) {
		return "application.properties"
	}
	
	override getPath(IGeneratorProjectContext context) {
		return "src/main/resources"
	}
	
	override getContent(IGeneratorProjectContext context,InvocationContext invocationContext) {
		'''
		vorto.service.name=«context.generatorName»
		vorto.service.description=Here goes some short description about the generator
		vorto.service.creator= organization or person's name
		vorto.service.documentationUrl = http://www.eclipse.org/vorto/documentation
		vorto.service.image32x32= img/icon32x32.png
		vorto.service.image144x144= img/icon144x144.png
		vorto.service.classifier=platform
		'''
	}
	
}