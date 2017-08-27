/*******************************************************************************
 *  Copyright (c) 2017 Oliver Meili
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Oliver Meili <omi@ieee.org>
 *******************************************************************************/
package org.eclipse.vorto.codegen.mqtt.python

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.api.IFileTemplate

class PythonSampleTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel model) {
		return model.name + ".py";
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
	import «fb.type.namespace».«fb.type.name» as «fb.type.name» 
    «ENDFOR»
	import «model.namespace».«model.name» as «model.name»
	import serializer.DittoSerializer as DittoSerializer

	# Function that creates a unique client ID based on a prefix and a MAC address
	def getClientId(prefix):
	    netifs = netifaces.interfaces()
	    for netif in netifs[:]:
	        if netif != "lo":
	            mac_addr = netifaces.ifaddresses(netif)[netifaces.AF_LINK][0]['addr']
	            return prefix + mac_addr

	# The callback for when the client receives a CONNACK response from the server.
	def on_connect(client, userdata, flags, rc):
	    global next_call

	    if rc != 0:
	        print("Connection to MQTT broker failed: "+connack_string(rc))
	        return
	
	    # Subscribing in on_connect() means that if we lose the connection and
	    # reconnect then subscriptions will be renewed.
	
	    # BEGIN SAMPLE CODE
	    client.subscribe("commands/DEFAULT_TENANT/")
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
	    	
	# The function that will be executed periodically once the connection to the MQTT broker was established
	def periodicAction():
	    global next_call
	
	    ### BEGIN SAMPLE CODE
	
	    # Setting properties of function blocks
	    «FOR fb : model.properties»
	        «FOR status : fb.type.functionblock.status.properties»
	            infomodel.«fb.name».«status.name» += 1
	        «ENDFOR»
	    «ENDFOR»

	    # Serializing information model
	    payload = ser.serialize_infomodel("NewInfomodel", infomodel)
	    print(payload)

	    ### END SAMPLE CODE

	    # Publish payload
	    client.publish(publishTopic, payload)

	    # Schedule next call
	    next_call = next_call + timePeriod;
	    threading.Timer(next_call - time.time(), periodicAction).start()


	# Initialization of Information Model 
	infomodel = «model.name».«model.name»()
	«FOR fb : model.properties»
	    «FOR status : fb.type.functionblock.status.properties»
	        infomodel.«fb.name».«status.name» = 0
	    «ENDFOR»
	«ENDFOR»

	# Create a serializer for the MQTT payload from the Information Model
	ser = DittoSerializer.DittoSerializer()

	# Timer variable for periodic function
	next_call = 0

	# Period for publishing data to the MQTT broker in seconds
	timePeriod = 10

	# Configuration of client ID and publish topic
	clientId = getClientId("RPi-")
	publishTopic = "telemetry/DEFAULT_TENANT/" + clientId

	# Output relevant information for consumers of our information
	print("Connecting client:    ", clientId)
	print("Publishing to topic:  ", publishTopic)

	# Create the MQTT client
	client = mqtt.Client(clientId)
	client.on_connect = on_connect
	client.on_message = on_message
	
	client.tls_set("<path to CA cert file>")

	# Connect to the MQTT broker
	client.connect("<MQTT broker>", 8883, 60)

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

