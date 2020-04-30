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
package org.eclipse.vorto.codegen.hono.python

import org.eclipse.vorto.core.api.model.BuilderUtils
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.model.ModelId
import org.eclipse.vorto.core.api.model.model.ModelType
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.junit.Assert
import org.junit.Test

class PythonAppTemplateTest {
	
	@Test
	def void testCreateInformationModelSimple() {
		var template = new PythonSampleTemplate();
		
		var fbm = BuilderUtils.newFunctionblock(new ModelId(ModelType.Functionblock,"Temperature","org.eclipse.vorto","1.0.0"))
		   .withStatusProperty("value",PrimitiveType.FLOAT)
		   .withStatusProperty("unit",PrimitiveType.STRING).build();
		   
		var im = BuilderUtils.newInformationModel(new ModelId(ModelType.InformationModel,"MyDevice","org.eclipse.vorto","1.0.0"))
		im.withFunctionBlock(fbm,"temperature","some description", true);	
					
		var generated = template.getContent(im.build, InvocationContext.simpleInvocationContext());
		Assert.assertEquals(getExpectedTemplate,generated);
	}
	
	def String getExpectedTemplate() {
		'''
		import netifaces
		import paho.mqtt.client as mqtt
		import datetime, threading, time
		import model.functionblock.Temperature as temperature 
		import model.infomodel.MyDevice as MyDevice
		import model.DittoSerializer as DittoSerializer
		
		# DEVICE CONFIG GOES HERE
		tenantId = "ADD TENANT HERE"
		device_password = "ADD DEVICE PASSWORD HERE"
		hub_adapter_host = "mqtt.bosch-iot-hub.com"
		deviceId = "ADD DEVICE ID HERE"
		clientId = deviceId
		authId = "ADD AUTH ID HERE"
		certificatePath = "ADD PATH TO SERVER CERTIFICATE HERE"
		ditto_topic = "ADD TOPIC HERE, e.g. com.mycompany/4711"
		
		
		# The callback for when the client receives a CONNACK response from the server.
		def on_connect(client, userdata, flags, rc):
		    global next_call
		
		    if rc != 0:
		        print("Connection to MQTT broker failed: " + str(rc))
		        return
		
		    # Subscribing in on_connect() means that if we lose the connection and
		    # reconnect then subscriptions will be renewed.
		
		    # BEGIN SAMPLE CODE
		    client.subscribe("commands/" + tenantId + "/")
		    # END SAMPLE CODE
		
		    # Time stamp when the periodAction function shall be called again
		    next_call = time.time()
		    
		    # Start the periodic task for publishing MQTT messages
		    periodicAction()
		
		# The callback for when a PUBLISH message is received from the server.
		def on_message(client, userdata, msg):
		    
		    ### BEGIN SAMPLE CODE
		    
		    print(msg.topic+" "+str(msg.payload))
		
		    ### END SAMPLE CODE
		
		# The functions to publish the functionblocks data
		def publishTemperature():
		    payload = ser.serialize_functionblock("temperature", infomodel.temperature, ditto_topic, deviceId)
		    print("Publish Payload: ", payload, " to Topic: ", publishTopic)
		    client.publish(publishTopic, payload)
		
		
		# The function that will be executed periodically once the connection to the MQTT broker was established
		def periodicAction():
		    global next_call
		
		    ### BEGIN READING SENSOR DATA
		    
		    infomodel.temperature.value = 0
		    infomodel.temperature.unit = ""
		
		    ### END READING SENSOR DATA
		
		    # Publish payload
		    publishTemperature()
		
		    # Schedule next call
		    next_call = next_call + timePeriod
		    threading.Timer(next_call - time.time(), periodicAction).start()
		
		# Initialization of Information Model
		infomodel = MyDevice.MyDevice()
		
		# Create a serializer for the MQTT payload from the Information Model
		ser = DittoSerializer.DittoSerializer()
		
		# Timer variable for periodic function
		next_call = 0
		
		# Period for publishing data to the MQTT broker in seconds
		timePeriod = 10
		
		# Configuration of client ID and publish topic	
		publishTopic = "telemetry/" + tenantId + "/" + deviceId
		
		# Output relevant information for consumers of our information
		print("Connecting client:    ", clientId)
		print("Publishing to topic:  ", publishTopic)
		
		# Create the MQTT client
		client = mqtt.Client(clientId)
		client.on_connect = on_connect
		client.on_message = on_message
		
		client.tls_set(certificatePath)
		
		username = authId + "@" + tenantId
		client.username_pw_set(username, device_password)
		
		# Connect to the MQTT broker
		client.connect(hub_adapter_host, 8883, 60)
		
		# Blocking call that processes network traffic, dispatches callbacks and
		# handles reconnecting.
		# Other loop*() functions are available that give a threaded interface and a
		# manual interface.
		client.loop_start()
		
		while (1):
		    pass
		'''
	}
	
