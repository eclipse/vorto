package org.eclipse.vorto.codegen.examples.webui.tasks.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class ApplicationYmlTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		return "application.yml";
	}
	
	override getPath(InformationModel context) {
		return "webdevice.example/src/main/resources"
	}
	
	override getContent(InformationModel context,InvocationContext invocationContext) {
		'''
		server:
		  port: 8080
		  contextPath: /webdevice
		'''
	}
	
}
