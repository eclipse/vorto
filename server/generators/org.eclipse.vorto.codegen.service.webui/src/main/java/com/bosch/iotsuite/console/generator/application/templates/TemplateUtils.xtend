package com.bosch.iotsuite.console.generator.application.templates

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class TemplateUtils {
	
	public def static String getBaseApplicationPath(InformationModel model) {
		'''«model.name.toLowerCase»-solution/src/main/java/com/example/iot/«model.name.toLowerCase»'''
	}
	
	
}