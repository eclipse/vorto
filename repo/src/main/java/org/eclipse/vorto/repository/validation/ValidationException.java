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
package org.eclipse.vorto.repository.validation;

import org.eclipse.vorto.repository.model.ModelResource;

public class ValidationException extends RuntimeException {

	private ModelResource modelResource = null;
	
	public ValidationException(String msg, ModelResource modelResource) {
		super(msg);
		this.modelResource = modelResource;
	}
	
	public ValidationException(String msg, ModelResource modelResource, Throwable t) {
		super(msg,t);
		this.modelResource = modelResource;
	}
	
	public ModelResource getModelResource() {
		return modelResource;
	}
}
