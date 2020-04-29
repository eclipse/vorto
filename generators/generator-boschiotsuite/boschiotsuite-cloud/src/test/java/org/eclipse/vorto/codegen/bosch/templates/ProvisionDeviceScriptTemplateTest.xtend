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

import org.eclipse.vorto.core.api.model.BuilderUtils
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.model.ModelId
import org.eclipse.vorto.core.api.model.model.ModelType
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.TestUtils
import org.junit.Assert
import org.junit.Test

class ProvisionDeviceScriptTemplateTest {

	@Test
	def void testCreateScriptWithSingleFb() {
		var template = new ProvisionDeviceScriptTemplate(new ProvisioningAPIRequestTemplate);

		var fbm = BuilderUtils.newFunctionblock(
			new ModelId(ModelType.Functionblock, "Temperature", "org.eclipse.vorto", "1.0.0")).
			withStatusProperty("value", PrimitiveType.FLOAT).withStatusProperty("unit", PrimitiveType.STRING).build();

		var im = BuilderUtils.newInformationModel(
			new ModelId(ModelType.InformationModel, "RPi", "org.eclipse.vorto", "1.0.0"))
		im.withFunctionBlock(fbm, "cpuTemperature", null, false);

		var generated = template.getContent(im.build, InvocationContext.simpleInvocationContext());
		Assert.assertEquals(TestUtils.normalizeEOL(expectedTemplate1), TestUtils.normalizeEOL(generated));
	}

	def String getExpectedTemplate1() {
		'''
			{"variables":[],"info":{"name":"RPiProvisioning","_postman_id":"8147ad0e-c8f1-e51f-a036-73a13a4c567f","description":"","schema":"https://schema.getpostman.com/json/collection/v2.0.0/collection.json"},"item":[{"name":"ProvisionRequest","event":[{"listen":"prerequest","script":{"type":"text/javascript","exec":["postman.setEnvironmentVariable(\"service-instance-id\",\"1234\");","postman.setEnvironmentVariable(\"device-password\",\"secret\");","postman.setEnvironmentVariable(\"device-id\",\"com.mycompany:4711\");"]}}],"request":{"url":"https://deviceprovisioning.eu-1.bosch-iot-suite.com/api/1/{{service-instance-id}}/devices","method":"POST","header":[{"key":"Content-Type","value":"application/json","description":""},{"key":"Authorization","value":"BearerADD_BEARER_TOKEN_HERE","description":""}],"body":{"mode":"raw","raw":"{\n\"id\":\"{{device-id}}\",\n\"hub\":{\n\"device\":{\n\"enabled\":true\n},\n\"credentials\":{\n\"type\":\"hashed-password\",\n\"secrets\":[\n{\n\"password\":\"{{device-password}}\"\n}\n]\n}\n},\n\"things\":{\n\"thing\":{\n\"definition\":\"org.eclipse.vorto:RPi:1.0.0\",\n\"attributes\":{\n\t\"modelDisplayName\":\"RPi\" \n\t},\n\t\"features\":{\n\t\"cpuTemperature\":{\n\t\"definition\":[\n\t\t\"org.eclipse.vorto:Temperature:1.0.0\"\n\t],\n\t\"properties\":{\n\t\t\"status\":{\n\t\t\t\"value\":0.0,\n\t\t\t\"unit\":\"\"\n\t\t}\n\t}\n\t}\n}\n}\n\t}\n}\n"},"description":"ProvisionstheRPiintheBoschIoTSuite"},"response":[]}]}
		'''
	}

