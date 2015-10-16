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

import java.util.Collection;
import java.util.Collections;

import org.eclipse.vorto.core.model.ModelId;

/**
 * We need this views because of the difference in how data is structured between API
 * in the client and server.
 */
public class UploadResultView {
	String handleId;
	String errorMessage;
	boolean valid;
	ModelView modelResource;
	Collection<ModelId> unresolvedReferences = Collections.emptyList();

	public UploadResultView() {

	}
	
	public boolean statusOk() {
		return handleId != null && valid;
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

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public Collection<ModelId> getUnresolvedReferences() {
		return unresolvedReferences;
	}

	public void setUnresolvedReferences(Collection<ModelId> unresolvedReferences) {
		this.unresolvedReferences = unresolvedReferences;
	}

	@Override
	public String toString() {
		return "UploadResult [handleId=" + handleId + ", errorMessage=" + errorMessage + ", valid=" + valid
				+ ", modelResource=" + modelResource + "]";
	}
}
