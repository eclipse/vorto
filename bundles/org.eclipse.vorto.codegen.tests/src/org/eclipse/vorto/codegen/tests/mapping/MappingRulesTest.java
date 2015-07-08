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
import org.eclipse.vorto.codegen.internal.mapping.DefaultMappingRules;
import org.eclipse.vorto.codegen.tests.TestMappingModelFactory;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.junit.Test;

public class MappingRulesTest {
	@Test
	public void testGetRuleByStereoType() {
		MappingModel mappingModel = TestMappingModelFactory.createRuleModel();
		IMappingRules mappingRule = new DefaultMappingRules(mappingModel);
		List<IMappingRule> mappingRules = mappingRule
				.getRulesContainStereoType("channelType");
		assertEquals(1, mappingRules.size());
	}
}
