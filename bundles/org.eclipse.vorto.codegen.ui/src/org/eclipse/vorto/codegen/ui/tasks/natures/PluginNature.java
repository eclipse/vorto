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

package org.eclipse.vorto.codegen.ui.tasks.natures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.ui.tasks.ClasspathConfiguration;
import org.eclipse.vorto.codegen.ui.tasks.ManifestFileModule;
import org.eclipse.vorto.codegen.ui.tasks.PluginBuildFileModule;

/**
 * Plug-in Development Configuration of an Eclipse Project
 * 
 */
public class PluginNature<Context> extends CustomNature {

	private static final String PLUGIN_NATURE_STRING = "org.eclipse.pde.PluginNature";
	private final PluginBuildFileModule<Context> buildFile;
	private final ManifestFileModule<Context> manifestFile;

	public PluginNature(ITemplate<Context> buildFile,
			ITemplate<Context> manifestFile) {
		super(PLUGIN_NATURE_STRING);
		this.buildFile = new PluginBuildFileModule<Context>(buildFile);
		this.manifestFile = new ManifestFileModule<Context>(manifestFile);
	}

	public PluginNature(ITemplate<Context> buildFile,
			ITemplate<Context> manifestFile,
			ClasspathConfiguration classpathConfiguration) {
		super(PLUGIN_NATURE_STRING);
		classpathConfiguration.addEntry(ClasspathConfiguration.PLUGIN);
		this.buildFile = new PluginBuildFileModule<Context>(buildFile);
		this.manifestFile = new ManifestFileModule<Context>(manifestFile);
	}

	@SuppressWarnings({ "hiding", "unchecked" })
	public <Context> List<ICodeGeneratorTask<Context>> getGeneratorTasks() {
		List<ICodeGeneratorTask<Context>> modules = new ArrayList<ICodeGeneratorTask<Context>>();
		modules.add((ICodeGeneratorTask<Context>) buildFile);
		modules.add((ICodeGeneratorTask<Context>) manifestFile);
		return modules;
	}

}
