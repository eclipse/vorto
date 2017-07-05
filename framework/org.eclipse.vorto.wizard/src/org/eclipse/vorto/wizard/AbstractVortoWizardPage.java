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
package org.eclipse.vorto.wizard;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.vorto.codegen.ui.context.IProjectContext;

import com.google.common.base.Strings;

public abstract class AbstractVortoWizardPage extends WizardPage implements
		IProjectContext {

	private String workspaceLocation;

	protected AbstractVortoWizardPage(String pageName) {
		super(pageName);
	}

	protected void dialogChanged() {
		if (this.validateProject()) {
			this.setErrorMessage(null);
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}

	}

	protected abstract boolean validateProject();

	protected abstract void updateWorkspaceLocationField(String directory);

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

	public String getWorkspaceLocation() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if (workspaceLocation == null) {
			workspaceLocation = workspace.getRoot().getLocation().toString();
		}
		return workspaceLocation;
	}

	protected boolean validateStrExist(String string, String errorMsgToBeShown) {
		if (Strings.isNullOrEmpty(string)) {
			this.setErrorMessage(errorMsgToBeShown);
			return false;
		}
		return true;
	}

	protected boolean validateLocation(String input, String errorMsg,
			String type) {
		if (input.lastIndexOf(".") < 0
				|| !input.substring(input.lastIndexOf(".") + 1, input.length())
						.equalsIgnoreCase(type)) {
			this.setErrorMessage("Invalid file location for " + errorMsg);
			return false;
		}
		File file = new File(input);
		if (file != null && file.exists() && !file.isDirectory()) {
			return true;
		} else {
			this.setErrorMessage(errorMsg + " file does not exist");
			return false;
		}
	}

	protected boolean validateExistingSameProjectName() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		String projectName = getProjectName();

		if (workspace.getRoot().getProject(getProjectName()).exists()) {
			setErrorMessage("Project " + getProjectName() + " already exists.");
			return false;
		}

		IPath projectLocation = ResourcesPlugin.getWorkspace().getRoot()
				.getLocation().append(projectName);
		if (projectLocation.toFile().exists()) {
			try {
				String canonicalPath = projectLocation.toFile()
						.getCanonicalPath();
				projectLocation = new Path(canonicalPath);

				String existingName = projectLocation.lastSegment();
				if (!existingName.equals(projectName)) {
					setErrorMessage("Project " + getProjectName()
							+ " already exists.");
					return false;
				}
			} catch (IOException e) {
			}

		}

		return true;
	}

	protected boolean validateNoSpaceForLocation(String location) {
		return location.contains(" ");
	}

	protected boolean checkForRegexPattern(String input, String regexPattern) {
		return Pattern.compile(regexPattern).matcher(input).find();
	}

}
