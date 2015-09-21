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
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates.FbServiceClassTemplate
import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.*

class FBImplGeneratorModuleTest extends GeneratorTestHelper{
	
	
	@Before
	def void set() {
	}
	
	@Test
	def testFunctionBlockWitNoOperations() {
		
		var fbmodel = FunctionblockFactory.eINSTANCE.createFunctionblockModel();
		var fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();
		
		fbmodel.setName("Lamp");
		fbmodel.setDescription("Function block model for Lamp");
		fbmodel.setCategory("demo");
		fbmodel.setNamespace("com.bosch");
		fbmodel.setVersion("1.0.0");

		fbmodel.setFunctionblock(fb);
		
		var constructed = FbServiceClassTemplate.generate(new FbModelWrapper(fbmodel))
		var expected = getExpectedClassWithNoOperations();
		assertEquals(expected, constructed);
	}
	
	
	def getExpectedClassWithNoOperations() {
		var template = '''package com.bosch.functionblock.demo.lamp.internal;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.bosch.functionblock.demo.lamp._1.Lamp;
import com.bosch.functionblock.demo.lamp._1.LampProperties;
import com.bosch.functionblock.dummy.api.device.IDummyDevice;

import com.bosch.functionblock.demo.lamp.api.AbstractDeviceService;
import com.bosch.functionblock.demo.lamp.api.mapping.EventMappingsConfiguration;
import com.bosch.ism.InformationModelConstants;

public class LampService extends AbstractDeviceService<IDummyDevice, LampProperties> implements Lamp {

	public LampService(final BundleContext context, final ServiceReference<IDummyDevice> reference) {
		super(context, reference, LampProperties.class);
	}
	
	@Override
	protected Dictionary<String, String> addInformationModelInstanceProperties(Dictionary<String, String> properties) {
		properties.put(InformationModelConstants.DESCRIPTION,"Function block model for Lamp");
		properties.put(InformationModelConstants.CATEGORY, "demo");
		properties.put(org.osgi.service.device.Constants.DEVICE_CATEGORY, "LampFB");
		return properties;
	}
		
	@Override
	protected Class<Lamp> getFunctionBlockModelClass(){
		return Lamp.class;
	}	
				
	@Override
	protected Set<String> getEventTopics() {
		Set<String> topics = new HashSet<>();
		return topics;
	}

	//Event topic sent from device driver
	private String getDriverEventTopic(String eventName) {
		return "DeviceDriver/Lamp/"  + eventName;		
	}	
	
	@Override
	protected void registerMappingRules(EventMappingsConfiguration configuration) {
	}

}'''
		return template;
	}
	
	
	@Test
	def testFunctionBlockWithRequestResponseOperation() {
		
		var fbmodel = FunctionblockFactory.eINSTANCE.createFunctionblockModel();
		var fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();
		
		fbmodel.setName("Lamp");
		fbmodel.setNamespace("com.bosch");
		fbmodel.setVersion("1.0.0");
		
		fbmodel.setDescription("Function block model for Lamp");
		fbmodel.setCategory("demo");

		
		var op = FunctionblockFactory.eINSTANCE.createOperation();
		op.setName("freezThings");
		op.setDescription("it freezes things in it.");
		var booleanType = FunctionblockFactory.eINSTANCE.createReturnPrimitiveType();
		booleanType.setReturnType(PrimitiveType.BOOLEAN);
		op.setReturnType(booleanType)
		fb.operations.add(op);

		fbmodel.setFunctionblock(fb);
		
		var constructed = FbServiceClassTemplate.generate(new FbModelWrapper(fbmodel))
		var expected = getExpectedFbClassWithRequestResponseOperation();
		assertEquals(expected, constructed);
	}
	
