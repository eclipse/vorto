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

import org.eclipse.vorto.repository.internal.service.validation.exception.CouldNotResolveReferenceException;
import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.eclipse.vorto.repository.validation.ValidationException;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * Validation class for multiple file model upload/checkin.
 * @author Nagavijay Sivakumar - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class BulkModelReferencesValidation extends ModelReferencesValidation {
	
	private List<ModelId> zipModelIds;
	
	public BulkModelReferencesValidation(IModelRepository modelRepository, List<ModelResource> modelResources) {
		super(modelRepository);
		Function<ModelResource, ModelId> modelIdFilter = new Function<ModelResource, ModelId>() {
			@Override
			public ModelId apply(ModelResource resource) {
				return resource.getId();
			}
		};
		zipModelIds = Lists.transform(modelResources, modelIdFilter);
	}

	@Override
	public void validate(ModelResource modelResource) throws ValidationException {
		validateInRepository(modelResource);
		//Validate other references in zip files.
		validateInZipFiles(modelResource);
	}

	private List<ModelId> validateInRepository(ModelResource modelResource) {
		List<ModelId> missingReference = new ArrayList<ModelId>();
		try {
			super.validate(modelResource);
		} catch (CouldNotResolveReferenceException e) {
			return e.getMissingReferences();
		}
		return missingReference;
	}

	private boolean isNotInRepository(ModelId modelId) {
		return getModelRepository().getById(modelId) == null;
		
	}

	private void validateInZipFiles(ModelResource modelResource) {
		List<ModelId> references = modelResource.getReferences();
		for (ModelId modelId : references) {
			if(!zipModelIds.contains(modelId)) {
				if(isNotInRepository(modelId)) {
					throw new ValidationException("Cannot resolve reference :" + modelId.getPrettyFormat() , modelResource);
				}	
			}
		}
	}	
}
