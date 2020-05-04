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

import org.eclipse.vorto.core.api.model.functionblock.Configuration
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.ITemplate

class PropertiesConfigurationValidationTemplate implements ITemplate<Configuration> {
	
	new() {
	}
	
	override getContent(Configuration configuration, InvocationContext invocationContext) {
		var fbm = configuration.eContainer.eContainer as FunctionblockModel;
		var definition = fbm.namespace + ":" + fbm.name + ":" + fbm.version;
				'''
			{
				"$schema": "http://json-schema.org/draft-04/schema#",
				"title": "Properties validation of definition <«definition»> for <configuration> properties",
				"type": "object",
				"properties": {
					«EntityValidationTemplate.handleProperties(configuration.properties, invocationContext).toString.trim»
				},
				«EntityValidationTemplate.calculateRequired(configuration.properties)»
			}
		'''
	}

}
