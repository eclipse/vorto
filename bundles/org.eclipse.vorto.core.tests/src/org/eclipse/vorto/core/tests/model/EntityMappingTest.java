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
package org.eclipse.vorto.core.tests.model;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;
import org.eclipse.vorto.core.model.IMapping;
import org.eclipse.vorto.core.model.MappingResourceFactory;
import org.eclipse.vorto.core.tests.model.helper.TestEntityMappingFactory;
import org.junit.Test;

public class EntityMappingTest {
	@Test
	public void testGetRuleByEntityAttribute() {
		MappingModel mappingModel = TestEntityMappingFactory.createEntityMappingModel();

		IMapping mappingRule = MappingResourceFactory.getInstance().createMapping(mappingModel,
				Collections.<IMapping> emptyList());
		List<MappingRule> mappingRules = mappingRule.getRulesByModelAttribute(ModelAttribute.VERSION);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByEntityProperty() {
		MappingModel mappingModel = TestEntityMappingFactory.createEntityMappingModel();

		Property entityProperty = TestEntityMappingFactory.entity.getProperties().get(0);
		IMapping mappingRule = MappingResourceFactory.getInstance().createMapping(mappingModel,
				Collections.<IMapping> emptyList());
		List<MappingRule> mappingRules = mappingRule.getRulesByModelObject(entityProperty);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByStereoType() {
		MappingModel mappingModel = TestEntityMappingFactory.createEntityMappingModel();
		IMapping mappingRule = MappingResourceFactory.getInstance().createMapping(mappingModel,
				Collections.<IMapping> emptyList());
		List<MappingRule> mappingRules = mappingRule.getRulesByStereoType("DummyStereoType");
		assertEquals(2, mappingRules.size());
	}
}
