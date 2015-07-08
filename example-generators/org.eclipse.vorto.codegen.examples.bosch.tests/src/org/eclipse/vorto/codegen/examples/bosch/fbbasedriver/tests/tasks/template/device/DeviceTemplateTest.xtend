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
package org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.template.device

import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.template.device.DummyDeviceTemplate
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.TestFunctionblockModelFactory
import org.junit.Test

import static org.junit.Assert.assertEquals

class DeviceTemplateTest {

	@Test
	def testGetContent() {
		var model = TestFunctionblockModelFactory.populateFBmodelWithProperties();

		var result = new DummyDeviceTemplate().getContent(model);
		assertEquals(fetchExpected, result);
	}

	private def String fetchExpected() {
		'''package com.bosch.functionblock.dummy.internal.device.fridge;
		
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;

import com.bosch.functionblock.dummy.internal.device.AbstractDummyDevice;
		
public class DummyFridgeDevice extends AbstractDummyDevice {
	
	public DummyFridgeDevice(BundleContext bundleContext, String serialNo) {
		super(bundleContext, serialNo);
	}
					
	public boolean isAlive() {
		return true;
	}
	
	@Override	
	public void sendCommand(String commandId, Object payload){
		//TODO:Please implement this method to make it communicate to actual device
		logger.trace("Received command {}, payload: {}", commandId, payload);
	}
	
	@Override
	public String getEventTopic(String eventName) {
		return "DeviceDriver/Fridge/"  + eventName;		
	}	
	
	@Override
	public Map<String, List<String>> getAllEventPropertyNames() {
		Map<String, List<String>> allEventPropertyNames = new HashMap<String, List<String>>();
		allEventPropertyNames.put("dummyEventName", getDummyEventNameEventPropertyNames());
		return allEventPropertyNames;
	}	
		
	private List<String> getDummyEventNameEventPropertyNames(){
		List<String> propertyNames = new ArrayList<String>();
		propertyNames.add("testString");	
		propertyNames.add("testShort");	
		propertyNames.add("testInt");	
		propertyNames.add("testLong");	
		propertyNames.add("testFloat");	
		propertyNames.add("testDouble");	
		propertyNames.add("testDatetime");	
		propertyNames.add("testByte");	
		propertyNames.add("testBase64");	
		propertyNames.add("testBoolean");	
		return propertyNames;
	}
}'''
	}
}
