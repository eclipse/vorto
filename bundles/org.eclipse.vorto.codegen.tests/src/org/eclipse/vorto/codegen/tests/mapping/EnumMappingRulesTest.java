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
import org.eclipse.vorto.codegen.internal.mapping.EnumMappingResource;
import org.eclipse.vorto.codegen.tests.mapping.helper.TestEnumFactory;
import org.eclipse.vorto.codegen.tests.mapping.helper.TestEnumMappingFactory;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteral;
import org.eclipse.vorto.core.api.model.mapping.EnumMapping;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.junit.Test;

/**
 * @author sgp0247
 *
 */
public class EnumMappingRulesTest {
	org.eclipse.vorto.core.api.model.datatype.Enum enumType = TestEnumFactory.createEnum();
	
	@Test
	public void testGetRuleByEnumAttribute() {
		MappingModel mappingModel = TestEnumMappingFactory.createEnumMappingModel();

		IMapping mappingRule = new EnumMappingResource(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByModelAttribute(MappingAttribute.version);
		assertEquals(1, mappingRules.size());
	}
	
	@Test
	public void testGetRuleByEnumLiteral() {
		EnumMapping mappingModel = TestEnumMappingFactory.createEnumMappingModel();
		EnumLiteral enumLiteral = enumType.getEnums().get(0);
		IMapping mappingRule = new EnumMappingResource(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByModelObject(enumLiteral);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByStereoType() {
		MappingModel mappingModel = TestEnumMappingFactory.createEnumMappingModel();
		IMapping mappingRule = new EnumMappingResource(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByStereoType("DummyStereoType");
		assertEquals(2, mappingRules.size());
	}	
}
