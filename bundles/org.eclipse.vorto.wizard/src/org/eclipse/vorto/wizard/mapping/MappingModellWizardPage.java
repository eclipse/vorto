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
package org.eclipse.vorto.wizard.mapping;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;
import org.eclipse.vorto.wizard.AbstractWizardPage;

public class MappingModellWizardPage extends AbstractWizardPage  {

	public static final String TARGETPLATFORM_REGEX = "[^a-zA-Z0-9\\._]";
	private static final String DEFAULT_VERSION = "1.0.0";
	private IModelProject selectedModelProject = null;
	
	protected MappingModellWizardPage(String pageName) {
		super(pageName);
	}
	
	private Text txtModelName;
	private Text txtTargetPlatform;
	private Text txtVersion;
	private Text txtDescription;

	public void createControl(Composite parent) {
		Composite topContainer = new Composite(parent, SWT.NULL);

		setControl(topContainer);
		topContainer.setLayout(new GridLayout(1, false));

		Group grp = new Group(topContainer, SWT.NONE);
		grp.setText(getGroupTitle());
		grp.setLayout(new GridLayout(2, false));
		GridData gridGroup = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gridGroup.heightHint = 164;
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
		

		Label lblTargetPlatform = new Label(grp, SWT.NONE);
		lblTargetPlatform.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblTargetPlatform.setText("Target Platform:");
		
		txtTargetPlatform = new Text(grp, SWT.BORDER);
		GridData gridTxtTargetPlatform = new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1);
		gridTxtTargetPlatform.widthHint = 400;
		txtTargetPlatform.setLayoutData(gridTxtTargetPlatform);

		txtTargetPlatform.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				targetPlatformChanged();
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
		setControl(topContainer);
		
		dialogChanged();
	}

	protected void initialize() {
		this.selectedModelProject= ModelProjectServiceFactory.getDefault().getProjectFromSelection();		
		txtModelName.setText(getDefaultModelName());
		txtVersion.setText(getDefaultVersion());
		txtDescription.setText(getDefaultDescription());
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

		String modelName = getModelName();
		String modelVersion = getModelVersion();
		String targetPlatform = this.getTargetPlatform();

		result &= validateStrExist(modelName,
				"Model name must be specified");

		result &= validateStrExist(targetPlatform,
				"Target platform must be specified");
		
		result &= checkModelName(modelName);
		result &= checkMappingModelNotExist();
		result &= checkFBVersion(modelVersion);

		return result;
	}

	protected boolean checkMappingModelNotExist(){
		String mappingFile = this.txtModelName.getText() + ".mapping";
		if(this.getSelectedModelProject().getProject().getFile("src/mappings/" + mappingFile).exists()){
			setErrorMessage("Mapping " + mappingFile + " already exist!");
			return false;
		}else {
			return true;
		}
	}
	
	protected boolean checktTargetPlatform(String TargetPlatform) {
		if (checkForRegexPattern(TargetPlatform, true, TARGETPLATFORM_REGEX)) {
			setErrorMessage("Target Platform should not contain special characters.");
			return false;
		}
		return true;
	}
	
	protected void targetPlatformChanged() {
		String targetPlatform = this.getTargetPlatform();
		this.txtDescription.setText(getDefaultDescription());
		this.txtModelName.setText(this.getDefaultModelName() + "_" + targetPlatform);
	}

	protected void setModelDescription(String desc){
		txtDescription.setText(desc);
	}
	
	@Override
	public String getWorkspaceLocation() {
		return getSelectedModelProject().getProject().getWorkspace().toString();
	}

	@Override
	public String getProjectName() {		
		return getSelectedModelProject().getProject().getName();
	}
	
	public IModelProject getSelectedModelProject(){
		return this.selectedModelProject;
	}

	@Override
	public String getModelVersion() {
		return txtVersion.getText();
	}

	@Override
	public String getModelName() {
		return txtModelName.getText();
	}

	public String getTargetPlatform() {
		return this.txtTargetPlatform.getText();
	}
	
	@Override
	public String getModelDescription() {
		return txtDescription.getText();
	}

	protected String getDefaultVersion() {
		return this.selectedModelProject.getModel().getVersion();
	}

	protected String getDefaultDescription() {
		return this.selectedModelProject.getModel().getName() + " to " + this.getTargetPlatform() + " mapping";
	}

	protected String getDefaultModelName() {
		return  this.selectedModelProject.getModel().getName();
	}

	protected String getGroupTitle() {
		return "Mapping Details";
	}

	protected String getModelLabel() {
		return "Mapping Name:";
	}
}






