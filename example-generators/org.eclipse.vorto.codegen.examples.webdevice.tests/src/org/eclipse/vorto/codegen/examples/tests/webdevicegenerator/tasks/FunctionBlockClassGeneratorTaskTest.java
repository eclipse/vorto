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
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.FunctionBlockClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ModuleUtil;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.FunctionBlockClassTemplate;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.junit.Before;
import org.junit.Test;

public class FunctionBlockClassGeneratorTaskTest {
	FunctionBlockClassGeneratorTask functionBlockClassGenerator;

	FunctionblockModel model = TestFunctionblockModelFactory
			.createFBmodelWithProperties();

	@Before
	public void init() {
		functionBlockClassGenerator = new FunctionBlockClassGeneratorTask();
	}

	@Test
	public void testGetFileName() {
		String expectedFileName = model.getName() + ".java";
		assertEquals(expectedFileName,
				functionBlockClassGenerator.getFileName(model));
	}

	@Test
	public void testGetPath() {
		String expectedPath = ModuleUtil.getModelPath(model);
		assertEquals(expectedPath, functionBlockClassGenerator.getPath(model));
	}

	@Test
	public void testGetTemplate() {
		assertTrue(functionBlockClassGenerator.getTemplate() instanceof FunctionBlockClassTemplate);
	}

}
