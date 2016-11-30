package org.eclipse.vorto.wizard;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
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

public abstract class AbstractProjectWizardPage extends AbstractWizardPage implements IProjectContext {

	private String projectName;
	private Text txtProjectName;
	private Text txtWorkspaceLocation;
	private String workspaceLocation;
	
	protected AbstractProjectWizardPage(String pageName, String title, ImageDescriptor descriptor) {
		super(pageName, title, descriptor);
	}
	
	protected AbstractProjectWizardPage(String pageName) {
		super(pageName);
	}

	
	protected abstract String getDefaultProjectName();
	
	
	private void initialize() {
		txtProjectName.setText(getDefaultProjectName());
		txtWorkspaceLocation.setText(getWorkspaceLocation() + "/" + getDefaultProjectName());

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
	
	@Override
	public String getProjectName() {
		return txtProjectName.getText();
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
