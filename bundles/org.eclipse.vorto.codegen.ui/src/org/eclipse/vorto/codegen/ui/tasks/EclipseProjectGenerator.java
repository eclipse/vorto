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
package org.eclipse.vorto.codegen.ui.tasks;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.IFileTemplate;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
import org.eclipse.vorto.codegen.ui.context.IGeneratorProjectContext;

/**
 * Code Generator which generates an Eclipse project
 * 
 */
public class EclipseProjectGenerator<Context> {

	private ConfigurationContainer configuration;

	private List<ICodeGeneratorTask<Context>> tasks = new ArrayList<ICodeGeneratorTask<Context>>();

	private NatureConfiguration natureConfiguration = new NatureConfiguration();

	private IProject project;

	private LocationWrapper locationW;

	@SafeVarargs
	public EclipseProjectGenerator(LocationWrapper location,
			ConfigurationContainer configuration,
			ICodeGeneratorTask<Context>... tasks) {
		this.locationW = location;
		this.configuration = configuration;
		addConfiguration(natureConfiguration);
		this.tasks.addAll(Arrays.asList(tasks));
	}

	@SafeVarargs
	public EclipseProjectGenerator(String projectName,
			ConfigurationContainer configuration,
			ICodeGeneratorTask<Context>... tasks) {
		String rootWorkspace = ResourcesPlugin.getWorkspace().getRoot()
				.getLocation().toString();
		this.locationW = new LocationWrapper(rootWorkspace, projectName);
		this.configuration = configuration;
		addConfiguration(natureConfiguration);
		this.tasks.addAll(Arrays.asList(tasks));
	}

	public EclipseProjectGenerator(LocationWrapper location) {
		this.locationW = location;
		this.configuration = new ConfigurationContainer();
		addConfiguration(natureConfiguration);
	}

	public EclipseProjectGenerator(String projectName) {
		String rootWorkspace = ResourcesPlugin.getWorkspace().getRoot()
				.getLocation().toString();
		this.locationW = new LocationWrapper(rootWorkspace, projectName);
		this.configuration = new ConfigurationContainer();
		addConfiguration(natureConfiguration);
	}

	@SuppressWarnings("unchecked")
	public void generate(Context context, InvocationContext invocationContext, IProgressMonitor monitor) {
		project = getWorkspace().getProject(locationW.getProjectName());

		this.tasks
				.addAll((Collection<? extends ICodeGeneratorTask<Context>>) configuration
						.getGeneratorTasks());

		if (!project.exists()) {
			try {
				project = createProjectInWorkspace(monitor);
				project.open(monitor);

				for (ICodeGeneratorTask<Context> task : tasks) {
					task.generate(context,invocationContext, new ProjectFileOutputter(project));
				}

				configuration.configure(project);

			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
		} else {
			for (ICodeGeneratorTask<Context> task : tasks) {
				task.generate(context,invocationContext, new ProjectFileOutputter(project));
			}
		}

		try {
			project.refreshLocal(1, monitor);
		} catch (CoreException e1) {
			throw new RuntimeException(e1);
		}

		if (!project.isOpen()) {
			try {
				project.open(monitor);
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private IProject createProjectInWorkspace(IProgressMonitor monitor)
			throws CoreException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProjectDescription desc = workspace.newProjectDescription(locationW
				.getProjectName());

		desc.setLocation(locationW.getValidPath());

		if (!isDefaultWorkLocation(workspace)) {
			project.create(desc, new SubProgressMonitor(monitor, 1));
		} else {
			project.create(new SubProgressMonitor(monitor, 1));
		}
		return project;
	}

	private boolean isDefaultWorkLocation(IWorkspace workspace) {
		String defaultWorkspace = workspace.getRoot().getLocation().toString();
		return StringUtils.equals(defaultWorkspace, locationW.getPath());
	}

	public IProject getProject() {
		return project;
	}

	private IWorkspaceRoot getWorkspace() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	public EclipseProjectGenerator<Context> addFolder(String folder) {
		tasks.add(new FolderModule<Context>(folder));
		return this;
	}

	public EclipseProjectGenerator<Context> addNature(String nature) {
		this.natureConfiguration.addNature(nature);
		return this;
	}

	public EclipseProjectGenerator<Context> mavenNature(
			ITemplate<Context> pomFileTemplate, String... srcFolders) {
		this.natureConfiguration.addMavenNature(pomFileTemplate, srcFolders);
		return this;
	}

	public EclipseProjectGenerator<Context> javaNature(String... srcFolders) {
		this.natureConfiguration.addJavaNature(srcFolders);
		return this;
	}

	public EclipseProjectGenerator<Context> addConfiguration(
			IEclipseProjectConfiguration configuration) {
		this.configuration.addConfiguration(configuration);
		return this;
	}

	public EclipseProjectGenerator<Context> addTask(
			ICodeGeneratorTask<Context> task) {
		tasks.add(task);
		return this;
	}

	public EclipseProjectGenerator<Context> addTask(
			IFileTemplate<Context> template) {
		tasks.add(new GeneratorTaskFromFileTemplate(template));
		return this;
	}

	public EclipseProjectGenerator<Context> pluginNature(
			ITemplate<Context> buildFileModule,
			ITemplate<Context> manifestFileModule, String... srcFolders) {
		natureConfiguration.addPluginNature(buildFileModule,
				manifestFileModule, srcFolders);
		return this;
	}

	public EclipseProjectGenerator<Context> pluginNature(String outputLocation,
			ITemplate<IGeneratorProjectContext> buildFileModule,
			ITemplate<IGeneratorProjectContext> manifestFileModule,
			String... srcFolders) {
		natureConfiguration.addPluginNature(outputLocation, buildFileModule,
				manifestFileModule, srcFolders);
		return this;
	}

	/**
	 * copies the specified resource (directory or file) to the specified
	 * targetFolder
	 * 
	 * Example
	 * 
	 * Bundle bundle = Platform.getBundle(pluginId); URL resource =
	 * bundle.getEntry("templates/Demo.java.template");
	 * 
	 * copy(resource,"src/main/java",".template")
	 * 
	 * @param resourceUrl
	 *            directory or file to copy
	 * @param targetFolder
	 *            target folder, e.g. src/main/java
	 * @param fileSuffix
	 *            suffix of the individual file, e.g. '.template'
	 */
	public EclipseProjectGenerator<Context> copy(URL resource,
			String targetFolder, String fileSuffix) {
		tasks.add(new CopyResourceTask<Context>(resource, targetFolder));
		return this;
	}

	/**
	 * copies the specified resource (directory or file) to the specified
	 * targetFolder
	 * 
	 * Example
	 * 
	 * Bundle bundle = Platform.getBundle(pluginId); URL resource =
	 * bundle.getEntry("templates/image.png");
	 * 
	 * copy(resource,"icons")
	 * 
	 * @param resourceUrl
	 *            directory or file to copy
	 * @param targetFolder
	 *            target folder, e.g. src/main/java
	 */
	public EclipseProjectGenerator<Context> copy(URL resource,
			String targetFolder) {
		tasks.add(new CopyResourceTask<Context>(resource, targetFolder));
		return this;
	}
}
