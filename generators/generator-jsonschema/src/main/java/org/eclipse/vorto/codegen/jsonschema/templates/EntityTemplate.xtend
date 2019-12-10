/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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

import java.util.List
import org.eclipse.vorto.codegen.jsonschema.Utils
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.ITemplate

class EntityTemplate implements ITemplate<Entity>{
	val static EnumTemplate enumValidationTemplate = new EnumTemplate();
	val static PrimitiveTypeTemplate primitiveTypeValidationTemplate = new PrimitiveTypeTemplate();
	
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
			«ELSE» //dictionary type
			"«property.name»": {
				«IF !property.description.nullOrEmpty»"description": "«property.description»",«ENDIF»
					"type": "object"
			}
		«ENDIF»
	'''
	
}
