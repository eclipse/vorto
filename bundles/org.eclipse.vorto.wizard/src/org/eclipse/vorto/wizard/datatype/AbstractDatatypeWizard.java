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

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.ui.context.IModelProjectContext;
import org.eclipse.vorto.codegen.ui.handler.ModelGenerationTask;
import org.eclipse.vorto.codegen.ui.progresstask.ProgressTaskExecutionService;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.model.nature.FbDatatypeProjectNature;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;
import org.eclipse.vorto.wizard.AbstractVortoWizard;
import org.eclipse.vorto.wizard.ProjectCreationTask;

public abstract class AbstractDatatypeWizard extends AbstractVortoWizard
		implements INewWizard {

	private DatatypeWizardPage iotWizardPage;

	private final String fileExt = ".type";

	private Datatype datatype;

	public void addPages() {
		iotWizardPage = new DatatypeWizardPage(datatype, "New Datatype");
		setTitle("Create " + datatype.name().toLowerCase() + " type ");
		setDescription("Please enter the details for creating a "
				+ datatype.name().toLowerCase() + " model.");
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
				return new ModelGenerationTask(fileExt,
						new DataTypeFileTemplate(datatype.name().toLowerCase()));
			}

			@Override
			protected String[] getProjectNature() {
				return new String[] { FbDatatypeProjectNature.NATURE_ID };
			}
		});

		BasicNewProjectResourceWizard
				.updatePerspective(getConfigurationElement());
		return true;
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