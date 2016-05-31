/*******************************************************************************
 *  Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.wizard.vorto;

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
import org.eclipse.vorto.codegen.ui.context.IProjectContext;
import org.eclipse.vorto.wizard.AbstractWizardPage;

public class VortoProjectWizardPage extends AbstractWizardPage implements IProjectContext {
	
	
	private Text txtProjectName;
	
	private String projectName;
	
	private Text txtWorkspaceLocation;
	private String workspaceLocation;

	public static final String PROJECTNAME_REGEX = "[^a-zA-Z0-9 \\._]";
	private static final String DEFAULT_PROJECT_NAME = "NewVortoProject";

	protected VortoProjectWizardPage(String pageName) {
		super(pageName);
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite topContainer = new Composite(parent, SWT.NULL);

		setControl(topContainer);
		topContainer.setLayout(new GridLayout(1, false));
		Group grpProjectDetails = new Group(topContainer, SWT.NONE);
		grpProjectDetails.setLayout(new GridLayout(3, false));
		GridData gridGrpProjectDetails = new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1);
		gridGrpProjectDetails.heightHint = 60;
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
		setControl(topContainer);		
		
	}
	
	private void initialize() {
		txtProjectName.setText(DEFAULT_PROJECT_NAME);
		txtWorkspaceLocation.setText(getWorkspaceLocation() + "/" + DEFAULT_PROJECT_NAME);

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
		this.projectName = getProjectName();
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
		result &= validateStrExist(projectName,
				"Project name must be specified");
		result &= validateExistingSameProjectName(projectName);
		result &= checkProjectName(projectName);
		return result;
	}
	
	public void updateWorkspaceLocationField(String directory) {
		txtWorkspaceLocation.setText(directory + "/" + getProjectName());
	}
	
	@Override
	public String getProjectName() {
		return txtProjectName.getText();
	}
	
	public String getProjName() {
		return this.projectName;
	}

	@Override
	public String getWorkspaceLocation() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if (workspaceLocation == null) {
			workspaceLocation = workspace.getRoot().getLocation().toString();
		}
		return workspaceLocation;
	}
}