	@Test
	def void testCreateInformationModelComplex() {
		var template = new PythonSampleTemplate();
		
		var entity = BuilderUtils.newEntity(new ModelId(ModelType.Datatype,"SensorValue","org.eclipse.vorto","1.0.0"))
		entity.withProperty("current",PrimitiveType.FLOAT)
		entity.withProperty("unit",PrimitiveType.STRING)
		
		var fbm = BuilderUtils.newFunctionblock(new ModelId(ModelType.Functionblock,"Temperature","org.eclipse.vorto","1.0.0"))
		   .withStatusProperty("value",entity.build)
		   .withStatusProperty("otherProp",PrimitiveType.STRING).build();
		   
		var im = BuilderUtils.newInformationModel(new ModelId(ModelType.InformationModel,"MyDevice","org.eclipse.vorto","1.0.0"))
		im.withFunctionBlock(fbm,"temperature",null,false);	
					
		var generated = template.getContent(im.build, InvocationContext.simpleInvocationContext());
		System.out.println(generated);
		Assert.assertEquals(getExpectedTemplate2,generated);
	}
	
	def String getExpectedTemplate2() {
		'''
		import netifaces
		import paho.mqtt.client as mqtt
		import datetime, threading, time
		import model.functionblock.Temperature as temperature 
		import model.infomodel.MyDevice as MyDevice
		import model.DittoSerializer as DittoSerializer
		
		# DEVICE CONFIG GOES HERE
		tenantId = "ADD TENANT HERE"
		device_password = "ADD DEVICE PASSWORD HERE"
		hub_adapter_host = "mqtt.bosch-iot-hub.com"
		deviceId = "ADD DEVICE ID HERE"
		clientId = deviceId
		authId = "ADD AUTH ID HERE"
		certificatePath = "ADD PATH TO SERVER CERTIFICATE HERE"
		ditto_topic = "ADD TOPIC HERE, e.g. com.mycompany/4711"
		
		
		# The callback for when the client receives a CONNACK response from the server.
		def on_connect(client, userdata, flags, rc):
		    global next_call
		
		    if rc != 0:
		        print("Connection to MQTT broker failed: " + str(rc))
		        return
		
		    # Subscribing in on_connect() means that if we lose the connection and
		    # reconnect then subscriptions will be renewed.
		
		    # BEGIN SAMPLE CODE
		    client.subscribe("commands/" + tenantId + "/")
		    # END SAMPLE CODE
		
		    # Time stamp when the periodAction function shall be called again
		    next_call = time.time()
		    
		    # Start the periodic task for publishing MQTT messages
		    periodicAction()
		
		# The callback for when a PUBLISH message is received from the server.
		def on_message(client, userdata, msg):
		    
		    ### BEGIN SAMPLE CODE
		    
		    print(msg.topic+" "+str(msg.payload))
		
		    ### END SAMPLE CODE
		
		# The functions to publish the functionblocks data
		def publishTemperature():
		    payload = ser.serialize_functionblock("temperature", infomodel.temperature, ditto_topic, deviceId)
		    print("Publish Payload: ", payload, " to Topic: ", publishTopic)
		    client.publish(publishTopic, payload)
		
		
		# The function that will be executed periodically once the connection to the MQTT broker was established
		def periodicAction():
		    global next_call
		
		    ### BEGIN READING SENSOR DATA
		    
		    infomodel.temperature.value = {
		    	"current" : 0,
		    	"unit" : 0
		    }
		    infomodel.temperature.otherProp = ""
		
		    ### END READING SENSOR DATA
		
		    # Publish payload
		    publishTemperature()
		
		    # Schedule next call
		    next_call = next_call + timePeriod
		    threading.Timer(next_call - time.time(), periodicAction).start()
		
		# Initialization of Information Model
		infomodel = MyDevice.MyDevice()
		
		# Create a serializer for the MQTT payload from the Information Model
		ser = DittoSerializer.DittoSerializer()
		
		# Timer variable for periodic function
		next_call = 0
		
		# Period for publishing data to the MQTT broker in seconds
		timePeriod = 10
		
		# Configuration of client ID and publish topic	
		publishTopic = "telemetry/" + tenantId + "/" + deviceId
		
		# Output relevant information for consumers of our information
		print("Connecting client:    ", clientId)
		print("Publishing to topic:  ", publishTopic)
		
		# Create the MQTT client
		client = mqtt.Client(clientId)
		client.on_connect = on_connect
		client.on_message = on_message
		
		client.tls_set(certificatePath)
		
		username = authId + "@" + tenantId
		client.username_pw_set(username, device_password)
		
		# Connect to the MQTT broker
		client.connect(hub_adapter_host, 8883, 60)
		
		# Blocking call that processes network traffic, dispatches callbacks and
		# handles reconnecting.
		# Other loop*() functions are available that give a threaded interface and a
		# manual interface.
		client.loop_start()
		
		while (1):
		    pass
		'''
	}
}