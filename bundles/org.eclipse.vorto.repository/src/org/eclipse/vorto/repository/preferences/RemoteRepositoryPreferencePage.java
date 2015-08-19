package org.eclipse.vorto.repository.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.vorto.repository.Activator;

public class RemoteRepositoryPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public RemoteRepositoryPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Repository Preferences");
	}
	
	public void createFieldEditors() {
		addField(
			new StringFieldEditor(PreferenceConstants.P_REMOTE_REPO_URL, "&Repository URL:", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	}
	
}