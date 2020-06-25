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
import org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.ITemplate
import org.eclipse.vorto.core.api.model.datatype.PropertyType

class JavaClassFieldSetterTemplate implements ITemplate<Property> {
	
	var String setterPrefix;
	
	new(String setterPrefix) {
		this.setterPrefix = setterPrefix;
	}
	
	override getContent(Property property,InvocationContext invocationContext) {
		'''
			/**
			* Setter for «property.name».
			*/
			«IF property.type instanceof PrimitivePropertyType»
				public void «setterPrefix»«ValueMapper.normalize(property.name.toFirstUpper)»(«ValueMapper.mapSimpleDatatype((property.type as PrimitivePropertyType).type as PrimitiveType)» «ValueMapper.normalize(property.name)») {
					this.«ValueMapper.normalize(property.name)» = «ValueMapper.normalize(property.name)»;
				}
			«ELSEIF property.type instanceof ObjectPropertyType»
				«var ObjectPropertyType object = property.type as ObjectPropertyType»
				«IF object.type instanceof Entity» 
					public void «setterPrefix»«ValueMapper.normalize(property.name.toFirstUpper)»(«namespaceOfDatatype»«(object.type as Entity).name.toFirstUpper» «ValueMapper.normalize(property.name)») {
						this.«ValueMapper.normalize(property.name)» = «ValueMapper.normalize(property.name)»;
					}
				«ELSEIF object.type instanceof Enum»
					public void «setterPrefix»«ValueMapper.normalize(property.name.toFirstUpper)»(«namespaceOfDatatype»«(object.type as Enum).name.toFirstUpper» «ValueMapper.normalize(property.name)») {
						this.«ValueMapper.normalize(property.name)» = «ValueMapper.normalize(property.name)»;
					}
				«ENDIF»
			«ELSEIF property.type instanceof DictionaryPropertyType»
            	«var DictionaryPropertyType dictionary = property.type as DictionaryPropertyType»
            	«IF dictionary.keyType !== null && dictionary.valueType !== null»
            		public void «setterPrefix»«ValueMapper.normalize(property.name.toFirstUpper)»(java.util.Map<«getPropertyType(dictionary.keyType)»,«getPropertyType(dictionary.valueType)»> «ValueMapper.normalize(property.name)») {
            			this.«ValueMapper.normalize(property.name)» = «ValueMapper.normalize(property.name)»;
            		}
            	«ELSE»
            	    public void «setterPrefix»«ValueMapper.normalize(property.name.toFirstUpper)»(java.util.Map «ValueMapper.normalize(property.name)») {
            	        this.«ValueMapper.normalize(property.name)» = «ValueMapper.normalize(property.name)»;
            	    }
            	«ENDIF»
            «ENDIF»
		'''
	}
	
	protected def getNamespaceOfDatatype() {
		''''''
	}

	def String getPropertyType(PropertyType propertyType) {
        if (propertyType instanceof PrimitivePropertyType) {
            return ValueMapper.mapSimpleDatatype((propertyType as PrimitivePropertyType).type as PrimitiveType);
        } else if (propertyType instanceof ObjectPropertyType) {
            return getNamespaceOfDatatype() + (propertyType as ObjectPropertyType).type.name.toFirstUpper
        } else {
            return "java.util.Map"
        }
    }
}
