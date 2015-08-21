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
import org.eclipse.vorto.codegen.internal.mapping.EntityMappingRules;
import org.eclipse.vorto.codegen.tests.mapping.helper.TestEntityMappingFactory;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.mapping.EntityExpression;
import org.eclipse.vorto.core.api.model.mapping.EntityMapping;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression;
import org.junit.Test;

/**
 * @author sgp0247
 *
 */
public class EntityMappingRulesTest {
	@Test
	public void testGetRuleByEntityAttribute() {
		MappingModel mappingModel = TestEntityMappingFactory.createEntityMappingModel();

		IMappingRules mappingRule = new EntityMappingRules(mappingModel);
		List<IMappingRule> mappingRules = mappingRule.getRules(MappingAttribute.description);
		assertEquals(1, mappingRules.size());
	}
	
	@Test
	public void testGetRuleByEntityProperty() {
		MappingModel mappingModel = TestEntityMappingFactory.createEntityMappingModel();
		EntityMapping entityMapping = (EntityMapping) mappingModel.getMapping();
		NestedEntityExpression nestedEntityExpression = (NestedEntityExpression)entityMapping.getEntityMappingRules().get(1).getEntitySourceElement().get(0);
		Entity entity = ((EntityExpression)nestedEntityExpression.getRef()).getEntity();
		Property entityProperty = entity.getProperties().get(0);
		IMappingRules mappingRule = new EntityMappingRules(mappingModel);
		List<IMappingRule> mappingRules = mappingRule.getRules(entityProperty);
		assertEquals(1, mappingRules.size());
	}

	@Test
	public void testGetRuleByStereoType() {
		MappingModel mappingModel = TestEntityMappingFactory.createEntityMappingModel();
		IMappingRules mappingRule = new EntityMappingRules(mappingModel);
		List<IMappingRule> mappingRules = mappingRule.getRulesContainStereoType("DummyStereoType");
		assertEquals(2, mappingRules.size());
	}	
}
