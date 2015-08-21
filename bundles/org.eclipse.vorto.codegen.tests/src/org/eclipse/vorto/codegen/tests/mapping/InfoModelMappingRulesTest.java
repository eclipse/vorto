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
import org.eclipse.vorto.codegen.internal.mapping.InfoModelMappingRules;
import org.eclipse.vorto.codegen.tests.mapping.helper.TestInfoModelMappingFactory;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMapping;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.junit.Test;

public class InfoModelMappingRulesTest {
	@Test
	public void testGetRuleByInfoModelAttribute() {
		MappingModel mappingModel = TestInfoModelMappingFactory.createInfoModelMappingModel();

		IMappingRules mappingRule = new InfoModelMappingRules(mappingModel);
		List<IMappingRule> mappingRules = mappingRule.getRules(MappingAttribute.displayname);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByInfoModelElement() {
		MappingModel mappingModel = TestInfoModelMappingFactory.createInfoModelMappingModel();
		InfoModelMapping infoModelMapping = (InfoModelMapping) mappingModel.getMapping();
		InformationModel infoModel = infoModelMapping.getInfoModelMappingRules().get(0).getInfoModelSourceElements()
				.get(0).getInfoModel();
		FunctionblockProperty functionblockProperty = infoModel.getProperties().get(0);
		IMappingRules mappingRule = new InfoModelMappingRules(mappingModel);
		List<IMappingRule> mappingRules = mappingRule.getRules(functionblockProperty);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByStereoType() {
		MappingModel mappingModel = TestInfoModelMappingFactory.createInfoModelMappingModel();
		IMappingRules mappingRule = new InfoModelMappingRules(mappingModel);
		List<IMappingRule> mappingRules = mappingRule.getRulesContainStereoType("DummyStereoType");
		assertEquals(1, mappingRules.size());
	}
}
