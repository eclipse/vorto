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
package org.eclipse.vorto.repository.internal.service.validation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.vorto.repository.internal.service.validation.exception.CouldNotResolveReferenceException;
import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.eclipse.vorto.repository.validation.IModelValidator;
import org.eclipse.vorto.repository.validation.ValidationException;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ModelReferencesValidation implements IModelValidator {

	private IModelRepository modelRepository;
	
	public ModelReferencesValidation(IModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}
	
	public IModelRepository getModelRepository() {
		return modelRepository;
	}
	
	@Override
	public void validate(ModelResource modelResource)
			throws ValidationException {
		List<ModelId> missingReferences = new ArrayList<ModelId>();
		if (!modelResource.getReferences().isEmpty()) {
			checkReferencesRecursive(modelResource, missingReferences);
		}
		
		if (!missingReferences.isEmpty()) {
			throw new CouldNotResolveReferenceException(modelResource, missingReferences);
		}
	}
	
	private void checkReferencesRecursive(ModelResource modelResource, List<ModelId> accumulator) {
		for (ModelId modelId : modelResource.getReferences()) {
			ModelResource reference = modelRepository.getById(modelId);
			if (reference == null) {
				accumulator.add(modelId);
			} else {
				checkReferencesRecursive(reference, accumulator);
			}
		}
	}
}
