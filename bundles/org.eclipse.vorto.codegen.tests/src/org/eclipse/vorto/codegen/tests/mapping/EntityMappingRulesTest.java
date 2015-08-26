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
import org.eclipse.vorto.codegen.internal.mapping.EntityMappingResource;
import org.eclipse.vorto.codegen.tests.mapping.helper.TestEntityMappingFactory;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.mapping.EntityMapping;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.junit.Test;

/**
 * @author sgp0247
 *
 */
public class EntityMappingRulesTest {
	@Test
	public void testGetRuleByEntityAttribute() {
		MappingModel mappingModel = TestEntityMappingFactory.createEntityMappingModel();

		IMapping mappingRule = new EntityMappingResource(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByModelAttribute(MappingAttribute.version);
		assertEquals(1, mappingRules.size());
	}
	
	@Test
	public void testGetRuleByEntityProperty() {
		MappingModel mappingModel = TestEntityMappingFactory.createEntityMappingModel();
		
		Property entityProperty = TestEntityMappingFactory.entity.getProperties().get(0);
		IMapping mappingRule = new EntityMappingResource(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByModelObject(entityProperty);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByStereoType() {
		MappingModel mappingModel = TestEntityMappingFactory.createEntityMappingModel();
		IMapping mappingRule = new EntityMappingResource(mappingModel);
		List<MappingRule> mappingRules = mappingRule.getRulesByStereoType("DummyStereoType");
		assertEquals(2, mappingRules.size());
	}	
}
