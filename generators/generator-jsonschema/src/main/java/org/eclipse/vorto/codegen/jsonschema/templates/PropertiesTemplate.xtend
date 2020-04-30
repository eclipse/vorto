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
package org.eclipse.vorto.codegen.jsonschema.templates;

import java.util.ArrayList
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.IFileTemplate

class PropertiesTemplate implements IFileTemplate<FunctionblockModel> {
	
	override getContent(FunctionblockModel fbm, InvocationContext invocationContext) {
		var definition = fbm.namespace + ":" + fbm.name + ":" + fbm.version;
		
		val properties = new ArrayList<Property>();
		if (fbm.functionblock.configuration !== null && fbm.functionblock.configuration.properties !== null) {
			for(property : fbm.functionblock.configuration.properties) {
				properties.add(property);	
			}	
		}
		
		if (fbm.functionblock.status !== null && fbm.functionblock.status.properties !== null) {
			for(property : fbm.functionblock.status.properties) {
				properties.add(property);	
			}
		}
		
		'''
			{
				"$schema": "http://json-schema.org/draft-07/schema#",
				"title": "Properties of <«definition»>",
				"type": "object",
				"properties": {
					«EntityTemplate.handleProperties(properties, invocationContext).toString.trim»
				},
				«EntityTemplate.calculateRequired(properties)»
			}
		'''
	}
	
	override getFileName(FunctionblockModel fbm) {
		fbm.namespace+"_"+fbm.name+"_"+fbm.version.replace(".","_")+".schema.json"
	}
	
	override getPath(FunctionblockModel context) {
		return null
	}
		
}
