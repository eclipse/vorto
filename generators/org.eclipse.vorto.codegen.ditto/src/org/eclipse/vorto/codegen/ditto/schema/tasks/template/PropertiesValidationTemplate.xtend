/*******************************************************************************
 * Copyright (c) 2017 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.codegen.ditto.schema.tasks.template;

import java.util.ArrayList
import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class PropertiesValidationTemplate implements ITemplate<FunctionBlock> {
	
	new() {
	}
	
	override getContent(FunctionBlock fb, InvocationContext invocationContext) {
		var fbm = fb.eContainer as FunctionblockModel;
		var definition = fbm.namespace + ":" + fbm.name + ":" + fbm.version;
		
		val configProperties = new ArrayList<Property>();
		if (fb.configuration !== null && fb.configuration.properties !== null) {
			for(property : fb.configuration.properties) {
				configProperties.add(property);	
			}	
		}
		
		val statusProperties = new ArrayList<Property>();
		if (fb.status !== null && fb.status.properties !== null) {
			for(property : fb.status.properties) {
				statusProperties.add(property);	
			}
		}
		
		val faultProperties = new ArrayList<Property>();
		if (fb.fault !== null && fb.fault.properties !== null) {
			for(property : fb.fault.properties) {
				faultProperties.add(property);	
			}
		}
		
		'''
		{
			"$schema": "http://json-schema.org/draft-04/schema#",
			"title": "Properties validation of definition <«definition»>",
			"type": "object",
			"properties": {
				«IF !configProperties.empty»
				"config": {
					"type": "object",
					"properties": {
						«EntityValidationTemplate.handleProperties(configProperties, invocationContext).toString.trim»
					},
					«EntityValidationTemplate.calculateRequired(configProperties)»
				}«IF !statusProperties.empty || !faultProperties.empty»,«ENDIF»«ENDIF»
				«IF !statusProperties.empty»
				"status": {
					"type": "object",
					"properties": {
						«EntityValidationTemplate.handleProperties(statusProperties, invocationContext).toString.trim»
					},
					«EntityValidationTemplate.calculateRequired(statusProperties)»
				}«IF !faultProperties.empty»,«ENDIF»«ENDIF»
				«IF !faultProperties.empty»
				"fault": {
					"type": "object",
					"properties": {
						«EntityValidationTemplate.handleProperties(faultProperties, invocationContext).toString.trim»
					},
					«EntityValidationTemplate.calculateRequired(statusProperties)»
				}«ENDIF»
			},
			"required" : [«IF configProperties.exists[presence !== null && presence.mandatory]»"config"«IF statusProperties.exists[presence !== null && presence.mandatory]»,«ENDIF»«ENDIF»«IF statusProperties.exists[presence !== null && presence.mandatory]»"status"«IF faultProperties.exists[presence !== null && presence.mandatory]»,«ENDIF»«ENDIF»«IF faultProperties.exists[presence !== null && presence.mandatory]»"fault"«ENDIF»]
		}
		'''
	}
		
}
