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
package org.eclipse.vorto.codegen.api.tasks.eclipse.natures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.vorto.codegen.api.tasks.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.tasks.ITemplate;
import org.eclipse.vorto.codegen.api.tasks.eclipse.FolderModule;
import org.eclipse.vorto.codegen.api.tasks.eclipse.PomFileModule;

/**
 * Maven Configuration of an Eclipse Project
 * 
 */
public class MavenNature<Context> extends CustomNature {

	public static final String MAVEN_NATURE_STRING = "org.eclipse.m2e.core.maven2Nature";

	@SuppressWarnings("rawtypes")
	private final static FolderModule mavenDefaultFolders = new FolderModule(
			"src/main/java", "src/main/test", "src/test/java",
			"src/test/resources", "target/classes", "target/test-classes");

	private final PomFileModule<Context> pomFileGenerator;

	public MavenNature(ITemplate<Context> pomFile) {
		super(MAVEN_NATURE_STRING);
		pomFileGenerator = new PomFileModule<Context>(pomFile);
	}

	@SuppressWarnings({ "hiding", "unchecked" })
	public <Context> List<ICodeGeneratorTask<Context>> getGeneratorTasks() {
		List<ICodeGeneratorTask<Context>> modules = new ArrayList<ICodeGeneratorTask<Context>>();
		modules.add((ICodeGeneratorTask<Context>) mavenDefaultFolders);
		modules.add((ICodeGeneratorTask<Context>) pomFileGenerator);
		return modules;
	}
}
