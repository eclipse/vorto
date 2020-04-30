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
package org.eclipse.vorto.plugin.generator.utils.javatemplates

import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.ITemplate

class JavaClassFieldGetterTemplate implements ITemplate<Property> {
	
	var String getterPrefix;
	
	new(String getterPrefix) {
		this.getterPrefix = getterPrefix;
	}
	
	override getContent(Property property,InvocationContext invocationContext) {
		'''
			/**
			* Getter for «property.name».
			*/
			«IF property.type instanceof PrimitivePropertyType»
				public «ValueMapper.mapSimpleDatatype((property.type as PrimitivePropertyType).type as PrimitiveType)» «getterPrefix»«ValueMapper.normalize(property.name.toFirstUpper)»() {
					return this.«ValueMapper.normalize(property.name)»;
				}
			«ELSEIF property.type instanceof ObjectPropertyType»
				«var ObjectPropertyType object = property.type as ObjectPropertyType»
				«IF object.type instanceof Entity» 
					public «namespaceOfDatatype»«(object.type as Entity).name.toFirstUpper» «getterPrefix»«ValueMapper.normalize(property.name.toFirstUpper)»() {
						return this.«ValueMapper.normalize(property.name)»;
					}
				«ELSEIF object.type instanceof Enum»
					public «namespaceOfDatatype»«(object.type as Enum).name.toFirstUpper» «getterPrefix»«ValueMapper.normalize(property.name.toFirstUpper)»() {
						return this.«ValueMapper.normalize(property.name)»;
					}
				«ENDIF»
			«ENDIF»
		'''
	}
	
	protected def getNamespaceOfDatatype() {
		''''''
	}
}
