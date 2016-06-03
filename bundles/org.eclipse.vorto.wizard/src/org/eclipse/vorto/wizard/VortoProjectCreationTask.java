/*******************************************************************************
 *  Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
import org.eclipse.vorto.codegen.ui.context.IProjectContext;
import org.eclipse.vorto.codegen.ui.progresstask.IProgressTask;
import org.eclipse.vorto.codegen.ui.tasks.EclipseProjectGenerator;
import org.eclipse.vorto.codegen.ui.tasks.LocationWrapper;
import org.eclipse.vorto.core.ui.model.nature.VortoProjectNature;

public class VortoProjectCreationTask implements IProgressTask {

	
	private static final String ERROR_MESSAGE = "Problem when creating project, error: ";

	private String errorMessage = "";

	private String projectName;
	private String workspaceLocation;

	public static final String XTEXT_NATURE = "org.eclipse.xtext.ui.shared.xtextNature";
	public static final String[] ALL_MODEL_FOLDERS = {"datatypes", "functionblocks", "infomodels","mappings"};

	private IProjectContext context = null;

	public VortoProjectCreationTask(IProjectContext context) {
		this.projectName = context.getProjectName();
		workspaceLocation = context.getWorkspaceLocation();
		this.context = context;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		try {
			EclipseProjectGenerator<IProjectContext> generator = new EclipseProjectGenerator<IProjectContext>(
					new LocationWrapper(workspaceLocation, projectName));
			generator.addNature(XTEXT_NATURE);
			generator.addNature(VortoProjectNature.VORTO_NATURE);
			for (String folder : ALL_MODEL_FOLDERS) {
				generator.addFolder(folder);
			}
			generator.generate(context, InvocationContext.simpleInvocationContext(), monitor);
			
			IProject iProject = generator.getProject();
			iProject.refreshLocal(IResource.DEPTH_ONE, monitor);

		} catch (CoreException e) {
			errorMessage = e.getMessage();
			throw new RuntimeException(ERROR_MESSAGE + e.getMessage(), e);
		} finally {
			monitor.done();
		}
	}

	@Override
	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
