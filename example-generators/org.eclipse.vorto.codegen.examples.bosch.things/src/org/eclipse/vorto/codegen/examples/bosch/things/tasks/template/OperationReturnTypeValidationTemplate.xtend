/*******************************************************************************
 * Copyright (c) 2015,2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.examples.bosch.things.tasks.template

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType

class OperationReturnTypeValidationTemplate implements ITemplate<Operation>{
		
	var EntityValidationTemplate entityValidationTemplate;
	var EnumValidationTemplate enumValidationTemplate;
	var PrimitiveTypeValidationTemplate primitiveTypeValidationTemplate;
	
	new(EntityValidationTemplate entityValidationTemplate,
		EnumValidationTemplate enumValidationTemplate,
		PrimitiveTypeValidationTemplate primitiveTypeValidationTemplate
	) {
		this.entityValidationTemplate = entityValidationTemplate;
		this.enumValidationTemplate = enumValidationTemplate;
		this.primitiveTypeValidationTemplate = primitiveTypeValidationTemplate;
	}
	
	override getContent(Operation operation,InvocationContext invocationContext) {
		'''
			{
				"$schema": "http://json-schema.org/draft-04/schema#",
				"title": "Return Type Validation",
				"description": "Operation Return Type Validation for «operation.name».",
				"type": "object",
				"properties": {
				«var returnType = operation.returnType»
					«IF returnType instanceof ReturnPrimitiveType»
						«var primitiveReturnType = returnType as ReturnPrimitiveType»
						«IF returnType.isMultiplicity»
							"result": {
								"type": "array",
								"items" : {
									«primitiveTypeValidationTemplate.getContent(primitiveReturnType.returnType,invocationContext)»
								}
							}
						«ELSE»
							"result": {
								«primitiveTypeValidationTemplate.getContent(primitiveReturnType.returnType,invocationContext)»
							}
						«ENDIF»
					«ELSEIF returnType instanceof ReturnObjectType»
					«var returnObjectType = returnType as ReturnObjectType»
					«IF returnObjectType.returnType instanceof Entity»
						«IF returnObjectType.isMultiplicity»
							"result": {
								"type": "array",
								"items": {
									"type": "object",
									"properties": {
										«entityValidationTemplate.getContent(returnObjectType.returnType as Entity,invocationContext)»
									}
								}
							}
						«ELSE»
							"result": {
								"type": "object",
								"properties": {
									«entityValidationTemplate.getContent(returnObjectType.returnType as Entity,invocationContext)»
								}
							}
						«ENDIF»
					«ELSEIF returnObjectType.returnType instanceof Enum»
						«IF returnObjectType.isMultiplicity»
							"result": {
								"type": "array",
								"items" : {
									«enumValidationTemplate.getContent(returnObjectType.returnType as Enum,invocationContext)»
								}
							}
						«ELSE»
							"result": {
								«enumValidationTemplate.getContent(returnObjectType.returnType as Enum,invocationContext)»
							}
						«ENDIF»
					«ENDIF»
				«ENDIF»
				}
			}
		'''
	}
}