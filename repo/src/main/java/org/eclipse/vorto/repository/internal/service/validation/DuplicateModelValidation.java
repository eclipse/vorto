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

import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.eclipse.vorto.repository.validation.IModelValidator;
import org.eclipse.vorto.repository.validation.ValidationException;

public class DuplicateModelValidation implements IModelValidator {

	private IModelRepository modelRepository;

	public DuplicateModelValidation(IModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}
	
	@Override
	public void validate(ModelResource modelResource)
			throws ValidationException {
		if (modelRepository.getById(modelResource.getId()) != null) {
			throw new ValidationException("Model already exists", modelResource);
		}
	}

}
