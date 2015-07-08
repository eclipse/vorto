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

import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.TestInforModelFactory;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.IndexHtmlFileGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.IndexHtmlFileTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.junit.Before;
import org.junit.Test;

public class IndexHtmlFileGeneratorTaskTest {
	IndexHtmlFileGeneratorTask indexHtmlFileGenerator;

	InformationModel informationModel = TestInforModelFactory
			.createInformationModel();

	@Before
	public void init() {
		indexHtmlFileGenerator = new IndexHtmlFileGeneratorTask();
	}

	@Test
	public void testGetFileName() {
		String expectedFileName = "index.html";
		assertEquals(expectedFileName,
				indexHtmlFileGenerator.getFileName(informationModel));
	}

	@Test
	public void testGetPath() {
		String expectedPath = "src/main/webapp";
		assertEquals(expectedPath,
				indexHtmlFileGenerator.getPath(informationModel));
	}

	@Test
	public void testGetTemplate() {
		assertTrue(indexHtmlFileGenerator.getTemplate() instanceof IndexHtmlFileTemplate);
	}
}
