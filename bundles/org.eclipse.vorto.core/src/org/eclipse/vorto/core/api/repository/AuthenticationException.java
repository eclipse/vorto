/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/

package org.eclipse.vorto.core.api.repository;

/**
 * Used to throw an exception if the user is not logged in to the repository
 * appropriately (Window -> Preferences -> Vorto -> Information Model
 * Repository)
 */

public class AuthenticationException extends RepositoryException {

	private static final long serialVersionUID = 1L;

	public AuthenticationException() {
		super("Please make sure that you have signed-up for the repository and entered your login credentials "
				+ "correctly in your Eclipse Settings (Window -> Preferences -> Vorto -> Information Model Repository).");
	}

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

}