	@Test
	def void testCreateScriptWithMultipleFbs() {
		var template = new ProvisionDeviceScriptTemplate(new ProvisioningAPIRequestTemplate);

		var fbm = BuilderUtils.newFunctionblock(
			new ModelId(ModelType.Functionblock, "Temperature", "org.eclipse.vorto", "1.0.0")).
			withStatusProperty("value", PrimitiveType.FLOAT).withStatusProperty("unit", PrimitiveType.STRING).build();

		var im = BuilderUtils.newInformationModel(
			new ModelId(ModelType.InformationModel, "RPi", "org.eclipse.vorto", "1.0.0"))
		im.withFunctionBlock(fbm, "cpuTemperature", null, false);
		im.withFunctionBlock(fbm, "outdoorTemperature", null, false)

		var generated = template.getContent(im.build, InvocationContext.simpleInvocationContext());
		Assert.assertEquals(TestUtils.normalizeEOL(expectedTemplate2), TestUtils.normalizeEOL(generated));
	}


	def String getExpectedTemplate2() {
		'''
			{"variables":[],"info":{"name":"RPiProvisioning","_postman_id":"8147ad0e-c8f1-e51f-a036-73a13a4c567f","description":"","schema":"https://schema.getpostman.com/json/collection/v2.0.0/collection.json"},"item":[{"name":"ProvisionRequest","event":[{"listen":"prerequest","script":{"type":"text/javascript","exec":["postman.setEnvironmentVariable(\"service-instance-id\",\"1234\");","postman.setEnvironmentVariable(\"device-password\",\"secret\");","postman.setEnvironmentVariable(\"device-id\",\"com.mycompany:4711\");"]}}],"request":{"url":"https://deviceprovisioning.eu-1.bosch-iot-suite.com/api/1/{{service-instance-id}}/devices","method":"POST","header":[{"key":"Content-Type","value":"application/json","description":""},{"key":"Authorization","value":"BearerADD_BEARER_TOKEN_HERE","description":""}],"body":{"mode":"raw","raw":"{\n\"id\":\"{{device-id}}\",\n\"hub\":{\n\"device\":{\n\"enabled\":true\n},\n\"credentials\":{\n\"type\":\"hashed-password\",\n\"secrets\":[\n{\n\"password\":\"{{device-password}}\"\n}\n]\n}\n},\n\"things\":{\n\"thing\":{\n\"definition\":\"org.eclipse.vorto:RPi:1.0.0\",\n\"attributes\":{\n\t\"modelDisplayName\":\"RPi\"\n\t},\n\t\"features\":{\n\t\"cpuTemperature\":{\n\t\"definition\":[\n\t\t\"org.eclipse.vorto:Temperature:1.0.0\"\n\t],\n\t\"properties\":{\n\t\t\"status\":{\n\t\t\t\"value\":0.0,\n\t\t\t\"unit\":\"\"\n\t\t}\n\t}\n\t},\n\t\"outdoorTemperature\":{\n\t\"definition\":[\n\t\t\"org.eclipse.vorto:Temperature:1.0.0\"\n\t],\n\t\"properties\":{\n\t\t\"status\":{\n\t\t\t\"value\":0.0,\n\t\t\t\"unit\":\"\"\n\t\t}\n\t}\n\t}\n}\n}\n\t}\n}\n"},"description":"ProvisionstheRPiintheBoschIoTSuite"},"response":[]}]}
			
		'''
	}

	@Test
	def void testCreateScriptWithFbContainingNestedDatatypes() {
		var template = new ProvisionDeviceScriptTemplate(new ProvisioningAPIRequestTemplate);

		var _enum = BuilderUtils.newEnum(new ModelId(ModelType.Datatype, "Units", "org.eclipse.vorto.types", "1.0.0"))
		_enum.withLiterals("F", "C")

		var _entity = BuilderUtils.newEntity(
			new ModelId(ModelType.Datatype, "SensorValue", "org.eclipse.vorto.types", "1.0.0"))
		_entity.withProperty("value", PrimitiveType.FLOAT)
		_entity.withProperty("unit", _enum.build())

		var fbm = BuilderUtils.newFunctionblock(
			new ModelId(ModelType.Functionblock, "Temperature", "org.eclipse.vorto", "1.0.0")).
			withStatusProperty("value", _entity.build).build();

		var im = BuilderUtils.newInformationModel(
			new ModelId(ModelType.InformationModel, "RPi", "org.eclipse.vorto", "1.0.0"))
		im.withFunctionBlock(fbm, "cpuTemperature", null, false);
		var generated = template.getContent(im.build, InvocationContext.simpleInvocationContext());
		Assert.assertEquals(TestUtils.normalizeEOL(expectedTemplate3), TestUtils.normalizeEOL(generated));

	}

