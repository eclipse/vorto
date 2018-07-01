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
package org.eclipse.vorto.wizard.functionblock;

import org.eclipse.ui.INewWizard;
import org.eclipse.vorto.codegen.api.InvocationContext;
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
		openTypeWithDefaultEditor(iotWizardPage);
		return true;
	}

//	private void openFBModelWithDefaultEditor() {
//		IWorkspace workspace = ResourcesPlugin.getWorkspace();
//		IProject project = workspace.getRoot().getProject(
//				iotWizardPage.getProjectName());
//
//		final IModelProject modelProject = ModelProjectFactory.getInstance().getProject(project);
//
//		final IFile modelFile = modelProject.getModelElementById(iotWizardPage.getModelId()).getModelFile();
//		
//		Display.getDefault().asyncExec(new Runnable() {
//			@Override
//			public void run() {
//				IWorkbenchWindow activeWindow = PlatformUI.getWorkbench()
//						.getActiveWorkbenchWindow();
//				if (activeWindow != null) {
//					IWorkbenchPage page = activeWindow.getActivePage();
//					if (page != null) {
//						try {
//							IDE.openEditor(page, modelFile);
//						} catch (PartInitException e) {
//							throw new RuntimeException(e);
//						}
//					}
//				}
//			}
//		});
//
//	}
}
