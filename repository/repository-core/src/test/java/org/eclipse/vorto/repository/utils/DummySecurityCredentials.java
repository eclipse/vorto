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

import java.util.Arrays;
import javax.jcr.Credentials;

public class DummySecurityCredentials implements Credentials {

	private static final long serialVersionUID = 1L;

	private String username;
	private String[] roles;
	
	public DummySecurityCredentials(String username, String... roles) {
		this.username = username;
		this.roles = roles;
	}

	public String getUsername(){
	  return username;
	}
	
	public boolean hasRole(String role) {
	  return Arrays.asList(roles).contains(role);
	}
}
