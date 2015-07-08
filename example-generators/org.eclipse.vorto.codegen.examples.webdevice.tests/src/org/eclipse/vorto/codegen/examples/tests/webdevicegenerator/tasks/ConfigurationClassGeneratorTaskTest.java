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
package org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.vorto.codegen.examples.tests.TestFunctionblockModelFactory;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ConfigurationClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ModuleUtil;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.ConfigurationClassTemplate;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationClassGeneratorTaskTest {
	ConfigurationClassGeneratorTask configurationClassGenerator;

	FunctionblockModel model = TestFunctionblockModelFactory
			.createFBmodelWithProperties();

	@Before
	public void init() {
		configurationClassGenerator = new ConfigurationClassGeneratorTask();
	}

	@Test
	public void testGetFileName() {
		String expectedFileName = model.getName() + "Configuration.java";
		assertEquals(expectedFileName,
				configurationClassGenerator.getFileName(model));
	}

	@Test
	public void testGetPath() {
		String expectedPath = ModuleUtil.getModelPath(model);
		assertEquals(expectedPath, configurationClassGenerator.getPath(model));
	}

	@Test
	public void testGetTemplate() {
		assertTrue(configurationClassGenerator.getTemplate() instanceof ConfigurationClassTemplate);
	}

}
