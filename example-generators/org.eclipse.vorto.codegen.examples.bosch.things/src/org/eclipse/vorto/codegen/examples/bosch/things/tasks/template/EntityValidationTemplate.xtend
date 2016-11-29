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
package org.eclipse.vorto.codegen.examples.bosch.things.tasks.template

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.ConstraintRule

class EntityValidationTemplate implements ITemplate<Entity>{
	var EnumValidationTemplate enumValidationTemplate;
	var PrimitiveTypeValidationTemplate primitiveTypeValidationTemplate;
	var ConstraintTemplate constraintTemplate
	
	new(EnumValidationTemplate enumValidationTemplate,
		PrimitiveTypeValidationTemplate primitiveTypeValidationTemplate,
		ConstraintTemplate constraintTemplate
	) {
		this.enumValidationTemplate = enumValidationTemplate;
		this.primitiveTypeValidationTemplate = primitiveTypeValidationTemplate;
		this.constraintTemplate = constraintTemplate
	}
	
	override getContent(Entity entity,InvocationContext invocationContext) {
		'''
			«FOR property : entity.properties SEPARATOR ','»
				«var propertyType = property.type»
				«IF propertyType instanceof PrimitivePropertyType»
					«var primitiveType = propertyType as PrimitivePropertyType»
					«IF property.isMultiplicity»
						"«property.name»": {
							"type": "array",
							"items": {
								"description" : "«property.description»",
								«primitiveTypeValidationTemplate.getContent(primitiveType.type,invocationContext)»
								«getConstraintsContent(property.constraintRule, invocationContext)»
							}
						}
					«ELSE» 
						"«property.name»": {
							"description" : "«property.description»",
							«primitiveTypeValidationTemplate.getContent(primitiveType.type,invocationContext)»
						}
					«ENDIF»
				«ELSEIF propertyType instanceof ObjectPropertyType»
					«var objectType = propertyType as ObjectPropertyType»
					«IF objectType.type instanceof Entity»
						«IF property.isMultiplicity»
							"«property.name»": {
								"type": "array",
								"items": {
									"type": "object",
									"properties": {
										"description" : "«property.description»",
										«getContent(objectType.type as Entity,invocationContext)»
										«getConstraintsContent(property.constraintRule, invocationContext)»
									}
								}
							}
						«ELSE»
							"«property.name»": {
								"type": "object",
								"properties": {
									"description" : "«property.description»",
									«getContent(objectType.type as Entity,invocationContext)»
									«getConstraintsContent(property.constraintRule, invocationContext)»
								}
							}
						«ENDIF»
					«ELSEIF objectType.type instanceof Enum»
						«IF property.isMultiplicity»
							"«property.name»": {
								"type": "array",
								"items": {
									"description" : "«property.description»",
									«enumValidationTemplate.getContent(objectType.type as Enum,invocationContext)»
									«getConstraintsContent(property.constraintRule, invocationContext)»
								}
							}
						«ELSE»
							"«property.name»": {
								"description" : "«property.description»",
								«enumValidationTemplate.getContent(objectType.type as Enum,invocationContext)»
								«getConstraintsContent(property.constraintRule, invocationContext)»
							}
						«ENDIF»
					«ENDIF»
				«ENDIF»
			«ENDFOR»
		'''
	}
	
	private def getConstraintsContent(ConstraintRule constraintRule,InvocationContext invocationContext){
		return 
		'''
		«IF constraintRule != null»
			,«FOR constraint : constraintRule.constraints SEPARATOR ', '»
				«constraintTemplate.getContent(constraint, invocationContext)»
			«ENDFOR»
		«ENDIF»
		'''

	}
}