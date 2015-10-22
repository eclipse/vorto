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

import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType
import org.junit.Before

public class GeneratorTestHelper {
	private FunctionblockModel model;

	private Operation operation1
	private Operation operation2
	private Operation operation3
	private ReturnPrimitiveType primitiveType;
	private ReturnObjectType returnObjectType;
	
	Entity oType;
	
	
	
	@Before
	def void setUp () {
		model = populateFBmodel();
	}	
	
	private def FunctionblockModel populateFBmodel(){
		var fbmodel = FunctionblockFactory.eINSTANCE.createFunctionblockModel();
		var fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();
		
		fbmodel.setName("Fridge");
		fbmodel.setDescription("Refrigerator");
		fbmodel.setCategory("demo");
		fbmodel.setDescription("comment");
		fbmodel.setNamespace("www.bosch.com");
		fbmodel.setVersion("1.0.0");

		operation1 = createMethod("freezThings", "it freezes things in it.");
		operation1.params.add(createParam("temperature", true));
		primitiveType = FunctionblockFactory.eINSTANCE.createReturnPrimitiveType();
		primitiveType.setReturnType(PrimitiveType.BOOLEAN);
		primitiveType.setMultiplicity(true);			
		operation1.setReturnType(primitiveType);
		fb.operations.add(operation1);
		
		operation2 = createMethod("defrosts", "it defrosts freezer.");
		operation2.params.add(createParam("time", true));
		
		operation3 = createMethod("lowerTemp", "it lowers the temperature.");
		operation3.params.add(createParam("temperature", true));
		primitiveType = FunctionblockFactory.eINSTANCE.createReturnPrimitiveType();
		primitiveType.setReturnType(PrimitiveType.BOOLEAN);
		operation3.setReturnType(primitiveType);
		fb.operations.add(operation3);
		
		returnObjectType = FunctionblockFactory.eINSTANCE.createReturnObjectType();
		oType = DatatypeFactory.eINSTANCE.createEntity()
		oType.setName("Random");	
		returnObjectType.setReturnType(oType);
		returnObjectType.setMultiplicity(true);
		operation2.setReturnType(returnObjectType);
		
		fb.operations.add(operation2);

		fbmodel.setFunctionblock(fb);
		
		return fbmodel;
	}
	
	protected def Operation createMethod(String name, String description) {
		var operation = FunctionblockFactory.eINSTANCE.createOperation();
		operation.setName(name);
		operation.setDescription(description);
		return operation
	}
	
	protected def createParam(String name, boolean multi) {
		var param = FunctionblockFactory.eINSTANCE.createParam();
		param.setMultiplicity(multi);
		param.setName(name);
		param
	}
}
