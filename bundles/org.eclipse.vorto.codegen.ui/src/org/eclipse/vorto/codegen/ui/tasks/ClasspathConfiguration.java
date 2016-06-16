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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.core.ui.MessageDisplayFactory;

/**
 * Configures the classpath and source folders for a Java Project
 * 
 */
public class ClasspathConfiguration implements IEclipseProjectConfiguration {

	private static final String JAVA_PATH = JavaRuntime.JRE_CONTAINER;
	private static final String PLUGIN_PATH = "org.eclipse.pde.core.requiredPlugins";

	private static final String DEFAULT_OUTPUT_LOCATION = "target";
	public static final IClasspathEntry PLUGIN = createClasspathEntry(PLUGIN_PATH);
	public static final IClasspathEntry JAVA_JRE = getVmClasspathEntry();
	private String[] sourceFolders = new String[0];
	private String outputLocation = "";

	private List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();

	public ClasspathConfiguration() {
		outputLocation = DEFAULT_OUTPUT_LOCATION;
	}

	private static IClasspathEntry createClasspathEntry(String path) {
		return JavaCore.newContainerEntry(new Path(path));
	}

	public IEclipseProjectConfiguration addEntry(IClasspathEntry entry) {
		this.entries.add(entry);
		return this;
	}

	public void setSourceFolders(String[] sourceFolders) {
		this.sourceFolders = sourceFolders;
	}

	public void setOutputLocation(String outputLocation) {
		this.outputLocation = outputLocation;
	}

	@Override
	public IEclipseProjectConfiguration configure(IProject project) {
		IJavaProject javaProject = JavaCore.create(project);
		try {
			javaProject.setRawClasspath(getClasspathEntries(javaProject),
					new NullProgressMonitor());
			javaProject.setOutputLocation(project.getFolder(outputLocation)
					.getFullPath(), new NullProgressMonitor());
		} catch (JavaModelException e1) {
			MessageDisplayFactory.getMessageDisplay().displayError(
					e1.getMessage());
		}

		return this;
	}

	private IClasspathEntry[] getClasspathEntries(IJavaProject project) {

		List<IClasspathEntry> newEntries = new ArrayList<IClasspathEntry>();

		for (int i = 0; i < sourceFolders.length; i++) {
			newEntries.add(JavaCore.newSourceEntry((project.getProject()
					.getFolder(sourceFolders[i]).getFullPath())));
		}
		newEntries.addAll(entries);

		return newEntries.toArray(new IClasspathEntry[newEntries.size()]);
	}

	private static IClasspathEntry getVmClasspathEntry() {
		IPath containerPath = new Path(JAVA_PATH);
		IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		IPath vmPath = containerPath.append(
				vmInstall.getVMInstallType().getId()).append(
				vmInstall.getName());
		return JavaCore.newContainerEntry(vmPath);
	}

	@Override
	public <Context> List<ICodeGeneratorTask<Context>> getGeneratorTasks() {
		return Collections.emptyList();
	}
}
