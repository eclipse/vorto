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
import org.eclipse.vorto.codegen.internal.ui.wizard.generation.tasks.XMLFileTask;
import org.eclipse.vorto.codegen.tests.wizard.util.XMLTestTemplate;
import org.junit.Before;
import org.junit.Test;

public class XMLFileTaskTest {

	private static final String FILE_NAME = "plugin.xml";
	private static final Object EXPECTED = new XMLTestTemplate().print();
	private static final String GENERATOR_NAME = "testGeneratorName";
	private static final String PACKAGE_NAME = "testPackageName";
	private static final String PROJECT_NAME = "testProjectName";
	private XMLFileTask task = null;

	@Before
	public void init() {
		task = new XMLFileTask();
	}

	@Test
	public void testFileName() {
		String fileName = task.getFileName(new ExampleProjectContext(null,
				null, null));
		assertEquals(fileName, FILE_NAME);
	}

	@Test
	public void testPath() {
		String path = task.getPath(new ExampleProjectContext(null, null, null));
		assertEquals(path, null);
	}

	@Test
	public void testContent() {
		ITemplate<IGeneratorProjectContext> template = task.getTemplate();
		IGeneratorProjectContext context = createMock(IGeneratorProjectContext.class);
		expect(context.getGeneratorName()).andReturn(GENERATOR_NAME).times(3);
		expect(context.getProjectName()).andReturn(PROJECT_NAME);
		expect(context.getPackageName()).andReturn(PACKAGE_NAME);
		replay(context);
		String actual = template.getContent(context);
		verify(context);
		assertEquals(EXPECTED, actual);
	}
}
