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
package org.eclipse.vorto.repository.core.impl.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.upload.UploadModelResult;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.impl.ITemporaryStorage;
import org.eclipse.vorto.repository.core.impl.InvocationContext;
import org.eclipse.vorto.repository.core.impl.ModelEMFResource;
import org.eclipse.vorto.repository.core.impl.UploadModelResultFactory;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.validation.BulkModelDuplicateIdValidation;
import org.eclipse.vorto.repository.core.impl.validation.BulkModelReferencesValidation;
import org.eclipse.vorto.repository.core.impl.validation.DuplicateModelValidation;
import org.eclipse.vorto.repository.core.impl.validation.IModelValidator;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.springframework.util.StringUtils;

public class BulkUploadHelper {

	public static final long TTL_TEMP_STORAGE_INSECONDS = 60 * 5;
	
	private IModelRepository repositoryService;

	private ITemporaryStorage uploadStorage;
	
	public BulkUploadHelper(IModelRepository modelRepository, ITemporaryStorage storage) {
		this.repositoryService = modelRepository;
		this.uploadStorage = storage;
	}

	public List<UploadModelResult> uploadMultiple(byte[] content, String zipFileName, String callerId) {
		if (!isValid(zipFileName)) {
			throw new FatalModelRepositoryException("Filename/type is invalid", null);
		}

		List<UploadModelResult> invalidResult = new ArrayList<UploadModelResult>();
		List<UploadModelResult> validResult = new ArrayList<UploadModelResult>();
		
		Set<ModelInfo> parsedModels = new HashSet<>();

		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(content));
		ZipEntry entry = null;
		
		try {
			while ((entry = zis.getNextEntry()) != null) {
				if (!entry.isDirectory()) {
					final String fileName = entry.getName();
					try {
						parsedModels.add(ModelParserFactory.getParser(fileName).parse(new ByteArrayInputStream(copyStream(zis,entry))));
					} catch (ValidationException grammarProblem) {
						invalidResult.add(UploadModelResultFactory.create(grammarProblem));
					}
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {

			List<IModelValidator> bulkUploadValidators = constructBulkUploadValidators(parsedModels);

			for (ModelInfo modelResource : parsedModels) {
				try {
					for (IModelValidator validator : bulkUploadValidators) {
						validator.validate(modelResource,InvocationContext.create(callerId));
					}
					
					UploadModelResult result = UploadModelResult.valid(modelResource);
					if (!invalidResult.contains(result)) {
						validResult.add(result);
					}
					
				} catch (ValidationException validationException) {
					invalidResult.add(UploadModelResultFactory.create(validationException));
				}
			}
			
			
			if (invalidResult.isEmpty()) {
				DependencyManager dm = new DependencyManager(parsedModels);
				return safelyUpload(dm.getSorted());
			} else {
				List<UploadModelResult> completeResult = new ArrayList<>();
				completeResult.addAll(invalidResult);
				completeResult.addAll(validResult);
				return completeResult;
			}

		} catch (Exception e) {
			throw new FatalModelRepositoryException("Invalid zip file", e);
		} 
	}

	/**
	 * @param zipFileName
	 */
	private List<UploadModelResult> safelyUpload(Collection<ModelInfo> resources) throws Exception {
		List<UploadModelResult> result = new ArrayList<UploadModelResult>();
		
		for (ModelInfo resource : resources) {
			result.add(UploadModelResult.valid(createUploadHandle(((ModelEMFResource)resource).toDSL(),resource.getType()), resource));
		}
		
		return result;
	}
	
	private String createUploadHandle(byte[] content, ModelType type) {
		final String handleId = UUID.randomUUID().toString() + type.getExtension();
		return this.uploadStorage.store(handleId, content, TTL_TEMP_STORAGE_INSECONDS).getKey();
	}
	
	private boolean isValid(String file) {
		return !StringUtils.isEmpty(file) && StringUtils.endsWithIgnoreCase(file, ".zip");
	}

	private List<IModelValidator> constructBulkUploadValidators(Set<ModelInfo> modelResources) {
		List<IModelValidator> bulkUploadValidators = new LinkedList<IModelValidator>();
		bulkUploadValidators.add(new DuplicateModelValidation(this.repositoryService));
		bulkUploadValidators.add(new BulkModelDuplicateIdValidation(this.repositoryService, modelResources));
		bulkUploadValidators.add(new BulkModelReferencesValidation(this.repositoryService, modelResources));
		return bulkUploadValidators;
	}

	protected static byte[] copyStream(ZipInputStream in, ZipEntry entry) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		int size;
		byte[] buffer = new byte[2048];

		BufferedOutputStream bos = new BufferedOutputStream(out);

		while ((size = in.read(buffer, 0, buffer.length)) != -1) {
			bos.write(buffer, 0, size);
		}
		bos.flush();
		bos.close();
		return out.toByteArray();
	}

}
