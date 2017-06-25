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

class ComponentXmlTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''component.xml'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.basePath»/OSGI-INF'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		<?xml version="1.0" encoding="UTF-8"?>
		
		«IF context.configurationProperties.getOrDefault("bluetooth","false").equalsIgnoreCase("true")»
		<scr:component xmlns:scr="http://www.osgi.org/xmlns/scr/v1.1.0" activate="activate" configuration-policy="require" deactivate="deactivate" enabled="true" immediate="true" modified="updated" name="«Utils.javaPackage».bluetooth.«element.name»BluetoothFinder">
		   <implementation class="«Utils.javaPackage».bluetooth.«element.name»BluetoothFinder"/>
		   <service>
		      <provide interface="org.eclipse.kura.configuration.ConfigurableComponent"/>
		   </service>
		   <property name="service.pid" value="«Utils.javaPackage».bluetooth.«element.name»BluetoothFinder"/>
		   «IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("false")»
		   <reference bind="setCloudService" 
		    		  cardinality="1..1" 
		    		  interface="org.eclipse.kura.cloud.CloudService"
		    		  unbind="unsetCloudService"/>
		    «ENDIF»
		    <reference bind="setBluetoothService" 
		              cardinality="1..1" 
		              interface="org.eclipse.kura.bluetooth.BluetoothService" 
		              name="BluetoothService" 
		              policy="static" 
		              unbind="unsetBluetoothService"/>
		</scr:component>
		«ELSE»
		<scr:component xmlns:scr="http://www.osgi.org/xmlns/scr/v1.1.0" activate="activate" configuration-policy="require" deactivate="deactivate" enabled="true" immediate="true" modified="updated" name="«Utils.javaPackage».«element.name»App">
			<implementation class="«Utils.javaPackage».«element.name»App"/>
		</scr:component>
		«ENDIF»
		'''
	}
	
}
