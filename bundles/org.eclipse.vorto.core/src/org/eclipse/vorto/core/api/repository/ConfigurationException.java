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

public class ConfigurationException extends RepositoryException {

	/**
	 * Used to throw ConfigurationExceptions (e.g. when the URL to the
	 * repository is wrong)
	 */
	private static final long serialVersionUID = 12L;

	public ConfigurationException() {
		super("Please make sure that the URL to the repository is entered correctly "
				+ "in your Eclipse Settings (Window -> Preferences -> Vorto -> Information Model Repository).");
	}

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
}
