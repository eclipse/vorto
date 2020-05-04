/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.codegen.ditto.schema.tasks.template;

import java.util.ArrayList
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.ITemplate

class PropertiesValidationTemplate implements ITemplate<FunctionBlock> {
	
	new() {
	}
	
	override getContent(FunctionBlock fb, InvocationContext invocationContext) {
		var fbm = fb.eContainer as FunctionblockModel;
		var definition = fbm.namespace + ":" + fbm.name + ":" + fbm.version;
		
		val configurationProperties = new ArrayList<Property>();
		if (fb.configuration !== null && fb.configuration.properties !== null) {
			for(property : fb.configuration.properties) {
				configurationProperties.add(property);	
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
				«IF !configurationProperties.empty»
				"configuration": {
					"type": "object",
					"properties": {
						«EntityValidationTemplate.handleProperties(configurationProperties, invocationContext).toString.trim»
					},
					«EntityValidationTemplate.calculateRequired(configurationProperties)»
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
			"required" : [«IF configurationProperties.exists[presence !== null && presence.mandatory]»"configuration"«IF statusProperties.exists[presence !== null && presence.mandatory]»,«ENDIF»«ENDIF»«IF statusProperties.exists[presence !== null && presence.mandatory]»"status"«IF faultProperties.exists[presence !== null && presence.mandatory]»,«ENDIF»«ENDIF»«IF faultProperties.exists[presence !== null && presence.mandatory]»"fault"«ENDIF»]
		}
		'''
	}
		
}
