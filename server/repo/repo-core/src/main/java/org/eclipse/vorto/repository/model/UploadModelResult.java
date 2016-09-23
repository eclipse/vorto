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
package org.eclipse.vorto.repository.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.vorto.repository.internal.service.validation.ValidationException;
import org.eclipse.vorto.repository.internal.service.validation.exception.CouldNotResolveReferenceException;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class UploadModelResult {
	private String handleId = null;
	private ModelResource modelResource = null;
	private boolean valid = false;
	private String errorMessage = null;
	private Collection<ModelId> unresolvedReferences;

	private UploadModelResult(String handleId, ModelResource modelResource, boolean valid, String errorMessage) {
		super();
		this.handleId = handleId;
		this.modelResource = modelResource;
		this.valid = valid;
		this.errorMessage = errorMessage;
	}

	private UploadModelResult(String handleId, ModelResource modelResource, boolean valid, String errorMessage,
			Collection<ModelId> missingReferences) {
		this(handleId, modelResource, valid, errorMessage);
		this.unresolvedReferences = missingReferences;
	}

	public static UploadModelResult invalid(ValidationException... validationExceptions) {
		Objects.requireNonNull(validationExceptions);
		if (validationExceptions.length <= 0) {
			throw new IllegalArgumentException("There ought to be more than 1 validation exception for this function.");
		}
		
		StringBuffer errorMessage = new StringBuffer();
		List<ModelId> missingReferences = Collections.emptyList();
		for(int i=0; i < validationExceptions.length; i++) {
			if (validationExceptions[i] instanceof CouldNotResolveReferenceException) {
				missingReferences = ((CouldNotResolveReferenceException) validationExceptions[i]).getMissingReferences();
			}
			
			if (errorMessage.length() != 0) {
				errorMessage.append("\n");
			}
			errorMessage.append((i + 1));
			errorMessage.append(") ");
			errorMessage.append(validationExceptions[i].getMessage());
		}
		
		return new UploadModelResult(null, validationExceptions[0].getModelResource(), false,
				errorMessage.toString(), missingReferences);
	}
	
	public static UploadModelResult invalid(CouldNotResolveReferenceException validationException) {
		return new UploadModelResult(null, validationException.getModelResource(), false,
				validationException.getMessage(), validationException.getMissingReferences());
	}

	public static UploadModelResult invalid(ValidationException validationException) {
		return new UploadModelResult(null, validationException.getModelResource(), false,
				validationException.getMessage());
	}

	public static UploadModelResult invalid(ModelResource modelResource, String msg) {
		return new UploadModelResult(null, modelResource, false, msg);
	}

	public static UploadModelResult valid(String uploadHandle, ModelResource modelResource) {
		return new UploadModelResult(uploadHandle, modelResource, true, null);
	}
	

	public static UploadModelResult valid(ModelResource modelResource) {
		return new UploadModelResult(null, modelResource, true, null);
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

	public Collection<ModelId> getUnresolvedReferences() {
		return unresolvedReferences;
	}

	@Override
	public String toString() {
		return "UploadModelResult [handleId=" + handleId + ", modelResource=" + modelResource + ", valid=" + valid
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((modelResource == null) ? 0 : modelResource.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UploadModelResult other = (UploadModelResult) obj;
		if (modelResource == null) {
			if (other.modelResource != null)
				return false;
		} else if (!modelResource.equals(other.modelResource))
			return false;
		return true;
	}
	
	
}
