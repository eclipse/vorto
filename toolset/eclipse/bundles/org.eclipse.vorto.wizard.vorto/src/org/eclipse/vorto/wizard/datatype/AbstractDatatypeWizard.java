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
package org.eclipse.vorto.wizard.datatype;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.ui.handler.ModelGenerationTask;
import org.eclipse.vorto.codegen.ui.tasks.ProjectFileOutputter;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.ui.model.IModelProject;
import org.eclipse.vorto.wizard.AbstractVortoWizard;

public abstract class AbstractDatatypeWizard extends AbstractVortoWizard implements INewWizard {

	private IModelProject modelProject;

	public AbstractDatatypeWizard(IModelProject modelProject) {
		this.modelProject = modelProject;
	}

	private DatatypeWizardPage iotWizardPage;

	private String modelFolder = "datatypes/";

	private Datatype datatype;

	public void addPages() {
		iotWizardPage = new DatatypeWizardPage(datatype, "New Datatype", modelProject);
		setTitle("Create " + datatype.name().toLowerCase() + " type ");
		setDescription("Please enter the details for creating a " + datatype.name().toLowerCase() + " model.");
		addPage(iotWizardPage);
	}

	public boolean performFinish() {
		new ModelGenerationTask(ModelType.Datatype.getExtension(),
				new DataTypeFileTemplate(datatype.name().toLowerCase()), modelFolder).generate(iotWizardPage,
						InvocationContext.simpleInvocationContext(),
						new ProjectFileOutputter(this.modelProject.getProject()));
		openTypeWithDefaultEditor(iotWizardPage);
		return true;
	}

//	private void openDatatypeWithDefaultEditor() {
//		IWorkspace workspace = ResourcesPlugin.getWorkspace();
//		IProject project = workspace.getRoot().getProject(iotWizardPage.getProjectName());
//
//		final IModelProject modelProject = ModelProjectFactory.getInstance().getProject(project);
//
//		final IFile modelFile = modelProject.getModelElementById(iotWizardPage.getModelId()).getModelFile();
//
//		Display.getDefault().asyncExec(new Runnable() {
//			@Override
//			public void run() {
//				IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
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

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
	}

	protected void setDatatypeName(Datatype datatype) {
		this.datatype = datatype;
	}

	protected void setDescription(String description) {
		iotWizardPage.setDescription(description);
	}

	protected void setTitle(String title) {
		iotWizardPage.setTitle(title);
	}

}