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

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.vorto.codegen.api.tasks.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.tasks.ITemplate;
import org.eclipse.vorto.codegen.api.tasks.eclipse.PluginBuildFileModule;
import org.eclipse.vorto.codegen.api.tasks.eclipse.IEclipseProjectConfiguration;
import org.eclipse.vorto.codegen.api.tasks.eclipse.ManifestFileModule;
import org.eclipse.vorto.codegen.api.tasks.eclipse.natures.PluginNature;
import org.eclipse.vorto.codegen.internal.context.ExampleProjectContext;
import org.junit.Before;
import org.junit.Test;

public class PluginNatureConfigurationTest {
	
	private static final String BUILD_FILE_CONTENT = "BuildFileContent";
	private static final String MANIFEST_FILE_CONTENT = "ManifestFileContent";
	PluginNature<ExampleProjectContext> natureConfig = null;
	ITemplate<ExampleProjectContext> buildFile = null;
	ITemplate<ExampleProjectContext> manifestFile = null;
	IEclipseProjectConfiguration conf = null;
	IProject project = null;
	
	@Before
	public void init() {
		buildFile = new BuildTemplate();
		manifestFile = new ManifestTemplate();
		natureConfig = new PluginNature<ExampleProjectContext>(buildFile, manifestFile);
	}
	
	@Test
	public void testNumberOfBuildFileTasks() {
		List<ICodeGeneratorTask<Object>> taskList = natureConfig.getGeneratorTasks();
		int i = 0;
		for (ICodeGeneratorTask<Object> task : taskList) {
			if (task instanceof PluginBuildFileModule){
				i++;
			}
		}
		assertEquals(i,1);
	}
	
	@Test
	public void testNumberOfManifestFileTasks() {
		List<ICodeGeneratorTask<Object>> taskList = natureConfig.getGeneratorTasks();
		int i = 0;
		for (ICodeGeneratorTask<Object> task : taskList) {
			if (task instanceof ManifestFileModule){
				i++;
			}
		}
		assertEquals(i,1);
	}
	
	private class BuildTemplate implements ITemplate<ExampleProjectContext> {
		@Override
		public String getContent(ExampleProjectContext context) {
			return BUILD_FILE_CONTENT;
		}
	}
	
	private class ManifestTemplate implements ITemplate<ExampleProjectContext> {
		@Override
		public String getContent(ExampleProjectContext context) {
			return MANIFEST_FILE_CONTENT;
		}
	}
	
}
