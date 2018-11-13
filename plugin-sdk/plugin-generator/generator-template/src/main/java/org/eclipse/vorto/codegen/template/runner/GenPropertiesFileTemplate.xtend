package org.eclipse.vorto.codegen.template.runner

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class GenPropertiesFileTemplate implements IFileTemplate<InformationModel> {
	
	private String serviceKey;
	
	new  (String serviceKey) {
		this.serviceKey = serviceKey;
	}
	
	override getFileName(InformationModel context) {
		serviceKey.toLowerCase+'.properties'
	}
	
	override getPath(InformationModel context) {
		'generator-parent/generator-runner/src/main/resources/generators'
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		vorto.service.name=«serviceKey»
		vorto.service.description=
		vorto.service.creator=
		vorto.service.documentationUrl=
		vorto.service.image32x32= img/icon32x32.png
		vorto.service.image144x144= img/icon144x144.png
		vorto.service.classifier=platform
		vorto.service.tags=production
		'''
	}
	
}