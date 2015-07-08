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

import java.util.Arrays;
import java.util.List;

import org.eclipse.vorto.codegen.api.context.IGeneratorProjectContext;
import org.eclipse.vorto.codegen.api.filewrite.IFileWritingStrategy;
import org.eclipse.vorto.codegen.api.tasks.Generated;
import org.eclipse.vorto.codegen.api.tasks.IOutputter;
import org.eclipse.vorto.codegen.api.tasks.eclipse.ManifestFileModule;
import org.eclipse.vorto.codegen.internal.ui.wizard.generation.templates.ManifestFileTemplate;
import org.eclipse.vorto.codegen.tests.wizard.util.ManifestTestTemplate;
import org.junit.Before;
import org.junit.Test;

public class ManifestFileModuleTest {
	private static final String EXPECTED_FILE_NAME = "MANIFEST.MF";
	private static final String EXPECTED_FOLDER_NAME = "META-INF";
	private static final Object EXPECTED_CONTENT_FILLED = new ManifestTestTemplate()
			.printFilled();
	private static final Object EXPECTED_CONTENT_EMPTY = new ManifestTestTemplate()
			.printEmpty();
	public static final String GENERATOR_NAME = "testGeneratorName";
	public static final String PACKAGE_NAME = "testPackageName";
	public static final String PACKAGE_FOLDERS = "testPackageFolders";
	public static final String PROJECT_NAME = "testProjectName";
	public static final String WORKSPACE_LOCATION = "testWorkspaceLocation";
	private Generated generatedFilled;
	private Generated generatedEmpty;
	private IGeneratorProjectContext metaData = null;

	@Before
	public void init() {
		OutputterMock outputterFilled = new OutputterMock();
		OutputterMock outputterEmpty = new OutputterMock();
		List<String> bundles = Arrays.asList("a", "b", "c", "d", "e");
		List<String> packages = Arrays.asList("f", "g", "h", "i", "j");
		ManifestFileModule<IGeneratorProjectContext> manifestFilled = new ManifestFileModule<IGeneratorProjectContext>(
				new ManifestFileTemplate().setBundles(bundles).setPackages(
						packages));
		ManifestFileModule<IGeneratorProjectContext> manifestEmpty = new ManifestFileModule<IGeneratorProjectContext>(
				new ManifestFileTemplate());
		metaData = new GeneratorProjectContextMock();
		manifestFilled.generate(metaData, outputterFilled);
		manifestEmpty.generate(metaData, outputterEmpty);
		generatedFilled = outputterFilled.getGeneratedFile();
		generatedEmpty = outputterEmpty.getGeneratedFile();
	}

	@Test
	public void testFileName() {
		String actual = generatedFilled.getFileName();
		assertEquals(EXPECTED_FILE_NAME, actual);
	}

	@Test
	public void testPathName() {
		String actual = generatedFilled.getFolderPath();
		assertEquals(EXPECTED_FOLDER_NAME, actual);
	}

	@Test
	public void testContentWithBundles() {
		String actual = generatedFilled.getContent();
		assertEquals(EXPECTED_CONTENT_FILLED, actual);
	}

	@Test
	public void testContentWithouBundles() {
		String actual = generatedEmpty.getContent();
		assertEquals(EXPECTED_CONTENT_EMPTY, actual);
	}

	private class OutputterMock implements IOutputter {

		private Generated generated = null;

		@Override
		public void output(Generated generated) {
			this.generated = generated;
		}

		@Override
		public void setFileWritingStrategy(
				IFileWritingStrategy fileWritingHandler) {

		}

		public Generated getGeneratedFile() {
			return generated;
		}

	}

	private class GeneratorProjectContextMock implements
			IGeneratorProjectContext {

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
			return false;
		}

		@Override
		public boolean isGenerateExampleProject() {
			return false;
		}

		@Override
		public String getProjectName() {
			return PROJECT_NAME;
		}

		@Override
		public String getWorkspaceLocation() {
			return WORKSPACE_LOCATION;
		}

	}
}
