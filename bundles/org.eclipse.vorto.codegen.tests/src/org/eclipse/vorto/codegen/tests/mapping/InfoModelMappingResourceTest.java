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
import org.eclipse.vorto.codegen.internal.mapping.InfoModelMappingResource;
import org.eclipse.vorto.codegen.tests.mapping.helper.TestInfoModelMappingFactory;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.junit.Test;

public class InfoModelMappingResourceTest {
	@Test
	public void testGetRuleByInfoModelAttribute() {
		MappingModel mappingModel = TestInfoModelMappingFactory.createInfoModelMappingModel();

		IMapping mappingRule = new InfoModelMappingResource(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByModelAttribute(MappingAttribute.displayname);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByInfoModelObject() {
		MappingModel mappingModel = TestInfoModelMappingFactory.createInfoModelMappingModel();

		InformationModel infoModel = (InformationModel) mappingModel.getRules().get(0).getSources().get(0).getModel();
		FunctionblockProperty functionblockProperty = infoModel.getProperties().get(0);
		IMapping mappingRule = new InfoModelMappingResource(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByModelObject(functionblockProperty);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByStereoType() {
		MappingModel mappingModel = TestInfoModelMappingFactory.createInfoModelMappingModel();
		IMapping mappingRule = new InfoModelMappingResource(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByStereoType("DummyStereoType");
		assertEquals(1, mappingRules.size());
	}
}
