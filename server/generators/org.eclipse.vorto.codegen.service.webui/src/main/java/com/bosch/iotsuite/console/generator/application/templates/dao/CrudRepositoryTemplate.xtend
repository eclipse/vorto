package com.bosch.iotsuite.console.generator.application.templates.dao

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import com.bosch.iotsuite.console.generator.application.templates.TemplateUtils

class CrudRepositoryTemplate implements IFileTemplate<InformationModel> {
	
	
	override getFileName(InformationModel context) {
		'''«context.name»Repository.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/dao'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».dao;
				
		import org.springframework.data.repository.CrudRepository;
		
		import com.example.iot.«element.name.toLowerCase».model.«element.name»;
		
		public interface «element.name»Repository extends CrudRepository<«element.name», Long>  {
		}
		'''
	}
}
