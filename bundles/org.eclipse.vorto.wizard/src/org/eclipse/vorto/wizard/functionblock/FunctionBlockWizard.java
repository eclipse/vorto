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
package org.eclipse.vorto.wizard.functionblock;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
import org.eclipse.vorto.codegen.ui.handler.ModelGenerationTask;
import org.eclipse.vorto.codegen.ui.tasks.ProjectFileOutputter;
import org.eclipse.vorto.core.ui.model.IModelProject;
import org.eclipse.vorto.wizard.AbstractVortoWizard;

public class FunctionBlockWizard extends AbstractVortoWizard implements
		INewWizard {
	
	private IModelProject modelProject;

	public FunctionBlockWizard(IModelProject modelProject) {
		this.modelProject = modelProject;
	}

	private static final String SUFFIX = ".fbmodel";
	private FunctionBlockWizardPage iotWizardPage;
	
	private String modelFolder = "functionblocks/";

	@Override
	public void addPages() {
		iotWizardPage = new FunctionBlockWizardPage("Function Block Wizard",modelProject);
		iotWizardPage.setTitle("Create Function Block Model");
		iotWizardPage
				.setDescription("Please enter the details for creating function block model project.");
		addPage(iotWizardPage);
	}

	@Override
	public boolean performFinish() {
		new ModelGenerationTask(SUFFIX, new FbmodelTemplateFileContent(), modelFolder).generate(iotWizardPage,
				InvocationContext.simpleInvocationContext(), new ProjectFileOutputter(this.modelProject.getProject()));
		openFBModelWithDefaultEditor();
		return true;
	}

	private void openFBModelWithDefaultEditor() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject project = workspace.getRoot().getProject(
				iotWizardPage.getProjectName());

		String fbName = iotWizardPage.getModelName();
		final IFile fbfile = project.getFile(modelFolder
				+ fbName + SUFFIX);

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow activeWindow = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();
				if (activeWindow != null) {
					IWorkbenchPage page = activeWindow.getActivePage();
					if (page != null) {
						try {
							IDE.openEditor(page, fbfile);
						} catch (PartInitException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		});

	}
}
