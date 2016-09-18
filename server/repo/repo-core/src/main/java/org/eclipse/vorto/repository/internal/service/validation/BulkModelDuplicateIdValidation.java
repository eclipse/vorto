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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.service.IModelRepository;

public class BulkModelDuplicateIdValidation extends ModelReferencesValidation {
	
	private List<ModelId> zipModelIds;
	
	public BulkModelDuplicateIdValidation(IModelRepository modelRepository, Set<ModelResource> modelResources) {
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
		Set<ModelId> nonduplicates = new HashSet<>();
		
		for (ModelId modelId : zipModelIds) {
			if (modelResource.getId().equals(modelId) && nonduplicates.contains(modelId)) {
				throw new ValidationException("Zip contains models with duplicate Ids", modelResource);
			} else {
				nonduplicates.add(modelId);
			}
		}
	}
}