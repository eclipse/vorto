/*******************************************************************************
 *  Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.ui.wizard;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardPage;
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
import org.eclipse.vorto.codegen.ui.context.IGeneratorProjectContext;

import com.google.common.base.Strings;

public class GeneratorWizardPage extends WizardPage implements
		IGeneratorProjectContext {

	private static final String DEFAULT_PACKAGE_NAME = "org.eclipse.vorto.example";
	private static final String SELECT_DIRECTORY = "Select a directory for this project";
	private static final String WORKSPACE_FOLDER_SELECTION = "Workspace folder selection";
	public static final String PROJECTNAME_REGEX = "[^a-zA-Z0-9 \\._]";
	public static final String DEFAULT_PROJECT_NAME = "MyGenerator";
	private static final boolean MICRO_SERVICE_SUPPORT_DEFAULT = true;
	private Text txtProjectName;
	private Text txtWorkspaceLocation;
	private Text txtPackageName;
	private String workspaceLocation;
	private boolean isMicroServiceSelected;

	public GeneratorWizardPage(String pageName) {
		super(pageName);
		setTitle("New Code Generator Project");
		setMessage("Create a new Code Generator Project");
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite topContainer = new Composite(parent, SWT.NULL);

		setControl(topContainer);
		topContainer.setLayout(new GridLayout(1, false));

		Group grpGeneratorProject = new Group(topContainer, SWT.NONE);
		grpGeneratorProject.setText("ProjectDetails");
		grpGeneratorProject.setLayout(new GridLayout(3, false));
		GridData gridGroupGeneratorProject = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gridGroupGeneratorProject.heightHint = 163;
		gridGroupGeneratorProject.widthHint = 570;
		grpGeneratorProject.setLayoutData(gridGroupGeneratorProject);

		Label lblProjectName = new Label(grpGeneratorProject, SWT.NONE);
		lblProjectName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblProjectName.setText("Project Name:");

		txtProjectName = new Text(grpGeneratorProject, SWT.BORDER);
		GridData gridTxtProjectName = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gridTxtProjectName.widthHint = 304;
		txtProjectName.setLayoutData(gridTxtProjectName);
		txtProjectName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				projectNameChanged();
				try {
					dialogChanged();
				} catch (IOException e1) {
					throw new RuntimeException();
				}
			}
		});
		new Label(grpGeneratorProject, SWT.NONE);

		Label lblPackageName = new Label(grpGeneratorProject, SWT.NONE);
		lblPackageName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblPackageName.setText("Package Name:");

		txtPackageName = new Text(grpGeneratorProject, SWT.BORDER);
		GridData gd_txtDemo = new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1);
		gd_txtDemo.widthHint = 79;
		txtPackageName.setLayoutData(gd_txtDemo);
		new Label(grpGeneratorProject, SWT.NONE);

		Label lblVersion = new Label(grpGeneratorProject, SWT.NONE);
		lblVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblVersion.setText("Location:");

		txtWorkspaceLocation = new Text(grpGeneratorProject, SWT.BORDER);
		GridData gd_txtWorkspaceLocation = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_txtWorkspaceLocation.widthHint = 403;
		txtWorkspaceLocation.setLayoutData(gd_txtWorkspaceLocation);
		txtWorkspaceLocation.setEditable(false);

		Button btnBrowse = new Button(grpGeneratorProject, SWT.NONE);
		btnBrowse.setText("Browse...");
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					handleBrowse(e);
				} catch (IOException e1) {
					throw new RuntimeException(e1);
				}
			}
		});
		new Label(grpGeneratorProject, SWT.NONE);

		Button btnCreateMicroService = new Button(grpGeneratorProject, SWT.CHECK);
		btnCreateMicroService
				.setToolTipText("This option adds support for running the generator as a server-side micro-service.");
		btnCreateMicroService.setSelection(MICRO_SERVICE_SUPPORT_DEFAULT);
		btnCreateMicroService.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isMicroServiceSelected = !isMicroServiceSelected;
			}
		});
		btnCreateMicroService.setText(" Server-side Generation Support");
		new Label(grpGeneratorProject, SWT.NONE);

		initialize();
		try {
			dialogChanged();
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		setControl(topContainer);
	}

	private void initialize() {
		txtProjectName.setText(DEFAULT_PROJECT_NAME);
		txtWorkspaceLocation.setText(getWorkspaceLocation() + "/"
				+ DEFAULT_PROJECT_NAME);
		txtPackageName.setText(DEFAULT_PACKAGE_NAME);
		isMicroServiceSelected = MICRO_SERVICE_SUPPORT_DEFAULT;
	}

	private void handleBrowse(SelectionEvent e) throws IOException {
		DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
		directoryDialog.setFilterPath(workspaceLocation);
		directoryDialog.setText(WORKSPACE_FOLDER_SELECTION);
		directoryDialog.setMessage(SELECT_DIRECTORY);

		String selectedDirectory = directoryDialog.open();
		setDirectoryToWorkspaceField(selectedDirectory);
	}

	private void setDirectoryToWorkspaceField(String selectedDirectory)
			throws IOException {
		selectedDirectory = StringUtils.replace(selectedDirectory, "\\", "/");

		if (selectedDirectory != null) {
			workspaceLocation = selectedDirectory;
			updateWorkspaceLocationField(workspaceLocation);
			dialogChanged();
		}
	}

	public void dialogChanged() throws IOException {
		if (this.validateProject()) {
			this.setErrorMessage(null);
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}

	}

	private boolean validateProject() throws IOException {
		boolean result = true;
		String projectName = getProjectName();

		result &= validateStrExist(projectName,
				"Project name must be specified");
		result &= validateExistingSameProjectName();

		if (checkForRegexPattern(projectName, true, PROJECTNAME_REGEX)) {
			setErrorMessage("Project name should not contain special characters.");
			result = false;
		}

		return result;
	}

	private boolean checkForRegexPattern(String input, boolean expectedBool,
			String regexPattern) {
		return Pattern.compile(regexPattern).matcher(input).find() == expectedBool;
	}

	private boolean validateStrExist(String string, String errorMsgToBeShown) {
		if (Strings.isNullOrEmpty(string)) {
			this.setErrorMessage(errorMsgToBeShown);
			return false;
		}
		return true;
	}

	private boolean validateExistingSameProjectName() throws IOException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		String projectName = getProjectName();

		if (workspace.getRoot().getProject(getProjectName()).exists()) {
			setErrorMessage("Project " + getProjectName() + " already exists.");
			return false;
		}

		IPath projectLocation = ResourcesPlugin.getWorkspace().getRoot()
				.getLocation().append(projectName);
		if (projectLocation.toFile().exists()) {
			String canonicalPath = projectLocation.toFile().getCanonicalPath();
			projectLocation = new Path(canonicalPath);

			String existingName = projectLocation.lastSegment();
			if (!existingName.equals(projectName)) {
				setErrorMessage("Project " + getProjectName()
						+ " already exists.");
				return false;
			}
		}

		return true;
	}

	public void updateWorkspaceLocationField(String directory) {
		txtWorkspaceLocation.setText(directory + "/" + getProjectName());
	}

	private void projectNameChanged() {
		String projectName = getProjectName();
		txtWorkspaceLocation
				.setText(getWorkspaceLocation() + "/" + projectName);
	}

	public String getWorkspaceLocation() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if (workspaceLocation == null) {
			workspaceLocation = workspace.getRoot().getLocation().toString();
		}
		return workspaceLocation;
	}

	public String getGeneratorName() {
		return txtProjectName.getText();
	}

	public String getProjectName() {
		return txtProjectName.getText();
	}

	public String getPackageName() {
		return txtPackageName.getText();
	}

	public String getPackageFolders() {
		return getPackageName().replaceAll("\\.", "/");
	}

	public boolean isMicroServiceSupport() {
		return isMicroServiceSelected;
	}
}
