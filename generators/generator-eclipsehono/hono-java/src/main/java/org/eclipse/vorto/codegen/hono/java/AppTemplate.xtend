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
package org.eclipse.vorto.codegen.hono.java

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.model.ModelIdFactory
import org.eclipse.vorto.core.api.model.model.ModelType
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.IFileTemplate

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
	 * Example App that uses the Hono Data Service by sending random data to MQTT Connector
	 */
	public class «element.name»App {
		
		private static final Logger LOGGER = Logger.getLogger(«element.name»App.class);
		
		/**************************************************************************/
		/* Configuration Section */
		/* Adjust according to your Endpoint Configuration*/
		/**************************************************************************/
		
		private static final String HUB_ADAPTER_HOST = "ssl://mqtt.bosch-iot-hub.com:8883";
		
		private static final String TENANT_ID = "ADD TENANT ID HERE";
		
		private static final String DEVICE_ID = "ADD DEVICE ID HERE";
		
		private static final String AUTH_ID = "ADD AUTH ID HERE";
		
		private static final String DITTO_TOPIC = "ADD DITTO TOPIC HERE, e.g. com.mycompany/1234";
		
		private static final String DEVICE_PASSWORD = "ADD DEVICE PASSWORD HERE";
		
		private static final long SEND_INTERVAL_IN_SECONDS = 2;
		
		public static void main(final String... args) {
			HonoDataService honoDataService = new HonoDataService(HUB_ADAPTER_HOST, TENANT_ID, DITTO_TOPIC, 
					DEVICE_ID, AUTH_ID + "@" + TENANT_ID, DEVICE_PASSWORD);
			
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
