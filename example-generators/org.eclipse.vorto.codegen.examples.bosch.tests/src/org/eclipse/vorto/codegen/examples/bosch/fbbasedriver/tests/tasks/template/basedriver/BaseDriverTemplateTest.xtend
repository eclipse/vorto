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
package org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.template.basedriver

import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.template.basedriver.BaseDriverTemplate
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.TestFunctionblockModelFactory
import org.junit.Test

import static org.junit.Assert.assertEquals

class BaseDriverTemplateTest {

	@Test
	def testGetContent() {
		var model = TestFunctionblockModelFactory.populateFBmodelWithProperties();

		var result = new BaseDriverTemplate().getContent(model);
		assertEquals(fetchExpected, result);
	}

	private def String fetchExpected() {
		'''package com.bosch.functionblock.dummy.internal.basedriver;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Timer;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosch.functionblock.dummy.api.device.IDummyDevice;
import com.bosch.functionblock.dummy.internal.device.fridge.DummyFridgeDevice;
		
public class DummyBaseDriver implements ManagedServiceFactory {
	protected static final Logger logger = LoggerFactory.getLogger(DummyBaseDriver.class.getName());
	
	private BundleContext bundleContext;

	public DummyBaseDriver(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		registerDevice(new DummyFridgeDevice(bundleContext,"12345"));		
	}
		
	@SuppressWarnings("rawtypes")
	private ServiceRegistration registerDevice(IDummyDevice device){
		Dictionary<String, String> deviceProperties = new Hashtable<String, String>();
		deviceProperties.put(org.osgi.service.device.Constants.DEVICE_SERIAL, device.getSerialNo());
		deviceProperties.put(org.osgi.framework.Constants.SERVICE_ID, device.getSerialNo());
			
		deviceProperties.put(
				org.osgi.service.device.Constants.DEVICE_DESCRIPTION,"Default dummy function block instance");
		deviceProperties.put(
				org.osgi.service.device.Constants.DEVICE_CATEGORY,"demo");		

		ServiceRegistration serviceRegistration = bundleContext
				.registerService(device.getClass().getName(),device,
						deviceProperties);

		logger.trace("RegsteredService with properties: " + deviceProperties);
		
		scanForDummyEvent(device);
	    return serviceRegistration; 						
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@SuppressWarnings({ "rawtypes"})
	@Override
	public void updated(String pid, Dictionary properties)
			throws ConfigurationException {
		logger.trace("Updated pid {}", pid);		
	}

	/**
	 * An example to show how to sent device events to OSGI bus
	 * Timer task will be started to check for dummy event file every few seconds
	 * If event file is updated, a event will then be sent with dummy event data file content as payload 
	 * @param device
	 */		
	private void scanForDummyEvent(IDummyDevice device){		
		DummyEventReadTask eventReadTask = new DummyEventReadTask(device);
		eventReadTask.deleteDummyEventFiles();
		new Timer().scheduleAtFixedRate(eventReadTask, 10000, 20000);
	}
	
	@Override
	public void deleted(String pid) {
		logger.trace("Deleted pid {}", pid);		
	}
}'''
	}
}
