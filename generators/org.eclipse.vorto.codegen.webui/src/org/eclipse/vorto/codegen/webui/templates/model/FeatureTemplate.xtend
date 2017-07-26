/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.webui.templates.model;

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.templates.java.JavaClassFieldGetterTemplate
import org.eclipse.vorto.codegen.templates.java.JavaClassFieldSetterTemplate
import org.eclipse.vorto.codegen.templates.java.JavaClassFieldTemplate
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class FeatureTemplate implements IFileTemplate<FunctionblockProperty> {
	
	private JavaClassFieldTemplate propertyTemplate;
	private JavaClassFieldSetterTemplate propertySetterTemplate;
	private JavaClassFieldGetterTemplate propertyGetterTemplate;
	
	new() {
		this.propertyTemplate = new JavaClassFieldTemplate();
		this.propertySetterTemplate = new JavaClassFieldSetterTemplate("set");
		this.propertyGetterTemplate = new JavaClassFieldGetterTemplate("get");
	}
	
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
					«IF element.type.functionblock.status != null»
						«FOR property : element.type.functionblock.status.properties»
							«propertyTemplate.getContent(property,context)»
						«ENDFOR»
										
						«FOR property : element.type.functionblock.status.properties»
							«propertySetterTemplate.getContent(property,context)»
							«propertyGetterTemplate.getContent(property,context)»
						«ENDFOR»
					«ENDIF»
				«ENDIF»
				
			}
		'''
	}
	
}