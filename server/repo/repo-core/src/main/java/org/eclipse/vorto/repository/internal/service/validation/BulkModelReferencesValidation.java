/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.vorto.repository.internal.service.validation.exception.CouldNotResolveReferenceException;
import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.eclipse.vorto.repository.validation.ValidationException;

/**
 * Validation class for multiple file model upload/checkin.
 * @author Nagavijay Sivakumar - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class BulkModelReferencesValidation extends ModelReferencesValidation {
	
	private List<ModelId> zipModelIds;
	
	public BulkModelReferencesValidation(IModelRepository modelRepository, Set<ModelResource> modelResources) {
		super(modelRepository);
		zipModelIds = modelResources.stream().map(new java.util.function.Function<ModelResource, ModelId>() {
			@Override
			public ModelId apply(ModelResource resource) {
				return resource.getId();
			}

		}).collect(Collectors.toList());
	}

	@Override
	public void validate(ModelResource modelResource) throws ValidationException {
		validateInRepository(modelResource);
		//Validate other references in zip files.
		validateInZipFiles(modelResource);
	}

	private List<ModelId> validateInRepository(ModelResource modelResource) {
		List<ModelId> missingReferences = new ArrayList<ModelId>();
		try {
			super.validate(modelResource);
		} catch (CouldNotResolveReferenceException e) {
			return e.getMissingReferences();
		}
		return missingReferences;
	}

	private boolean isNotInRepository(ModelId modelId) {
		return getModelRepository().getById(modelId) == null;
		
	}

	private void validateInZipFiles(ModelResource modelResource) {
		List<ModelId> references = modelResource.getReferences();
		List<ModelId> missingReferences = new ArrayList<ModelId>();
		for (ModelId modelId : references) {
			if(!zipModelIds.contains(modelId)) {
				if(isNotInRepository(modelId)) {
					missingReferences.add(modelId);
				}	
			}
		}
		if(missingReferences.size() > 0)
			throw new CouldNotResolveReferenceException(modelResource, missingReferences);
	}	
}
