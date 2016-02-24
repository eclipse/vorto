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
package org.eclipse.vorto.codegen.ui.context;

public class ExampleProjectContext implements IGeneratorProjectContext {
	private String projectName;
	private String packageName;
	private String workspaceLocation;

	public ExampleProjectContext(String projectName, String packageName,
			String workspaceLocation) {
		setProjectName(projectName);
		setPackageName(packageName);
		setWorkspaceLocation(workspaceLocation);
	}

	public ExampleProjectContext(String workspaceLocation) {
		this.projectName = "org.eclipse.vorto.codegen.example";
		this.packageName = "demo";
		this.workspaceLocation = workspaceLocation;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setWorkspaceLocation(String workspaceLocation) {
		this.workspaceLocation = workspaceLocation;
	}

	@Override
	public String getGeneratorName() {
		return "JsonGenerator";
	}

	@Override
	public String getProjectName() {
		return projectName;
	}

	@Override
	public String getPackageName() {
		return packageName;
	}

	@Override
	public String getWorkspaceLocation() {
		return workspaceLocation;
	}

	@Override
	public String getPackageFolders() {
		return getPackageName();
	}

	@Override
	public boolean isGenerateTemplate() {
		return false;
	}

	@Override
	public boolean isGenerateExampleProject() {
		return true;
	}
}
