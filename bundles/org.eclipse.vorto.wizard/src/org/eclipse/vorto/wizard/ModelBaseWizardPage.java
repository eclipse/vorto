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

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.vorto.codegen.ui.context.IModelProjectContext;
import org.eclipse.vorto.core.ui.model.IModelProject;

public abstract class ModelBaseWizardPage extends AbstractWizardPage  implements IModelProjectContext {

	private IModelProject modelProject;
	
	protected ModelBaseWizardPage(String pageName, IModelProject modelProject) {
		super(pageName);
		this.modelProject = modelProject;
	}

	
	protected Text txtModelName;
	protected Text txtVersion;
	protected Text txtDescription;
	protected String workspaceLocation;
	protected Composite topContainer;
	protected Group grp;
	
	protected Text txtPlatform;

	public void createControl(Composite parent) {
		topContainer = new Composite(parent, SWT.NULL);

		setControl(topContainer);
		topContainer.setLayout(new GridLayout(1, false));

		grp = new Group(topContainer, SWT.NONE);
		grp.setText(getGroupTitle());
		grp.setLayout(new GridLayout(2, false));
		GridData gridGroup = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gridGroup.heightHint = 145;
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

		Label labelPlatform = new Label(grp, SWT.NONE);
		GridData labelLayoutData = new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1);
		labelPlatform.setLayoutData(labelLayoutData);
		labelPlatform.setText("Platform");
		labelPlatform.setVisible(isMappingModelWizard());
		labelLayoutData.exclude = !isMappingModelWizard();
		
		txtPlatform = new Text(grp, SWT.BORDER);
		GridData gridTxtPlatform = new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1);
		gridTxtPlatform.widthHint = 400;
		txtPlatform.setLayoutData(gridTxtPlatform);
		txtPlatform.setVisible(isMappingModelWizard());
		gridTxtPlatform.exclude = !isMappingModelWizard();
		txtPlatform.addModifyListener(new ModifyListener() {
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

		initialize();
		dialogChanged();
		setControl(topContainer);
	}

	protected void initialize() {
		txtModelName.setText(getDefaultModelName());
		txtVersion.setText(getDefaultVersion());
		txtDescription.setText(getDefaultDescription() + getDefaultModelName());
	}

	

	public void dialogChanged() {
		if (this.validateProject()) {
			this.setErrorMessage(null);
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}

	}

	public Composite getTopContainer() {
		return topContainer;
	}

	protected boolean validateProject() {
		boolean result = true;
		String modelName = getModelName();
		String fbVersion = getModelVersion();
		result &= validateStrExist(modelName,
				"Functionblock name must be specified");
		result &= checkModelName(modelName);
		result &= checkFBVersion(fbVersion);

		return result;
	}

	protected void modelNameChanged() {
		String modelName = getModelName();
		txtDescription.setText(getDefaultDescription() + modelName);
	}

	public String getWorkspaceLocation() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if (workspaceLocation == null) {
			workspaceLocation = workspace.getRoot().getLocation().toString();
		}
		return workspaceLocation;
	}

	protected boolean isMappingModelWizard() {
		return false;
	}

	public IModelProject getModelProject() {
		return modelProject;
	}

	public void setModelProject(IModelProject modelProject) {
		this.modelProject = modelProject;
	}

	@Override
	public String getProjectName() {
		return modelProject.getProject().getName();
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
