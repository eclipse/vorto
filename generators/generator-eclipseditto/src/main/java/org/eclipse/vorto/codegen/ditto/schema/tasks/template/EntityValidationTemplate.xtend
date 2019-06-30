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

import java.util.List
import org.eclipse.vorto.codegen.ditto.schema.Utils
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.ITemplate

class EntityValidationTemplate implements ITemplate<Entity>{
	val static EnumValidationTemplate enumValidationTemplate = new EnumValidationTemplate();
	val static PrimitiveTypeValidationTemplate primitiveTypeValidationTemplate = new PrimitiveTypeValidationTemplate();
	
	new() {
	}

	override getContent(Entity entity, InvocationContext invocationContext) {
		'''
		«handleProperties(entity.properties, invocationContext).toString.trim»
		'''
	}

	static def CharSequence calculateRequired(List<Property> properties)
		'''
		"required": [«FOR property : properties.filter[presence !== null && presence.mandatory] SEPARATOR ','»"«property.name»"«ENDFOR»]
		'''
	
	static def CharSequence handleProperties(List<Property> properties, InvocationContext invocationContext)
		'''
		«FOR property : properties SEPARATOR ','»
			«handleProperty(property, invocationContext)»
		«ENDFOR»
		'''
	
	static def CharSequence handleProperty(Property property, InvocationContext invocationContext)
		'''
		«var propertyType = property.type»
		«IF propertyType instanceof PrimitivePropertyType»
			«var primitiveType = propertyType as PrimitivePropertyType»
			«IF property.isMultiplicity»
				"«property.name»": {
					«IF !property.description.nullOrEmpty»"description": "«property.description»",«ENDIF»
					"type": "array",
					"items": {
						«primitiveTypeValidationTemplate.getContent(primitiveType.type, invocationContext).toString.trim»«Utils.getConstraintsContent(property.constraintRule, invocationContext)»
					}
				}
			«ELSE»
				"«property.name»": {
					«IF !property.description.nullOrEmpty»"description": "«property.description»",«ENDIF»
					«primitiveTypeValidationTemplate.getContent(primitiveType.type, invocationContext).toString.trim»«Utils.getConstraintsContent(property.constraintRule, invocationContext)»
				}
			«ENDIF»
		«ELSEIF propertyType instanceof ObjectPropertyType»
			«val objectType = propertyType as ObjectPropertyType»
			«IF objectType.type instanceof Entity»
				«val theEntity = objectType.type as Entity»
				«IF property.isMultiplicity»
					"«property.name»": {
						«IF !property.description.nullOrEmpty»"description": "«property.description»",«ENDIF»
						"type": "array",
						"items": {
							"type": "object",
							"properties": {
								«handleProperties(theEntity.properties, invocationContext)»
							},
							"required": [«FOR prop : theEntity.properties.filter[presence !== null && presence.mandatory] SEPARATOR ','»"«prop.name»"«ENDFOR»]
						}
					}
				«ELSE»
					"«property.name»": {
						«IF !property.description.nullOrEmpty»"description": "«property.description»",«ENDIF»
						"type": "object",
						"properties": {
							«handleProperties(theEntity.properties, invocationContext)»
						},
						"required": [«FOR prop : theEntity.properties.filter[presence !== null && presence.mandatory] SEPARATOR ','»"«prop.name»"«ENDFOR»]
					}
				«ENDIF»
			«ELSEIF objectType.type instanceof Enum»
				«val enum = objectType.type as Enum»
				«IF property.isMultiplicity» 
					"«property.name»": {
						«IF !property.description.nullOrEmpty»"description": "«property.description»",«ENDIF»
						"type": "array",
						"items": {
							«enumValidationTemplate.getContent(enum, invocationContext)»
						}
					}
				«ELSE»
					"«property.name»": {
						«IF !property.description.nullOrEmpty»"description": "«property.description»",«ENDIF»
						«enumValidationTemplate.getContent(enum, invocationContext)»
					}
				«ENDIF»
			«ENDIF»
			«ELSE»
			"«property.name»": {
			«IF !property.description.nullOrEmpty»"description": "«property.description»",«ENDIF»
			"type": "object"
			}
		«ENDIF»
		'''
	
}
