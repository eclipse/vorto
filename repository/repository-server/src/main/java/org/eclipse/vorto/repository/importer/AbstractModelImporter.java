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
package org.eclipse.vorto.repository.importer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.upload.UploadModelResult;
import org.eclipse.vorto.repository.api.upload.ValidationReport;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.ITemporaryStorage;
import org.eclipse.vorto.repository.core.impl.ModelEMFResource;
import org.eclipse.vorto.repository.core.impl.StorageItem;
import org.eclipse.vorto.repository.core.impl.utils.DependencyManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Extend this class for Implementation of a special importer for Vorto
 *
 */
public abstract class AbstractModelImporter implements IModelImporter {

	public static final long TTL_TEMP_STORAGE_INSECONDS = 60 * 5;
	
	@Autowired
	private ITemporaryStorage uploadStorage;
	
	@Autowired
	private IModelRepository modelRepository;
	
	@Autowired
	private IUserRepository userRepository;


	public ITemporaryStorage getUploadStorage() {
		return uploadStorage;
	}

	public IModelRepository getModelRepository() {
		return modelRepository;
	}

	public IUserRepository getUserRepository() {
		return userRepository;
	}

	@Override
	public UploadModelResult upload(FileUpload fileUpload, IUserContext user) {
		ValidationReport report = this.validate(fileUpload, user);
		if (report.isValid()) {
			return new UploadModelResult(createUploadHandle(fileUpload), report);
		} else {
			return new UploadModelResult(null,report);
		}
	}
	
	private String createUploadHandle(FileUpload fileUpload) {
		final String handleId = UUID.randomUUID().toString() + fileUpload.getFileExtension();
		return this.uploadStorage.store(handleId, fileUpload, TTL_TEMP_STORAGE_INSECONDS).getKey();
	}

	@Override
	public List<ModelInfo> doImport(String uploadHandleId, IUserContext user) {
		StorageItem uploadedItem = this.uploadStorage.get(uploadHandleId);

		if (uploadedItem == null) {
			throw new ModelImporterException("No uploaded file found for handleId '" + uploadHandleId + "'");
		}
		
		List<ModelInfo> importedModels = new ArrayList<>();
		
		try {
			List<ModelEMFResource> resources = this.convert(uploadedItem.getValue(), user);
			DependencyManager dm = new DependencyManager();
			for (ModelEMFResource resource : resources) {
				dm.addResource(resource);
			}
			
			dm.getSorted().stream().forEach(resource -> {
				try {
					ModelInfo importedModel = this.modelRepository.save(resource.getId(), ((ModelEMFResource)resource).toDSL(), createFileName(resource), user);
					this.modelRepository.addFileContent(resource.getId(),new FileContent(uploadedItem.getValue().getFileName(),uploadedItem.getValue().getContent()));
					importedModels.add(importedModel);
				} catch (Exception e) {
					throw new ModelImporterException("Problem importing model",e);
				}
			});
		} finally {
			this.uploadStorage.remove(uploadHandleId);
		}
	
		return importedModels;
	}
	
	private String createFileName(ModelInfo resource) {
		return resource.getId().getName() + resource.getType().getExtension();
	}

	/**
	 * validates the given fileUpload content
	 * @param content
	 * @param fileName
	 * @param user
	 * @return
	 */
	protected abstract ValidationReport validate(FileUpload fileUpload, IUserContext user);
	
	/**
	 * converts the given file upload content to Vorto DSL content
	 * @param fileUpload
	 * @param user
	 * @return Vorto DSL content
	 */
	protected abstract List<ModelEMFResource> convert(FileUpload fileUpload, IUserContext user);

	public void setUploadStorage(ITemporaryStorage uploadStorage) {
		this.uploadStorage = uploadStorage;
	}

	public void setModelRepository(IModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	public void setUserRepository(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	
}
