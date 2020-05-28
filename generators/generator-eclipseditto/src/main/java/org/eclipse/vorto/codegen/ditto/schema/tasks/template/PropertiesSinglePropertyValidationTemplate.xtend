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
import org.eclipse.vorto.core.api.model.functionblock.Configuration
import org.eclipse.vorto.core.api.model.functionblock.Fault
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.Status
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.ITemplate

class PropertiesSinglePropertyValidationTemplate implements ITemplate<Property> {
	
	new() {
	}
	
	override getContent(Property property, InvocationContext invocationContext) {
		var fbm = property.eContainer.eContainer.eContainer as FunctionblockModel;
		var definition = fbm.namespace + ":" + fbm.name + ":" + fbm.version;
		val propertyStuff = EntityValidationTemplate.handleProperty(property, invocationContext).toString.trim
						.replace("\"" + property.name + "\": {", "").trim;
		val theStringContent = propertyStuff.substring(0, propertyStuff.length-1);
		'''
			{
				"$schema": "http://json-schema.org/draft-04/schema#",
				"title": "Property validation of definition <«definition»> for <«IF property.eContainer instanceof Configuration»configuration«ENDIF»«IF property.eContainer instanceof Status»status«ENDIF»«IF property.eContainer instanceof Fault»fault«ENDIF»> property <«property.name»>",
				«theStringContent»
			}
		'''
	}

}
