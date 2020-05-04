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

import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.functionblock.Event
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.ITemplate

class EventValidationTemplate implements ITemplate<Event>{
	
	new() {
	}
	
	override getContent(Event event, InvocationContext invocationContext) {
		var fbm = event.eContainer.eContainer as FunctionblockModel;
		var definition = fbm.namespace + ":" + fbm.name + ":" + fbm.version;
		'''
			{
				"$schema": "http://json-schema.org/draft-04/schema#",
				"title": "Event payload validation of definition <«definition»> for message subject (event name) <«event.name»>"«IF !event.properties.empty»,«ENDIF»
				«IF event.properties.size === 1»
				«calcSinglePropertyContent(event.properties.get(0), invocationContext)»
				«ELSEIF !event.properties.empty»
				"type": "object",
				"properties": {
					«EntityValidationTemplate.handleProperties(event.properties, invocationContext).toString.trim»
				},
				«EntityValidationTemplate.calculateRequired(event.properties)»
				«ENDIF»
			}
		'''
	}
	
	static def CharSequence calcSinglePropertyContent(Property property, InvocationContext invocationContext) {
		val propertyStuff = EntityValidationTemplate.handleProperty(property, invocationContext).toString.trim
						.replace("\"" + property.name + "\": {", "").trim;
		val theStringContent = propertyStuff.substring(0, propertyStuff.length-1);
		'''
		«theStringContent»
		'''
	}
}
		