		@Test
	def testFunctionBlockWithEvents() {
		
		var fbmodel = FunctionblockFactory.eINSTANCE.createFunctionblockModel();
		var fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();
		
		fbmodel.setName("Lamp");
		fbmodel.setNamespace("com.bosch");
		fbmodel.setVersion("1.0.0");
		
		fbmodel.setDescription("Function block model for Lamp");
		fbmodel.setCategory("demo");
		
		var event = FunctionblockFactory.eINSTANCE.createEvent
		event.name = "BlinkingStarted";
		var prop =DatatypeFactory.eINSTANCE.createProperty();
		
		var primitiveProperty = DatatypeFactory.eINSTANCE.createPrimitivePropertyType;
		prop.name = "freezeParam";
		primitiveProperty.type =PrimitiveType.STRING;
		
		prop.type = primitiveProperty
		event.properties.add(prop);
		fb.events.add(event);
		fbmodel.setFunctionblock(fb);
		
		var constructed = FbServiceClassTemplate.generate(new FbModelWrapper(fbmodel))
		var expected = getExpectedFbClassWithEvents();
		assertEquals(expected, constructed);
	}
	
	
	def getExpectedFbClassWithEvents() {
		var template = '''package com.bosch.functionblock.demo.lamp.internal;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.bosch.functionblock.demo.lamp._1.Lamp;
import com.bosch.functionblock.demo.lamp._1.LampProperties;
import com.bosch.functionblock.demo.lamp.internal.mapping.BlinkingStartedMapping;
import com.bosch.functionblock.dummy.api.device.IDummyDevice;

import com.bosch.functionblock.demo.lamp.api.AbstractDeviceService;
import com.bosch.functionblock.demo.lamp.api.mapping.EventMappingsConfiguration;
import com.bosch.ism.InformationModelConstants;

public class LampService extends AbstractDeviceService<IDummyDevice, LampProperties> implements Lamp {

	public LampService(final BundleContext context, final ServiceReference<IDummyDevice> reference) {
		super(context, reference, LampProperties.class);
	}
	
	@Override
	protected Dictionary<String, String> addInformationModelInstanceProperties(Dictionary<String, String> properties) {
		properties.put(InformationModelConstants.DESCRIPTION,"Function block model for Lamp");
		properties.put(InformationModelConstants.CATEGORY, "demo");
		properties.put(org.osgi.service.device.Constants.DEVICE_CATEGORY, "LampFB");
		return properties;
	}
		
	@Override
	protected Class<Lamp> getFunctionBlockModelClass(){
		return Lamp.class;
	}	
				
	@Override
	protected Set<String> getEventTopics() {
		Set<String> topics = new HashSet<>();
		topics.add(getDriverEventTopic("BlinkingStarted"));
		return topics;
	}

	//Event topic sent from device driver
	private String getDriverEventTopic(String eventName) {
		return "DeviceDriver/Lamp/"  + eventName;		
	}	
	
	@Override
	protected void registerMappingRules(EventMappingsConfiguration configuration) {
		configuration.registerMapping(getDriverEventTopic("BlinkingStarted"),new BlinkingStartedMapping());
	}

}'''
		return template;
	}
	
	
	def getExpectedFbClassWithRequestResponseOperation() {
		var template = '''package com.bosch.functionblock.demo.lamp.internal;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.bosch.functionblock.demo.lamp._1.Lamp;
import com.bosch.functionblock.demo.lamp._1.FreezThings;
import com.bosch.functionblock.demo.lamp._1.FreezThingsReply;
import com.bosch.functionblock.demo.lamp._1.LampProperties;
import com.bosch.functionblock.dummy.api.device.IDummyDevice;

import com.bosch.functionblock.demo.lamp.api.AbstractDeviceService;
import com.bosch.functionblock.demo.lamp.api.mapping.EventMappingsConfiguration;
import com.bosch.ism.InformationModelConstants;

public class LampService extends AbstractDeviceService<IDummyDevice, LampProperties> implements Lamp {

	public LampService(final BundleContext context, final ServiceReference<IDummyDevice> reference) {
		super(context, reference, LampProperties.class);
	}
	
	@Override
	protected Dictionary<String, String> addInformationModelInstanceProperties(Dictionary<String, String> properties) {
		properties.put(InformationModelConstants.DESCRIPTION,"Function block model for Lamp");
		properties.put(InformationModelConstants.CATEGORY, "demo");
		properties.put(org.osgi.service.device.Constants.DEVICE_CATEGORY, "LampFB");
		return properties;
	}
		
	@Override
	protected Class<Lamp> getFunctionBlockModelClass(){
		return Lamp.class;
	}	
				
	@Override
	protected Set<String> getEventTopics() {
		Set<String> topics = new HashSet<>();
		return topics;
	}

	//Event topic sent from device driver
	private String getDriverEventTopic(String eventName) {
		return "DeviceDriver/Lamp/"  + eventName;		
	}	
	
	@Override
	protected void registerMappingRules(EventMappingsConfiguration configuration) {
	}

	/**
	 * it freezes things in it.
	 *
	 * @return FreezThingsReply
	 */
	 public FreezThingsReply freezThings() {
	 	logger.trace("method freezThings is invoked");
	 	//IInformationModelInstance infoModelInstance = getInformationModelInstance();
	 	deviceDriver.sendCommand("freezThings", null);
	 	//TODO: Please implement this method.
	 	return null;
	 }
}'''
		return template;
	}
		
