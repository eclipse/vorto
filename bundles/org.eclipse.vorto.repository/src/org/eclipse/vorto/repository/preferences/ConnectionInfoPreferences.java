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

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.vorto.repository.Activator;
import org.eclipse.vorto.repository.ConnectionInfo;

public class ConnectionInfoPreferences implements ConnectionInfo, MutableConnectionInfo {
	
    static final String P_REMOTE_REPO_URL = "org.eclipse.vorto.preferences.repo.url";
	static final String P_USER_REPO = "org.eclipse.vorto.preferences.repo.user";
	static final String P_PASS_REPO = "org.eclipse.vorto.preferences.repo.pass";

	private IPreferenceStore preferences;
	
	public ConnectionInfoPreferences() {
		this.preferences = Activator.getDefault().getPreferenceStore();
	}
	
	@Override
	public String getUrl() {
		return this.preferences.getString(P_REMOTE_REPO_URL);
	}

	@Override
	public String getUserName() {
		return this.preferences.getString(P_USER_REPO);
	}

	@Override
	public String getPassword() {
		return this.preferences.getString(P_PASS_REPO);
	}

	@Override
	public void setUrl(String url) {
		this.preferences.putValue(P_REMOTE_REPO_URL, url);
		
	}

	@Override
	public void setUser(String user) {
		this.preferences.putValue(P_USER_REPO, user);
	}

	@Override
	public void setPassword(String password) {
		this.preferences.putValue(P_PASS_REPO, password);
	}
	
	
}
