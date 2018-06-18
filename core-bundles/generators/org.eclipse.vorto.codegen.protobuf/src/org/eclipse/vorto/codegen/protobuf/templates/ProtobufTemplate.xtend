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
package org.eclipse.vorto.codegen.protobuf.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.datatype.PropertyType
import org.eclipse.vorto.core.api.model.model.Model

abstract class ProtobufTemplate<T extends Model> implements IFileTemplate<T> {
	
	override getFileName(T element) {
		return element.name.toLowerCase + ".proto"
	}

	override getPath(T element) {
		return element.namespace.replace(".","/");
	}
	
	def String type(PropertyType type) {
		if (type instanceof PrimitivePropertyType) {
			return toProtoPrimitive((type as PrimitivePropertyType).getType);
		} else if (type instanceof DictionaryPropertyType){
			return "map<"+type((type as DictionaryPropertyType).keyType)+","+type((type as DictionaryPropertyType).valueType)+">";
		} else {
			return (type as ObjectPropertyType).getType().name
		}
	}
	
	def String toProtoPrimitive(PrimitiveType primitiveType) {
		switch (primitiveType) {
			case (PrimitiveType.DOUBLE): {
				return "double"
			}
			case (PrimitiveType.FLOAT): {
				return "float"
			}
			case (PrimitiveType.INT): {
				return "int32"
			}
			case (PrimitiveType.LONG): {
				return "int64"
			}
			case (PrimitiveType.BOOLEAN): {
				return "bool"
			}
			case (PrimitiveType.BASE64_BINARY): {
				return "bytes"
			}
			case (PrimitiveType.STRING): {
				return "string"
			}
			default: {
				return "string"
			}
		}
	}
}