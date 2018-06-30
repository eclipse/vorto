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
package org.eclipse.vorto.repository.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.vorto.repository.Activator;

public class RemoteRepositoryPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {	
	
	public RemoteRepositoryPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Information Model Repository Preferences");
	}
	
	public void createFieldEditors() {
		addField(new StringFieldEditor(ConnectionInfoPreferences.P_REMOTE_REPO_URL, "&URL:", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	}
	
}