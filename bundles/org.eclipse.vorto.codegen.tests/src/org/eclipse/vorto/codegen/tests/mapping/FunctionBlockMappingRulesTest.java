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

import java.util.List;

import org.eclipse.vorto.codegen.api.mapping.IMapping;
import org.eclipse.vorto.codegen.api.mapping.MappingAttribute;
import org.eclipse.vorto.codegen.internal.mapping.FunctionBlockMappingResource;
import org.eclipse.vorto.codegen.tests.mapping.helper.TestFunctionBlockMappingFactory;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.junit.Before;
import org.junit.Test;

/**
 * @author sgp0247
 *
 */
public class FunctionBlockMappingRulesTest {
	MappingModel mappingModel = TestFunctionBlockMappingFactory.createFunctionBlockMappingModel();
	FunctionblockModel functionblockModel = TestFunctionBlockMappingFactory.functionblockModel;

	@Before
	public void init() {

	}

	@Test
	public void testGetRuleByFunctionBlockAttribute() {
		IMapping mappingRule = new FunctionBlockMappingResource(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByModelAttribute(MappingAttribute.description);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByConfigurationProperty() {
		Property property = functionblockModel.getFunctionblock().getConfiguration().getProperties().get(0);
		IMapping mappingRule = new FunctionBlockMappingResource(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByModelObject(property);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByStatusProperty() {
		Property property = functionblockModel.getFunctionblock().getStatus().getProperties().get(0);
		IMapping mappingRule = new FunctionBlockMappingResource(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByModelObject(property);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByFaultProperty() {
		Property property = functionblockModel.getFunctionblock().getFault().getProperties().get(0);
		IMapping mappingRule = new FunctionBlockMappingResource(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByModelObject(property);
		assertEquals(1, mappingRules.size());
	}
	
	@Test
	public void testGetRuleByOperation() {
		Operation operation = functionblockModel.getFunctionblock().getOperations().get(0);
		IMapping mappingRule = new FunctionBlockMappingResource(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByModelObject(operation);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByStereoType() {
		IMapping mappingRule = new FunctionBlockMappingResource(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByStereoType("DummyStereoType");
		assertEquals(2, mappingRules.size());
	}
}
