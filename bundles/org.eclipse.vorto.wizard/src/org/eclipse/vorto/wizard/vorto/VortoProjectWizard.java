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
package org.eclipse.vorto.wizard.vorto;

import org.eclipse.ui.INewWizard;
import org.eclipse.vorto.codegen.ui.progresstask.ProgressTaskExecutionService;
import org.eclipse.vorto.wizard.AbstractVortoWizard;
import org.eclipse.vorto.wizard.VortoProjectCreationTask;

public class VortoProjectWizard extends AbstractVortoWizard implements INewWizard {

	
	protected static final String VORTO_WIZARD_TITLE = "New Vorto Project";

	private VortoProjectWizardPage vortoWizardPage;

	public VortoProjectWizard() {
		super();
		setWindowTitle(VORTO_WIZARD_TITLE);
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		vortoWizardPage = new VortoProjectWizardPage("Vorto Model Project Wizard");
		vortoWizardPage.setTitle("Create new Vorto Project");
		vortoWizardPage
				.setDescription("Please enter the details for creating new Vorto project.");
		addPage(vortoWizardPage);
	}

	@Override
	public boolean performFinish() {
		ProgressTaskExecutionService progressTaskExecutionService = ProgressTaskExecutionService
				.getProgressTaskExecutionService();
		progressTaskExecutionService.syncRun(new VortoProjectCreationTask(vortoWizardPage));
		return true;
	}
}