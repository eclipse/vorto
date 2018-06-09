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

import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory
import org.eclipse.vorto.editor.datatype.validation.DatatypeSystemMessage
import org.junit.Test

class FBEntityEnumCrossReferenceTest extends CrossReferenceTestTemplate{
	
	@Test
	def test_Duplicated_Property_InConfig() {
		var config = getFBFactoryInstance().createConfiguration();
		
		var feature = DatatypeFactory.eINSTANCE.createProperty();
		feature.name = "Test";
		
		var feature2 = DatatypeFactory.eINSTANCE.createProperty();
		feature2.name = "Test";
		
		config.properties.add(feature);
		config.properties.add(feature2);
		
		tester.validator().checkPropsIn(config);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_DUPLICATED_PROPERTY_NAME);
	}
	
	@Test
	def test_Duplicated_Property_InStatus() {
		var container = getFBFactoryInstance().createStatus();
		
		var feature = DatatypeFactory.eINSTANCE.createProperty();
		feature.name = "Test";
		
		var feature2 = DatatypeFactory.eINSTANCE.createProperty();
		feature2.name = "Test";
		
		container.properties.add(feature);
		container.properties.add(feature2);
		
		tester.validator().checkPropsIn(container);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_DUPLICATED_PROPERTY_NAME);
	}
	
	@Test
	def test_Duplicated_Property_InFault() {
		var container = getFBFactoryInstance().createFault();
		
		var feature = DatatypeFactory.eINSTANCE.createProperty();
		feature.name = "Test";
		
		var feature2 = DatatypeFactory.eINSTANCE.createProperty();
		feature2.name = "Test";
		
		container.properties.add(feature);
		container.properties.add(feature2);
		
		tester.validator().checkPropsIn(container);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_DUPLICATED_PROPERTY_NAME);
	}
	
	@Test
	def test_Duplicated_Property_InEvent() {
		var container = getFBFactoryInstance().createEvent();
		
		var feature = DatatypeFactory.eINSTANCE.createProperty();
		feature.name = "Test";
		
		var feature2 = DatatypeFactory.eINSTANCE.createProperty();
		feature2.name = "Test";
		
		container.properties.add(feature);
		container.properties.add(feature2);
		
		tester.validator().checkPropsIn(container);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_DUPLICATED_PROPERTY_NAME);
	}
	
	@Test
	def test_Duplicated_Property_InEntity() {
		var container = getDTFactoryInstance().createEntity();
		
		var feature = DatatypeFactory.eINSTANCE.createProperty();
		feature.name = "Test";
		
		var feature2 = DatatypeFactory.eINSTANCE.createProperty();
		feature2.name = "Test";
		
		container.properties.add(feature);
		container.properties.add(feature2);
		
		tester.validator().checkPropsIn(container);
		tester.diagnose().assertErrorContains(DatatypeSystemMessage.ERROR_DUPLICATED_PROPERTY_NAME);
	}
}