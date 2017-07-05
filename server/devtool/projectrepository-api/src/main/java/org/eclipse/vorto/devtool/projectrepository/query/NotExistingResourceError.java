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
package org.eclipse.vorto.devtool.projectrepository.query;

import org.eclipse.vorto.devtool.projectrepository.ProjectRepositoryException;

/**
 * This exception is thrown when a resource that is referred to is not existant.
 */
public class NotExistingResourceError extends ProjectRepositoryException {


	/**
	 * Constructor.
	 * 
	 * @param message
	 *            exception details
	 */
	public NotExistingResourceError(String message) {
		super(message);
	}
}

/* EOF */
