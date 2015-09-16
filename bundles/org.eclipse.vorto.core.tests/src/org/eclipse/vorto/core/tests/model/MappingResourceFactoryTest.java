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

import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.model.IMapping;
import org.eclipse.vorto.core.model.MappingResourceFactory;
import org.eclipse.vorto.core.tests.model.helper.TestEntityMappingFactory;
import org.eclipse.vorto.core.tests.model.helper.TestEnumMappingFactory;
import org.eclipse.vorto.core.tests.model.helper.TestFunctionBlockMappingFactory;
import org.eclipse.vorto.core.tests.model.helper.TestInfoModelMappingFactory;
import org.junit.Test;

public class MappingResourceFactoryTest {
	@Test
	public void createInfoModelMappingRules() {
		MappingModel mappingModel = TestInfoModelMappingFactory.createInfoModelMappingModel();
		IMapping mappingRule = MappingResourceFactory.getInstance().createMapping(mappingModel,
				Collections.<IMapping> emptyList());
		assertTrue(mappingRule.getClass().getName().endsWith("InfoModelMappingResource"));
	}

	@Test
	public void createFunctionBlockMappingRules() {
		MappingModel mappingModel = TestFunctionBlockMappingFactory.createFunctionBlockMappingModel();
		IMapping mappingRule = MappingResourceFactory.getInstance().createMapping(mappingModel,
				Collections.<IMapping> emptyList());
		assertTrue(mappingRule.getClass().getName().endsWith("FunctionBlockMappingResource"));
	}

	@Test
	public void createEntityMappingRules() {
		MappingModel mappingModel = TestEntityMappingFactory.createEntityMappingModel();
		IMapping mappingRule = MappingResourceFactory.getInstance().createMapping(mappingModel,
				Collections.<IMapping> emptyList());
		assertTrue(mappingRule.getClass().getName().endsWith("EntityMappingResource"));
	}

	@Test
	public void createEnumMappingRules() {
		MappingModel mappingModel = TestEnumMappingFactory.createEnumMappingModel();
		IMapping mappingRule = MappingResourceFactory.getInstance().createMapping(mappingModel,
				Collections.<IMapping> emptyList());
		assertTrue(mappingRule.getClass().getName().endsWith("EnumMappingResource"));
	}
}
