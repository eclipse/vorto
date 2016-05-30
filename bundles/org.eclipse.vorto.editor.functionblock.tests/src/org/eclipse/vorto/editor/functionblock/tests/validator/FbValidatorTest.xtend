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

import org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType
import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory
import org.eclipse.vorto.editor.datatype.validation.DatatypeSystemMessage
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup
import org.eclipse.vorto.editor.functionblock.validation.FunctionblockValidator
import org.eclipse.vorto.editor.functionblock.validation.SystemMessage
import org.eclipse.xtext.junit4.AbstractXtextTests
import org.eclipse.xtext.junit4.validation.ValidatorTester
import org.junit.After
import org.junit.Test

class FbValidatorTest extends AbstractXtextTests {
	
	private ValidatorTester<FunctionblockValidator> tester;

	def override void setUp() throws Exception {
		super.setUp();
		with(FunctionblockStandaloneSetup);
		var validator = get(FunctionblockValidator);
		tester = new ValidatorTester<FunctionblockValidator>(validator, getInjector());		
	}
	
	/**
	 * Need to overwrite tearDown method to avoid emf classes get de-registered causing inconsistency between test methods
	 * as static variable in *PackageImpl already set to true and it's not re-inited again
	 */
    @After
	def override void tearDown() throws Exception {
		
	}
	
