/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks

import org.eclipse.vorto.codegen.api.Generated
import org.eclipse.vorto.codegen.api.IGeneratedWriter
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.examples.bosch.common.FbModelWrapper
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class BluePrintGeneratorTask extends AbstractGeneratorTask<FunctionblockModel> {
	
		
	new(FbModelWrapper context) {
		super(context)
	}
	
	override generate(FunctionblockModel fbm, InvocationContext mappingContext, IGeneratedWriter outputter) {
		var template = constructBlueprintFromTemplate()
		outputter.write(new Generated("config.xml",baseDirectoryFolder,template))	
	}
	
	def String getBaseDirectoryFolder() {
		return '''com.bosch.« context.model.name.toLowerCase»-service/src/main/resources/OSGI-INF/blueprint'''
	}
	
	def String constructBlueprintFromTemplate() {
		var template = '''
		<?xml version="1.0" encoding="UTF-8" standalone="no"?>
		<blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0" xmlns:ext="http://aries.apache.org/blueprint/xmlns/blueprint-ext/v1.0.0" default-activation="eager">
			<ext:property-placeholder />
			<service>
				<interfaces>
					<value>org.osgi.service.device.Driver</value>
				</interfaces>
				<service-properties>
					<entry key="DRIVER_ID" value="«context.javaPackageName».internal.osgidriver.«context.functionBlockName»DeviceDriver" />
				</service-properties>
				<bean class="«context.javaPackageName».internal.osgidriver.«context.functionBlockName»DeviceDriver">
					<argument ref="blueprintBundleContext" />
				</bean>
		   </service>
		
		</blueprint>'''
		
		return template
	}
		
}
	