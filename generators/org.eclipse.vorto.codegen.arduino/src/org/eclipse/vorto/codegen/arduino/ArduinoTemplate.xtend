/*******************************************************************************
 *  Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *******************************************************************************/
package org.eclipse.vorto.codegen.arduino

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.datatype.PropertyType
import org.eclipse.vorto.core.api.model.model.Model

abstract class ArduinoTemplate<T extends Model> implements IFileTemplate<T> {
	
	public String rootPath;
	
	def String type(PropertyType type) {
		if (type instanceof PrimitivePropertyType) {
			return toCppPrimitive((type as PrimitivePropertyType).getType);
		} else if (type instanceof DictionaryPropertyType){
			return "map<"+type((type as DictionaryPropertyType).keyType)+","+type((type as DictionaryPropertyType).valueType)+">";
		} else {
			return (type as ObjectPropertyType).getType().name
		}
	}
	
	def String toCppPrimitive(PrimitiveType primitiveType) {
		switch (primitiveType) {
			case (PrimitiveType.DOUBLE): {
				return "double"
			}
			case (PrimitiveType.FLOAT): {
				return "float"
			}
			case (PrimitiveType.INT): {
				return "uint32_t"
			}
			case (PrimitiveType.LONG): {
				return "uint64_t"
			}
			case (PrimitiveType.BOOLEAN): {
				return "bool"
			}
			case (PrimitiveType.BASE64_BINARY): {
				return "char[]"
			}
			case (PrimitiveType.STRING): {
				return "String"
			}
			default: {
				return "String"
			}
		}
	}
	
	def boolean isNumericType(PropertyType type) {
		if (type instanceof PrimitivePropertyType) {
			switch ((type as PrimitivePropertyType).getType) {
				case (PrimitiveType.DOUBLE): {
					return true
				}
				case (PrimitiveType.FLOAT): {
					return true
				}
				case (PrimitiveType.INT): {
					return true
				}
				case (PrimitiveType.LONG): {
					return true
				}
				case (PrimitiveType.BOOLEAN): {
					return true
				}
				case (PrimitiveType.BASE64_BINARY): {
					return false
				}
				case (PrimitiveType.STRING): {
					return false
				}
				default: {
					return false
				}
			}
		}
		else
		{
			return false
		}
	}
}
