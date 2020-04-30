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
package org.eclipse.vorto.codegen.templates.java

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.templates.java.utils.ValueMapper
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.codegen.api.InvocationContext

/**
 * Use Plugin SDK API instead!
 */
@Deprecated
class JavaClassFieldTemplate implements ITemplate<Property> {
	override getContent(Property property,InvocationContext invocationContext) {
		'''
		/**
		* «property.description»
		*/
		«IF property.type instanceof PrimitivePropertyType»
			«addFieldAnnotations(property)»
			private «ValueMapper.mapSimpleDatatype((property.type as PrimitivePropertyType).type as PrimitiveType)» «ValueMapper.normalize(property.name)»;
		«ELSEIF property.type instanceof ObjectPropertyType»
			«var ObjectPropertyType object = property.type as ObjectPropertyType»
			«IF object.type instanceof Entity»
				«addFieldAnnotations(property)»
				private «namespaceOfDatatype»«(object.type as Entity).name.toFirstUpper» «ValueMapper.normalize(property.name)»;
			«ELSEIF object.type instanceof Enum»
				«addFieldAnnotations(property)»
				private «namespaceOfDatatype»«(object.type as Enum).name.toFirstUpper» «ValueMapper.normalize(property.name)»;
			«ENDIF»
		«ENDIF»
		'''
	}
	
	protected def addFieldAnnotations(Property property) {
		''''''
	}
	
	protected def getNamespaceOfDatatype() {
		''''''
	}
}