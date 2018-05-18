package org.eclipse.vorto.editor.functionblock.tests.validator

import org.eclipse.xtext.junit4.AbstractXtextTests
import org.eclipse.xtext.junit4.validation.ValidatorTester
import org.eclipse.vorto.editor.functionblock.validation.FunctionblockValidator
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory
import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.editor.functionblock.validation.SystemMessage
import org.junit.Test

class FBExtendsTest extends AbstractXtextTests {
		
	private ValidatorTester<FunctionblockValidator> tester;
	
	def override void setUp() throws Exception {
		super.setUp();
		with(FunctionblockStandaloneSetup);
		var validator = get(FunctionblockValidator);
		tester = new ValidatorTester<FunctionblockValidator>(validator, getInjector());
	}
	
	@Test
	def test_IM_extend_datatype_conflict() {
		var fbModel = FunctionblockFactory.eINSTANCE.createFunctionblockModel()
		fbModel.functionblock = FunctionblockFactory.eINSTANCE.createFunctionBlock()
		fbModel.functionblock.status = FunctionblockFactory.eINSTANCE.createStatus()
		fbModel.name = "FB"
		
		var prop = DatatypeFactory.eINSTANCE.createProperty()
		prop.name = "prop1"
		var strType = DatatypeFactory.eINSTANCE.createPrimitivePropertyType()
		strType.setType(PrimitiveType.STRING)
		prop.type = strType
	
		fbModel.functionblock.status.properties.add(prop)
		
		var extendedFb = FunctionblockFactory.eINSTANCE.createFunctionBlock()
		extendedFb.status = FunctionblockFactory.eINSTANCE.createStatus()
		var extProperty = DatatypeFactory.eINSTANCE.createProperty()
		extProperty.name = "prop1"
		var intType = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		intType.setType(PrimitiveType.INT);
		extProperty.type = intType	
		extendedFb.status.properties.add(extProperty)
		
		var fbModelExtended = FunctionblockFactory.eINSTANCE.createFunctionblockModel()
		fbModelExtended.functionblock = extendedFb
		fbModelExtended.superType = fbModel
		fbModelExtended.name = "FBextended"
		
		tester.validator().checkStatusOverriddenProperties(fbModelExtended)
		tester.diagnose().assertErrorContains(SystemMessage.ERROR_INCOMPATIBLE_TYPE);
	}
}