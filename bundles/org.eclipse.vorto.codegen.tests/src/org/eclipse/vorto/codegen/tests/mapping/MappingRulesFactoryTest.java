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

import org.eclipse.vorto.codegen.api.mapping.IMapping;
import org.eclipse.vorto.codegen.internal.mapping.EntityMappingResource;
import org.eclipse.vorto.codegen.internal.mapping.EnumMappingResource;
import org.eclipse.vorto.codegen.internal.mapping.FunctionBlockMappingResource;
import org.eclipse.vorto.codegen.internal.mapping.InfoModelMappingResource;
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
		IMapping mappingRules  = MappingRulesFactory.createMappingRules(mappingModel);
		assertTrue(mappingRules instanceof InfoModelMappingResource);
	}
	
	@Test
	public void createFunctionBlockMappingRules(){
		MappingModel mappingModel = TestFunctionBlockMappingFactory.createFunctionBlockMappingModel();
		IMapping mappingRules  = MappingRulesFactory.createMappingRules(mappingModel);
		assertTrue(mappingRules instanceof FunctionBlockMappingResource);
	}
	
	@Test
	public void createEntityMappingRules(){
		MappingModel mappingModel = TestEntityMappingFactory.createEntityMappingModel();
		IMapping mappingRules  = MappingRulesFactory.createMappingRules(mappingModel);
		assertTrue(mappingRules instanceof EntityMappingResource);
	}
	
	@Test
	public void createEnumMappingRules(){
		MappingModel mappingModel = TestEnumMappingFactory.createEnumMappingModel();
		IMapping mappingRules  = MappingRulesFactory.createMappingRules(mappingModel);
		assertTrue(mappingRules instanceof EnumMappingResource);
	}	
}
