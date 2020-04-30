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

import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.IFileTemplate
import org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType

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
			«FOR fb : model.properties»
				def publish«fb.name.toFirstUpper»():
				    payload = ser.serialize_functionblock("«fb.name»", infomodel.«fb.name», ditto_topic, deviceId)
				    print("Publish Payload: ", payload, " to Topic: ", publishTopic)
				    client.publish(publishTopic, payload)
			«ENDFOR»
			
			
			# The function that will be executed periodically once the connection to the MQTT broker was established
			def periodicAction():
			    global next_call
			
			    ### BEGIN READING SENSOR DATA
			    
			    «FOR fb : model.properties»
			    	«IF fb.type.functionblock.status !== null»
			    		«FOR statusProp : fb.type.functionblock.status.properties»
			    			«output(statusProp,"infomodel."+fb.name)»
			    		«ENDFOR»
			    	«ENDIF»
			    	«IF fb.type.functionblock.configuration !== null»
			    		«FOR configurationProp : fb.type.functionblock.configuration.properties»
			    			«output(configurationProp,"infomodel."+fb.name)»
			    		«ENDFOR»
			    	«ENDIF»
			    «ENDFOR»
			
			    ### END READING SENSOR DATA
			
			    # Publish payload
			    «FOR fb : model.properties»
			    	publish«fb.name.toFirstUpper»()
			    «ENDFOR»
			
			    # Schedule next call
			    next_call = next_call + timePeriod
			    threading.Timer(next_call - time.time(), periodicAction).start()
			
			# Initialization of Information Model
			infomodel = «model.name».«model.name»()
			
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

	def String output(Property property, String prefix) {
		'''
			«IF property.type instanceof PrimitivePropertyType»
				«var primitiveType = property.type as PrimitivePropertyType»
				«IF primitiveType.type == PrimitiveType.STRING»
					«prefix».«property.name» = ""
				«ELSE»
					«prefix».«property.name» = 0
				«ENDIF»
			«ELSEIF property.type instanceof ObjectPropertyType»
				«var objectType = property.type as ObjectPropertyType»
				«IF objectType.type instanceof Enum»
					«prefix».«property.name» = "«(objectType.type as Enum).enums.get(0).name»"
				«ELSE»
					«prefix».«property.name» = {
						«FOR entityProp : (objectType.type as Entity).properties SEPARATOR ","»
							«IF entityProp.type == PrimitiveType.STRING»
								"«entityProp.name»" : ""
							«ELSE»
								"«entityProp.name»" : 0
							«ENDIF»
						«ENDFOR»
					}
				«ENDIF»
			«ELSE» // property type is a dictionary type
				«prefix».«property.name» = {
					// add your dictionary object keys and values here
				}
			«ENDIF»
		'''
	}

}
