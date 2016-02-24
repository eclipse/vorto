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
package org.eclipse.vorto.repository.internal.service.validation;

import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.eclipse.vorto.repository.validation.IModelValidator;
import org.eclipse.vorto.repository.validation.ValidationException;

public class ModelReferencesValidation implements IModelValidator {

	private IModelRepository modelRepository;
	
	public ModelReferencesValidation(IModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}
	
	@Override
	public void validate(ModelResource modelResource)
			throws ValidationException {
		if (!modelResource.getReferences().isEmpty()) {
			checkReferencesRecursive(modelResource);
		}
	}
	
	private void checkReferencesRecursive(ModelResource modelResource) {
		for (ModelId modelId : modelResource.getReferences()) {
			ModelResource reference = modelRepository.getById(modelId);
			if (reference == null) {
				throw new CouldNotResolveReferenceException(modelResource, modelId);
			}
			checkReferencesRecursive(reference);
		}
	}
	
	protected static class CouldNotResolveReferenceException extends ValidationException {

		public CouldNotResolveReferenceException(ModelResource resource, ModelId faultyReference) {
			super(createErrorMessage(resource, faultyReference), resource);
		}	
	}

	private static String createErrorMessage(ModelResource resource,
			ModelId faultyReference) {	
		return String.format("Cannot resolve reference %s", faultyReference.getPrettyFormat());
	}

}
