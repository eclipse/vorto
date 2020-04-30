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

import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.Status
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.ITemplate

class PropertiesStatusValidationTemplate implements ITemplate<Status> {
	
	new() {
	}
	
	override getContent(Status status, InvocationContext invocationContext) {
		var fbm = status.eContainer.eContainer as FunctionblockModel;
		var definition = fbm.namespace + ":" + fbm.name + ":" + fbm.version;
				'''
			{
				"$schema": "http://json-schema.org/draft-04/schema#",
				"title": "Properties validation of definition <«definition»> for <status> properties",
				"type": "object",
				"properties": {
					«EntityValidationTemplate.handleProperties(status.properties, invocationContext).toString.trim»
				},
				«EntityValidationTemplate.calculateRequired(status.properties)»
			}
		'''
	}

}
