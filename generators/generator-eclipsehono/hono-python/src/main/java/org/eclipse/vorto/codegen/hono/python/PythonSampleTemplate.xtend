/*******************************************************************************
 *  Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *******************************************************************************/
package org.eclipse.vorto.codegen.hono.python
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType

class PythonSampleTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel model) {
		return model.name + "App.py";
	}
	
	override getPath(InformationModel model) {
		return ""
	}

	override getContent(InformationModel model, InvocationContext context) {
	'''
	import netifaces
	import paho.mqtt.client as mqtt
	import datetime, threading, time
	«FOR fb : model.properties»
	import model.functionblock.«fb.type.name» as «fb.name» 
	«ENDFOR»
	import model.infomodel.«model.name» as «model.name»
	import model.DittoSerializer as DittoSerializer
	
	# DEVICE CONFIG GOES HERE
	hono_tenant = "«context.configurationProperties.getOrDefault("hono_tenant","<hono_tenant>")»"
	hono_password = "<ADD PASSWORD HERE>"
	hono_endpoint = "«context.configurationProperties.getOrDefault("hono_endpoint","<hono_endpoint>")»"
	hono_deviceId = "«context.configurationProperties.getOrDefault("hono_deviceId","<hono_deviceId>")»"
	hono_clientId = hono_deviceId
	hono_authId = hono_deviceId + "@" + hono_tenant
	hono_certificatePath = "<ADD PATH TO CERTIFICATE HERE>"
	ditto_topic = "«context.configurationProperties.getOrDefault("ditto_topic","com.mycompany/4711")»"


	# The callback for when the client receives a CONNACK response from the server.
	def on_connect(client, userdata, flags, rc):
	    global next_call

	    if rc != 0:
	        print("Connection to MQTT broker failed: " + str(rc))
	        return
	
	    # Subscribing in on_connect() means that if we lose the connection and
	    # reconnect then subscriptions will be renewed.
	
	    # BEGIN SAMPLE CODE
	    client.subscribe("commands/" + hono_tenant + "/")
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
	«FOR fb : model.properties»
	def publish«fb.name.toFirstUpper»():
	    payload = ser.serialize_functionblock("«fb.name»", infomodel.«fb.name», ditto_topic, hono_deviceId)
	    print("Publish Payload: ", payload, " to Topic: ", publishTopic)
	    client.publish(publishTopic, payload)
	    
	«ENDFOR» 
	# The function that will be executed periodically once the connection to the MQTT broker was established
	def periodicAction():
	    global next_call
	
	    ### BEGIN SAMPLE CODE
	
	    # Setting properties of function blocks
	    «FOR fb : model.properties»
	        «IF fb.type.functionblock.status !== null»
	        	«FOR status : fb.type.functionblock.status.properties»
	        		«IF status.type instanceof PrimitivePropertyType»
	        			«var primitiveType = status.type as PrimitivePropertyType»
	        			«IF primitiveType.type == PrimitiveType.STRING»
	        			infomodel.«fb.name».«status.name» = ""
	        			«ELSE»
	        			infomodel.«fb.name».«status.name» += 1
	        			«ENDIF»
	        		«ENDIF»
	        	«ENDFOR»
	        «ENDIF»
	    «ENDFOR»

	    ### END SAMPLE CODE

	    # Publish payload
	    «FOR fb : model.properties»
	    publish«fb.name.toFirstUpper»()
	    «ENDFOR»

	    # Schedule next call
	    next_call = next_call + timePeriod;
	    threading.Timer(next_call - time.time(), periodicAction).start()


	# Initialization of Information Model 
	infomodel = «model.name».«model.name»()
	«FOR fb : model.properties»
	    «IF fb.type.functionblock.status !== null»
	        «FOR status : fb.type.functionblock.status.properties»
	            «IF status.type instanceof PrimitivePropertyType»
	            	«var primitiveType = status.type as PrimitivePropertyType»
	            	«IF primitiveType.type == PrimitiveType.STRING»
	            	infomodel.«fb.name».«status.name» = ""
	            	«ELSE»
	            	infomodel.«fb.name».«status.name» = 0
	            	«ENDIF»
	            «ENDIF»
	        «ENDFOR»
	    «ENDIF»
	    «IF fb.type.functionblock.configuration !== null»
	        «FOR configuration : fb.type.functionblock.configuration.properties»
	            «IF configuration.type instanceof PrimitivePropertyType»
	            	«var primitiveType = configuration.type as PrimitivePropertyType»
	            	«IF primitiveType.type == PrimitiveType.STRING»
	            	infomodel.«fb.name».«configuration.name» = ""
	            	«ELSE»
	            	infomodel.«fb.name».«configuration.name» = 0
	            	«ENDIF»
	            «ENDIF»
	        «ENDFOR»
	    «ENDIF»
	«ENDFOR»

	# Create a serializer for the MQTT payload from the Information Model
	ser = DittoSerializer.DittoSerializer()

	# Timer variable for periodic function
	next_call = 0

	# Period for publishing data to the MQTT broker in seconds
	timePeriod = 10

	# Configuration of client ID and publish topic	
	publishTopic = "telemetry/" + hono_tenant + "/" + hono_clientId

	# Output relevant information for consumers of our information
	print("Connecting client:    ", hono_clientId)
	print("Publishing to topic:  ", publishTopic)

	# Create the MQTT client
	client = mqtt.Client(hono_clientId)
	client.on_connect = on_connect
	client.on_message = on_message
	
	client.tls_set(hono_certificatePath)
	
	client.username_pw_set(hono_authId, hono_password)

	# Connect to the MQTT broker
	client.connect(hono_endpoint, 8883, 60)

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

