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

public class UploadResult {
	String handleId;
	String errorMessage;
	ModelView modelResource;

	public UploadResult() {

	}
	
	public boolean statusOk() {
		return handleId != null && errorMessage == null;
	}

	public String getHandleId() {
		return handleId;
	}

	public void setHandleId(String handleId) {
		this.handleId = handleId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ModelView getModelResource() {
		return modelResource;
	}

	public void setModelResource(ModelView modelResource) {
		this.modelResource = modelResource;
	}

}
