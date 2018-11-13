package org.eclipse.vorto.codegen.template.runner

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class LocalPropertiesFileTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'application-local.yml'
	}
	
	override getPath(InformationModel context) {
		'generator-parent/generator-runner/src/main/resources'
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		server:
		  host: localhost
		  port: 8081
		  contextPath: /generatorgateway
		  serviceUrl: http://localhost:8081/generatorgateway
		  config:
		    generatorUser:
		    generatorPassword:
		
		vorto:
		  serverUrl: http://localhost:8080/infomodelrepository
		  tenantId: default
		'''
	}
	
}