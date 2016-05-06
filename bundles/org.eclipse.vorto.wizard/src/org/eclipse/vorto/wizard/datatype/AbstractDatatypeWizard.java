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
package org.eclipse.vorto.wizard.datatype;

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
import org.eclipse.vorto.wizard.AbstractVortoWizard;

public abstract class AbstractDatatypeWizard extends AbstractVortoWizard
		implements INewWizard {

	private DatatypeWizardPage iotWizardPage;

	private final String SUFFIX = ".type";
	
	private String modelFolder = "datatypes/";

	private Datatype datatype;

	public void addPages() {
		iotWizardPage = new DatatypeWizardPage(datatype, "New Datatype");
		setTitle("Create " + datatype.name().toLowerCase() + " type ");
		setDescription("Please enter the details for creating a "
				+ datatype.name().toLowerCase() + " model.");
		addPage(iotWizardPage);
	}

	public boolean performFinish() {
		new ModelGenerationTask(SUFFIX, new DataTypeFileTemplate(datatype.name().toLowerCase()), modelFolder).generate(iotWizardPage,
				new DefaultMappingContext(), new ProjectFileOutputter(iotWizardPage.getProject()));
		openDatatypeWithDefaultEditor();
		return true;
	}

	private void openDatatypeWithDefaultEditor() {
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