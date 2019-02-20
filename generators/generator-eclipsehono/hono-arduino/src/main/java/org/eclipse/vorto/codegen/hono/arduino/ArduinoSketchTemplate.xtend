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
package org.eclipse.vorto.codegen.hono.arduino

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class ArduinoSketchTemplate extends ArduinoTemplate<InformationModel> {
	
	override getFileName(InformationModel model) {
		return model.name + "App.ino";
	}
	
	override getPath(InformationModel model) {
		return model.name + "App";
	}
	
	override getContent(InformationModel model, InvocationContext context) {
		'''
		// Generated by Vorto from «model.namespace».«model.name»:«model.version»
		
		#define USE_SECURE_CONNECTION 1
		
		#include <PubSubClient.h>
		#include <ESP8266WiFi.h>
		#if (USE_SECURE_CONNECTION == 1)
		    #include <WiFiClientSecure.h>
		#endif
		«FOR fb : model.properties»
		#include "src/model/functionblock/«fb.type.name».h"
		«ENDFOR»
		#include "src/model/infomodel/«model.name».h"
		
		/**************************************************************************/
		/* Configuration section, adjust to your settings                         */
		/**************************************************************************/
		
		/* Your tenant in Eclipse Hono / Bosch IoT Hub */
		#define hono_tenant "«context.configurationProperties.getOrDefault("hono_tenant","DEFAULT_TENANT")»"
		
		/* MQTT broker endpoint */
		const char* hono_endpoint = "«context.configurationProperties.getOrDefault("hono_endpoint","<ENTER YOUR MQTT BROKER DNS NAME>")»";
		
		#if (USE_SECURE_CONNECTION == 1)
			/* SHA-1 fingerprint of the server certificate of the MQTT broker, UPPERCASE and spacing */
			const char* mqttServerFingerprint = "<XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX>";
		#endif
		
		/* Define the period of data transmission in ms */
		#define MQTT_DATA_PERIOD 10000
		
		/* Define the buffer size for payload strings */
		#define MQTT_MAX_SIZE  50
		
		/* Device Configuration */
		String hono_deviceId = "«context.configurationProperties.getOrDefault("hono_deviceId","nodemcu-")»";
		String hono_authId = String(hono_deviceId) + "@" + String(hono_tenant);
		const char* hono_password = "«context.configurationProperties.getOrDefault("hono_password","<ENTER THE DEVICE PASSWORD HERE>")»";
		
		/* Payload Configuration*/
		String ditto_topic = "«context.configurationProperties.getOrDefault("ditto_topic","com.mycompany/4711")»";
		
		/* WiFi Configuration */
		const char* ssid = "<ENTER YOUR WIFI SSID>";
		const char* password = "<ENTER YOUR WIFI PASSWORD>";
		
		/* BEGIN SAMPLE CODE */
		/* Sample numeric */
		long value = 0;
		
		/* Sample text value */
		char msg[MQTT_MAX_SIZE];
		/* END SAMPLE CODE */
		
		/**************************************************************************/
		/* Implementation                                                         */
		/**************************************************************************/
		
		/* Port on which the MQTT broker is listening */
		#if (USE_SECURE_CONNECTION == 1)
		    #define MQTT_SERVER_PORT 8883
		#else
		    #define MQTT_SERVER_PORT 1883
		#endif

		/* Topic on which the telemetry data is published */
		String telemetryTopic = String("telemetry/") + String(hono_tenant) + String("/");
		
		/* This variables stores the client ID in the MQTT protocol */
		String hono_clientId;
		
		/* Timestamp of previous data transmission */
		long lastMqttMsg;
		
		/* Setup WiFi mqttClient and MQTT mqttClient */
		#if (USE_SECURE_CONNECTION == 1)
		    WiFiClientSecure wifiClient;
		#else
		    WiFiClient wifiClient;
		#endif
		PubSubClient mqttClient(wifiClient);
		
		/* The information model object */
		«model.namespace.replace(".","_")»::«model.name» infoModel;
		
		/**************************************************************************/
		/* Function to connect to the WiFi network                                */
		/**************************************************************************/
		void setup_wifi() {
		
		    delay(10);
		  
		    /* We start by connecting to a WiFi network */
		    Serial.print("Connecting to WiFi with SSID: ");
		    Serial.println(ssid);
		    WiFi.begin(ssid, password);
		
		    /* Wait for succesful connection, hang if there is none? */
		    while (WiFi.status() != WL_CONNECTED) {
		    	Serial.print(".");
		        delay(500);
		    }
		
		    randomSeed(micros());
		
		    Serial.println("");
		    Serial.println("WiFi connected");
		    Serial.print("IP address: ");
		    Serial.println(WiFi.localIP());
		    Serial.print("MAC address: ");
		    Serial.println(WiFi.macAddress());
		}
		
		/**************************************************************************/
		/* Function called when data on a subscribed topic arrives                */
		/**************************************************************************/
		void mqttDataReceived(char* topic, byte* payload, unsigned int length) {
			
		    /* BEGIN SAMPLE CODE */
		     		 
		    if ((char)payload[0] == '1') {		  	    
				Serial.println("MQTT Data sucessfully received"); 
		    } else {		  	    
		      	Serial.println("No MQTT Data reveived");
		    }
		    
		    /* END SAMPE CODE */
		}
		
		/**************************************************************************/
		/* Reconnect to MQTT broker in case the connection dropped                */
		/**************************************************************************/
		void reconnect() {
		    /* Loop while not connected */
		    while (!mqttClient.connected()) {		    
		
		        /* If connected to the MQTT broker... */
		        if (mqttClient.connect(hono_clientId.c_str(),hono_authId.c_str(),hono_password)) {
		            /* Attempt to Connect succesfull */		            
		            Serial.println("Succesfully connected to MQTT Broker");
		            /* SAMPLE CODE */
		            //String topic = telemetryTopic + "/led";
		            //mqttClient.subscribe(topic.c_str());		            
		            /* END SAMPLE CODE */
		        } else {
		    	    /* otherwise wait for 5 seconds before retrying */
		    	    Serial.println("Waiting for next attempt to connect to MQTT Broker");
		            delay(5000);
		        }
		    }
		}
		
		/**************************************************************************/
		/* Funtions for publishing data using MQTT					              */
		/**************************************************************************/
		
		«FOR fb : model.properties»
		boolean publish«fb.name.toFirstUpper»() {			
				String mqttPayload = infoModel.«fb.name».serialize(ditto_topic ,hono_deviceId, "«fb.name»");
			
				/* Debug output on console */
				Serial.print("Publishing Payload for «fb.name»: "); 
				Serial.print(mqttPayload);
				Serial.print(" to topic: "); 
				Serial.println(telemetryTopic);
			
				/* Publish all available «fb.name» data to the MQTT broker */
				if(!mqttClient.publish(telemetryTopic.c_str(), mqttPayload.c_str())) {		 	    
					Serial.println("Publish «fb.name» failed, if this happens repeatedly increase MQTT_MAX_PACKET_SIZE in PubSubClient.h");		 	    
					return false;
				} else {		 	    
					return true; 		  	
				}		 
		}       	
        «ENDFOR»
		
		/**************************************************************************/
		/* Arduino Setup and Loop Functions							              */
		/**************************************************************************/
		void setup() {		    
		    Serial.begin(115200);
		    
		    setup_wifi();
		  
		   /* Create a MQTT client ID */
		    hono_clientId = hono_deviceId;
		    
		    Serial.print("Device ID: ");
		    Serial.println(hono_clientId);
		    
		    /* Add the client ID to the telemetry topic as the final element */
		    telemetryTopic += hono_deviceId;
		  
		    /* Configure the MQTT client with the server and callback data */
		    mqttClient.setServer(hono_endpoint, MQTT_SERVER_PORT);
		    mqttClient.setCallback(mqttDataReceived);

		    #if (USE_SECURE_CONNECTION == 1)
		        if (!wifiClient.connect(hono_endpoint, MQTT_SERVER_PORT)) {
		        	Serial.println("Secure connection failed, restart Device");		            
			        ESP.restart();
		        } else {
		        	Serial.println("Successfully established secure connection to broker");
				}
				
		        if (!wifiClient.verify(mqttServerFingerprint, hono_endpoint)) {
		            Serial.println("Verification failed, restart Device");	
			        ESP.restart();
		        } else {
		        	Serial.println("Successfully verified server certificate");
		        }
		    #endif
		    
		    /*Test MQQT Client*/
		    mqttClient.publish("","");
		}
		
		void loop() {		
			/* Check if connection to MQTT broker is still good */
		    if (!mqttClient.connected()) {
		    	/* Reconnect if not */
		        reconnect();
		    }
		    
		    /* Event handling of the MQTT client */
		    mqttClient.loop();
		
		    /* Publish the telemetry data periodically */
		    long now = millis();
		    if (now - lastMqttMsg > MQTT_DATA_PERIOD) {
		        lastMqttMsg = now;
		        
		        /* SAMPLE CODE */
		        value++;
		        snprintf(msg, MQTT_MAX_SIZE - 1, "hello world #%ld", value);
		        
		        «FOR fb : model.properties»
		        	«IF fb.type.functionblock.status !== null»
		        	//Status Properties
		        		«FOR status : fb.type.functionblock.status.properties»
		        			«IF isNumericType(status.type)»
		        			infoModel.«fb.name».set«status.name»(value);
		        			«ELSEIF isAlphabetical(status.type)»
		        			infoModel.«fb.name».set«status.name»(msg);
		        			«ELSEIF isEnum(fb.type.functionblock, status.type)»
		        			infoModel.«fb.name».set«status.name»(«type(status.type)»::«getFirstValueEnum(fb.type.functionblock, status.type)»);
		        			«ELSEIF isEntity(fb.type.functionblock, status.type)»
		        			«type(status.type)» «status.name»;
		        			«FOR Entity : getEntity(fb.type.functionblock, status.type)»
		        			    «IF isNumericType(Entity.type)»
		        			        «status.name».set«Entity.name»(value);
		        			    «ELSEIF isAlphabetical(Entity.type)»
		        			        «status.name».set«Entity.name»(msg);
		        			    «ENDIF»
		        			«ENDFOR»
		        			infoModel.«fb.name».set«status.name»(«status.name»);
		        			«ENDIF»
		        		«ENDFOR»
		        	«ENDIF»
		        	«IF fb.type.functionblock.configuration !== null»
		        	//Configuration Properties
		        		«FOR configuration : fb.type.functionblock.configuration.properties»
		        			«IF isNumericType(configuration.type)»
		        			infoModel.«fb.name».set«configuration.name»(value);
		        			«ELSEIF isAlphabetical(configuration.type)»
		        			infoModel.«fb.name».set«configuration.name»(msg);
		        			«ELSEIF isEnum(fb.type.functionblock, configuration.type)»
		        			infoModel.«fb.name».set«configuration.name»(«type(configuration.type)»::«getFirstValueEnum(fb.type.functionblock, configuration.type)»);
		        			«ELSEIF isEntity(fb.type.functionblock, configuration.type)»
		        			«type(configuration.type)» «configuration.name»;
		        			«FOR Entity : getEntity(fb.type.functionblock, configuration.type)»
		        			    «IF isNumericType(Entity.type)»
		        			        «configuration.name».set«Entity.name»(value);
		        			    «ELSEIF isAlphabetical(Entity.type)»
		        			        «configuration.name».set«Entity.name»(msg);
		        			    «ENDIF»
		        			«ENDFOR»
		        			infoModel.«fb.name».set«configuration.name»(«configuration.name»);
		        			«ENDIF»
		        		«ENDFOR»
		        	«ENDIF»		        	
		        «ENDFOR»				
		        
		        «FOR fb : model.properties»
		        publish«fb.name.toFirstUpper»();
                «ENDFOR»                 
		/* END OF SAMPLE CODE */
		    }
		}
		'''
	}		
}

