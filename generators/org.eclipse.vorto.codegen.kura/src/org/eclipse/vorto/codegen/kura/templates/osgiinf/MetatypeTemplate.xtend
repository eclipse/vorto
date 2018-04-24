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
package org.eclipse.vorto.codegen.kura.templates.osgiinf

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.kura.templates.Utils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class MetatypeTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''«Utils.getJavaPackage(context)».«context.name»BluetoothFinder.xml'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.getBasePath(context)»/OSGI-INF/metatype'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		<?xml version="1.0" encoding="UTF-8"?>
		<MetaData xmlns="http://www.osgi.org/xmlns/metatype/v1.2.0" localization="en_us">
		    <OCD id="«Utils.getJavaPackage(element)».«element.name»BluetoothFinder"
		         name="«element.displayname» Bluetooth Finder" 
		         description="App that reads «element.displayname» data via bluetooth and sends it to the IoT cloud backend.">
		         
		        <Icon resource="http://vorto.eclipse.org/rest/model/image/«element.namespace»/«element.name»/«element.version»" size="32"/>
				
				«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
				<AD id="boschcloud_solutionid"
					name="boschcloud_solutionid"
					type="String" 
					cardinality="0"
					required="true"
					default=""
					description="Bosch IoT Suite Solution ID" />
					
				<AD id="boschcloud_endpoint"
					name="boschcloud_endpoint"
					type="String"
					cardinality="0"
					required="true"
					default="wss://events.apps.bosch-iot-cloud.com"
					description="Bosch IoT Suite Endpoint" />
					
				«ELSE»
				«IF context.configurationProperties.getOrDefault("boschhub","false").equalsIgnoreCase("true")»
				<AD id="mqttHostUrl"  
					name="mqttHostUrl"
					type="String"
					cardinality="0" 
					required="true"
					default="ssl://mqtt.bosch-iot-hub.com:8883" 
					description="The Bosch IoT Suite Hub URL"/>
					
				<AD id="hubTenant"  
					name="hubTenant"
					type="String"
					cardinality="0" 
					required="true"
					default="DEFAULT_TENANT" 
					description="The Bosch IoT Hub Tenant"/>
					
				«ELSE»
				<AD id="publishTopic"  
					name="publishTopic"
					type="String"
					cardinality="0" 
					required="true"
					default="" 
					description="The topic to publish data to the Cloud."/>
				
				«ENDIF»										
				«ENDIF»
				<AD id="scan_enable"
					name="scan_enable"
					type="Boolean"
					cardinality="0"
					required="true"
					default="false"
					description="Enable scan for «element.displayname» devices." />
				
				<AD id="scan_interval"
					name="scan_interval"
					type="Integer"
					cardinality="0"
					required="true"
					default="60"
					description="Interval of scan for devices in seconds." />
				
				<AD id="scan_time"
					name="scan_time"
					type="Integer"
					cardinality="0"
					required="true"
					default="10"
					description="Duration of scan for devices in seconds." />
					
				<AD id="update_interval"
					name="update_interval"
					type="Integer"
					cardinality="0"
					required="true"
					default="10"
					description="Interval of device update to Cloud in seconds"/>
				
				<AD id="device_filter"  
					name="device_filter"
					type="String"
					cardinality="0" 
					required="true"
					default="FC:D6:BD" 
					description="Filter for «element.displayname» bluetooth devices"/>
					
				<AD id="iname"
					name="iname"
					type="String"
					cardinality="0"
					required="true"
					default="hci0"
					description="Name of bluetooth adapter."/>
					
				«FOR fbProperty : element.properties»
				<AD id="enable«fbProperty.name.toFirstUpper»"
					name="enable«fbProperty.name.toFirstUpper»"
					type="Boolean"
					cardinality="0"
					required="true"
					default="false"
					description="Enable «fbProperty.name»."/>
					
		         «ENDFOR»
		    </OCD>
		
		    <Designate pid="«Utils.getJavaPackage(element)».«element.name»BluetoothFinder">
		        <Object ocdref="«Utils.getJavaPackage(element)».«element.name»BluetoothFinder"/>
		    </Designate>
		</MetaData>
		
		'''
	}
	
}