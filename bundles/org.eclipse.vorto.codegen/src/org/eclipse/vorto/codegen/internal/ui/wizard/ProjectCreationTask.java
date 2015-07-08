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
package org.eclipse.vorto.codegen.internal.ui.wizard;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.vorto.codegen.api.context.IGeneratorProjectContext;
import org.eclipse.vorto.codegen.api.tasks.ITemplate;
import org.eclipse.vorto.codegen.api.tasks.eclipse.EclipseProjectGenerator;
import org.eclipse.vorto.codegen.api.tasks.eclipse.LocationWrapper;
import org.eclipse.vorto.codegen.internal.context.ExampleProjectContext;
import org.eclipse.vorto.codegen.internal.ui.wizard.generation.tasks.ActivatorFileTask;
import org.eclipse.vorto.codegen.internal.ui.wizard.generation.tasks.JsonFileTask;
import org.eclipse.vorto.codegen.internal.ui.wizard.generation.tasks.TemplateFileTask;
import org.eclipse.vorto.codegen.internal.ui.wizard.generation.tasks.XMLFileTask;
import org.eclipse.vorto.codegen.internal.ui.wizard.generation.templates.ManifestFileTemplate;
import org.eclipse.vorto.codegen.internal.ui.wizard.generation.templates.PluginBuildFileTemplate;
import org.eclipse.vorto.codegen.ui.progresstask.IProgressTask;

public class ProjectCreationTask implements IProgressTask {

	private static final String META_INF_FOLDER = "META-INF";
	private static final String XTEND_GEN_FOLDER = "xtend-gen";
	private static final String SRC_FOLDER = "src";
	private static final String OUTPUT_LOCATION = "bin";
	private static final String ERROR_MESSAGE = "Problem when creating project, error: ";
	private static final String[] XTEND_SRC = { "src/", "xtend-gen/" };
	private static final String[] BIN_INCLUDES = { "META-INF/", "plugin.xml" };
	private static final String[] EXTENDED_BUNDLES = { "org.eclipse.ui",
			"org.eclipse.vorto.core", "org.eclipse.core.runtime",
			"org.eclipse.vorto.codegen", "com.google.guava",
			"org.eclipse.xtext.xbase.lib", "org.eclipse.xtend.lib",
			"org.eclipse.xtend.lib.macro", "org.eclipse.emf.common",
			"org.eclipse.emf.ecore" };
	private static final String[] ESSENTIAL_BUNDLES = { "org.eclipse.ui",
			"org.eclipse.vorto.core", "org.eclipse.core.runtime",
			"org.eclipse.vorto.codegen", "com.google.guava",
			"org.eclipse.xtext.xbase.lib", "org.eclipse.xtend.lib",
			"org.eclipse.xtend.lib.macro", "org.eclipse.emf.ecore" };
	private static final String[] PACKAGES = { "org.eclipse.vorto.editor.infomodel" };
	private static final ITemplate<IGeneratorProjectContext> EXTENDED_MANIFEST_FILE_TEMPLATE = new ManifestFileTemplate()
			.setBundles(Arrays.asList(EXTENDED_BUNDLES)).setPackages(
					Arrays.asList(PACKAGES));
	private static final ITemplate<IGeneratorProjectContext> ESSENTIAL_MANIFEST_FILE_TEMPLATE = new ManifestFileTemplate()
			.setBundles(Arrays.asList(ESSENTIAL_BUNDLES)).setPackages(
					Arrays.asList(PACKAGES));
	private static final ITemplate<IGeneratorProjectContext> BUILD_FILE_TEMPLATE = new PluginBuildFileTemplate<IGeneratorProjectContext>()
			.setSources(Arrays.asList(XTEND_SRC)).setBinIncludes(
					Arrays.asList(BIN_INCLUDES));

	private String errorMessage = "";
	private IGeneratorProjectContext context = null;
	private IWorkspace workspace;

	public ProjectCreationTask(IGeneratorProjectContext context) {
		this.context = context;
		workspace = ResourcesPlugin.getWorkspace();
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		try {
			createTemplateGeneratorProject(monitor);
		} catch (CoreException e) {
			this.errorMessage = e.getMessage();
			throw new RuntimeException(ERROR_MESSAGE + e.getMessage(), e);
		} finally {
			monitor.done();
		}
	}

	private void createTemplateGeneratorProject(IProgressMonitor monitor)
			throws JavaModelException {
		if (context.isGenerateTemplate()) {
			createProjectWithTemplate(monitor);
		} else {
			createProjectWithoutTemplate(monitor);
		}
		if (context.isGenerateExampleProject()) {
			createExampleProject(monitor);
		}
	}

	private void createExampleProject(IProgressMonitor monitor) {
		ExampleProjectContext jsonContext = new ExampleProjectContext(
				context.getWorkspaceLocation());
		EclipseProjectGenerator<IGeneratorProjectContext> generator = createEclipseProjectGenerator(new LocationWrapper(
				context.getWorkspaceLocation(), jsonContext.getProjectName()));
		generator.addTask(new JsonFileTask());
		generator.pluginNature(OUTPUT_LOCATION, BUILD_FILE_TEMPLATE,
				EXTENDED_MANIFEST_FILE_TEMPLATE, SRC_FOLDER, XTEND_GEN_FOLDER)
				.generate(jsonContext, monitor);
	}

	private EclipseProjectGenerator<IGeneratorProjectContext> createEclipseProjectGenerator(
			LocationWrapper projectLocation) {

		EclipseProjectGenerator<IGeneratorProjectContext> generator = new EclipseProjectGenerator<IGeneratorProjectContext>(
				projectLocation).addTask(new ActivatorFileTask())
				.addTask(new XMLFileTask()).addFolder(META_INF_FOLDER)
				.addFolder(OUTPUT_LOCATION).addFolder(XTEND_GEN_FOLDER)
				.addFolder(SRC_FOLDER);

		return generator;
	}

	private void createProjectWithoutTemplate(IProgressMonitor monitor) {
		EclipseProjectGenerator<IGeneratorProjectContext> generator = createEclipseProjectGenerator(
				getProjectLocation()).pluginNature(OUTPUT_LOCATION,
				BUILD_FILE_TEMPLATE, ESSENTIAL_MANIFEST_FILE_TEMPLATE,
				SRC_FOLDER, XTEND_GEN_FOLDER);
		generator.addTask(new TemplateFileTask());
		generator.generate(context, monitor);
	}

	private void createProjectWithTemplate(IProgressMonitor monitor) {
		EclipseProjectGenerator<IGeneratorProjectContext> generator = createEclipseProjectGenerator(
				getProjectLocation()).pluginNature(OUTPUT_LOCATION,
				BUILD_FILE_TEMPLATE, EXTENDED_MANIFEST_FILE_TEMPLATE,
				SRC_FOLDER, XTEND_GEN_FOLDER);
		generator.addTask(new TemplateFileTask());
		generator.generate(context, monitor);
	}

	private LocationWrapper getProjectLocation() {
		return new LocationWrapper(context.getWorkspaceLocation(),
				context.getProjectName());
	}

	@Override
	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public IGeneratorProjectContext getGenUserInut() {
		return context;
	}

	public IWorkspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(IWorkspace workspace) {
		this.workspace = workspace;
	}
}
