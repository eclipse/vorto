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
package org.eclipse.vorto.codegen.tests.mapping;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.codegen.api.mapping.IMappingRule;
import org.eclipse.vorto.codegen.internal.mapping.FunctionBlockMappingRuleWrapper;
import org.eclipse.vorto.codegen.tests.mapping.helper.TestFunctionBlockMappingFactory;
import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.StereoType;
import org.junit.Test;

public class FunctionBlockMappingRuleWrapperTest {
	@Test
	public void testGetRule() {
		FunctionBlockMappingRule functionBlockMappingRule = TestFunctionBlockMappingFactory.createFunctionBlockAttributeToStereoTypeMappingRule();
		IMappingRule mappingRule = new FunctionBlockMappingRuleWrapper(functionBlockMappingRule);

		EObject modelMappingRule = mappingRule.getRule();
		assertEquals(modelMappingRule, functionBlockMappingRule);

	}

	@Test
	public void testGetStereoType() {
		FunctionBlockMappingRule functionBlockMappingRule = TestFunctionBlockMappingFactory.createFunctionBlockAttributeToStereoTypeMappingRule();
		IMappingRule mappingRule = new FunctionBlockMappingRuleWrapper(functionBlockMappingRule);

		StereoType stereoType = mappingRule.getStereoType("DummyStereoType");

		assertEquals("DummyStereoType", stereoType.getName());
		assertEquals(1, stereoType.getAttributes().size());
		assertEquals("DummyAttribute", stereoType.getAttributes().get(0).getName());
		assertEquals("Dummy Attribute Value", stereoType.getAttributes().get(0).getValue());
	}

	@Test
	public void testGetStereoTypeAttribute() {
		FunctionBlockMappingRule functionBlockMappingRule = TestFunctionBlockMappingFactory.createFunctionBlockAttributeToStereoTypeMappingRule();
		IMappingRule mappingRule = new FunctionBlockMappingRuleWrapper(functionBlockMappingRule);

		Attribute attribute = mappingRule.getStereoTypeAttribute("DummyStereoType", "DummyAttribute");

		assertEquals("DummyAttribute", attribute.getName());
		assertEquals("Dummy Attribute Value", attribute.getValue());
	}

}
