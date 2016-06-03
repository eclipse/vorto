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
package org.eclipse.vorto.wizard;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
import org.eclipse.vorto.codegen.ui.context.IModelProjectContext;
import org.eclipse.vorto.codegen.ui.progresstask.IProgressTask;
import org.eclipse.vorto.codegen.ui.tasks.EclipseProjectGenerator;
import org.eclipse.vorto.codegen.ui.tasks.LocationWrapper;
import org.eclipse.vorto.core.ui.model.IModelProject;

public abstract class ProjectCreationTask implements IProgressTask {

	public static final String WORK_SPACE = "WorkSpace";
	public static final String PROJECT_NAME = "ProjectName";

	private String errorMessage = "";

	private String projectName;
	private String workspaceLocation;

	public static final String XTEXT_NATURE = "org.eclipse.xtext.ui.shared.xtextNature";
	public static final String TARGET_JAVA_CLASS_FOLDER = "target/classes";
	public static final String ADDITIONAL_SOURCES_FOLDER = "target/generated-sources";

	private IModelProjectContext context = null;
	private IProject iproject = null;

	public ProjectCreationTask(IModelProjectContext context) {
		this.projectName = context.getProjectName();
		workspaceLocation = context.getWorkspaceLocation();
		this.context = context;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		try {
			EclipseProjectGenerator<IModelProjectContext> generator = new EclipseProjectGenerator<IModelProjectContext>(
					new LocationWrapper(workspaceLocation, projectName));
			generator.addNature(XTEXT_NATURE);
			for (String nature : getProjectNature()) {
				generator.addNature(nature);
			}
			generator.addTask(getCodeGeneratorTask());
			generator.generate(context, InvocationContext.simpleInvocationContext(), monitor);
			setIproject(generator.getProject());
			IModelProject modelProject = getIotproject(generator.getProject());

			modelProject.refresh(monitor);
			fireRefreshEvent(modelProject);

		} finally {
			monitor.done();
		}
	}

	private void fireRefreshEvent(IModelProject modelProject) {
		
	}

	@Override
	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getWorkspaceLocation() {
		return workspaceLocation;
	}

	public void setWorkspaceLocation(String workspaceLocation) {
		this.workspaceLocation = workspaceLocation;
	}

	protected abstract IModelProject getIotproject(IProject project);

	protected abstract ICodeGeneratorTask<IModelProjectContext> getCodeGeneratorTask();

	protected abstract String[] getProjectNature();

	public IProject getIproject() {
		return iproject;
	}

	protected void setIproject(IProject iproject) {
		this.iproject = iproject;
	}
}