	def String getExpectedTemplate3() {
		'''
			{
				"variables": [],
				"info": {
					"name": "RPi Provisioning",
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
								"raw": "{\n  \"id\": \"{{device-id}}\",\n  \"hub\": {\n    \"device\": {\n      \"enabled\": true\n    },\n    \"credentials\": {\n      \"type\": \"hashed-password\",\n      \"secrets\": [\n        {\n          \"password\": \"{{device-password}}\"\n        }\n      ]\n    }\n  },\n   \"things\": {\n    \"thing\": {    \n\"definition\": \"org.eclipse.vorto:RPi:1.0.0\",\n      \"attributes\": {\n     \t\"modelDisplayName\": \"RPi\"\n     \t },\n     \t\"features\": {\n     \t\"cpuTemperature\" : {\n     \t\"definition\": [\n     \t\t\"org.eclipse.vorto:Temperature:1.0.0\"\n     \t],\n     \t\"properties\": {\n     \t\t\"status\": {\n     \t\t\t\"value\" : {\n     \t\t\t\t\"value\" : 0.0,\n     \t\t\t\t\"unit\" : \"F\"\n     \t\t\t}\n     \t\t}\n     \t}\n     \t}\n    }\n    }\n\t}\n}\n"
							},
							"description": "Provisions the RPi in the Bosch IoT Suite"
						},
						"response": []
					}
				]
			}
			
			
		'''
	}

	@Test
	def void testCreateScriptWithFbContainingEnum() {
		var template = new ProvisionDeviceScriptTemplate(new ProvisioningAPIRequestTemplate);

		var _enum = BuilderUtils.newEnum(new ModelId(ModelType.Datatype, "Units", "org.eclipse.vorto.types", "1.0.0"))
		_enum.withLiterals("F", "C")

		var fbm = BuilderUtils.newFunctionblock(
			new ModelId(ModelType.Functionblock, "Temperature", "org.eclipse.vorto", "1.0.0")).
			withStatusProperty("unit", _enum.build).build();

		var im = BuilderUtils.newInformationModel(
			new ModelId(ModelType.InformationModel, "RPi", "org.eclipse.vorto", "1.0.0"))
		im.withFunctionBlock(fbm, "cpuTemperature", null, false);
		var generated = template.getContent(im.build, InvocationContext.simpleInvocationContext());
		Assert.assertEquals(TestUtils.normalizeEOL(expectedTemplate4), TestUtils.normalizeEOL(generated));

	}

	def String getExpectedTemplate4() {
		'''
			{
				"variables": [],
				"info": {
					"name": "RPi Provisioning",
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
								"raw": "{\n  \"id\": \"{{device-id}}\",\n  \"hub\": {\n    \"device\": {\n      \"enabled\": true\n    },\n    \"credentials\": {\n      \"type\": \"hashed-password\",\n      \"secrets\": [\n        {\n          \"password\": \"{{device-password}}\"\n        }\n      ]\n    }\n  },\n   \"things\": {\n    \"thing\": {\n  \"definition\": \"org.eclipse.vorto:RPi:1.0.0\",\n    \"attributes\": {\n     \t\"modelDisplayName\": \"RPi\"\n     \t },\n     \t\"features\": {\n     \t\"cpuTemperature\" : {\n     \t\"definition\": [\n     \t\t\"org.eclipse.vorto:Temperature:1.0.0\"\n     \t],\n     \t\"properties\": {\n     \t\t\"status\": {\n     \t\t\t\"unit\" : \"F\"\n     \t\t}\n     \t}\n     \t}\n    }\n    }\n\t}\n}\n"
							},
							"description": "Provisions the RPi in the Bosch IoT Suite"
						},
						"response": []
					}
				]
			}
		'''
	}
}
