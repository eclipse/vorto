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
package org.eclipse.vorto.core.internal.model;

import org.eclipse.core.resources.IProject;

public class ModelNotFoundException extends IllegalArgumentException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Project that is throwing this exception, optional
	 */
	private IProject projectThrownFrom;

	public ModelNotFoundException(String message) {
		super(message);
	}

	public ModelNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModelNotFoundException(String message, IProject project) {
		super(message);
		this.projectThrownFrom = project;
	}

	public ModelNotFoundException(String message, Throwable cause,
			IProject project) {
		super(message, cause);
		this.projectThrownFrom = project;
	}

	public IProject getProjectThrownFrom() {
		return projectThrownFrom;
	}
}
