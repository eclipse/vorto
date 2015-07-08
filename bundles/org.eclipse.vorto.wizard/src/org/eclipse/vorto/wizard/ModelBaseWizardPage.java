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

package org.eclipse.vorto.wizard;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class ModelBaseWizardPage extends AbstractWizardPage  {

	protected ModelBaseWizardPage(String pageName) {
		super(pageName);
	}

	
	private Text txtModelName;
	private Text txtVersion;
	private Text txtProjectName;
	private Text txtWorkspaceLocation;
	private Text txtDescription;
	private String workspaceLocation;

	public void createControl(Composite parent) {
		Composite topContainer = new Composite(parent, SWT.NULL);

		setControl(topContainer);
		topContainer.setLayout(new GridLayout(1, false));

		Group grp = new Group(topContainer, SWT.NONE);
		grp.setText(getGroupTitle());
		grp.setLayout(new GridLayout(2, false));
		GridData gridGroup = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gridGroup.heightHint = 134;
		gridGroup.widthHint = 570;
		grp.setLayoutData(gridGroup);

		Label lblModelName = new Label(grp, SWT.NONE);
		lblModelName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblModelName.setText(getModelLabel());

		txtModelName = new Text(grp, SWT.BORDER);
		GridData gridTxtFunctionBlockName = new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1);
		gridTxtFunctionBlockName.widthHint = 400;
		txtModelName.setLayoutData(gridTxtFunctionBlockName);
		txtModelName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				modelNameChanged();
				dialogChanged();

			}
		});

		Label lblVersion = new Label(grp, SWT.NONE);
		lblVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblVersion.setText("Version:");

		txtVersion = new Text(grp, SWT.BORDER);
		GridData gridTxtVersion = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gridTxtVersion.widthHint = 411;
		txtVersion.setLayoutData(gridTxtVersion);
		txtVersion.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				modelNameChanged();
				dialogChanged();
			}
		});

		Label lblDescription = new Label(grp, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblDescription.setText("Description:");

		txtDescription = new Text(grp, SWT.BORDER | SWT.V_SCROLL
				| SWT.MULTI);
		GridData gridTxtDescription = new GridData(SWT.FILL, SWT.TOP, true,
				false, 1, 1);
		gridTxtDescription.heightHint = 53;
		txtDescription.setLayoutData(gridTxtDescription);

		Group grpProjectDetails = new Group(topContainer, SWT.NONE);
		grpProjectDetails.setLayout(new GridLayout(3, false));
		GridData gridGrpProjectDetails = new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1);
		gridGrpProjectDetails.heightHint = 97;
		gridGrpProjectDetails.widthHint = 575;
		grpProjectDetails.setLayoutData(gridGrpProjectDetails);
		grpProjectDetails.setText("Project Details");

		Label lblProjectName = new Label(grpProjectDetails, SWT.NONE);
		GridData gridLblProjectName = new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1);
		gridLblProjectName.widthHint = 78;
		lblProjectName.setLayoutData(gridLblProjectName);
		lblProjectName.setText("Project Name:");

		txtProjectName = new Text(grpProjectDetails, SWT.BORDER);
		GridData gridTxtProjectName = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gridTxtProjectName.widthHint = 370;
		txtProjectName.setLayoutData(gridTxtProjectName);
		txtProjectName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				projectNameChanged();
				dialogChanged();
			}
		});
		new Label(grpProjectDetails, SWT.NONE);

		Label lblLocation = new Label(grpProjectDetails, SWT.NONE);
		GridData gridLblLocation = new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1);
		gridLblLocation.widthHint = 48;
		lblLocation.setLayoutData(gridLblLocation);
		lblLocation.setText("Location:");

		txtWorkspaceLocation = new Text(grpProjectDetails, SWT.BORDER);
		txtWorkspaceLocation.setEditable(false);
		GridData gridTxtLocation = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gridTxtLocation.widthHint = 385;
		txtWorkspaceLocation.setLayoutData(gridTxtLocation);

		Button btnBrowse = new Button(grpProjectDetails, SWT.NONE);
		btnBrowse.setText("Browse...");
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse(e);
			}

		});
		initialize();
		dialogChanged();
		setControl(topContainer);
	}

	private void initialize() {
		txtModelName.setText(getDefaultModelName());
		txtVersion.setText(getDefaultVersion());
		txtProjectName.setText(getDefaultModelName());
		txtWorkspaceLocation.setText(getWorkspaceLocation() + "/"
				+ getDefaultModelName());
		txtDescription.setText(getDefaultDescription() + getDefaultModelName());

	}

	protected void handleBrowse(SelectionEvent e) {
		DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
		directoryDialog.setFilterPath(workspaceLocation);
		directoryDialog.setText("Workspace folder selection");
		directoryDialog.setMessage("Select a directory for this project");

		String selectedDirectory = directoryDialog.open();
		selectedDirectory = StringUtils.replace(selectedDirectory, "\\", "/");

		if (selectedDirectory != null) {
			workspaceLocation = selectedDirectory;
			updateWorkspaceLocationField(workspaceLocation);
			dialogChanged();
		}
	}

	private void projectNameChanged() {
		txtWorkspaceLocation.setText(getWorkspaceLocation() + "/"
				+ getProjectName());
	}

	public void dialogChanged() {
		if (this.validateProject()) {
			this.setErrorMessage(null);
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}

	}

	protected boolean validateProject() {
		boolean result = true;
		String projectName = getProjectName();
		String modelName = getModelName();
		String fbVersion = getModelVersion();

		result &= validateStrExist(projectName,
				"Project name must be specified");
		result &= validateStrExist(modelName,
				"Functionblock name must be specified");
		result &= validateExistingSameProjectName(projectName);

		result &= checkProjectName(projectName);
		
		result &= checkModelName(modelName);
		result &= checkFBVersion(fbVersion);

		return result;
	}

	public void updateWorkspaceLocationField(String directory) {
		txtWorkspaceLocation.setText(directory + "/" + getProjectName());
	}

	private void modelNameChanged() {
		String modelName = getModelName();
		txtProjectName.setText(modelName);
		txtWorkspaceLocation.setText(getWorkspaceLocation() + "/" + modelName);
		txtDescription.setText(getDefaultDescription() + modelName);
	}

	@Override
	public String getWorkspaceLocation() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if (workspaceLocation == null) {
			workspaceLocation = workspace.getRoot().getLocation().toString();
		}
		return workspaceLocation;
	}

	@Override
	public String getProjectName() {
		return txtProjectName.getText();
	}

	@Override
	public String getModelVersion() {
		return txtVersion.getText();
	}

	@Override
	public String getModelName() {
		return txtModelName.getText();
	}

	@Override
	public String getModelDescription() {
		return txtDescription.getText();
	}

	protected abstract String getDefaultVersion();

	protected abstract String getDefaultDescription();

	protected abstract String getDefaultModelName();
	
	protected abstract String getGroupTitle();
	
	protected abstract String getModelLabel();
}