	@Test
	def test_EntityName_CamelCase() {
		var entity = getDTFactoryInstance().createEntity();
		entity.setName("abc");
		
		tester.validator().checkEntityName(entity);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_ENTITYNAME_INVALID_CAMELCASE);
	}
	
	@Test
	def test_EntityName_SuffixReply() {
		var entity = getDTFactoryInstance().createEntity();
		entity.setName("AbcReply");
		
		tester.validator().checkEntityName(entity);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_ENTITYNAME_SUFFIX_REPLY);
	}
	
	@Test
	def test_EntityName_SuffixReply_JustReply() {
		var entity = getDTFactoryInstance().createEntity();
		entity.setName("Reply");
		
		tester.validator().checkEntityName(entity);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_ENTITYNAME_SUFFIX_REPLY);
	}
	
	@Test
	def test_FBName() {
		var fbmodel = getFBFactoryInstance().createFunctionblockModel();
					
		fbmodel.setName("fame")
		
		tester.validator().checkFunctionBlockName( fbmodel);
		tester.diagnose().assertErrorContains(SystemMessage.ERROR_FBNAME_INVALID);
	}
	
	@Test
	def test_Property_Name_SuffixTS(){
		var property = getDTFactoryInstance().createProperty();
		property.name = "sodaTS"
		
		tester.validator().checkPropertyName(property)
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_PROPNAME_SUFFIX_TS);
	}
	
	@Test
	def test_Property_Name_Suffixts(){
		var property = getDTFactoryInstance().createProperty();
		property.name = "products"
		
		tester.validator().checkPropertyName(property)
		tester.diagnose().assertOK()
	}
	
	@Test
	def test_FuncBlock_Version_Valid(){
		var fb = getFBFactoryInstance().createFunctionblockModel
		fb.version = "1.1.1-RC"
		
		tester.validator().checkVersionPattern(fb)
		tester.diagnose().assertOK
		
		var fb1 = getFBFactoryInstance().createFunctionblockModel
		fb1.version = "1.1.1"
		
		tester.validator().checkVersionPattern(fb1)
		tester.diagnose().assertOK
		
		var fb2 = getFBFactoryInstance().createFunctionblockModel
		fb2.version = "11.11.11"
		
		tester.validator().checkVersionPattern(fb2)
		tester.diagnose().assertOK
	}
	
	@Test
	def test_FuncBlock_Version_Invalid(){
		var fb = getFBFactoryInstance().createFunctionblockModel
		fb.version = "1.1"
		
		tester.validator().checkVersionPattern(fb)
		tester.diagnose().assertErrorContains(SystemMessage.ERROR_VERSION_PATTERN);
		
		var fb1 = getFBFactoryInstance().createFunctionblockModel
		fb1.version = "1."
		
		tester.validator().checkVersionPattern(fb1)
		tester.diagnose().assertErrorContains(SystemMessage.ERROR_VERSION_PATTERN);
		
		var fb2 = getFBFactoryInstance().createFunctionblockModel
		fb2.version = "1"
		
		tester.validator().checkVersionPattern(fb2)
		tester.diagnose().assertErrorContains(SystemMessage.ERROR_VERSION_PATTERN);
		
		var fb3 = getFBFactoryInstance().createFunctionblockModel
		fb3.version = "1.1-RC"
		
		tester.validator().checkVersionPattern(fb3)
		tester.diagnose().assertErrorContains(SystemMessage.ERROR_VERSION_PATTERN);
		
		var fb4 = getFBFactoryInstance().createFunctionblockModel
		fb4.version = "1-RC"
		
		tester.validator().checkVersionPattern(fb4)
		tester.diagnose().assertErrorContains(SystemMessage.ERROR_VERSION_PATTERN);
	}
	
	@Test
	def test_Enum_Empty_Literal() {
		
		var enum1 = getDTFactoryInstance().createEnum();
		enum1.setName("Abc");
		
		tester.validator().checkEnumName_Literal(enum1);
		tester.diagnose().assertOK;
		
		var lit = getDTFactoryInstance().createEnumLiteral;
		lit.name = "test"
		
		enum1.enums.add(lit)
		
		tester.validator().checkEnumName_Literal(enum1);
		tester.diagnose().assertOK
		
	}
	
	@Test
	def test_Enum_Name() {
		
		var enum1 = getDTFactoryInstance().createEnum();
		enum1.setName("abc");
		
		var literal = getDTFactoryInstance().createEnumLiteral()
		literal.name = "e"
		
		enum1.enums.add(literal)
		
		tester.validator().checkEnumName_Literal(enum1);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_ENUMNAME_INVALID_CAMELCASE);
		
		var enum2 = getDTFactoryInstance().createEnum();
		enum2.setName("Abc");
		
		enum2.enums.add(literal)
		
		tester.validator().checkEnumName_Literal(enum2);
		tester.diagnose().assertOK
	}
	
	@Test
	def test_Duplicated_OperationName() {
		var fbmodel = getFBFactoryInstance().createFunctionBlock();
		
		var op1=getFBFactoryInstance().createOperation();
		op1.name = "on";
		
		var op2=getFBFactoryInstance().createOperation();
		op2.name = "on";
		
		fbmodel.operations.add(op1);
		fbmodel.operations.add(op2);
		
		tester.validator().checkDuplicateOperation(op1);
		tester.diagnose().assertErrorContains(SystemMessage.ERROR_DUPLICATED_METHOD_NAME);
	}
	
	@Test
	def test_Duplicated_ParameterName() {
		
		var operation = getFBFactoryInstance().createOperation();
		
		var param1= getFBFactoryInstance().createPrimitiveParam()
		param1.name = "ABB";
		
		var param2= getFBFactoryInstance().createPrimitiveParam();
		param2.name = "ABB";

		operation.getParams().add(param1);
		operation.getParams().add(param2);
		
		tester.validator().checkDuplicateParameter(operation);
		tester.diagnose().assertErrorContains(SystemMessage.ERROR_DUPLICATED_PARAMETER_NAME);
	}
	
	@Test
	def test_Duplicated_Literal() {
		var enum1 = getDTFactoryInstance().createEnum();
		enum1.setName("Abc");
		
		var literal1 = getDTFactoryInstance().createEnumLiteral()
		var literal2 = getDTFactoryInstance().createEnumLiteral()
		
		literal1.name = "green"
		literal2.name = "green"
		
		enum1.enums.add(literal1)
		enum1.enums.add(literal2)
		
		tester.validator().checkDuplicatedLiteral(enum1)
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_DUPLICATED_ENUM_LITERAL);
	}

	@Test
	def test_Valid_Namespace() {
		var sampleFunctionBlock = getFBFactoryInstance().createFunctionblockModel
		sampleFunctionBlock.namespace = "com.bosch.demo"
		
		tester.validator().checkNamespacePattern(sampleFunctionBlock)
		tester.diagnose().assertOK
	}

	@Test
	def test_Valid_Namespace_WithNumbers() {
		var sampleFunctionBlock = getFBFactoryInstance().createFunctionblockModel
		sampleFunctionBlock.namespace = "com.bosch.demo.cloud9"
		
		tester.validator().checkNamespacePattern(sampleFunctionBlock)
		tester.diagnose().assertOK
	}

	@Test
	def test_Invalid_Namespace_WithNumbers() {
		var sampleFunctionBlock = getFBFactoryInstance().createFunctionblockModel
		sampleFunctionBlock.namespace = "com.bosch.demo.#$#$"
		
		tester.validator().checkNamespacePattern(sampleFunctionBlock)
		tester.diagnose().assertErrorContains(SystemMessage.ERROR_NAMESPACE_PATTERN)
	}
	
	@Test
	def test_Duplicated_Constraint() {
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.STRING);		

		prop.type = primi	
				
		var constraint1 = getDTFactoryInstance().createConstraint();
		constraint1.type = ConstraintIntervalType.STRLEN
		prop.constraints.add(constraint1)
		
		var constraint2 = getDTFactoryInstance().createConstraint();
		constraint2.type = ConstraintIntervalType.STRLEN
		prop.constraints.add(constraint2)
		
		tester.validator().checkDuplicatedConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_DUPLICATED_CONSTRAINT);
	}
	
	def protected DatatypeFactory getDTFactoryInstance(){
		DatatypeFactory.eINSTANCE
	}
	
	def protected FunctionblockFactory getFBFactoryInstance(){
		FunctionblockFactory.eINSTANCE
	}
	
}
