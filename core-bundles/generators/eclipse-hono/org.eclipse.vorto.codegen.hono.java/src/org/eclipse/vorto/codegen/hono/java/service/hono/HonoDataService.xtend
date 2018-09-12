/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.codegen.hono.java.service.hono

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.hono.java.Utils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.model.ModelIdFactory
import org.eclipse.vorto.core.api.model.model.ModelType

class HonoDataService implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''HonoDataService.java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.getJavaPackageBasePath(context)»/service/hono'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
	'''
	package «Utils.getJavaPackage(element)».service.hono;
	
	import java.util.HashMap;
	import java.util.Map;
	import java.util.Objects;
	
	«FOR reference : element.references»
	«var modelId = ModelIdFactory.newInstance(ModelType.Functionblock,reference)»
	import «Utils.getJavaPackage(element)».model.«modelId.name»;
	«ENDFOR»
	import «Utils.getJavaPackage(element)».service.IDataService;
	import com.google.gson.Gson;
	
	/**
	 * Data Service Implementation that sends device data to Eclipse Hono MQTT Endpoint
	 *
	 */
	public class HonoDataService implements IDataService {
		
		private String mqttHostUrl;
		private String honoTenant;
		private String dittoNamespace;
		private String authId;
		private String deviceId;
		private String password;
		private Map<String, HonoMqttClient> deviceClients = new HashMap<String, HonoMqttClient>();
		private Gson gson = new Gson();
		
		public HonoDataService(String mqttHostUrl, String honoTenant, String dittoNamespace, String deviceId, String authId, String password) {
			this.mqttHostUrl = Objects.requireNonNull(mqttHostUrl);
			this.honoTenant = Objects.requireNonNull(honoTenant);
			this.dittoNamespace = Objects.requireNonNull(dittoNamespace);
			this.deviceId = Objects.requireNonNull(deviceId);
			this.authId = Objects.requireNonNull(authId);
			this.password = Objects.requireNonNull(password);
		}
		
		«FOR fbProperty : element.properties»
		public void publish«fbProperty.name.toFirstUpper»(String resourceId, «fbProperty.type.name» «fbProperty.name») {
			getConnectedHonoClient(resourceId).send("telemetry/" + honoTenant + "/" + resourceId, gson.toJson(wrap(«fbProperty.name».getStatusProperties(),«fbProperty.name».getConfigurationProperties(),"«fbProperty.name»")));
		}
		«ENDFOR»
		
		private <T> Map<String, Object> wrap(T StatusProperties, T ConfigurationProperties, String featureName) {				
			Map<String, Object> headers = new HashMap<String, Object>();
			headers.put("response-required", Boolean.FALSE);
			
			Map<String, Object> wrapper = new HashMap<String, Object>();
			wrapper.put("topic", dittoNamespace + "/" + deviceId + "/things/twin/commands/modify");
		    wrapper.put("path", "/features/"+featureName);
		    wrapper.put("value", createValue(StatusProperties,ConfigurationProperties));
			wrapper.put("headers", headers);
	
			return wrapper; 
		}
		
		private <T> Map<String, Object> createValue(T StatusProperties, T ConfigurationProperties) {
			Map<String, Object> value = new HashMap<String, Object>();
			Map<String, Object> properties = new HashMap<String, Object>();
			
			properties.put("status",StatusProperties);
			properties.put("configuration",ConfigurationProperties);
			value.put("properties", properties);
			return value;
		}
	
		private HonoMqttClient getConnectedHonoClient(String resourceId) {
			HonoMqttClient client = deviceClients.get(resourceId);
			if (client == null) {
				client = new HonoMqttClient(mqttHostUrl, resourceId, authId, password);
				deviceClients.put(resourceId, client);
			}
			
			if (!client.isConnected()) {
				client.connect();
			}
			
			return client;
		}
	}
	'''
	}
	
}
