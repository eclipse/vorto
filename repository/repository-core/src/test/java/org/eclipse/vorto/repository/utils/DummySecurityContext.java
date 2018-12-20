/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.utils;

import org.modeshape.jcr.security.SecurityContext;

public class DummySecurityContext implements SecurityContext {

	private DummySecurityCredentials credentials;

	public DummySecurityContext(DummySecurityCredentials credentials) {
		this.credentials = credentials;
	}

	@Override
	public boolean isAnonymous() {
		return false;
	}

	@Override
	public String getUserName() {
		return credentials.getUsername();
	}

	@Override
	public boolean hasRole(String roleName) {
	  return true;
	}

	@Override
	public void logout() {
	  //should do nothing as it is a dummy context
	}

}
