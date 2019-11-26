package org.eclipse.vorto.codegen.bosch.templates

import org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.IFileTemplate
import org.eclipse.vorto.codegen.ditto.schema.tasks.template.DittoThingStructureTemplate

class ProvisioningAPIRequestTemplate implements IFileTemplate<InformationModel> {

	override getFileName(InformationModel context) {
		return "provisioningRequest.json";
	}

	override getPath(InformationModel context) {
		return null;
	}
	
	override getContent(InformationModel model, InvocationContext context) {
		'''
			{
			  "id": "{{device-id}}",
			  "hub": {
			    "device": {
			      "enabled": true
			    },
			    "credentials": {
			      "type": "hashed-password",
			      "secrets": [
			        {
			          "password": "{{device-password}}"
			        }
			      ]
			    }
			  },
				«new DittoThingStructureTemplate().getContent(model, context)»
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
