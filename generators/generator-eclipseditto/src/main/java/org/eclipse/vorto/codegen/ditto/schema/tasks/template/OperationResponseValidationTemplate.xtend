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

import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.ITemplate

class OperationResponseValidationTemplate implements ITemplate<Operation> {

	val static EntityValidationTemplate entityValidationTemplate = new EntityValidationTemplate();
	val static EnumValidationTemplate enumValidationTemplate = new EnumValidationTemplate();
	val static PrimitiveTypeValidationTemplate primitiveTypeValidationTemplate = new PrimitiveTypeValidationTemplate();

	new() {
	}

	override getContent(Operation operation, InvocationContext invocationContext) {
		var fbm = operation.eContainer.eContainer as FunctionblockModel;
		var definition = fbm.namespace + ":" + fbm.name + ":" + fbm.version;
		'''
			{
				"$schema": "http://json-schema.org/draft-04/schema#",
				"title": "Operation response payload validation of definition <«definition»> for message subject (operation name) <«operation.name»>",
				"description": "«operation.description»"«IF operation.returnType !== null»,«ENDIF»
				«IF operation.returnType !== null»
					«val returnType = operation.returnType»
					«IF returnType instanceof ReturnPrimitiveType»
						«val primitiveReturnType = returnType as ReturnPrimitiveType»
						«IF returnType.isMultiplicity»
							"type": "array",
							"items" : {
								«primitiveTypeValidationTemplate.getContent(primitiveReturnType.returnType, invocationContext)»
							}
						«ELSE»
							«primitiveTypeValidationTemplate.getContent(primitiveReturnType.returnType, invocationContext)»
						«ENDIF»
					«ELSEIF returnType instanceof ReturnObjectType»
						«val returnObjectType = returnType as ReturnObjectType»
						«IF returnObjectType.returnType instanceof Entity»
							«val entity = returnObjectType.returnType as Entity»
							«IF returnObjectType.isMultiplicity»
								"type": "array",
								"items": {
									"type": "object",
									"properties": {
										«entityValidationTemplate.getContent(entity, invocationContext)»
									},
									«EntityValidationTemplate.calculateRequired(entity.properties)»
								}
							«ELSE»
								"type": "object",
								"properties": {
									«entityValidationTemplate.getContent(entity, invocationContext)»
								},
								«EntityValidationTemplate.calculateRequired(entity.properties)»
							«ENDIF»
						«ELSEIF returnObjectType.returnType instanceof Enum»
							«val enum = returnObjectType.returnType as Enum»
							«IF returnObjectType.isMultiplicity»
								"type": "array",
								"items" : {
									«enumValidationTemplate.getContent(enum, invocationContext)»
								}
							«ELSE»
							«enumValidationTemplate.getContent(enum, invocationContext)»
							«ENDIF»
						«ENDIF»
						«ELSE»
						"type": "object"
					«ENDIF»
				«ENDIF»
			}
		'''
	}
}
