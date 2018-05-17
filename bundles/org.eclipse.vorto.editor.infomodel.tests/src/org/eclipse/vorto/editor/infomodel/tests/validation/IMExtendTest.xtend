package org.eclipse.vorto.editor.infomodel.tests.validation

/*******************************************************************************
 * Copyright (c) 2018 Bosch Software Innovations GmbH and others.
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

import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory
import org.eclipse.vorto.editor.functionblock.validation.SystemMessage
import org.eclipse.xtext.junit4.AbstractXtextTests
import org.eclipse.xtext.junit4.validation.ValidatorTester
import org.junit.After
import org.junit.Test
import org.eclipse.vorto.editor.infomodel.InformationModelStandaloneSetup
import org.eclipse.vorto.editor.infomodel.validation.InformationModelValidator
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelFactory
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup

class IMExtendTest extends AbstractXtextTests {
	
	private ValidatorTester<InformationModelValidator> tester;

	def override void setUp() throws Exception {
		super.setUp();
		with(FunctionblockStandaloneSetup);
		with(InformationModelStandaloneSetup);
		var validator = get(InformationModelValidator);
		tester = new ValidatorTester<InformationModelValidator>(validator, getInjector());		
	}
	
	/**
	 * Need to overwrite tearDown method to avoid emf classes get de-registered causing inconsistency between test methods
	 * as static variable in *PackageImpl already set to true and it's not re-inited again
	 */
    @After
	def override void tearDown() throws Exception {
		
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
		
		var fbProperty = InformationModelFactory.eINSTANCE.createFunctionblockProperty()
		fbProperty.name = "fb1"
		fbProperty.type = fbModel
		fbProperty.extendedFunctionBlock = extendedFb
		
		var imModel = InformationModelFactory.eINSTANCE.createInformationModel()
		imModel.name = "IM"
		imModel.properties.add(fbProperty)
		
		tester.validator().checkStatusExtendOverriddenProperties(imModel)
		tester.diagnose().assertErrorContains(SystemMessage.ERROR_INCOMPATIBLE_TYPE);
	}
}
