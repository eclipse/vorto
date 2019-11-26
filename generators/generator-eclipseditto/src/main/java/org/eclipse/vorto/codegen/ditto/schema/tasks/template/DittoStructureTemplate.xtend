/*******************************************************************************
	* Copyright (c) 2019 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.ditto.schema.tasks.template

import org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.IFileTemplate

class DittoThingStructureTemplate implements IFileTemplate<InformationModel> {

	override getFileName(InformationModel context) {
		return "dittoStructure.json";
	}

	override getPath(InformationModel context) {
		return null;
	}
	
	override getContent(InformationModel model, InvocationContext context) {
		'''
 "things": {
  "thing": {
  "attributes": {
 	"thingName": "«model.displayname»",
 	"definition": "«model.namespace»:«model.name»:«model.version»"
 	 },
 	"features": {
 	«FOR fbProperty : model.properties SEPARATOR ","»
 	"«fbProperty.name»" : {
  	"definition": [
 		"«fbProperty.type.namespace»:«fbProperty.type.name»:«fbProperty.type.version»"
 	],
 	"properties": {
 		«IF fbProperty.type.functionblock.status !== null && !fbProperty.type.functionblock.status.properties.isEmpty»
 		"status": {
 			«FOR statusProperty : fbProperty.type.functionblock.status.properties SEPARATOR ","»
 			"«statusProperty.name»" : «IF statusProperty.type instanceof PrimitivePropertyType»«getJsonPrimitive(statusProperty.type as PrimitivePropertyType)»«ELSEIF statusProperty.type instanceof ObjectPropertyType»«getJsonObjectType(statusProperty.type as ObjectPropertyType)»«ELSE»«getJsonDictionaryType(statusProperty.type as DictionaryPropertyType)»«ENDIF»
 			«ENDFOR»
 		}«IF fbProperty.type.functionblock.configuration !== null && !fbProperty.type.functionblock.configuration.properties.isEmpty»,«ENDIF»
 		«ENDIF»
 		«IF fbProperty.type.functionblock.configuration !== null && !fbProperty.type.functionblock.configuration.properties.isEmpty»
 			"configuration": {
 			«FOR configProperty : fbProperty.type.functionblock.configuration.properties SEPARATOR ","»
 			"«configProperty.name»" : «IF configProperty.type instanceof PrimitivePropertyType»«getJsonPrimitive(configProperty.type as PrimitivePropertyType)»«ELSEIF configProperty.type instanceof ObjectPropertyType»«getJsonObjectType(configProperty.type as ObjectPropertyType)»«ELSE»«getJsonDictionaryType(configProperty.type as DictionaryPropertyType)»«ENDIF»
 			«ENDFOR»
 	}
 		«ENDIF»
 	}
 	}
 	«ENDFOR»
}
}
}
		'''
	}
	
	def getJsonDictionaryType(DictionaryPropertyType propertyType) {
		'''
			{
				"key" : "value"
			}
		'''
	}

	def String getJsonObjectType(ObjectPropertyType propertyType) {
		if (propertyType.type instanceof org.eclipse.vorto.core.api.model.datatype.Enum) {
			var literals = (propertyType.type as org.eclipse.vorto.core.api.model.datatype.Enum).enums;
			if (literals.empty) {
				return "\"\""
			} else {
				return "\"" + literals.get(0).name + "\"";
			}
		} else {
			return getEntityJson(propertyType.type as Entity).toString();
		}
	}

	def getEntityJson(Entity entity) {
		'''
			{
				«FOR property : entity.properties SEPARATOR ","»
					"«property.name»" : «IF property.type instanceof PrimitivePropertyType»«getJsonPrimitive(property.type as PrimitivePropertyType)»«ELSE»«getJsonObjectType(property.type as ObjectPropertyType)»«ENDIF»
				«ENDFOR»
			}
		'''
	}

	def getJsonPrimitive(PrimitivePropertyType propertyType) {
		if (propertyType.type === PrimitiveType.BASE64_BINARY) {
			return "\"\""
		} else if (propertyType.type === PrimitiveType.BOOLEAN) {
			return false
		} else if (propertyType.type === PrimitiveType.BYTE) {
			return "\"\""
		} else if (propertyType.type === PrimitiveType.DATETIME) {
			return "\"2019-04-01T18:25:43-00:00\""
		} else if (propertyType.type === PrimitiveType.DOUBLE) {
			return 0.0
		} else if (propertyType.type === PrimitiveType.FLOAT) {
			return 0.0
		} else if (propertyType.type === PrimitiveType.INT) {
			return 0
		} else if (propertyType.type === PrimitiveType.LONG) {
			return 0
		} else if (propertyType.type === PrimitiveType.SHORT) {
			return 0
		} else {
			return "\"\""
		}
	}
	
}
