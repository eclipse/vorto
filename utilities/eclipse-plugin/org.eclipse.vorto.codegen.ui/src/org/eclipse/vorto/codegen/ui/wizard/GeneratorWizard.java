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
package org.eclipse.vorto.codegen.ui.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.vorto.codegen.ui.context.IGeneratorProjectContext;
import org.eclipse.vorto.codegen.ui.progresstask.ProgressTaskExecutionService;
import org.eclipse.vorto.codegen.ui.utils.WizardUtil;

public class GeneratorWizard extends Wizard implements INewWizard,
		IExecutableExtension {

	private static final String SLASH = "/";
	private static final String SRC_FOLDER = "src/";
	private static final String XTEND_FILE_ENDING = ".xtend";

	private GeneratorWizardPage generatorWizardPage;
	private IConfigurationElement configurationElement;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
	public void addPages() {
		generatorWizardPage = new GeneratorWizardPage("wizardPage");
		generatorWizardPage.setTitle("New Code Generator Project");
		generatorWizardPage
				.setDescription("Please enter the details for creating the project.");
		addPage(generatorWizardPage);
	}

	@Override
	public boolean performFinish() {

		ProgressTaskExecutionService progressTaskExecutionService = ProgressTaskExecutionService
				.getProgressTaskExecutionService();
		progressTaskExecutionService.syncRun(new ProjectCreationTask(
				generatorWizardPage));

		openFileWithDefaultEditor(generatorWizardPage.getPackageName()+"."+generatorWizardPage.getGeneratorName().toLowerCase(),
				getPath(generatorWizardPage));

		BasicNewProjectResourceWizard
				.updatePerspective(getConfigurationElement());
		return true;
	}

	private String getPath(IGeneratorProjectContext metaData) {
		return SRC_FOLDER + metaData.getPackageFolders() + SLASH
				+ metaData.getGeneratorName() + XTEND_FILE_ENDING;
	}

	private void openFileWithDefaultEditor(String projectName, String path) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject project = workspace.getRoot().getProject(projectName);

		String fileName = path;

		final IFile fbfile = project.getFile(fileName);

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

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {

		this.configurationElement = config;

	}

	public IConfigurationElement getConfigurationElement() {
		if (this.configurationElement != null) {
			return this.configurationElement;
		} else {
			return WizardUtil.getWizardConfigurationElement(this.getClass()
					.getName());
		}
	}
}