	@Test
	def testFunctionBlockWithOnewayNoParamOp() {
		
		var fbmodel = FunctionblockFactory.eINSTANCE.createFunctionblockModel();
		var fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();
		
		fbmodel.setName("Lamp");
		fbmodel.setNamespace("com.bosch");
		fbmodel.setVersion("1.0.0");
		
		fbmodel.setDescription("Function block model for Lamp");
		fbmodel.setCategory("demo");
		var op = FunctionblockFactory.eINSTANCE.createOperation();
		op.setName("freezThings");
		fb.operations.add(op);

		fbmodel.setFunctionblock(fb);
		
		var constructed = FbServiceClassTemplate.generate(new FbModelWrapper(fbmodel))
		var expected = getExpectedTemplateWithOnewayWithNoParamOp();
		assertEquals(expected, constructed);
	}

def getExpectedTemplateWithOnewayWithNoParamOp() {
		var template = '''package com.bosch.functionblock.demo.lamp.internal;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.bosch.functionblock.demo.lamp._1.Lamp;
import com.bosch.functionblock.demo.lamp._1.FreezThings;
import com.bosch.functionblock.demo.lamp._1.LampProperties;
import com.bosch.functionblock.dummy.api.device.IDummyDevice;

import com.bosch.functionblock.demo.lamp.api.AbstractDeviceService;
import com.bosch.functionblock.demo.lamp.api.mapping.EventMappingsConfiguration;
import com.bosch.ism.InformationModelConstants;

public class LampService extends AbstractDeviceService<IDummyDevice, LampProperties> implements Lamp {

	public LampService(final BundleContext context, final ServiceReference<IDummyDevice> reference) {
		super(context, reference, LampProperties.class);
	}
	
	@Override
	protected Dictionary<String, String> addInformationModelInstanceProperties(Dictionary<String, String> properties) {
		properties.put(InformationModelConstants.DESCRIPTION,"Function block model for Lamp");
		properties.put(InformationModelConstants.CATEGORY, "demo");
		properties.put(org.osgi.service.device.Constants.DEVICE_CATEGORY, "LampFB");
		return properties;
	}
		
	@Override
	protected Class<Lamp> getFunctionBlockModelClass(){
		return Lamp.class;
	}	
				
	@Override
	protected Set<String> getEventTopics() {
		Set<String> topics = new HashSet<>();
		return topics;
	}

	//Event topic sent from device driver
	private String getDriverEventTopic(String eventName) {
		return "DeviceDriver/Lamp/"  + eventName;		
	}	
	
	@Override
	protected void registerMappingRules(EventMappingsConfiguration configuration) {
	}

	/**
	 * 
	 *
	 */
	 public void freezThings() {
	 	logger.trace("method freezThings is invoked");
	 	//IInformationModelInstance infoModelInstance = getInformationModelInstance();
	 	deviceDriver.sendCommand("freezThings", null);
	 	//TODO: Please implement this method.
	 }
}'''
		return template;
	}
	
	@Test
	def testFunctionBlockWithOperationHavingParams() {
		
		var fbmodel = FunctionblockFactory.eINSTANCE.createFunctionblockModel();
		var fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();
		
		fbmodel.setNamespace("com.bosch");
		fbmodel.setVersion("1.0.0");
		fbmodel.setName("Lamp");
		fbmodel.setDescription("Function block model for Lamp");
		fbmodel.setCategory("demo");
		
		var op = FunctionblockFactory.eINSTANCE.createOperation();
		op.setName("freezThings");
		op.setDescription("it freezes things in it.");
		var primitiveParam = FunctionblockFactory.eINSTANCE.createPrimitiveParam();
		primitiveParam.setName("freezeParam");
		primitiveParam.setType(PrimitiveType.STRING)
		op.params.add(primitiveParam)
		fb.operations.add(op);

		fbmodel.setFunctionblock(fb);
		
		var constructed = FbServiceClassTemplate.generate(new FbModelWrapper(fbmodel))
		var expected = getExpectedFbClassWithOnewayOperationHavingParam();
		assertEquals(expected, constructed);
	}
	
