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

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;

/**
 * Securily stores preference settings in an encrypted store.
 *
 */
public class SecureConnectionInfoPreferences extends ConnectionInfoPreferences {

	private ISecurePreferences securePreferences;
	
	public SecureConnectionInfoPreferences() {
		super();
		this.securePreferences = SecurePreferencesFactory.getDefault();
	}
	
	@Override
	public String getPassword() {
		try {
			return this.securePreferences.get(P_PASS_REPO,"");
		} catch (StorageException e) {
			throw new RuntimeException("password could not be read from secure store",e);
		}
	}
	
	@Override
	public void setPassword(String password) {
		try {
			this.securePreferences.put(P_PASS_REPO, password, true);
			this.securePreferences.flush();
		} catch (Exception e) {
			throw new RuntimeException("Problem storing repository password",e);
		}
	}
}
