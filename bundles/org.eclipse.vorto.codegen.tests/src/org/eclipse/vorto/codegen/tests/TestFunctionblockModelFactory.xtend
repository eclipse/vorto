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
package org.eclipse.vorto.codegen.tests

import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.functionblock.Configuration
import org.eclipse.vorto.core.api.model.functionblock.Event
import org.eclipse.vorto.core.api.model.functionblock.Fault
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.Status

class TestFunctionblockModelFactory {

	public static def FunctionblockModel populateFBmodelWithProperties() {
		var fbmodel = FunctionblockFactory.eINSTANCE.createFunctionblockModel();
		var fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();

		fbmodel.setName("Lamp");
		fb.setDescription("Light with color");
		fb.setCategory("demo");
		fb.setDescription("comment");
		fbmodel.setNamespace("www.bosch.com");
		fbmodel.setVersion("1.2.3");
		fb.getOperations().add(createSetROperation());
		fb.getOperations().add(createSetGOperation());
		fb.getOperations().add(createSetBOperation());
		fb.setConfiguration(createConfiguration);
		fb.events.add(createEvent());
		fb.setStatus(createStatus);
		fb.setFault(createFault);
		fbmodel.setFunctionblock(fb);

		return fbmodel;
	}
	
	public static def FunctionblockModel populateFBmodel() {
		var fbmodel = FunctionblockFactory.eINSTANCE.createFunctionblockModel();
		var fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();

		fbmodel.setName("Lamp");
		fb.setDescription("Light with color");
		fb.setCategory("demo");
		fb.setDescription("comment");
		fbmodel.setNamespace("www.bosch.com");
		fbmodel.setVersion("1.2.3");
		fb.getOperations().add(createSetROperation());
		fb.getOperations().add(createSetGOperation());
		fb.getOperations().add(createSetBOperation());

		fbmodel.setFunctionblock(fb);

		return fbmodel;
	}

	private static def Operation createSetROperation() {
		var operation = FunctionblockFactory.eINSTANCE.createOperation();
		operation.setName("setR");
		operation.setDescription("Set color R value");
		return operation;
	}

	private static def Operation createSetGOperation() {
		var operation = FunctionblockFactory.eINSTANCE.createOperation();
		operation.setName("setG");
		operation.setDescription("Set color G value");
		return operation;
	}

	private static def Operation createSetBOperation() {
		var operation = FunctionblockFactory.eINSTANCE.createOperation();
		operation.setName("setB");
		operation.setDescription("Set color B value");
		return operation;
	}

	private static def Configuration createConfiguration() {
		var configuration = FunctionblockFactory.eINSTANCE.createConfiguration()
		configuration.properties.add(createPrimitiveProperty("testString", PrimitiveType.STRING));
		configuration.properties.add(createPrimitiveProperty("testShort", PrimitiveType.SHORT));
		configuration.properties.add(createPrimitiveProperty("testInt", PrimitiveType.INT));

		return configuration;
	}

	private static def Status createStatus() {
		var status = FunctionblockFactory.eINSTANCE.createStatus();
		status.properties.add(createPrimitiveProperty("testString", PrimitiveType.STRING));
		status.properties.add(createPrimitiveProperty("testShort", PrimitiveType.SHORT));
		status.properties.add(createPrimitiveProperty("testInt", PrimitiveType.INT));
		status.properties.add(createPrimitiveProperty("testLong", PrimitiveType.LONG));
		status.properties.add(createPrimitiveProperty("testFloat", PrimitiveType.FLOAT));
		status.properties.add(createPrimitiveProperty("testDouble", PrimitiveType.DOUBLE));
		status.properties.add(createPrimitiveProperty("testDatetime", PrimitiveType.DATETIME));
		status.properties.add(createPrimitiveProperty("testByte", PrimitiveType.BYTE));
		status.properties.add(createPrimitiveProperty("testBase64", PrimitiveType.BASE64_BINARY));
		status.properties.add(createPrimitiveProperty("testBoolean", PrimitiveType.BOOLEAN));
		return status;
	}
	
	private static def Event createEvent(){
		var event = FunctionblockFactory.eINSTANCE.createEvent();
		event.name = "dummyEventName";
		event.properties.add(createPrimitiveProperty("testString", PrimitiveType.STRING));
		event.properties.add(createPrimitiveProperty("testShort", PrimitiveType.SHORT));
		event.properties.add(createPrimitiveProperty("testInt", PrimitiveType.INT));
		event.properties.add(createPrimitiveProperty("testLong", PrimitiveType.LONG));
		event.properties.add(createPrimitiveProperty("testFloat", PrimitiveType.FLOAT));
		event.properties.add(createPrimitiveProperty("testDouble", PrimitiveType.DOUBLE));
		event.properties.add(createPrimitiveProperty("testDatetime", PrimitiveType.DATETIME));
		event.properties.add(createPrimitiveProperty("testByte", PrimitiveType.BYTE));
		event.properties.add(createPrimitiveProperty("testBase64", PrimitiveType.BASE64_BINARY));
		event.properties.add(createPrimitiveProperty("testBoolean", PrimitiveType.BOOLEAN));
		return event;		
	}
	
	private static def Fault createFault() {
		var fault = FunctionblockFactory.eINSTANCE.createFault();
		fault.properties.add(createPrimitiveProperty("isFault", PrimitiveType.BOOLEAN));
		return fault;
	}

	private static def org.eclipse.vorto.core.api.model.datatype.Property createPrimitiveProperty(String propName, PrimitiveType type) {
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		prop.setName(propName);
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.type = type
		prop.setType(primi);
		return prop;
	}

}
