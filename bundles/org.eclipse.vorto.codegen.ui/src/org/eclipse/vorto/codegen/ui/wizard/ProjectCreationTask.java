/*******************************************************************************
 *  Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.ui.wizard;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
import org.eclipse.vorto.codegen.ui.context.IGeneratorProjectContext;
import org.eclipse.vorto.codegen.ui.progresstask.IProgressTask;
import org.eclipse.vorto.codegen.ui.tasks.EclipseProjectGenerator;
import org.eclipse.vorto.codegen.ui.tasks.LocationWrapper;
import org.eclipse.vorto.codegen.ui.tasks.natures.MavenNature;
import org.eclipse.vorto.codegen.ui.wizard.generation.templates.GeneratorTemplate;
import org.eclipse.vorto.codegen.ui.wizard.generation.templates.ManifestFileTemplate;
import org.eclipse.vorto.codegen.ui.wizard.generation.templates.PluginBuildFileTemplate;
import org.eclipse.vorto.codegen.ui.wizard.generation.templates.PluginXMLFileTemplate;
import org.eclipse.vorto.codegen.ui.wizard.generation.templates.PomTemplate;
import org.eclipse.vorto.codegen.ui.wizard.generation.templates.server.ApplicationProfileProperties;
import org.eclipse.vorto.codegen.ui.wizard.generation.templates.server.ApplicationPropertiesTemplate;
import org.eclipse.vorto.codegen.ui.wizard.generation.templates.server.MicroServicePomTemplate;
import org.eclipse.vorto.codegen.ui.wizard.generation.templates.server.PlatformGeneratorMainTemplate;

public class ProjectCreationTask implements IProgressTask {

	private static final String ERROR_MESSAGE = "Problem when creating project, error: ";
	
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
			createGeneratorProject(monitor);
			if (context.isMicroServiceSupport()) {
				createMicroServiceProject(monitor);
			}
		} catch (CoreException e) {
			this.errorMessage = e.getMessage();
			throw new RuntimeException(ERROR_MESSAGE + e.getMessage(), e);
		} finally {
			monitor.done();
		}
	}


	private void createMicroServiceProject(IProgressMonitor monitor)
			throws JavaModelException {
		EclipseProjectGenerator<IGeneratorProjectContext> generator = new EclipseProjectGenerator<>(new LocationWrapper(
				context.getWorkspaceLocation(), context.getPackageName()+".service"));
		generator.mavenNature(new MicroServicePomTemplate(), "src/main/java","src/main/resources","src/test/java");
		generator.addTask(new PlatformGeneratorMainTemplate());
		generator.addTask(new ApplicationPropertiesTemplate());
		generator.addTask(new ApplicationProfileProperties());
		generator.generate(context, InvocationContext.simpleInvocationContext(), monitor);
	}

	private void createGeneratorProject(IProgressMonitor monitor) {
		EclipseProjectGenerator<IGeneratorProjectContext> generator = new EclipseProjectGenerator<IGeneratorProjectContext>(new LocationWrapper(
				context.getWorkspaceLocation(),context.getPackageName()+"."+context.getGeneratorName().toLowerCase()));
		generator.pluginNature(new PluginBuildFileTemplate(), new ManifestFileTemplate(), "src","xtend-gen");
		generator.addNature(MavenNature.MAVEN_NATURE_STRING);
		generator.addTask(new GeneratorTemplate());
		generator.addTask(new PomTemplate());
		generator.addTask(new PluginXMLFileTemplate());
		generator.generate(context,InvocationContext.simpleInvocationContext(), monitor);
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
