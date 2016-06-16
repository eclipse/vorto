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
package org.eclipse.vorto.repository.internal.service.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.internal.service.ModelParserFactory;
import org.eclipse.vorto.repository.internal.service.validation.BulkModelDuplicateIdValidation;
import org.eclipse.vorto.repository.internal.service.validation.BulkModelReferencesValidation;
import org.eclipse.vorto.repository.internal.service.validation.DuplicateModelValidation;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.model.UploadModelResult;
import org.eclipse.vorto.repository.service.FatalModelRepositoryException;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.eclipse.vorto.repository.validation.IModelValidator;
import org.eclipse.vorto.repository.validation.ValidationException;
import org.springframework.util.StringUtils;

public class BulkUploadHelper {

	private IModelRepository repositoryService;
	
	public BulkUploadHelper(IModelRepository modelRepository) {
		this.repositoryService = modelRepository;
	}

	public List<UploadModelResult> uploadMultiple(String zipFileName) {
		if (!isValid(zipFileName)) {
			throw new FatalModelRepositoryException("Filename/type is invalid", null);
		}

		List<UploadModelResult> invalidResult = new ArrayList<UploadModelResult>();
		List<UploadModelResult> validResult = new ArrayList<UploadModelResult>();
		
		Set<ModelResource> parsedModels = new HashSet<>();

		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(zipFileName);

			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (!entry.isDirectory()) {
					final String fileName = entry.getName();
					try {
						parsedModels.add(ModelParserFactory.getParser(fileName).parse(zipFile.getInputStream(entry)));
					} catch (ValidationException grammarProblem) {
						invalidResult.add(UploadModelResult.invalid(grammarProblem));
					}
				}
			}

			List<IModelValidator> bulkUploadValidators = constructBulkUploadValidators(parsedModels);

			for (ModelResource modelResource : parsedModels) {
				try {
					for (IModelValidator validator : bulkUploadValidators) {
						validator.validate(modelResource);
					}
					
					UploadModelResult result = UploadModelResult.valid(modelResource);
					if (!invalidResult.contains(result)) {
						validResult.add(result);
					}
					
				} catch (ValidationException validationException) {
					invalidResult.add(UploadModelResult.invalid(validationException));
				}
			}
			
			
			if (invalidResult.isEmpty()) {
				return safelyUpload(zipFileName);
			} else {
				List<UploadModelResult> completeResult = new ArrayList<>();
				completeResult.addAll(invalidResult);
				completeResult.addAll(validResult);
				return completeResult;
			}

		} catch (IOException e) {
			throw new FatalModelRepositoryException("Invalid zip file", e);
		} finally {
			if (zipFile != null) {
				try {
					zipFile.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	/**
	 * @param zipFileName
	 */
	private List<UploadModelResult> safelyUpload(final String zipFileName) {
		List<UploadModelResult> result = new ArrayList<UploadModelResult>();
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(zipFileName);

			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (!entry.isDirectory()) {
					result.add(this.repositoryService.upload(IOUtils.toByteArray(zipFile.getInputStream(entry)),
							entry.getName()));
				}
			}
		} catch (IOException e) {
			throw new FatalModelRepositoryException("Invalid zip file", e);
		} finally {
			if (zipFile != null) {
				try {
					zipFile.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

		return result;
	}
	
	private boolean isValid(String file) {
		return !StringUtils.isEmpty(file) && StringUtils.endsWithIgnoreCase(file, ".zip");
	}

	private List<IModelValidator> constructBulkUploadValidators(Set<ModelResource> modelResources) {
		List<IModelValidator> bulkUploadValidators = new LinkedList<IModelValidator>();
		bulkUploadValidators.add(new DuplicateModelValidation(this.repositoryService));
		bulkUploadValidators.add(new BulkModelDuplicateIdValidation(this.repositoryService, modelResources));
		bulkUploadValidators.add(new BulkModelReferencesValidation(this.repositoryService, modelResources));
		return bulkUploadValidators;
	}


}
