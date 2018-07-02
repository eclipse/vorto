/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.repository.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.core.impl.validation.CouldNotResolveReferenceException;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;

public class ValidationReport {

	private ModelInfo model = null;
	private boolean valid = false;
	private String errorMessage = null;
	private Collection<ModelId> unresolvedReferences = new ArrayList<ModelId>();
	
	public ValidationReport(ModelInfo model, boolean valid, String errorMessage, Collection<ModelId> missingReferences) {
		super();
		this.model = model;
		this.valid = valid;
		this.errorMessage = errorMessage;
		this.unresolvedReferences.addAll(missingReferences);
	}
	
	public static ValidationReport invalid(ModelInfo model, String msg) {
		return new ValidationReport(model, false, msg,Collections.emptyList());
	}
	
	public static ValidationReport invalid(String msg) {
		return new ValidationReport(null, false, msg,Collections.emptyList());
	}
	
	public static ValidationReport invalid(ModelInfo model, String msg, Collection<ModelId> missingReferences) {
		return new ValidationReport(model, false, msg,missingReferences);
	}
	
	public static ValidationReport invalid(ModelInfo model, ValidationException exception) {
		if (exception instanceof CouldNotResolveReferenceException) {
			CouldNotResolveReferenceException ex = (CouldNotResolveReferenceException)exception;
			return new ValidationReport(model, false, exception.getMessage(),ex.getMissingReferences());

		} else {
			return new ValidationReport(model, false, exception.getMessage(),Collections.emptyList());

		}
	}

	public static ValidationReport valid(ModelInfo model) {
		return new ValidationReport(model, true, null, Collections.emptyList());
	}
	
	protected ValidationReport() {
		
	}

	public ModelInfo getModel() {
		return model;
	}

	public void setModel(ModelInfo model) {
		this.model = model;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Collection<ModelId> getUnresolvedReferences() {
		return unresolvedReferences;
	}

	public void setUnresolvedReferences(Collection<ModelId> unresolvedReferences) {
		this.unresolvedReferences = unresolvedReferences;
	}
	
	
}
