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
package org.eclipse.vorto.codegen.hono.java

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.model.ModelIdFactory
import org.eclipse.vorto.core.api.model.model.ModelType

class AppTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''«context.name»App.java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.getJavaPackageBasePath(context)»'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
	'''
	package «Utils.getJavaPackage(element)»;
	
	import org.apache.log4j.Logger;
	
	«FOR reference : element.references»
	«var modelId = ModelIdFactory.newInstance(ModelType.Functionblock,reference)»
	import «Utils.getJavaPackage(element)».model.«modelId.name»;
	«ENDFOR»
	import «Utils.getJavaPackage(element)».service.hono.HonoDataService;
	
	/**
	 * Example App that uses the Hono Data Service by sending random data to Eclipse Hono MQTT Connector
	 */
	public class «element.name»App {
	
		private static final Logger LOGGER = Logger.getLogger(«element.name»App.class);
	
		/**************************************************************************/
		/* Configuration Section */
		/* Adjust according to your Endpoint Configuration*/
		/**************************************************************************/
	
		// Hono MQTT Endpoint
		private static final String MQTT_ENDPOINT = "«context.configurationProperties.getOrDefault("hono_endpoint","<INSERT HONO MQTT ENDPOINT HERE>")»";
	
		// Your Tenant
		private static final String HONO_TENANT = "«context.configurationProperties.getOrDefault("hono_tenant","DEFAULT_TENANT")»";
	
		// Your DeviceId
		private static final String DEVICE_ID = "«context.configurationProperties.getOrDefault("deviceId","<INSERT DEVICE ID HERE>")»";
		
		// Device authentication ID
		private static final String AUTH_ID = "«context.configurationProperties.getOrDefault("hono_user","<INSERT DEVICE AUTH ID HERE>")»";
		
		// Ditto topic , e.g. com.mycompany/4711
		private static final String DITTO_TOPIC = "«context.configurationProperties.getOrDefault("ditto_topic","com.mycompany/1234")»";
	
		// Device authentication Password
		private static final String PASSWORD = "ENTER_DEVICE_PASSWORD";
		
		private static final long SEND_INTERVAL_IN_SECONDS = 2;
	
		public static void main(final String... args) {
			HonoDataService honoDataService = new HonoDataService(MQTT_ENDPOINT, HONO_TENANT, DITTO_TOPIC, 
					DEVICE_ID, AUTH_ID,PASSWORD);
			
			while (true) {
				«FOR fbProperty : element.properties»
				honoDataService.publish«fbProperty.name.toFirstUpper»(DEVICE_ID,read«fbProperty.name.toFirstUpper»());
				«ENDFOR»
				try {
					Thread.sleep(SEND_INTERVAL_IN_SECONDS * 1000);
				} catch (InterruptedException e) {
					LOGGER.error(e);
				}
			}
		}
	
		«FOR fbProperty : element.properties»
		/**
		* Reads the «fbProperty.name» from the device
		*/
		private static «fbProperty.type.name» read«fbProperty.name.toFirstUpper»() {
			«fbProperty.type.name» «fbProperty.name» = new «fbProperty.type.name»();
			«IF fbProperty.type.functionblock.status !== null»
			//Status properties
			«FOR statusProperty : fbProperty.type.functionblock.status.properties»
			«fbProperty.name».set«TypeMapper.checkKeyword(statusProperty.name).toFirstUpper»(«TypeMapper.getRandomValue(statusProperty.type)»);
			«ENDFOR»
			«ENDIF»
			«IF fbProperty.type.functionblock.configuration !== null»
			//Configuration properties
			«FOR configurationProperty : fbProperty.type.functionblock.configuration.properties»
			«fbProperty.name».set«TypeMapper.checkKeyword(configurationProperty.name).toFirstUpper»(«TypeMapper.getRandomValue(configurationProperty.type)»);
			«ENDFOR»
			«ENDIF»
			return «fbProperty.name»;
		}
		«ENDFOR»
	
	}
	'''
	}
	
}
