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
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.editor.datatype.internal.ConstraintValidatorFactory
import org.eclipse.vorto.editor.datatype.internal.validation.PropertyConstraintMappingValidation
import org.eclipse.vorto.editor.datatype.validation.DatatypeSystemMessage
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup
import org.eclipse.vorto.editor.functionblock.validation.FunctionblockValidator
import org.eclipse.xtext.junit4.AbstractXtextTests
import org.eclipse.xtext.junit4.validation.ValidatorTester
import org.junit.Test

class FbConstraintValidationTest extends AbstractXtextTests {
		
	private ValidatorTester<FunctionblockValidator> tester;
	
	def override void setUp() throws Exception {
		super.setUp();
		with(FunctionblockStandaloneSetup);
		var validator = get(FunctionblockValidator);
		tester = new ValidatorTester<FunctionblockValidator>(validator, getInjector());
	}
	
	@Test
	def test_Constraint_INT_IntValue(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.INT);		

		prop.type = primi
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MAX)
		constraint1.setConstraintValues('22')
		
		prop.constraints.add(constraint1)
		
		var constraint2 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint2.setType(ConstraintIntervalType.MIN)
		constraint2.setConstraintValues('1')
		
		prop.constraints.add(constraint2)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertTrue(validator.evaluateValueType(PrimitiveType.INT,constraint1))
		assertEquals("",validator.getErrorMessage()) 
		
		var validator2 = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertTrue(validator2.evaluateValueType(PrimitiveType.INT,constraint2))
		assertEquals("",validator2.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertOK
	}
	
	@Test
	def test_Constraint_Float_FloatValue(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.FLOAT);		

		prop.type = primi
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MAX)
		constraint1.setConstraintValues('2.2')
		
		prop.constraints.add(constraint1)
		
		var constraint2 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint2.setType(ConstraintIntervalType.MIN)
		constraint2.setConstraintValues('1')
		
		prop.constraints.add(constraint2)
		
		var validator1 = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertTrue(validator1.evaluateValueType(PrimitiveType.FLOAT,constraint1))
		assertEquals("",validator1.getErrorMessage()) 
		
		var validator2 = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertTrue(validator2.evaluateValueType(PrimitiveType.FLOAT,constraint2))
		assertEquals("",validator2.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertOK
	}
	
	@Test
	def test_Constraint_String_StrValue(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.STRING);		

		prop.type = primi
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.REGEX)
		constraint1.setConstraintValues('[A..B]')
		
		prop.constraints.add(constraint1)
		
		var constraint2 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint2.setType(ConstraintIntervalType.STRLEN)
		constraint2.setConstraintValues('11')
		
		prop.constraints.add(constraint2)
		
		var validator1 = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertTrue(validator1.evaluateValueType(PrimitiveType.STRING,constraint1))
		assertEquals("",validator1.getErrorMessage()) 
		
		var validator2 = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertTrue(validator2.evaluateValueType(PrimitiveType.STRING,constraint2))
		assertEquals("",validator2.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertOK
	}
	
	@Test
	def test_Constraint_Datetime_DtValue(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.DATETIME);		

		prop.type = primi

		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MAX)
		constraint1.setConstraintValues('2002-05-30T09:30:10+06:00')
		
		prop.constraints.add(constraint1)
		
		var constraint2 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint2.setType(ConstraintIntervalType.MIN)
		constraint2.setConstraintValues('2002-04-30T01:30:10+06:00')
		
		prop.constraints.add(constraint2)
		
		var validator1 = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertTrue(validator1.evaluateValueType((prop.type as PrimitivePropertyType).getType,constraint1))
		assertEquals("",validator1.getErrorMessage()) 
		
		var validator2 = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertTrue(validator2.evaluateValueType(primi.getType,constraint2))
		assertEquals("",validator2.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertOK
	}
	
	@Test
	def test_Constraint_Datetime_Wrong_DtValue(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.DATETIME);		

		prop.type = primi
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MAX)
		constraint1.setConstraintValues('2002-05-30T09:30:10')
		
		prop.constraints.add(constraint1)
		
		var validator1 = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertFalse(validator1.evaluateValueType(primi.getType,constraint1))
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_DATETIME,validator1.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_DATETIME)
	}
	
	@Test
	def test_Constraint_Boolean_NoConstraint(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.BOOLEAN);		

		prop.type = primi
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertOK
	}
	
	
	@Test
	def test_Constraint_INT_MAX_StrValue(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.INT);		

		prop.type = primi 
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MAX)
		constraint1.setConstraintValues('abc')
		
		prop.constraints.add(constraint1)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertFalse(validator.evaluateValueType(PrimitiveType.INT,constraint1))
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_INT,validator.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_INT);
	}
	
	@Test
	def test_Constraint_Float_MAX_StrValue(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.FLOAT);		

		prop.type = primi
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MAX)
		constraint1.setConstraintValues('abc')
		
		prop.constraints.add(constraint1)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertFalse(validator.evaluateValueType(PrimitiveType.FLOAT,constraint1))
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_FLOAT,validator.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_FLOAT);
	}
	
	@Test
	def test_Constraint_DateTime_MIN_IntValue(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.DATETIME);		

		prop.type = primi 
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MIN)
		constraint1.setConstraintValues('211')
		
		prop.constraints.add(constraint1)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertFalse(validator.evaluateValueType(PrimitiveType.DATETIME,constraint1))
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_DATETIME,validator.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_DATETIME);
	}
	
	@Test
	def test_Constraint_Str_StrLen_FloatValue(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.STRING);		

		prop.type = primi
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.STRLEN)
		constraint1.setConstraintValues('21.1')
		
		prop.constraints.add(constraint1)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertFalse(validator.evaluateValueType(PrimitiveType.STRING,constraint1))
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_INT,validator.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_INT);
		
	}

	@Test
	def test_Constraint_Boolean_Max_StrValue(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.BOOLEAN);		

		prop.type = primi
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MAX)
		constraint1.setConstraintValues('sadfasfa')
		
		prop.constraints.add(constraint1)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertTrue(validator.evaluateValueType(PrimitiveType.STRING,constraint1))
		
		var validat = new PropertyConstraintMappingValidation(); 
		assertFalse(validat.checkPropertyConstraints(PrimitiveType.BOOLEAN,constraint1))	
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID,validat.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID);
		
	}
	
	@Test
	def test_Constraint_StringProperty_MIN() {
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.STRING);		

		prop.type = primi
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		constraint1.setType(ConstraintIntervalType.MIN)
		prop.constraints.add(constraint1)
		
		var validator = new PropertyConstraintMappingValidation(); 
		assertFalse(validator.checkPropertyConstraints(PrimitiveType.STRING,constraint1))	
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID,validator.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID);
	}
	
	@Test
	def test_Constraint_IntProperty_RegeX() {
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.INT);		

		prop.type = primi		
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		constraint1.setType(ConstraintIntervalType.REGEX)
		prop.constraints.add(constraint1)
		
		var validator = new PropertyConstraintMappingValidation(); 
		assertFalse(validator.checkPropertyConstraints(PrimitiveType.INT,constraint1))	
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID,validator.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID);
	}
	
	@Test
	def test_Constraint_IntProperty_Max_NegValue() {
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.INT);		

		prop.type = primi	
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		constraint1.setType(ConstraintIntervalType.MAX)
		constraint1.constraintValues = '-1111'
		prop.constraints.add(constraint1)
		
		var validator = new PropertyConstraintMappingValidation(); 
		assertTrue(validator.checkPropertyConstraints(PrimitiveType.INT,constraint1))	
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertOK
	}
	
	@Test
	def test_Constraint_BoolProperty_Min() {
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.BOOLEAN);		

		prop.type = primi
				
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		constraint1.setType(ConstraintIntervalType.MIN)
		prop.constraints.add(constraint1)
		
		var validator = new PropertyConstraintMappingValidation(); 
		assertFalse(validator.checkPropertyConstraints(PrimitiveType.BOOLEAN,constraint1))	
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID,validator.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID);
	}
	
	@Test
	def test_Constraint_ShortProperty_MAX_StrValue(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.SHORT);		

		prop.type = primi
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MAX)
		constraint1.setConstraintValues('abc')
		
		prop.constraints.add(constraint1)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertFalse(validator.evaluateValueType(PrimitiveType.SHORT,constraint1))
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_SHORT,validator.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_SHORT);
	}
	
	@Test
	def test_Constraint_ShortProperty_MIN_OutOfRange(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.SHORT);		

		prop.type = primi
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MIN)
		constraint1.setConstraintValues('32800')
		
		prop.constraints.add(constraint1)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertFalse(validator.evaluateValueType(PrimitiveType.SHORT,constraint1))
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_SHORT,validator.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_SHORT);
	}
	
	@Test
	def test_Constraint_ShortProperty_MAX_OutOfRange_Negative(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.SHORT);		

		prop.type = primi	
				
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MAX)
		constraint1.setConstraintValues('-32800')
		
		prop.constraints.add(constraint1)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertFalse(validator.evaluateValueType(PrimitiveType.SHORT,constraint1))
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_SHORT,validator.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_SHORT);
	}
	
	@Test
	def test_Constraint_ShortProperty_MIN_NegValue(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.SHORT);		

		prop.type = primi	
				
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MIN)
		constraint1.setConstraintValues('-111')
		
		prop.constraints.add(constraint1)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertTrue(validator.evaluateValueType(PrimitiveType.SHORT,constraint1))
		assertEquals("",validator.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertOK
	}
	
	@Test
	def test_Constraint_LongProperty_RegeX() {
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.LONG);		

		prop.type = primi	
				
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		constraint1.setType(ConstraintIntervalType.REGEX)
		prop.constraints.add(constraint1)
		
		var validator = new PropertyConstraintMappingValidation(); 
		assertFalse(validator.checkPropertyConstraints(PrimitiveType.LONG,constraint1))	
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID,validator.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID);
	}
	
	@Test
	def test_Constraint_Base64BinaryProperty_NoConstraint() {
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.BASE64_BINARY);		

		prop.type = primi	 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertOK
	}
	
	@Test
	def test_Constraint_Base64BinaryProperty_Min() {
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.BASE64_BINARY);		

		prop.type = primi	
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		constraint1.setType(ConstraintIntervalType.MIN)
		prop.constraints.add(constraint1)
		
		var validator = new PropertyConstraintMappingValidation(); 
		
		assertFalse(validator.checkPropertyConstraints(primi.getType,constraint1))	
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID,validator.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID);
	}
	
	@Test
	def test_Constraint_DoubleProperty_Strlen() {
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.DOUBLE);		

		prop.type = primi	
		
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		constraint1.setType(ConstraintIntervalType.STRLEN)
		prop.constraints.add(constraint1)
		
		var validator = new PropertyConstraintMappingValidation(); 
		assertFalse(validator.checkPropertyConstraints(PrimitiveType.DOUBLE,constraint1))	
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID,validator.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID);
	}
	
	@Test
	def test_Constraint_DoubleProperty_Max_NegValue() {
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.DOUBLE);		

		prop.type = primi	
			
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		constraint1.setType(ConstraintIntervalType.MAX)
		constraint1.constraintValues = '-11.11'
		prop.constraints.add(constraint1)
		
		var validator = new PropertyConstraintMappingValidation(); 
		assertTrue(validator.checkPropertyConstraints(PrimitiveType.DOUBLE,constraint1))	
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertOK
	}
	
	
	@Test
	def test_Constraint_ByteProperty_MAX_StrValue(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.BYTE);		

		prop.type = primi		
				
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MAX)
		constraint1.setConstraintValues('abc')
		
		prop.constraints.add(constraint1)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertFalse(validator.evaluateValueType(PrimitiveType.BYTE,constraint1))
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_BYTE,validator.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_BYTE);
	}
	
	@Test
	def test_Constraint_ByteProperty_MIN_OutOfRange(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.BYTE);		

		prop.type = primi			
				
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MIN)
		constraint1.setConstraintValues('128')
		
		prop.constraints.add(constraint1)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertFalse(validator.evaluateValueType(PrimitiveType.BYTE,constraint1))
		assertEquals(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_BYTE,validator.getErrorMessage()) 
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_BYTE);
	}
	
	@Test
	def test_Constraint_ByteProperty_MIN_NegValue(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.BYTE);		

		prop.type = primi		
				
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MIN)
		constraint1.setConstraintValues('-128')
		
		prop.constraints.add(constraint1)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertTrue(validator.evaluateValueType(PrimitiveType.BYTE,constraint1));
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertOK();
	}
	
	@Test
	def test_Constraint_MultipleByteProperty_MIMETYPE_positiveVal(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.BYTE);		

		prop.type = primi	
		
		prop.multiplicity = true 
				
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MIMETYPE)
		constraint1.setConstraintValues('testing')
		
		prop.constraints.add(constraint1)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertTrue(validator.evaluateValueType(PrimitiveType.BYTE,constraint1));
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertOK();
	}
	
	@Test
	def test_Constraint_ByteProperty_MIMETYPE_ONlyAllowedForByteArr(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.BYTE);		

		prop.type = primi
				
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MIMETYPE)
		constraint1.setConstraintValues('testing')
		
		prop.constraints.add(constraint1)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertTrue(validator.evaluateValueType(PrimitiveType.BYTE,constraint1));
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_MIMETYPE_FOR_BYTE);
	}
	
	@Test
	def test_Constraint_DoubleProperty_MIMETYPE(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.DOUBLE);		

		prop.type = primi		
				
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MIMETYPE)
		constraint1.setConstraintValues('testing')
		
		prop.constraints.add(constraint1)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertTrue(validator.evaluateValueType(PrimitiveType.BYTE,constraint1));
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID);
	}
	
	@Test
	def test_Constraint_Base64binaryProperty_MIMETYPE_positiveVal(){
		var prop = DatatypeFactory.eINSTANCE.createProperty();
		
		var primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(PrimitiveType.BASE64_BINARY);		

		prop.type = primi		
				
		var constraint1 = DatatypeFactory.eINSTANCE.createConstraint();
		
		constraint1.setType(ConstraintIntervalType.MIMETYPE)
		constraint1.setConstraintValues('testing')
		
		prop.constraints.add(constraint1)
		
		var validator = ConstraintValidatorFactory.getValueValidator(constraint1.type);
		
		assertTrue(validator.evaluateValueType(PrimitiveType.BYTE,constraint1));
		
		tester.validator().checkConstraint(prop);
		tester.diagnose().assertOK();
	}
				
}