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
package org.eclipse.vorto.repository.model;

import org.eclipse.vorto.repository.validation.ValidationException;


public class UploadModelResult {
	private String handleId = null;
	private ModelResource modelResource = null;
	private boolean valid = false;
	private String errorMessage = null;

	private UploadModelResult(String handleId, ModelResource modelResource,
			boolean valid, String errorMessage) {
		super();
		this.handleId = handleId;
		this.modelResource = modelResource;
		this.valid = valid;
		this.errorMessage = errorMessage;
	}

	public static UploadModelResult invalid(ValidationException validationException) {
		return new UploadModelResult(null,validationException.getModelResource(), false, validationException.getMessage());
	}
	
	public static UploadModelResult invalid(ModelResource modelResource,String msg) {
		return new UploadModelResult(null,modelResource, false, msg);
	}
	
	public static UploadModelResult valid(String uploadHandle, ModelResource modelResource) {
		return new UploadModelResult(uploadHandle,modelResource, true,null);
	}

	public ModelResource getModelResource() {
		return modelResource;
	}

	public boolean isValid() {
		return valid;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getHandleId() {
		return handleId;
	}
		
	
}
