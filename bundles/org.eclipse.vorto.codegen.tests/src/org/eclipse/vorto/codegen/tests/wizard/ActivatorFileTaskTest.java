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
package org.eclipse.vorto.codegen.tests.wizard;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.eclipse.vorto.codegen.api.context.IGeneratorProjectContext;
import org.eclipse.vorto.codegen.api.tasks.ITemplate;
import org.eclipse.vorto.codegen.internal.context.ExampleProjectContext;
import org.eclipse.vorto.codegen.internal.ui.wizard.generation.tasks.ActivatorFileTask;
import org.eclipse.vorto.codegen.tests.wizard.util.ActivatorTestTemplate;
import org.junit.Before;
import org.junit.Test;

public class ActivatorFileTaskTest {

	private static final String GENERATOR_NAME = "testGeneratorName";
	private static final String PACKAGE_NAME = "testPackageName";
	private static final String PATH = "/src/" + PACKAGE_NAME;
	private static final String FILE_NAME = "Activator.java";
	private static final Object EXPECTED = new ActivatorTestTemplate().print();
	private ActivatorFileTask task = null;

	@Before
	public void init() {
		task = new ActivatorFileTask();
	}

	@Test
	public void testFileName() {
		String fileName = task.getFileName(new ExampleProjectContext(null,
				null, null));
		assertEquals(FILE_NAME, fileName);
	}

	@Test
	public void testPath() {
		String path = task.getPath(new ExampleProjectContext(null,
				PACKAGE_NAME, null));
		assertEquals(PATH, path);
	}

	@Test
	public void testContent() {
		ITemplate<IGeneratorProjectContext> template = task.getTemplate();
		IGeneratorProjectContext context = createMock(IGeneratorProjectContext.class);
		expect(context.getPackageName()).andReturn(PACKAGE_NAME);
		expect(context.getGeneratorName()).andReturn(GENERATOR_NAME);
		replay(context);
		String actual = template.getContent(context);
		verify(context);
		assertEquals(EXPECTED, actual);
	}

}
