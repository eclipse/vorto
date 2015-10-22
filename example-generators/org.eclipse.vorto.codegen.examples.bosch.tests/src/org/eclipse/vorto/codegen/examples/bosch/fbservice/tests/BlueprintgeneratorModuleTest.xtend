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
package org.eclipse.vorto.codegen.examples.bosch.fbservice.tests

import org.eclipse.vorto.codegen.examples.bosch.common.FbModelWrapper
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.BluePrintGeneratorTask
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.junit.Test

import static org.junit.Assert.*

class BlueprintgeneratorModuleTest {
	
	private FunctionblockModel model;
	private BluePrintGeneratorTask blueprintModule;
	
	@Test
	def void testBlueprintContentVerification() {
		
		model = createSampleFunctionBlockModel()
		
		blueprintModule =  new BluePrintGeneratorTask(new FbModelWrapper(model))
		
		var generatedBluePrintContent = blueprintModule.constructBlueprintFromTemplate()
		assertEquals(expectedContent, generatedBluePrintContent)
	}
	
	def String getExpectedContent() {
		var content = '''
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0" xmlns:ext="http://aries.apache.org/blueprint/xmlns/blueprint-ext/v1.0.0" default-activation="eager">
	<ext:property-placeholder />
	<service>
		<interfaces>
			<value>org.osgi.service.device.Driver</value>
		</interfaces>
		<service-properties>
			<entry key="DRIVER_ID" value="com.bosch.functionblock.demo.lamp.internal.osgidriver.LampDeviceDriver" />
		</service-properties>
		<bean class="com.bosch.functionblock.demo.lamp.internal.osgidriver.LampDeviceDriver">
			<argument ref="blueprintBundleContext" />
		</bean>
   </service>

</blueprint>'''
		
		return content
	}
	
	def createSampleFunctionBlockModel() {
		var fbmodel = FunctionblockFactory.eINSTANCE.createFunctionblockModel();
		var fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();
		
		fbmodel.setName("Lamp");
		fbmodel.setNamespace("com.bosch");
		fbmodel.setVersion("1.0.0");
		
		fbmodel.setDescription("Driver for function block Lamp");
		fbmodel.setCategory("demo");				
		fbmodel.setFunctionblock(fb);
		
		return fbmodel;
	}
	
}
