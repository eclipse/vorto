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
