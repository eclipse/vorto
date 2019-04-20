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
package org.eclipse.vorto.codegen.bosch.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.xtext.util.Strings
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.Entity

/**
 * Template that creates a Postman Script (collection) containing the requests 
 * to provision the Vorto modelled device in the Bosch IoT Suite
 */
class ProvisionDeviceScriptTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		return "Provisioning_"+context.name+".postman.json";
	}
	
	override getPath(InformationModel context) {
		return "scripts";
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		{
			"variables": [],
			"info": {
				"name": "«element.name» Provisioning",
				"_postman_id": "8147ad0e-c8f1-e51f-a036-73a13a4c567f",
				"description": "",
				"schema": "https://schema.getpostman.com/json/collection/v2.0.0/collection.json"
			},
			"item": [
				{
					"name": "Provision Request",
					"event": [
						{
							"listen": "prerequest",
							"script": {
								"type": "text/javascript",
								"exec": [
									"postman.setEnvironmentVariable(\"service-instance-id\", \"1234\");",
									"postman.setEnvironmentVariable(\"device-password\", \"secret\");",
									"postman.setEnvironmentVariable(\"device-id\", \"com.mycompany:4711\");"
								]
							}
						}
					],
					"request": {
						"url": "https://deviceprovisioning.eu-1.bosch-iot-suite.com/api/1/{{service-instance-id}}/devices",
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"description": ""
							},
							{
								"key": "Authorization",
								"value": "Bearer ADD_BEARER_TOKEN_HERE",
								"description": ""
							}
						],
						"body": {
							"mode": "raw",
							"raw": "«Strings.convertToJavaString(getJson(element).toString)»"
						},
						"description": "Provisions the «element.name» in the Bosch IoT Suite"
					},
					"response": []
				}
			]
		}
		'''
	}
	
	def getJson(InformationModel model) {
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
		  			 	"«statusProperty.name»" : «IF statusProperty.type instanceof PrimitivePropertyType»«getJsonPrimitive(statusProperty.type as PrimitivePropertyType)»«ELSE»«getJsonObjectType(statusProperty.type as ObjectPropertyType)»«ENDIF»
		  			 	«ENDFOR»
		  			 }«IF fbProperty.type.functionblock.configuration !== null && !fbProperty.type.functionblock.configuration.properties.isEmpty»,«ENDIF»
		  			«ENDIF»
		  			«IF fbProperty.type.functionblock.configuration !== null && !fbProperty.type.functionblock.configuration.properties.isEmpty»
		  			 "configuration": {
		  			 	«FOR configProperty : fbProperty.type.functionblock.configuration.properties SEPARATOR ","»
		  			 	"«configProperty.name»" : «IF configProperty.type instanceof PrimitivePropertyType»«getJsonPrimitive(configProperty.type as PrimitivePropertyType)»«ELSE»«getJsonObjectType(configProperty.type as ObjectPropertyType)»«ENDIF»
		  			 	«ENDFOR»
		  			 }
		  			«ENDIF»
		  		}
		  	}
		  	«ENDFOR» 
		  }
		}
		}
		}
		'''
	}
	
	def String getJsonObjectType(ObjectPropertyType propertyType) {
		if (propertyType.type instanceof org.eclipse.vorto.core.api.model.datatype.Enum) {
			var literals = (propertyType.type as org.eclipse.vorto.core.api.model.datatype.Enum).enums;
			if (literals.empty) {
				return "\"\""
			} else {
				return "\""+literals.get(0).name+"\"";
			}
		} else {
			return getEntityJson(propertyType.type as org.eclipse.vorto.core.api.model.datatype.Entity).toString();
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
			return "2019-04-01T18:25:43-00:00"
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