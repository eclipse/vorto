/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/

package org.eclipse.vorto.editor.functionblock.tests.validator

import java.util.ArrayList
import org.eclipse.emf.ecore.EObject
import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory
import org.eclipse.vorto.core.api.model.datatype.Type
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup
import org.eclipse.vorto.editor.functionblock.validation.FunctionblockValidator
import org.eclipse.vorto.editor.functionblock.validation.TypeHelper
import org.eclipse.xtext.junit4.AbstractXtextTests
import org.eclipse.xtext.junit4.validation.ValidatorTester

abstract class CrossReferenceTestTemplate extends AbstractXtextTests{
	
	protected ValidatorTester<FunctionblockValidator> tester;
	
	
	def override void setUp() throws Exception {
		super.setUp();
		with(FunctionblockStandaloneSetup);
		var validator = get(FunctionblockValidator);
		validator.helper = new TestTypeHelper();
		tester = new ValidatorTester<FunctionblockValidator>(validator, getInjector());
	}
	
	/*
	 * A test mock class to return an entity and enum in a list
	 * 
	 * checkout actual class : TypeFileAccessingHelper
	 */
	protected static class TestTypeHelper implements TypeHelper {
		
		override getAllTypeFromReferencedTypeFile(EObject eObject) {
			var ent= DatatypeFactory.eINSTANCE.createEntity()
			ent.name = "abc"
			
			var enu= DatatypeFactory.eINSTANCE.createEnum()
			enu.name = "bcd"
			
			var list = new ArrayList<Type>()
			list.addAll(ent, enu);
			
			return list
		}
		
	}
	
		def protected DatatypeFactory getDTFactoryInstance(){
		DatatypeFactory.eINSTANCE
	}
	
	def protected FunctionblockFactory getFBFactoryInstance(){
		FunctionblockFactory.eINSTANCE
	}
}