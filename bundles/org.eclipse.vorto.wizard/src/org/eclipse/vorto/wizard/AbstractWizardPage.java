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

import java.io.IOException;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;

import com.google.common.base.Strings;

public abstract class AbstractWizardPage extends WizardPage {

	public static final String PROJECTNAME_REGEX = "[^a-zA-Z0-9 \\._]";
	public static final String MODEL_NAME_REGEX = "[A-Z][a-zA-Z0-9_]*$";
	public static final String VERSION_REGEX = "^\\d+\\.\\d+\\.\\d+(-\\w+)*$";

	protected AbstractWizardPage(String pageName) {
		super(pageName);
	}
	
	protected AbstractWizardPage(String pageName, String title, ImageDescriptor descriptor) {
		super(pageName, title, descriptor);
	}

	protected String getWindowTitle() {
		return "";
	}

	protected boolean checkProjectName(String projectName) {
		if (checkForRegexPattern(projectName, true, PROJECTNAME_REGEX)) {
			setErrorMessage("Project name should not contain special characters.");
			return false;
		}
		return true;
	}

	protected boolean checkModelName(String modelName) {
		if (checkForRegexPattern(modelName, false, MODEL_NAME_REGEX)) {
			setErrorMessage("Model name should start with a capital letter and must not contain any special characters.");
			return false;
		}
		return true;
	}

	protected boolean checkFBVersion(String fbVersion) {
		if (checkForRegexPattern(fbVersion, false, VERSION_REGEX)) {
			setErrorMessage("Version should follow pattern <MAJOR>.<MINOR>.<PATCH>");
			return false;
		}
		return true;
	}

	protected boolean checkForRegexPattern(String input, boolean expectedBool,
			String regexPattern) {
		return Pattern.compile(regexPattern).matcher(input).matches() == expectedBool;
	}

	protected boolean validateStrExist(String string, String errorMsgToBeShown) {
		if (Strings.isNullOrEmpty(string)) {
			this.setErrorMessage(errorMsgToBeShown);
			return false;
		}
		return true;
	}

	protected boolean validateExistingSameProjectName(String projectName) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

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

	protected abstract String getProjectName();

}
