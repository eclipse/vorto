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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.vorto.codegen.ui.context.IModelProjectContext;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelIdFactory;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.ui.model.IModelProject;

import com.google.common.base.Strings;

public abstract class ModelBaseWizardPage extends AbstractWizardPage  implements IModelProjectContext {

	private IModelProject modelProject;
	
	protected ModelBaseWizardPage(String pageName, IModelProject modelProject) {
		super(pageName);
		this.modelProject = modelProject;
	}

	protected ModifyListener modificationListener = new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			modelNameChanged();
			dialogChanged();
		};
	};
	
	private Text txtModelName;
	private Text txtNamespace;
	private Text txtVersion;
	private Text txtDescription;
	private String workspaceLocation;

	public void createControl(Composite parent) {
		Composite topContainer = new Composite(parent, SWT.NULL);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		topContainer.setLayout(fillLayout);

		Group grp = new Group(topContainer, SWT.NONE);
		grp.setText(getGroupTitle());
		grp.setLayout(new GridLayout(2, false));
		
		txtModelName = newLabeledText(grp, getModelLabel(), SWT.BORDER, newTxtGridData(400, null), modificationListener);
		
		txtNamespace = newLabeledText(grp, "Namespace:", SWT.BORDER, newTxtGridData(400, null), modificationListener);

		decorate(grp);
		
		txtVersion = newLabeledText(grp, "Version:", SWT.BORDER, newTxtGridData(400, null), modificationListener);

		txtDescription = newLabeledText(grp, "Description:", SWT.BORDER | SWT.V_SCROLL | SWT.MULTI, newTxtGridData(null, 53), null);

		initialize();
		dialogChanged();
		setControl(topContainer);
	}
	
	protected Text newLabeledText(Composite parent, String labelStr, int style, Object layoutData, ModifyListener listener) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		label.setText(labelStr);
		
		Text text = new Text(parent, style);
		text.setLayoutData(layoutData);
		if (listener != null) {
			text.addModifyListener(listener);
		}
		
		return text;
	}

	protected GridData newTxtGridData(Integer widthHint, Integer heightHint) {
		GridData gridData = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		if (widthHint != null) {
			gridData.widthHint = widthHint;
		}
		if (heightHint != null) {
			gridData.heightHint = heightHint;
		}
		return gridData;
	}

	protected void initialize() {
		txtModelName.setText(getDefaultModelName());
		txtVersion.setText(getDefaultVersion());
		txtDescription.setText(getDefaultDescription() + getDefaultModelName());
		txtNamespace.setText(getDefaultNamespace());
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
		
		result &= checkModelName(txtModelName.getText());
		result &= checkNamespace(txtNamespace.getText());
		result &= checkVersion(txtVersion.getText());

		return result;
	}
	
	private boolean checkNamespace(String nameSpace) {
		if (Strings.nullToEmpty(nameSpace).trim().isEmpty()) {
			setErrorMessage("Model namespace must not be empty.");
			return false;
		}
		
		/*
		if (checkForRegexPattern(nameSpace, false, MODEL_NAME_REGEX)) {
			setErrorMessage("Model namespace should start with a capital letter and must not contain any special characters.");
			return false;
		}*/
		
		return true;
	}

	protected boolean checkModelName(String modelName) {
		if (Strings.nullToEmpty(modelName).trim().isEmpty()) {
			setErrorMessage("Model name must not be empty.");
			return false;
		}
		
		if (checkForRegexPattern(modelName, false, MODEL_NAME_REGEX)) {
			setErrorMessage("Model name should start with a capital letter and must not contain any special characters.");
			return false;
		}
		return true;
	}

	protected boolean checkVersion(String version) {
		if (Strings.nullToEmpty(version).trim().isEmpty()) {
			setErrorMessage("Model version must not be empty.");
			return false;
		}
		
		if (checkForRegexPattern(version, false, VERSION_REGEX)) {
			setErrorMessage("Model version should follow pattern <MAJOR>.<MINOR>.<PATCH>");
			return false;
		}
		return true;
	}

	protected void modelNameChanged() {
		String modelName = txtModelName.getText();
		txtDescription.setText(getDefaultDescription() + modelName);
	}

	public String getWorkspaceLocation() {
		if (workspaceLocation == null) {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			workspaceLocation = workspace.getRoot().getLocation().toString();
		}
		return workspaceLocation;
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
	public String getModelDescription() {
		return txtDescription.getText();
	}
	
	protected ModelId getModelId(ModelType modelType) {
		return ModelIdFactory.newInstance(modelType, txtNamespace.getText(), txtVersion.getText(), txtModelName.getText());
	}
	
	protected void decorate(Composite parent) {};

	protected abstract String getDefaultVersion();

	protected abstract String getDefaultDescription();

	protected abstract String getDefaultModelName();
	
	protected abstract String getGroupTitle();
	
	protected abstract String getModelLabel();
	
	protected abstract String getDefaultNamespace();
}
