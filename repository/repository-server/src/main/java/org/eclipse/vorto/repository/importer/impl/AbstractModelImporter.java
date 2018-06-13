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
package org.eclipse.vorto.repository.importer.impl;

import java.util.UUID;

import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.impl.ITemporaryStorage;
import org.eclipse.vorto.repository.core.impl.StorageItem;
import org.eclipse.vorto.repository.importer.IModelImporter;
import org.springframework.beans.factory.annotation.Autowired;

abstract public class AbstractModelImporter implements IModelImporter {

	public static final long TTL_TEMP_STORAGE_INSECONDS = 60 * 5;
	
	@Autowired
	private ITemporaryStorage uploadStorage;
	
	@Autowired
	private IModelRepository modelRepository;
	
	@Autowired
	private IUserRepository userRepository;
	
	/*
	protected class ValidationResult {
		boolean valid;
		Object resource;
		Object validationException;
	}
	
	abstract protected ValidationResult validate(byte[] content, String filename, UserContext user);
	
	abstract protected Object stagingSuccessDetailObject(ValidationResult result);
	
	abstract protected String stagingFailureErrorMessage(ValidationResult result);
	
	abstract protected Object stagingFailureDetailObject(ValidationResult result);
	
	@Override
	public StagingResult stageModel(byte[] content, String filename, UserContext user) {
		
		ValidationResult result =  validate(content, filename, user);

		if (result.valid) {
			String stagingId = stageContent(content, filename);
			
			return StagingResult.success(getId(), stagingId, stagingSuccessDetailObject(result));
		} else {
			return StagingResult.fail(getId(), stagingFailureErrorMessage(result), stagingFailureDetailObject(result));
		}
	}*/
	
	@Override
	public boolean canHandle(String stagingId) {
		StorageItem item = uploadStorage.get(stagingId);
		return canHandle((byte []) item.getValue(), item.getKey());
	}
	
	protected String stageContent(byte[] content, String fileName) {
		final String handleId = UUID.randomUUID().toString() + fileName;
		return uploadStorage.store(handleId, content, TTL_TEMP_STORAGE_INSECONDS).getKey();
	}

	public ITemporaryStorage getUploadStorage() {
		return uploadStorage;
	}

	public IModelRepository getModelRepository() {
		return modelRepository;
	}

	public IUserRepository getUserRepository() {
		return userRepository;
	}

}
