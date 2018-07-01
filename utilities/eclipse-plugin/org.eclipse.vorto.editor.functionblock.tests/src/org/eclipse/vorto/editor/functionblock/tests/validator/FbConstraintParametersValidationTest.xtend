/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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
 package org.eclipse.vorto.editor.functionblock.tests.validator

import org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType
import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory
import org.eclipse.vorto.editor.datatype.validation.DatatypeSystemMessage
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup
import org.eclipse.vorto.editor.functionblock.validation.FunctionblockValidator
import org.eclipse.xtext.junit4.AbstractXtextTests
import org.eclipse.xtext.junit4.validation.ValidatorTester
import org.junit.Test

class FbConstraintParametersValidationTest extends AbstractXtextTests {
		
	private ValidatorTester<FunctionblockValidator> tester;
	
	def override void setUp() throws Exception {
		super.setUp();
		with(FunctionblockStandaloneSetup);
		var validator = get(FunctionblockValidator);
		tester = new ValidatorTester<FunctionblockValidator>(validator, getInjector());
	}
	
	
	@Test
	def test_Operation_NoParameter_Constraint(){
		val operation = FunctionblockFactory.eINSTANCE.createOperation
		operation.name = "on"
		operation.description = "Turn on switch"		
		tester.validator().checkParametersConstraint(operation)
		tester.diagnose().assertOK();
	}
	
	@Test
	def test_Operation_String_ReturnType_Valid_Constraint(){
		val operation = FunctionblockFactory.eINSTANCE.createOperation
		operation.name = "off"
		operation.description = "Turn off switch"
		
		val returnPrimitiveType = FunctionblockFactory.eINSTANCE.createReturnPrimitiveType
		returnPrimitiveType.returnType = PrimitiveType.STRING
		val constraintRule = DatatypeFactory.eINSTANCE.createConstraintRule
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint
		
		constraint1.setType(ConstraintIntervalType.STRLEN)
		constraint1.constraintValues = '20'
		
		constraintRule.constraints.add(constraint1)
		
		returnPrimitiveType.constraintRule = constraintRule
		
		operation.returnType = returnPrimitiveType
		tester.validator().checkReturnTypeConstraint(operation)
		tester.diagnose().assertOK();
	}
				
	@Test
	def test_Operation_String_ReturnType_Invalid_Constraint(){
		val operation = FunctionblockFactory.eINSTANCE.createOperation
		operation.name = "off"
		operation.description = "Turn off switch"
		
		val returnPrimitiveType = FunctionblockFactory.eINSTANCE.createReturnPrimitiveType
		returnPrimitiveType.returnType = PrimitiveType.INT
		val constraintRule = DatatypeFactory.eINSTANCE.createConstraintRule
		
		var invalidTypeConstraint = DatatypeFactory.eINSTANCE.createConstraint
		
		invalidTypeConstraint.setType(ConstraintIntervalType.STRLEN)
		invalidTypeConstraint.constraintValues = '20'
		
		constraintRule.constraints.add(invalidTypeConstraint)
		
		returnPrimitiveType.constraintRule = constraintRule
		
		operation.returnType = returnPrimitiveType
				
		tester.validator().checkReturnTypeConstraint(operation)
		val msg = 'Constraint cannot apply on this property\'s datatype'
		tester.diagnose().assertErrorContains(msg)
	}
	
	@Test
	def test_Operation_Int_ReturnType_Valid_Constraint(){
		val operation = FunctionblockFactory.eINSTANCE.createOperation
		operation.name = "off"
		operation.description = "Turn off switch"
		
		val returnPrimitiveType = FunctionblockFactory.eINSTANCE.createReturnPrimitiveType
		returnPrimitiveType.returnType = PrimitiveType.INT
		val constraintRule = DatatypeFactory.eINSTANCE.createConstraintRule
		
		var minConstraint = DatatypeFactory.eINSTANCE.createConstraint
		
		minConstraint.setType(ConstraintIntervalType.MIN)
		minConstraint.constraintValues = '20'
		
		var maxConstraint = DatatypeFactory.eINSTANCE.createConstraint
		maxConstraint.type = ConstraintIntervalType.MAX
		maxConstraint.constraintValues = '40'
		
		constraintRule.constraints.add(minConstraint)
		constraintRule.constraints.add(maxConstraint)
		
		returnPrimitiveType.constraintRule = constraintRule
		
		operation.returnType = returnPrimitiveType
		tester.validator().checkReturnTypeConstraint(operation)
		tester.diagnose().assertOK();
	}
				
