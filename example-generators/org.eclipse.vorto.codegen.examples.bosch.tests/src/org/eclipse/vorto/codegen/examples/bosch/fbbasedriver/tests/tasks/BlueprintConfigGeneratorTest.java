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
package org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.BlueprintConfigGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.template.BlueprintConfigTemplate;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.junit.Test;

public class BlueprintConfigGeneratorTest {
	BlueprintConfigGeneratorTask blueprintConfigGenerator = new BlueprintConfigGeneratorTask();

	FunctionblockModel model = TestFunctionblockModelFactory
			.populateFBmodelWithProperties();

	@Test
	public void testGetFileName() {
		String expectedFileName = "config.xml";
		assertEquals(expectedFileName,
				blueprintConfigGenerator.getFileName(model));
	}

	@Test
	public void testGetPath() {
		String expectedPath = "src/main/resources/OSGI-INF/blueprint";
		assertEquals(expectedPath, blueprintConfigGenerator.getPath(model));
	}

	@Test
	public void testGetTemplate() {
		assertTrue(blueprintConfigGenerator.getTemplate() instanceof BlueprintConfigTemplate);
	}
}
