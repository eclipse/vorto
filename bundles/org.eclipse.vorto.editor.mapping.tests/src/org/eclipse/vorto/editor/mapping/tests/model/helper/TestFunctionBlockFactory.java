/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.editor.mapping.tests.model.helper;

import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.Fault;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Status;

public class TestFunctionBlockFactory {
	public static FunctionblockModel createFunctionBlockModel() {
		FunctionblockModel fbmodel = FunctionblockFactory.eINSTANCE.createFunctionblockModel();
		FunctionBlock fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();

		fbmodel.setName("Fridge");
		fb.setCategory("demo");
		fb.setDescription("A Simple Fridge Functionblock for tester");
		fbmodel.setNamespace("www.bosch.com");
		fbmodel.setVersion("1.2.3");
		fb.setDisplayname("Fridge Function Block");
		fb.getOperations().add(createOnOperation());
		fb.getOperations().add(createOffOperation());
		fb.getOperations().add(createToggleOperation());
		fb.setConfiguration(createConfiguration());
		fb.setStatus(createStatus());
		fb.setFault(createFault());
		fbmodel.setFunctionblock(fb);

		return fbmodel;
	}
		

	public static  Operation createOnOperation() {
		Operation operation = FunctionblockFactory.eINSTANCE.createOperation();
		operation.setName("on");
		operation.setDescription("Turn device on");
		return operation;
	}

	private static Operation createOffOperation() {
		Operation operation = FunctionblockFactory.eINSTANCE.createOperation();
		operation.setName("Off");
		operation.setDescription("Turn device off");
		return operation;
	}

	private static Operation createToggleOperation() {
		Operation operation = FunctionblockFactory.eINSTANCE.createOperation();
		operation.setName("Toggle");
		operation.setDescription("Toggle device");
		return operation;
	}

	private static  Configuration createConfiguration() {
		Configuration configuration = FunctionblockFactory.eINSTANCE.createConfiguration();
		configuration.getProperties().add(createPrimitiveProperty("configStringParam", PrimitiveType.STRING));
		return configuration;
	}

	private static Status createStatus() {
		Status status = FunctionblockFactory.eINSTANCE.createStatus();
		status.getProperties().add(createPrimitiveProperty("statusStringParam", PrimitiveType.STRING));
		return status;
	}

	private static Fault createFault() {
		Fault fault = FunctionblockFactory.eINSTANCE.createFault();
		fault.getProperties().add(createPrimitiveProperty("faultStringParam", PrimitiveType.BOOLEAN));
		return fault;
	}

	private static  org.eclipse.vorto.core.api.model.datatype.Property createPrimitiveProperty(String propName, PrimitiveType type) {
		org.eclipse.vorto.core.api.model.datatype.Property prop = DatatypeFactory.eINSTANCE.createProperty();
		prop.setName(propName);
		PrimitivePropertyType primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(type);
		prop.setType(primi);
		return prop;
	}	
}
