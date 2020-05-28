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
package org.eclipse.vorto.codegen.hono.arduino

import org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.datatype.PropertyType
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock
import org.eclipse.vorto.core.api.model.model.Model
import org.eclipse.vorto.plugin.generator.utils.IFileTemplate
import org.eclipse.vorto.plugin.utils.Utils

abstract class ArduinoTemplate<T extends Model> implements IFileTemplate<T> {
	
	public String rootPath;
	
	def String type(PropertyType type) {
		if (type instanceof PrimitivePropertyType) {
			return toCppPrimitive((type as PrimitivePropertyType).getType);
		} else if (type instanceof DictionaryPropertyType){
			return "map()"
		} else {
			return (type as ObjectPropertyType).getType().namespace.replace(".","_") + "::" + (type as ObjectPropertyType).getType().name
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
	
	def boolean isAlphabetical (PropertyType type) {
	    if (type instanceof PrimitivePropertyType) {
            switch ((type as PrimitivePropertyType).getType) {
                case (PrimitiveType.STRING): {
                    return true
                }
                case (PrimitiveType.BASE64_BINARY): {
                    return true
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
	
	def boolean isEnum (FunctionBlock fb, PropertyType type) {
        if((type instanceof ObjectPropertyType) && 
            !Utils.getReferencedEnums(fb).empty) {
            for (enum : Utils.getReferencedEnums(fb)) {
                if (enum.displayname.equals((type as ObjectPropertyType).getType().name) && 
                    enum.namespace.equals((type as ObjectPropertyType).getType().namespace)) {
                    return true
                } else {
                    return false
                }
            }
        } else {
            return false
        }
    }
    
	def String getFirstValueEnum (FunctionBlock fb, PropertyType type) {
	    for (enum : Utils.getReferencedEnums(fb)) {
	        if (enum.displayname.equals((type as ObjectPropertyType).getType().name) && 
	            enum.namespace.equals((type as ObjectPropertyType).getType().namespace)
	        ) {
	            return enum.enums.get(0).name
	        }
	    }
	    return ""
	}
	
    def boolean isEntity (FunctionBlock fb, PropertyType type) {
        if((type instanceof ObjectPropertyType) &&
            !Utils.getReferencedEntities(fb).empty) {
            for (entity : Utils.getReferencedEntities(fb)) {
                if (entity.displayname.equals((type as ObjectPropertyType).getType().name) && 
                    entity.namespace.equals((type as ObjectPropertyType).getType().namespace)) {
                    return true
                } else {
                    return false
                }
            }
        } else {
            return false
        }
    }
    
    def getEntity (FunctionBlock fb, PropertyType type) {
        for (entity : Utils.getReferencedEntities(fb)) {
            if (entity.displayname.equals((type as ObjectPropertyType).getType().name) && 
                entity.namespace.equals((type as ObjectPropertyType).getType().namespace)) {
                return entity.properties
            }
        }
        return null;
    }
}
