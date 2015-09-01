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
package org.eclipse.vorto.editor.mapping.tests.model;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMapping;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.model.IMapping;
import org.eclipse.vorto.core.model.MappingAttribute;
import org.eclipse.vorto.core.model.MappingFactory;
import org.eclipse.vorto.editor.mapping.tests.model.helper.TestInfoModelMappingFactory;
import org.junit.Test;

public class InfoModelMappingTest {
	@Test
	public void testGetRuleByInfoModelAttribute() {
		MappingModel mappingModel = TestInfoModelMappingFactory.createInfoModelMappingModel();

		IMapping mappingRule = MappingFactory.createMapping(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByModelAttribute(MappingAttribute.displayname);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByInfoModelObject() {
		InfoModelMapping mappingModel = TestInfoModelMappingFactory.createInfoModelMappingModel();

		InformationModel infoModel = TestInfoModelMappingFactory.informationModel;
		FunctionblockProperty functionblockProperty = infoModel.getProperties().get(0);
		IMapping mappingRule = MappingFactory.createMapping(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByModelObject(functionblockProperty);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByStereoType() {
		MappingModel mappingModel = TestInfoModelMappingFactory.createInfoModelMappingModel();
		IMapping mappingRule = MappingFactory.createMapping(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByStereoType("DummyStereoType");
		assertEquals(1, mappingRules.size());
	}
}
