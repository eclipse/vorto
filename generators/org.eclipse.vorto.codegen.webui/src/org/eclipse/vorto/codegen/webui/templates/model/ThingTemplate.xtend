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
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ThingTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''«context.name».java''';
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/model'''
	}
	
	override getContent(InformationModel model, InvocationContext context) {
		'''
		package com.example.iot.«model.name.toLowerCase».model;
		
		import com.bosch.iotsuite.management.thingtype.api.ModelId;
		
		«IF context.configurationProperties.getOrDefault("history","true").equalsIgnoreCase("true")»@javax.persistence.Entity«ENDIF»
		public class «model.name» {
			
			«IF context.configurationProperties.getOrDefault("history","true").equalsIgnoreCase("true")»
			@javax.persistence.Id
			@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
			private Long id;
				
			@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
			private java.util.Date createdOn;
		    «ENDIF»
			
			private String thingId;
			private String name;
			
			«IF context.configurationProperties.getOrDefault("history","true").equalsIgnoreCase("true")»@javax.persistence.Transient«ENDIF»
			private ModelId thingType;
			
			«FOR fbProperty : model.properties»
			«IF context.configurationProperties.getOrDefault("history","true").equalsIgnoreCase("true")»
			@javax.persistence.OneToOne(cascade = {javax.persistence.CascadeType.PERSIST,javax.persistence.CascadeType.MERGE})
			«ENDIF»
			private «fbProperty.type.name» «fbProperty.name»;
			«ENDFOR»
				
			public «model.name»(String thingId, String name, ModelId thingType) {
				this.thingId = thingId;
				this.name = name;
				this.thingType = thingType;
				
			}
			
			protected «model.name»() {
			}
		
			public String getThingId() {
				return thingId;
			}
		
			public void setThingId(String thingId) {
				this.thingId = thingId;
			}
		
			«FOR fbProperty : model.properties»
			public «fbProperty.type.name» get«fbProperty.name.toFirstUpper»() {
				return «fbProperty.name»;
			}
			«ENDFOR»
			
			«FOR fbProperty : model.properties»
			public void set«fbProperty.name.toFirstUpper»(«fbProperty.type.name» «fbProperty.name») {
				this.«fbProperty.name» = «fbProperty.name»;
			}
			«ENDFOR»
					
			public ModelId getThingType() {
				return thingType;
			}
		
			public void setThingType(ModelId thingType) {
				this.thingType = thingType;
			}
		
			public String getName() {
				return name;
			}
		
			public void setName(String name) {
				this.name = name;
			}
			
			«IF context.configurationProperties.getOrDefault("history","true").equalsIgnoreCase("true")»
			public Long getId() {
				return id;
			}
			
			public void setId(Long id) {
				this.id = id;
			}
			
			public java.util.Date getCreatedOn() {
				return createdOn;
			}
			
			public void setCreatedOn(java.util.Date createdOn) {
				this.createdOn = createdOn;
			}
			«ENDIF»
		}
		'''
	}
	
}