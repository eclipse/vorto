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
package org.eclipse.vorto.editor.functionblock.tests.validator

import org.eclipse.vorto.editor.functionblock.validation.SystemMessage
import org.junit.Test

class FBOperationCrossReferenceTest extends CrossReferenceTestTemplate{
	
	@Test
	def test_OperationName_Same_EnumName_FromTypeFile() {
		var fbmodel = getFBFactoryInstance().createFunctionblockModel();
		var fb = getFBFactoryInstance().createFunctionBlock();
		var op1=getFBFactoryInstance().createOperation();
		
		op1.name = "bCD"
		
		fb.operations.add(op1)
		fbmodel.setFunctionblock(fb);
		
		tester.validator().checkOpNameAgainstEntityName(fbmodel)
		tester.diagnose().assertErrorContains(SystemMessage.ERROR_OPERATION_SAME_NAME_AS_TYPE);
	}

	@Test
	def test_OperationName_Same_EntityName_FromFbFile() {
		var fbmodel = getFBFactoryInstance().createFunctionblockModel();
		var fb = getFBFactoryInstance().createFunctionBlock();
		var op1=getFBFactoryInstance().createOperation();
		var ent = getDTFactoryInstance().createEntity()
		
		op1.name = "transAction"
		ent.name ="TransAction"
		
		fb.operations.add(op1)
		fbmodel.entities.add(ent)
		fbmodel.setFunctionblock(fb);
		
		tester.validator().checkOpNameAgainstEntityName(fbmodel)
		tester.diagnose().assertErrorContains(SystemMessage.ERROR_OPERATION_SAME_NAME_AS_TYPE);
	}
	
	@Test
	def test_OperationName_Same_EnumName_FromFbFile() {
		var fbmodel = getFBFactoryInstance().createFunctionblockModel();
		var fb = getFBFactoryInstance().createFunctionBlock();
		var op1=getFBFactoryInstance().createOperation();
		var ent = getDTFactoryInstance().createEnum()
		
		op1.name = "transAction"
		ent.name ="TransAction"
		
		fb.operations.add(op1)
		fbmodel.enums.add(ent)
		fbmodel.setFunctionblock(fb);
		
		tester.validator().checkOpNameAgainstEntityName(fbmodel)
		tester.diagnose().assertErrorContains(SystemMessage.ERROR_OPERATION_SAME_NAME_AS_TYPE);
	}
	
	@Test
	def test_OperationName_Diff_EntityEnumName() {
		var fbmodel = getFBFactoryInstance().createFunctionblockModel();
		var fb = getFBFactoryInstance().createFunctionBlock();
		var op1=getFBFactoryInstance().createOperation();
		var enu = getDTFactoryInstance().createEnum()
		var ent = getDTFactoryInstance().createEntity()
		
		op1.name = "transActionz"
		enu.name ="TransAction"
		ent.name = "TransActionZZ"
		
		fb.operations.add(op1)
		fbmodel.enums.add(enu)
		fbmodel.entities.add(ent)
		fbmodel.setFunctionblock(fb);
		
		tester.validator().checkOpNameAgainstEntityName(fbmodel)
		tester.diagnose().assertOK
	}
	
	@Test
	def test_OperationName_Same_EntityName_Multiple_Op() {
		var fbmodel = getFBFactoryInstance().createFunctionblockModel();
		var fb = getFBFactoryInstance().createFunctionBlock();
		var op1=getFBFactoryInstance().createOperation();
		var op2=getFBFactoryInstance().createOperation();
		var ent = getDTFactoryInstance().createEntity()
		var ent1 = getDTFactoryInstance().createEntity()
		
		op1.name = "transAction"
		op2.name = "action"
		
		ent.setName("TransAction")
		ent1.setName("Trans");
		
		fb.operations.add(op1)
		fb.operations.add(op2)
		fbmodel.entities.add(ent)
		fbmodel.entities.add(ent1)
		fbmodel.setFunctionblock(fb);
		
		tester.validator().checkOpNameAgainstEntityName(fbmodel) 
		tester.diagnose().assertErrorContains(SystemMessage.ERROR_OPERATION_SAME_NAME_AS_TYPE);
	}
	
	@Test
	def test_OperationName_Same_EntityNameFromTypeFile_Multiple_Op() {
		var fbmodel = getFBFactoryInstance().createFunctionblockModel();
		var fb = getFBFactoryInstance().createFunctionBlock();
		var op1=getFBFactoryInstance().createOperation();
		var op2=getFBFactoryInstance().createOperation();
		
		op1.name = "abc"
		op2.name = "abc"
		
		fb.operations.add(op1)
		fb.operations.add(op2)
		fbmodel.setFunctionblock(fb);
		
		tester.validator().checkOpNameAgainstEntityName(fbmodel) 
		var diags = tester.diagnose().diagnostic.children;

		assertEquals(2,diags.size)
		assertEquals(SystemMessage.ERROR_OPERATION_SAME_NAME_AS_TYPE, diags.get(1).message)
		assertEquals(SystemMessage.ERROR_OPERATION_SAME_NAME_AS_TYPE, diags.get(0).message)
	}
	
}