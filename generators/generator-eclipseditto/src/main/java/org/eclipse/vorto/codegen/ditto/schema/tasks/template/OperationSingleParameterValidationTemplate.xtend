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

import org.eclipse.vorto.codegen.ditto.schema.Utils
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.functionblock.Param
import org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam
import org.eclipse.vorto.core.api.model.functionblock.RefParam
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.ITemplate

class OperationSingleParameterValidationTemplate implements ITemplate<Param>{
		
	val static EntityValidationTemplate entityValidationTemplate = new EntityValidationTemplate();
	val static EnumValidationTemplate enumValidationTemplate = new EnumValidationTemplate();
	val static PrimitiveTypeValidationTemplate primitiveTypeValidationTemplate = new PrimitiveTypeValidationTemplate();
	
	new() {
	}
	
	override getContent(Param param, InvocationContext invocationContext) {
		'''
		«handleParam(param, invocationContext, true)»
		'''
	}
	
	static def CharSequence handleParam(Param param, InvocationContext invocationContext, boolean includeDescriptions)
		'''
		«IF param instanceof PrimitiveParam»
			«val primitiveParam = param as PrimitiveParam»
			«IF param.isMultiplicity»
				"«param.name»": {
					«IF includeDescriptions && !param.description.nullOrEmpty»"description": "«param.description»",«ENDIF»
					"type": "array",
					"items" : {
						«primitiveTypeValidationTemplate.getContent(primitiveParam.type, invocationContext)»«Utils.getConstraintsContent(param.constraintRule, invocationContext)»
					}
				}
			«ELSE»
				"«param.name»": {
					«IF includeDescriptions && !param.description.nullOrEmpty»"description": "«param.description»",«ENDIF»
					«primitiveTypeValidationTemplate.getContent(primitiveParam.type, invocationContext)»«Utils.getConstraintsContent(param.constraintRule, invocationContext)»
				}
			«ENDIF»
		«ELSEIF param instanceof RefParam»
			«var refParam = param as RefParam»
			«IF refParam.type instanceof Entity»
				«val entity = refParam.type as Entity»
				«IF refParam.isMultiplicity»
					"«param.name»": {
						«IF includeDescriptions && !param.description.nullOrEmpty»"description": "«param.description»",«ENDIF»
						"type": "array",
						"items" : {
							"type": "object",
							"properties": {
								«entityValidationTemplate.getContent(entity, invocationContext)»
							},
							"required": [«FOR property : entity.properties.filter[presence !== null && presence.mandatory] SEPARATOR ','»"«property.name»"«ENDFOR»]
						}
					}
				«ELSE»
					"«param.name»": {
						«IF includeDescriptions && !param.description.nullOrEmpty»"description": "«param.description»",«ENDIF»
						"type": "object",
						"properties": {
							«entityValidationTemplate.getContent(entity, invocationContext)»
						},
						"required": [«FOR property : entity.properties.filter[presence !== null && presence.mandatory] SEPARATOR ','»"«property.name»"«ENDFOR»]
					}
				«ENDIF»
			«ELSEIF refParam.type instanceof Enum»
				«val enum = refParam.type as Enum»
				«IF refParam.isMultiplicity»
					"«param.name»": {
						«IF includeDescriptions && !param.description.nullOrEmpty»"description": "«param.description»",«ENDIF»
						"type": "array",
						"items" : {
							«enumValidationTemplate.getContent(enum, invocationContext)»
						}
					}
				«ELSE»
					"«param.name»": {
						«enumValidationTemplate.getContent(enum, invocationContext)»
					}
				«ENDIF»
			«ENDIF»
			«ELSE»
			"«param.name»": {
				«IF !param.description.nullOrEmpty»"description": "«param.description»",«ENDIF»
				"type": "object"
			}
		«ENDIF»
		'''
	
}
