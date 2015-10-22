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
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.vorto.codegen.api.context.IModelProjectContext;
import org.eclipse.vorto.codegen.api.tasks.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.ui.ModelGenerationTask;
import org.eclipse.vorto.codegen.ui.progresstask.ProgressTaskExecutionService;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.model.nature.InformationModelProjectNature;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;
import org.eclipse.vorto.wizard.AbstractVortoWizard;
import org.eclipse.vorto.wizard.ProjectCreationTask;

public class MappingModelWizard extends AbstractVortoWizard implements INewWizard {
	private static final String SUFFIX = ".mapping";
	private static final String MAPPING_PATH = "src/mappings/";

	private MappingModellWizardPage iotWizardPage;

	public MappingModelWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		iotWizardPage = new MappingModellWizardPage("Mapping Model Wizard");
		iotWizardPage.setTitle("Create Mapping Model");
		iotWizardPage
				.setDescription("Please enter the details for creating mapping model.");
		addPage(iotWizardPage);
	}

	public boolean performFinish() {
		ProgressTaskExecutionService progressTaskExecutionService = ProgressTaskExecutionService
				.getProgressTaskExecutionService();

		progressTaskExecutionService.syncRun(new ProjectCreationTask(
				iotWizardPage) {
			@Override
			public IModelProject getIotproject(IProject project) {
				return ModelProjectServiceFactory.getDefault()
						.getProjectFromEclipseProject(project);
			}

			@Override
			protected ICodeGeneratorTask<IModelProjectContext> getCodeGeneratorTask() {
				return new ModelGenerationTask(SUFFIX,
						new MappingModelTemplateFileContent()) {
					@Override
					public String getPath(IModelProjectContext ctx) {
						return MAPPING_PATH;
					}
				};
			}

			@Override
			protected String[] getProjectNature() {
				return new String[] { };
			}
		});

		openModelWithDefaultEditor();

		return true;
	}

	private void openModelWithDefaultEditor() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject project = workspace.getRoot().getProject(
				iotWizardPage.getProjectName());

		String modelName = iotWizardPage.getModelName();
		final IFile modelfile = project.getFile(MAPPING_PATH
				+ modelName + SUFFIX);

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

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
	}
}