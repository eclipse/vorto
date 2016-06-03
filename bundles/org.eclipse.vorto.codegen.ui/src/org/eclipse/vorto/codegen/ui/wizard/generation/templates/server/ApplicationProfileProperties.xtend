package org.eclipse.vorto.codegen.ui.wizard.generation.templates.server

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.ui.context.IGeneratorProjectContext

class ApplicationProfileProperties implements IFileTemplate<IGeneratorProjectContext> {
	
	override getFileName(IGeneratorProjectContext context) {
		return "application-dev.properties"
	}
	
	override getPath(IGeneratorProjectContext context) {
		return "src/main/resources"
	}
	
	override getContent(IGeneratorProjectContext context,InvocationContext invocationContext) {
		'''
		vorto.service.repositoryUrl=http://localhost:8080/infomodelrepository/rest
		server.contextPath=/vorto-«context.generatorName.toLowerCase»
		server.host=localhost
		server.port=9001
		'''
	}
	
}