	def getExpectedFbClassWithOnewayOperationHavingParam() {
				var template = '''package com.bosch.functionblock.demo.lamp.internal;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.bosch.functionblock.demo.lamp._1.Lamp;
import com.bosch.functionblock.demo.lamp._1.FreezThings;
import com.bosch.functionblock.demo.lamp._1.LampProperties;
import com.bosch.functionblock.dummy.api.device.IDummyDevice;

import com.bosch.functionblock.demo.lamp.api.AbstractDeviceService;
import com.bosch.functionblock.demo.lamp.api.mapping.EventMappingsConfiguration;
import com.bosch.ism.InformationModelConstants;

public class LampService extends AbstractDeviceService<IDummyDevice, LampProperties> implements Lamp {

	public LampService(final BundleContext context, final ServiceReference<IDummyDevice> reference) {
		super(context, reference, LampProperties.class);
	}
	
	@Override
	protected Dictionary<String, String> addInformationModelInstanceProperties(Dictionary<String, String> properties) {
		properties.put(InformationModelConstants.DESCRIPTION,"Function block model for Lamp");
		properties.put(InformationModelConstants.CATEGORY, "demo");
		properties.put(org.osgi.service.device.Constants.DEVICE_CATEGORY, "LampFB");
		return properties;
	}
		
	@Override
	protected Class<Lamp> getFunctionBlockModelClass(){
		return Lamp.class;
	}	
				
	@Override
	protected Set<String> getEventTopics() {
		Set<String> topics = new HashSet<>();
		return topics;
	}

	//Event topic sent from device driver
	private String getDriverEventTopic(String eventName) {
		return "DeviceDriver/Lamp/"  + eventName;		
	}	
	
	@Override
	protected void registerMappingRules(EventMappingsConfiguration configuration) {
	}

	/**
	 * it freezes things in it.
	 *
	 * @param FreezThings part1
	 */
	 public void freezThings(FreezThings part1) {
	 	logger.trace("method freezThings is invoked");
	 	//IInformationModelInstance infoModelInstance = getInformationModelInstance();
	 	deviceDriver.sendCommand("freezThings", null);
	 	//TODO: Please implement this method.
	 }
}'''
		return template;
}

@Test
	def testFunctionBlockWithOtherMetadata() {
		
		var fbmodel = FunctionblockFactory.eINSTANCE.createFunctionblockModel();
		var fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();
		
		fbmodel.setName("Lamp");
		fbmodel.setDescription("Function block model for Lamp");
		fbmodel.setCategory("iot");
		fbmodel.setNamespace("com.bosch");
		fbmodel.setVersion("1.1.0");

		fbmodel.setFunctionblock(fb);
		
		var constructed = FbServiceClassTemplate.generate(new FbModelWrapper(fbmodel))
		var expected = getExpectedClassWithOtherMetadata();
		assertEquals(expected, constructed);
	}
	
	
	def getExpectedClassWithOtherMetadata() {
						var template = '''package com.bosch.functionblock.iot.lamp.internal;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.bosch.functionblock.iot.lamp._1.Lamp;
import com.bosch.functionblock.iot.lamp._1.LampProperties;
import com.bosch.functionblock.dummy.api.device.IDummyDevice;

import com.bosch.functionblock.iot.lamp.api.AbstractDeviceService;
import com.bosch.functionblock.iot.lamp.api.mapping.EventMappingsConfiguration;
import com.bosch.ism.InformationModelConstants;

public class LampService extends AbstractDeviceService<IDummyDevice, LampProperties> implements Lamp {

	public LampService(final BundleContext context, final ServiceReference<IDummyDevice> reference) {
		super(context, reference, LampProperties.class);
	}
	
	@Override
	protected Dictionary<String, String> addInformationModelInstanceProperties(Dictionary<String, String> properties) {
		properties.put(InformationModelConstants.DESCRIPTION,"Function block model for Lamp");
		properties.put(InformationModelConstants.CATEGORY, "iot");
		properties.put(org.osgi.service.device.Constants.DEVICE_CATEGORY, "LampFB");
		return properties;
	}
		
	@Override
	protected Class<Lamp> getFunctionBlockModelClass(){
		return Lamp.class;
	}	
				
	@Override
	protected Set<String> getEventTopics() {
		Set<String> topics = new HashSet<>();
		return topics;
	}

	//Event topic sent from device driver
	private String getDriverEventTopic(String eventName) {
		return "DeviceDriver/Lamp/"  + eventName;		
	}	
	
	@Override
	protected void registerMappingRules(EventMappingsConfiguration configuration) {
	}

}'''
		return template;
	}
}
