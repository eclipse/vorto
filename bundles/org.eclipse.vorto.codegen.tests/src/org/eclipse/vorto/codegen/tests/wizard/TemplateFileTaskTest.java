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

import static org.junit.Assert.assertEquals;

import org.eclipse.vorto.codegen.api.context.IGeneratorProjectContext;
import org.eclipse.vorto.codegen.api.tasks.ITemplate;
import org.eclipse.vorto.codegen.internal.context.ExampleProjectContext;
import org.eclipse.vorto.codegen.internal.ui.wizard.generation.tasks.TemplateFileTask;
import org.eclipse.vorto.codegen.tests.wizard.util.EmptyTestTemplate;
import org.eclipse.vorto.codegen.tests.wizard.util.FilledTestTemplate;
import org.junit.Before;
import org.junit.Test;

public class TemplateFileTaskTest {

	private static final String PACKAGE_NAME = "testPackageName";
	private static final String PATH = "/src/" + PACKAGE_NAME;
	private static final String FILE_NAME = "JsonGenerator.xtend";
	private static final Object FILLED_EXPECTED = new FilledTestTemplate()
			.print();
	private static final Object EMPTY_EXPECTED = new EmptyTestTemplate()
			.print();
	public static final String PACKAGE_FOLDERS = "testPackageFolders";
	public static final String GENERATOR_NAME = "testGeneratorName";
	public static final String PROJECT_NAME = "testProjectName";
	public static final String WORKSPACE_LOCATION = "testWorkspaceLocation";
	private TemplateFileTask task = null;
	private ITemplate<IGeneratorProjectContext> template = null;

	@Before
	public void init() {
		task = new TemplateFileTask();
		template = task.getTemplate();
	}

	@Test
	public void testFileName() {
		String fileName = task.getFileName(new ExampleProjectContext(null, null, null));
		assertEquals(fileName, FILE_NAME);
	}

	@Test
	public void testPath() {
		String path = task.getPath(new ExampleProjectContext(null,
				PACKAGE_NAME, null));
		assertEquals(path, PATH);
	}

	@Test
	public void testFilledContent() {
		String actual = template.getContent(new GeneratorProjectContextMock(true));
		assertEquals(FILLED_EXPECTED, actual);
	}

	@Test
	public void testEmptyContent() {
		String actual = template.getContent(new GeneratorProjectContextMock(false));
		assertEquals(EMPTY_EXPECTED, actual);
	}
	
	private class GeneratorProjectContextMock implements IGeneratorProjectContext {
		
		private boolean isTemplateGenerated;

		public GeneratorProjectContextMock(boolean isTemplateGenerated) {
			this.isTemplateGenerated = isTemplateGenerated;
		}
		
		@Override
		public String getProjectName() {
			return PROJECT_NAME;
		}

		@Override
		public String getWorkspaceLocation() {
			return WORKSPACE_LOCATION;
		}

		@Override
		public String getGeneratorName() {
			return GENERATOR_NAME;
		}

		@Override
		public String getPackageName() {
			return PACKAGE_NAME;
		}

		@Override
		public String getPackageFolders() {
			return PACKAGE_FOLDERS;
		}

		@Override
		public boolean isGenerateTemplate() {
			return isTemplateGenerated;
		}

		@Override
		public boolean isGenerateExampleProject() {
			return false;
		}
		
	}

}
