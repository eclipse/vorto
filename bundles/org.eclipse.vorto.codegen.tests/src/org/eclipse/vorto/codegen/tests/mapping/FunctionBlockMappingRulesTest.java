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

import org.eclipse.vorto.codegen.api.mapping.IMappingRule;
import org.eclipse.vorto.codegen.api.mapping.IMappingRules;
import org.eclipse.vorto.codegen.api.mapping.MappingAttribute;
import org.eclipse.vorto.codegen.internal.mapping.FunctionBlockMappingRules;
import org.eclipse.vorto.codegen.tests.mapping.helper.TestFunctionBlockMappingFactory;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.junit.Before;
import org.junit.Test;

/**
 * @author sgp0247
 *
 */
public class FunctionBlockMappingRulesTest {
	MappingModel mappingModel = TestFunctionBlockMappingFactory.createFunctionBlockMappingModel();
	FunctionBlockMapping functionBlockMapping = (FunctionBlockMapping) mappingModel.getMapping();
	FunctionblockModel functionblockModel = functionBlockMapping.getFunctionBlockMappingRules().get(0)
			.getFunctionBlockSourceElements().get(0).getFunctionblock();

	@Before
	public void init() {

	}

	@Test
	public void testGetRuleByFunctionBlockAttribute() {
		IMappingRules mappingRule = new FunctionBlockMappingRules(mappingModel);
		List<IMappingRule> mappingRules = mappingRule.getRules(MappingAttribute.description);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByConfigurationProperty() {
		Property property = functionblockModel.getFunctionblock().getConfiguration().getProperties().get(0);
		IMappingRules mappingRule = new FunctionBlockMappingRules(mappingModel);
		List<IMappingRule> mappingRules = mappingRule.getRules(property);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByStatusProperty() {
		Property property = functionblockModel.getFunctionblock().getStatus().getProperties().get(0);
		IMappingRules mappingRule = new FunctionBlockMappingRules(mappingModel);
		List<IMappingRule> mappingRules = mappingRule.getRules(property);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByFaultProperty() {
		Property property = functionblockModel.getFunctionblock().getFault().getProperties().get(0);
		IMappingRules mappingRule = new FunctionBlockMappingRules(mappingModel);
		List<IMappingRule> mappingRules = mappingRule.getRules(property);
		assertEquals(1, mappingRules.size());
	}
	
	@Test
	public void testGetRuleByOperation() {
		Operation operation = functionblockModel.getFunctionblock().getOperations().get(0);
		IMappingRules mappingRule = new FunctionBlockMappingRules(mappingModel);
		List<IMappingRule> mappingRules = mappingRule.getRules(operation);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByStereoType() {
		IMappingRules mappingRule = new FunctionBlockMappingRules(mappingModel);
		List<IMappingRule> mappingRules = mappingRule.getRulesContainStereoType("DummyStereoType");
		assertEquals(2, mappingRules.size());
	}
}
