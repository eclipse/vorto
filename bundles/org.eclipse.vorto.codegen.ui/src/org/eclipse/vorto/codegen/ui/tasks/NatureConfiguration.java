/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.ui.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.ui.tasks.natures.CustomNature;
import org.eclipse.vorto.codegen.ui.tasks.natures.JavaNature;
import org.eclipse.vorto.codegen.ui.tasks.natures.MavenNature;
import org.eclipse.vorto.codegen.ui.tasks.natures.PluginNature;

/**
 * Configures the natures of a project
 * 
 * 
 * 
 */
public class NatureConfiguration implements IEclipseProjectConfiguration {

	private ClasspathConfiguration classpathConfiguration = new ClasspathConfiguration();
	private List<CustomNature> natures = new ArrayList<CustomNature>();

	private boolean configureClasspath = false;

	private String[] allNatures() {
		List<String> natures = new ArrayList<String>(this.natures.size());
		for (CustomNature nature : this.natures) {
			natures.add(nature.asString());
		}

		return natures.toArray(new String[natures.size()]);
	}

	public NatureConfiguration configure(IProject project) {
		try {
			IProjectDescription description = project.getDescription();
			description.setNatureIds(allNatures());
			project.setDescription(description, new NullProgressMonitor());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (configureClasspath) {
			classpathConfiguration.configure(project);
		}
		return this;
	}

	public NatureConfiguration addNature(String nature) {
		this.natures.add(new CustomNature(nature));
		return this;
	}

	public NatureConfiguration addJavaNature(String[] srcFolders) {
		this.natures.add(new JavaNature(srcFolders, classpathConfiguration));
		this.configureClasspath = true;
		return this;
	}

	public <Context> NatureConfiguration addPluginNature(
			ITemplate<Context> buildFile, ITemplate<Context> manifestFile,
			String[] srcFolders) {
		this.natures.add(new PluginNature<Context>(buildFile, manifestFile,
				classpathConfiguration));
		addJavaNature(srcFolders);
		return this;
	}

	public <Context> NatureConfiguration addPluginNature(String outputLocation,
			ITemplate<Context> buildFile, ITemplate<Context> manifestFile,
			String[] srcFolders) {
		this.natures.add(new PluginNature<Context>(buildFile, manifestFile,
				classpathConfiguration));
		classpathConfiguration.setOutputLocation(outputLocation);
		addJavaNature(srcFolders);
		return this;
	}

	public <Context> NatureConfiguration addMavenNature(
			ITemplate<Context> pomFile, String[] srcFolders) {
		this.natures.add(new MavenNature<Context>(pomFile));
		addJavaNature(srcFolders);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Context> List<ICodeGeneratorTask<Context>> getGeneratorTasks() {

		List<ICodeGeneratorTask<Context>> modules = new ArrayList<ICodeGeneratorTask<Context>>();
		for (CustomNature nature : natures) {
			modules.addAll((Collection<? extends ICodeGeneratorTask<Context>>) nature
					.getGeneratorTasks());
		}
		return modules;
	}
}
