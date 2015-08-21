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

import static org.junit.Assert.assertTrue;

import org.eclipse.vorto.codegen.api.mapping.IMappingRules;
import org.eclipse.vorto.codegen.internal.mapping.EntityMappingRules;
import org.eclipse.vorto.codegen.internal.mapping.EnumMappingRules;
import org.eclipse.vorto.codegen.internal.mapping.FunctionBlockMappingRules;
import org.eclipse.vorto.codegen.internal.mapping.InfoModelMappingRules;
import org.eclipse.vorto.codegen.internal.mapping.MappingRulesFactory;
import org.eclipse.vorto.codegen.tests.mapping.helper.TestEntityMappingFactory;
import org.eclipse.vorto.codegen.tests.mapping.helper.TestEnumMappingFactory;
import org.eclipse.vorto.codegen.tests.mapping.helper.TestFunctionBlockMappingFactory;
import org.eclipse.vorto.codegen.tests.mapping.helper.TestInfoModelMappingFactory;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.junit.Test;

/**
 * @author sgp0247
 *
 */
public class MappingRulesFactoryTest {
	@Test
	public void createInfoModelMappingRules(){
		MappingModel mappingModel = TestInfoModelMappingFactory.createInfoModelMappingModel();
		IMappingRules mappingRules  = MappingRulesFactory.createMappingRules(mappingModel);
		assertTrue(mappingRules instanceof InfoModelMappingRules);
	}
	
	@Test
	public void createFunctionBlockMappingRules(){
		MappingModel mappingModel = TestFunctionBlockMappingFactory.createFunctionBlockMappingModel();
		IMappingRules mappingRules  = MappingRulesFactory.createMappingRules(mappingModel);
		assertTrue(mappingRules instanceof FunctionBlockMappingRules);
	}
	
	@Test
	public void createEntityMappingRules(){
		MappingModel mappingModel = TestEntityMappingFactory.createEntityMappingModel();
		IMappingRules mappingRules  = MappingRulesFactory.createMappingRules(mappingModel);
		assertTrue(mappingRules instanceof EntityMappingRules);
	}
	
	@Test
	public void createEnumMappingRules(){
		MappingModel mappingModel = TestEnumMappingFactory.createEnumMappingModel();
		IMappingRules mappingRules  = MappingRulesFactory.createMappingRules(mappingModel);
		assertTrue(mappingRules instanceof EnumMappingRules);
	}	
}
