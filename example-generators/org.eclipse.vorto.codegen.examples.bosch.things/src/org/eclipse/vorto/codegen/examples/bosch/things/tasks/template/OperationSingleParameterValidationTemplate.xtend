/*******************************************************************************
 * Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
import org.eclipse.vorto.core.api.model.datatype.ConstraintRule
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.functionblock.Param
import org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam
import org.eclipse.vorto.core.api.model.functionblock.RefParam

class OperationSingleParameterValidationTemplate implements ITemplate<Param>{
		
	var EntityValidationTemplate entityValidationTemplate;
	var EnumValidationTemplate enumValidationTemplate;
	var PrimitiveTypeValidationTemplate primitiveTypeValidationTemplate;
	var ConstraintTemplate constraintTemplate
	
	new(EntityValidationTemplate entityValidationTemplate,
		EnumValidationTemplate enumValidationTemplate,
		PrimitiveTypeValidationTemplate primitiveTypeValidationTemplate,
		ConstraintTemplate constraintTemplate
		
	) {
		this.entityValidationTemplate = entityValidationTemplate;
		this.enumValidationTemplate = enumValidationTemplate;
		this.constraintTemplate = constraintTemplate
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
							"description" : "«param.description»",
							«primitiveTypeValidationTemplate.getContent(primitiveParam.type,invocationContext)»
							«getConstraintsContent(param.constraintRule, invocationContext)»
						}
					}
				«ELSE»
					"«param.name»": {
						"description" : "«param.description»",
						«primitiveTypeValidationTemplate.getContent(primitiveParam.type,invocationContext)»
						«getConstraintsContent(param.constraintRule, invocationContext)»
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
	
	private def getConstraintsContent(ConstraintRule constraintRule,InvocationContext invocationContext){
		return 
		'''
		«IF constraintRule != null»
			«FOR constraint : constraintRule.constraints BEFORE ',\n' SEPARATOR ', '»
				«constraintTemplate.getContent(constraint, invocationContext)»
			«ENDFOR»
		«ENDIF»
		'''

	}
	
	
}