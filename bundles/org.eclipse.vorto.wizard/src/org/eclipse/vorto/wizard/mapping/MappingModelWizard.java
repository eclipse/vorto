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
package org.eclipse.vorto.wizard.mapping;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.vorto.codegen.api.DefaultMappingContext;
import org.eclipse.vorto.codegen.ui.handler.ModelGenerationTask;
import org.eclipse.vorto.codegen.ui.tasks.ProjectFileOutputter;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.ui.model.IModelProject;
import org.eclipse.vorto.wizard.AbstractVortoWizard;
import org.eclipse.vorto.wizard.infomodel.InfomodelTemplateFileContent;

public class MappingModelWizard extends AbstractVortoWizard implements INewWizard {
	
	private MappingModellWizardPage iotWizardPage;

	private String modelFolder = "informationmodels/";

	private IModelProject modelProject;
	
	public MappingModelWizard(IModelProject modelProject) {
		this.modelProject = modelProject;
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		iotWizardPage = new MappingModellWizardPage("Mapping Model Wizard",modelProject);
		iotWizardPage.setTitle("Create Mapping Model");
		iotWizardPage
				.setDescription("Please enter the details for creating mapping model.");
		addPage(iotWizardPage);
	}

	public boolean performFinish() {
		
		new ModelGenerationTask(ModelType.Mapping.getExtension(), new InfomodelTemplateFileContent(), modelFolder).generate(iotWizardPage,
				new DefaultMappingContext(), new ProjectFileOutputter(this.modelProject.getProject()));

		openFBModelWithDefaultEditor();
		return true;
	}

	private void openFBModelWithDefaultEditor() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject project = workspace.getRoot().getProject(
				iotWizardPage.getProjectName());

		String modelName = iotWizardPage.getModelName();
		final IFile modelfile = project.getFile(modelFolder
				+ modelName + ModelType.Mapping.getExtension());

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow activeWindow = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();
				if (activeWindow != null) {
					IWorkbenchPage page = activeWindow.getActivePage();
					if (page != null) {
						try {
							IDE.openEditor(page, modelfile);
						} catch (PartInitException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		});

	}

	public MappingModellWizardPage getIotWizardPage() {
		return iotWizardPage;
	}

	public void setIotWizardPage(MappingModellWizardPage iotWizardPage) {
		this.iotWizardPage = iotWizardPage;
	}

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
	}
}