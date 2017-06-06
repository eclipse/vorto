package com.bosch.iotsuite.console.generator.application.templates.model

import com.bosch.iotsuite.console.generator.application.templates.TemplateUtils
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.templates.java.JavaClassFieldGetterTemplate
import org.eclipse.vorto.codegen.templates.java.JavaClassFieldSetterTemplate
import org.eclipse.vorto.codegen.templates.java.JavaClassFieldTemplate
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class FeatureStatusPropertyTemplate implements IFileTemplate<FunctionblockProperty> {
	
	private JavaClassFieldTemplate propertyTemplate;
	private JavaClassFieldSetterTemplate propertySetterTemplate;
	private JavaClassFieldGetterTemplate propertyGetterTemplate;
	
	new() {
		this.propertyTemplate = new JavaClassFieldTemplate();
		this.propertySetterTemplate = new JavaClassFieldSetterTemplate("set");
		this.propertyGetterTemplate = new JavaClassFieldGetterTemplate("get");
	}
	
	override getFileName(FunctionblockProperty context) {
		'''«context.type.name»Status.java'''
	}
	
	override getPath(FunctionblockProperty context) {
		'''«TemplateUtils.getBaseApplicationPath((context.eContainer as InformationModel))»/model'''
	}
	
	override getContent(FunctionblockProperty element, InvocationContext context) {
		'''
		package com.example.iot.«(element.eContainer as InformationModel).name.toLowerCase».model;
		
		«IF context.configurationProperties.getOrDefault("history","true").equalsIgnoreCase("true")»
		@javax.persistence.Entity
		«ENDIF»
		public class «element.type.name»Status {
					
					«IF context.configurationProperties.getOrDefault("history","true").equalsIgnoreCase("true")»
					@javax.persistence.Id
					@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
					private Long id;
					«ENDIF»
					
					«IF element.type.functionblock.status != null»
					«FOR property : element.type.functionblock.status.properties»
						«propertyTemplate.getContent(property,context)»
					«ENDFOR»
					
					«FOR property : element.type.functionblock.status.properties»
						«propertySetterTemplate.getContent(property,context)»
						«propertyGetterTemplate.getContent(property,context)»
					«ENDFOR»
					«ENDIF»
					
				}
		'''
	}
	
}