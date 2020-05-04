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
package org.eclipse.vorto.codegen.bosch.templates

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.IFileTemplate
import org.eclipse.xtext.util.Strings

/**
 * Template that creates a Postman Script (collection) containing the requests 
 * to provision the Vorto modelled device in the Bosch IoT Suite
 */
class ProvisionDeviceScriptTemplate implements IFileTemplate<InformationModel> {
	
	ProvisioningAPIRequestTemplate requestTemplate = null;
	
	new (ProvisioningAPIRequestTemplate template) {
		requestTemplate = template;
	}
	
	override getFileName(InformationModel context) {
		return "Provisioning_" + context.name + ".postman.json";
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
								"raw": "«Strings.convertToJavaString(requestTemplate.getContent(element,context).toString,true)»"
							},
							"description": "Provisions the «element.name» in the Bosch IoT Suite"
						},
						"response": []
					}
				]
			}
		'''
	}
}
