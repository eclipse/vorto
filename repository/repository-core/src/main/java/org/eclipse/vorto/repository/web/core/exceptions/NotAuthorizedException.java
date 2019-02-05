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
package org.eclipse.vorto.repository.web.core.exceptions;

import org.eclipse.vorto.model.ModelId;

public class NotAuthorizedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ModelId secureResource = null;
	
	public NotAuthorizedException(ModelId modelId, Throwable t) {
		super(t);
		this.secureResource = modelId;
	}
	
	public NotAuthorizedException(ModelId modelId) {
	  super();
	  this.secureResource = modelId;
	}
	
	public ModelId getModelId() {
		return this.secureResource;
	}

}
