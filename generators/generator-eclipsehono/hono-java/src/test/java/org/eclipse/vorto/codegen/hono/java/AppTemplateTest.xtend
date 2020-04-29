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

import org.eclipse.vorto.core.api.model.BuilderUtils
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.model.ModelId
import org.eclipse.vorto.core.api.model.model.ModelType
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.junit.Assert
import org.junit.Test

class AppTemplateTest {
	
	@Test
	def void testCreateInformationModelSimple() {
		var template = new AppTemplate();
		
		var fbm = BuilderUtils.newFunctionblock(new ModelId(ModelType.Functionblock,"Temperature","org.eclipse.vorto","1.0.0"))
		   .withStatusProperty("value",PrimitiveType.FLOAT)
		   .withStatusProperty("unit",PrimitiveType.STRING).build();
		   
		var im = BuilderUtils.newInformationModel(new ModelId(ModelType.InformationModel,"MyDevice","org.eclipse.vorto","1.0.0"))
		im.withFunctionBlock(fbm,"temperature","some description",true);	
					
		var generated = template.getContent(im.build, InvocationContext.simpleInvocationContext());
		System.out.println(generated);
		Assert.assertEquals(generated,getExpectedTemplate);
	}
	
	def String getExpectedTemplate() {
		'''
		package device.mydevice;
		
		import org.apache.log4j.Logger;
		
		import device.mydevice.service.hono.HonoDataService;
		
		/**
		 * Example App that uses the Hono Data Service by sending random data to MQTT Connector
		 */
		public class MyDeviceApp {
			
			private static final Logger LOGGER = Logger.getLogger(MyDeviceApp.class);
			
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
					honoDataService.publishTemperature(DEVICE_ID,readTemperature());
					try {
						Thread.sleep(SEND_INTERVAL_IN_SECONDS * 1000);
					} catch (InterruptedException e) {
						LOGGER.error(e);
					}
				}
			}
		
			/**
			* Reads the temperature from the device
			*/
			private static Temperature readTemperature() {
				Temperature temperature = new Temperature();
				//Status properties
				temperature.setValue(Math.round(new java.util.Random().nextFloat()*(float)100));
				temperature.setUnit("");
				return temperature;
			}
		
		}
		'''
	}
}