/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.examples.jsonschema.tasks.template

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.functionblock.Param
import org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam
import org.eclipse.vorto.core.api.model.functionblock.RefParam

class OperationSingleParameterValidation implements ITemplate<Param>{
		
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
	
	override getContent(Param param,InvocationContext invocationContext) {
		'''
			«IF param instanceof PrimitiveParam»
				«var primitiveParam = param as PrimitiveParam»
				«IF param.isMultiplicity»
					"«param.name»": {
						"type": "array",
						"items" : {
							«primitiveTypeValidationTemplate.getContent(primitiveParam.type,invocationContext)»
						}
					}
				«ELSE»
					"«param.name»": {
						«primitiveTypeValidationTemplate.getContent(primitiveParam.type,invocationContext)»
					}
				«ENDIF»
			«ELSEIF param instanceof RefParam»
				«var refParam = param as RefParam»
				«IF refParam.type instanceof Entity»
					«IF refParam.isMultiplicity»
						"«param.name»": {
							"type": "array",
							"items" : {
								"type": "object",
								"properties": {
									«entityValidationTemplate.getContent(refParam.type as Entity,invocationContext)»
								}
							}
						}
					«ELSE»
						"«param.name»": {
							"type": "object",
							"properties": {
								«entityValidationTemplate.getContent(refParam.type as Entity,invocationContext)»
							}
						}
					«ENDIF»
				«ELSEIF refParam.type instanceof Enum»
					«IF refParam.isMultiplicity»
						"«param.name»": {
							"type": "array",
							"items" : {
								«enumValidationTemplate.getContent(refParam.type as Enum,invocationContext)»
							}
						}
					«ELSE»
						"«param.name»": {
							«enumValidationTemplate.getContent(refParam.type as Enum,invocationContext)»
						}
					«ENDIF»
				«ENDIF»
			«ENDIF»
		'''
	}
}