	@Test
	def test_Operation_Int_ReturnType_Invalid_Constraint(){
		val operation = FunctionblockFactory.eINSTANCE.createOperation
		operation.name = "off"
		operation.description = "Turn off switch"
		
		val returnPrimitiveType = FunctionblockFactory.eINSTANCE.createReturnPrimitiveType
		returnPrimitiveType.returnType = PrimitiveType.STRING
		val constraintRule = DatatypeFactory.eINSTANCE.createConstraintRule
		
		var minConstraint = DatatypeFactory.eINSTANCE.createConstraint
		
		minConstraint.setType(ConstraintIntervalType.MIN)
		minConstraint.constraintValues = '20'
		
		constraintRule.constraints.add(minConstraint)
		
		returnPrimitiveType.constraintRule = constraintRule
		
		operation.returnType = returnPrimitiveType
				
		tester.validator().checkReturnTypeConstraint(operation)
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID)
	}
	
	@Test
	def test_Operation_Mime_ReturnType_Valid_Constraint(){
		val operation = FunctionblockFactory.eINSTANCE.createOperation
		operation.name = "off"
		operation.description = "Turn off switch"
		
		
		val returnPrimitiveType = FunctionblockFactory.eINSTANCE.createReturnPrimitiveType
		returnPrimitiveType.returnType = PrimitiveType.BYTE
		returnPrimitiveType.multiplicity = true;
		val constraintRule = DatatypeFactory.eINSTANCE.createConstraintRule
		
		var mimeConstraint = DatatypeFactory.eINSTANCE.createConstraint
		
		mimeConstraint.setType(ConstraintIntervalType.MIMETYPE)
		mimeConstraint.constraintValues = 'gif'
		
		constraintRule.constraints.add(mimeConstraint)
		
		returnPrimitiveType.constraintRule = constraintRule
		
		operation.returnType = returnPrimitiveType
		tester.validator().checkReturnTypeConstraint(operation)
		tester.diagnose().assertOK();
	}
				
	@Test
	def test_Operation_Mime_ReturnType_Invalid_Constraint(){
		val operation = FunctionblockFactory.eINSTANCE.createOperation
		operation.name = "off"
		operation.description = "Turn off switch"
		
		val returnPrimitiveType = FunctionblockFactory.eINSTANCE.createReturnPrimitiveType
		returnPrimitiveType.returnType = PrimitiveType.BYTE
		returnPrimitiveType.multiplicity = false;
		val constraintRule = DatatypeFactory.eINSTANCE.createConstraintRule
		
		var mimeConstraint = DatatypeFactory.eINSTANCE.createConstraint
		
		mimeConstraint.setType(ConstraintIntervalType.MIMETYPE)
		mimeConstraint.constraintValues = 'gif'
		
		constraintRule.constraints.add(mimeConstraint)
		
		returnPrimitiveType.constraintRule = constraintRule
		
		operation.returnType = returnPrimitiveType
				
		tester.validator().checkReturnTypeConstraint(operation)
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_MIMETYPE_FOR_BYTE)
	}
	
	@Test
	def test_Operation_Int_Parameters_Valid_Constraint(){
		val operation = FunctionblockFactory.eINSTANCE.createOperation
		operation.name = "off"
		operation.description = "Turn off switch"
		
		val param1 = FunctionblockFactory.eINSTANCE.createPrimitiveParam
		param1.type = PrimitiveType.INT
		operation.params.add(param1)
		
		val constraintRule = DatatypeFactory.eINSTANCE.createConstraintRule
		
		var maxConstraint = DatatypeFactory.eINSTANCE.createConstraint
		
		maxConstraint.setType(ConstraintIntervalType.MAX)
		maxConstraint.constraintValues = '20'
		
		constraintRule.constraints.add(maxConstraint)
		
		param1.constraintRule = constraintRule
		
		tester.validator().checkParametersConstraint(operation)
		tester.diagnose().assertOK();
	}
				
	@Test
	def test_Operation_Int_Parameters_InValid_Constraint(){
		val operation = FunctionblockFactory.eINSTANCE.createOperation
		operation.name = "off"
		operation.description = "Turn off switch"
		
		val param1 = FunctionblockFactory.eINSTANCE.createPrimitiveParam
		param1.type = PrimitiveType.STRING
		operation.params.add(param1)
		
		val constraintRule = DatatypeFactory.eINSTANCE.createConstraintRule
		
		var maxConstraint = DatatypeFactory.eINSTANCE.createConstraint
		
		maxConstraint.setType(ConstraintIntervalType.MAX)
		maxConstraint.constraintValues = '20'
		
		constraintRule.constraints.add(maxConstraint)
		
		param1.constraintRule = constraintRule
				
		tester.validator().checkParametersConstraint(operation)
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID)
	}
				
}