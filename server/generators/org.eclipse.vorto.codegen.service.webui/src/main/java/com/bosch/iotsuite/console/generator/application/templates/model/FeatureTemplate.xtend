package com.bosch.iotsuite.console.generator.application.templates.model

import com.bosch.iotsuite.console.generator.application.templates.TemplateUtils
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class FeatureTemplate implements IFileTemplate<FunctionblockProperty> {
	
	override getFileName(FunctionblockProperty context) {
		'''«context.type.name».java'''
	}
	
	override getPath(FunctionblockProperty context) {
		'''«TemplateUtils.getBaseApplicationPath((context.eContainer as InformationModel))»/model'''
	}
	
	override getContent(FunctionblockProperty element, InvocationContext context) {
		'''			
			package com.example.iot.«(element.eContainer as InformationModel).name.toLowerCase».model;
						
			/**
			* «element.description»
			*/
			«IF context.configurationProperties.getOrDefault("history","true").equalsIgnoreCase("true")»
			@javax.persistence.Entity
			«ENDIF»
			public class «element.type.name» {
				
				«IF context.configurationProperties.getOrDefault("history","true").equalsIgnoreCase("true")»
				@javax.persistence.Id
				@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
				private Long id;
				«ENDIF»
				
				«var fb = element.type.functionblock»	
				«IF fb.status != null»
				«IF context.configurationProperties.getOrDefault("history","true").equalsIgnoreCase("true")»
				    @javax.persistence.OneToOne(cascade = {javax.persistence.CascadeType.PERSIST,javax.persistence.CascadeType.MERGE})
				    «ENDIF»
					private «element.type.name»Status status = null;
				«ENDIF»
				
				«IF fb.status != null»
					public «element.type.name»Status getStatus() {
						return this.status;
					}
					
					public void setStatus(«element.type.name»Status status) {
						this.status = status;
					}
				«ENDIF»
				
				
			}
		'''
	}
	
}