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
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.eclipse.vorto.codegen.api.context.IGeneratorProjectContext;
import org.eclipse.vorto.codegen.api.filewrite.IFileWritingStrategy;
import org.eclipse.vorto.codegen.api.tasks.Generated;
import org.eclipse.vorto.codegen.api.tasks.IOutputter;
import org.eclipse.vorto.codegen.api.tasks.eclipse.PluginBuildFileModule;
import org.eclipse.vorto.codegen.internal.ui.wizard.generation.templates.PluginBuildFileTemplate;
import org.eclipse.vorto.codegen.tests.wizard.util.PluginBuildFileTestTemplate;
import org.junit.Before;
import org.junit.Test;

public class PluginBuildFileModuleTest {
	private static final String EXPECTED_FILE_NAME = "build.properties";
	private static final String EXPECTED_FOLDER_NAME = null;
	private static final Object EXPECTED_CONTENT_FILLED = new PluginBuildFileTestTemplate()
			.print();
	private static final Object EXPECTED_CONTENT_EMPTY = "";
	private Generated generatedFilled;
	private Generated generatedEmpty;
	private IGeneratorProjectContext metaData;

	@Before
	public void init() {
		metaData = createMock(IGeneratorProjectContext.class);
		OutputterMock outputterFilled = new OutputterMock();
		OutputterMock outputterEmpty = new OutputterMock();
		List<String> sources = Arrays.asList("a", "b", "c");
		List<String> output = Arrays.asList("d", "e", "f");
		List<String> binIncludes = Arrays.asList("g", "h", "i");
		List<String> binExcludes = Arrays.asList("j", "k", "l");
		List<String> srcIncludes = Arrays.asList("m", "n", "o");
		List<String> srcExcludes = Arrays.asList("p", "q", "r");
		PluginBuildFileModule<IGeneratorProjectContext> buildPropertiesFilled = new PluginBuildFileModule<IGeneratorProjectContext>(
				new PluginBuildFileTemplate<IGeneratorProjectContext>()
						.setOutput(output).setSources(sources)
						.setBinIncludes(binIncludes)
						.setBinExcludes(binExcludes)
						.setSrcIncludes(srcIncludes)
						.setSrcExcludes(srcExcludes));
		PluginBuildFileModule<IGeneratorProjectContext> buildPropertiesEmpty = new PluginBuildFileModule<IGeneratorProjectContext>(
				new PluginBuildFileTemplate<IGeneratorProjectContext>());
		buildPropertiesFilled.generate(metaData, outputterFilled);
		buildPropertiesEmpty.generate(metaData, outputterEmpty);
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
		String actual = new String(generatedFilled.getContent());
		assertEquals(EXPECTED_CONTENT_FILLED, actual);
	}

	@Test
	public void testContentWithoutBunldes() {
		String actual = new String(generatedEmpty.getContent());
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
}
