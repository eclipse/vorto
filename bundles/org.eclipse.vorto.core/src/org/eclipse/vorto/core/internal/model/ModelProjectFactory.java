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
package org.eclipse.vorto.core.internal.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.vorto.core.model.DatatypeModelProject;
import org.eclipse.vorto.core.model.FunctionblockModelProject;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.model.InformationModelProject;
import org.eclipse.vorto.core.parser.IModelParser;

/**
 * Factory to get model project instances from an Eclipse {@link IProject}
 * 
 */
public class ModelProjectFactory {

	private static ModelProjectFactory singleton = null;

	private IModelParser modelParser;

	private ModelProjectFactory() {
		this.modelParser = getModelParser();
	}

	private IModelParser getModelParser() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry()
				.getExtensionPoint(IModelParser.EXTENSIONPOINT_ID);
		IExtension[] modelParserExtensions = extensionPoint.getExtensions();
		if (modelParserExtensions.length == 0) {
			throw new RuntimeException("No Vorto Model Parser found!");
		} else if (modelParserExtensions.length > 1) {
			throw new RuntimeException(
					"More than 1 Vorto Model Parser found. Cannot decide which one to bind. ");
		}
		try {
			return (IModelParser) modelParserExtensions[0]
					.getConfigurationElements()[0]
					.createExecutableExtension("class");
		} catch (Exception e) {
			throw new RuntimeException("Problem binding Vorto Model Parser", e);
		}
	}

	public static ModelProjectFactory getInstance() {
		if (singleton == null) {
			singleton = new ModelProjectFactory();
		}

		return singleton;
	}

	/**
	 * creates a modelProject for the specified {@link IProject}
	 * 
	 * @param project
	 * @return
	 */
	public IModelProject getProject(IProject project) {
		if (DatatypeModelProject.isDatatypeModelProject(project)) {
			return new DatatypeModelProject(project, this.modelParser);
		} else if (FunctionblockModelProject
				.isFunctionBlockModelProject(project)) {
			return new FunctionblockModelProject(project, this.modelParser);
		} else if (InformationModelProject.isInformationModelProject(project)) {
			return new InformationModelProject(project, this.modelParser);
		} else {
			throw new IllegalArgumentException(
					"Project is not a valid Vorto Model Project");
		}
	}

	public IModelProject getProjectByName(String projectName) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);
		if (project == null) {
			throw new IllegalArgumentException("Project with name "
					+ projectName + " is closed or does not exist");
		}
		return getProject(project);
	}

	public IModelProject getProjectFromSelection() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null) {

			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow activeWorkbenchWindow = workbench
					.getActiveWorkbenchWindow();
			ISelectionService selectionService = activeWorkbenchWindow
					.getSelectionService();
			IStructuredSelection selection = null;
			selection = (IStructuredSelection) selectionService.getSelection();

			if (selection.isEmpty()) {
				return null;
			}

			return getProjectSelection(selection);
		}

		return null;
	}

	private IModelProject getProjectSelection(IStructuredSelection selection) {
		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof IModelProject) {
			return (IModelProject) firstElement;
		} else if (firstElement instanceof IResource) {
			IResource s = (IResource) firstElement;
			return getProject(s.getProject());
		} else if (firstElement instanceof IAdaptable) {
			IProject project = (IProject) ((IAdaptable) firstElement);
			return getProject(project);
		} else if (firstElement instanceof IProject) {
			return getProject((IProject) firstElement);
		} else {
			throw new IllegalStateException(
					"Could not retrieve Model Project from selection");
		}
	}
}
