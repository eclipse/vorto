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
 package org.eclipse.vorto.codegen.examples.tests

import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.functionblock.Configuration
import org.eclipse.vorto.core.api.model.functionblock.Fault
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.Status

class TestFunctionblockModelFactory {

	public static def FunctionblockModel createFBmodelWithProperties() {
		var fbmodel = FunctionblockFactory.eINSTANCE.createFunctionblockModel();
		var fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();

		fbmodel.setName("Fridge");
		fb.setCategory("demo");
		fb.setDescription("A Simple Fridge Functionblock for tester");
		fbmodel.setNamespace("www.bosch.com");
		fbmodel.setVersion("1.2.3");
		fb.displayname = "Fridge Function Block"
		fb.getOperations().add(createOnOperation());
		fb.getOperations().add(createOffOperation());
		fb.getOperations().add(createToggleOperation());
		fb.setConfiguration(createConfiguration);
		fb.setStatus(createStatus);
		fb.setFault(createFault);
		fbmodel.setFunctionblock(fb);

		return fbmodel;
	}
	
	public static def FunctionblockModel createFBmodel() {
		var fbmodel = FunctionblockFactory.eINSTANCE.createFunctionblockModel();
		var fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();

		fbmodel.setName("Lamp");
		fb.setDescription("Lighting device");
		fb.setCategory("demo");
		fb.setDescription("comment");
		fbmodel.setNamespace("www.mynamespace.com");
		fbmodel.setVersion("1.2.3");
		fb.getOperations().add(createOnOperation());
		fb.getOperations().add(createOffOperation());
		fb.getOperations().add(createToggleOperation());

		fbmodel.setFunctionblock(fb);

		return fbmodel;
	}

	private static def Operation createOnOperation() {
		var operation = FunctionblockFactory.eINSTANCE.createOperation();
		operation.setName("on");
		operation.setDescription("Turn device on");
		return operation;
	}

	private static def Operation createOffOperation() {
		var operation = FunctionblockFactory.eINSTANCE.createOperation();
		operation.setName("Off");
		operation.setDescription("Turn device off");
		return operation;
	}

	private static def Operation createToggleOperation() {
		var operation = FunctionblockFactory.eINSTANCE.createOperation();
		operation.setName("Toggle");
		operation.setDescription("Toggle device");
		return operation;
	}

	private static def Configuration createConfiguration() {
		var configuration = FunctionblockFactory.eINSTANCE.createConfiguration();
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
