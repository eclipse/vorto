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
package org.eclipse.vorto.repository.internal.service.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.internal.model.ModelEMFResource;
import org.eclipse.vorto.repository.internal.service.ModelParserFactory;
import org.eclipse.vorto.repository.internal.service.validation.BulkModelDuplicateIdValidation;
import org.eclipse.vorto.repository.internal.service.validation.BulkModelReferencesValidation;
import org.eclipse.vorto.repository.internal.service.validation.DuplicateModelValidation;
import org.eclipse.vorto.repository.internal.service.validation.IModelValidator;
import org.eclipse.vorto.repository.internal.service.validation.ValidationException;
import org.eclipse.vorto.repository.model.ModelHandle;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.model.UploadModelResult;
import org.eclipse.vorto.repository.service.FatalModelRepositoryException;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.springframework.util.StringUtils;

public class BulkUploadHelper {

	private IModelRepository repositoryService;
	
	public BulkUploadHelper(IModelRepository modelRepository) {
		this.repositoryService = modelRepository;
	}

	public List<UploadModelResult> uploadMultiple(byte[] content, String zipFileName) {
		if (!isValid(zipFileName)) {
			throw new FatalModelRepositoryException("Filename/type is invalid", null);
		}

		List<UploadModelResult> invalidResult = new ArrayList<UploadModelResult>();
		List<UploadModelResult> validResult = new ArrayList<UploadModelResult>();
		
		Set<ModelResource> parsedModels = new HashSet<>();

		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(content));
		ZipEntry entry = null;
		
		try {
			while ((entry = zis.getNextEntry()) != null) {
				if (!entry.isDirectory()) {
					final String fileName = entry.getName();
					try {
						parsedModels.add(ModelParserFactory.getParser(fileName).parse(new ByteArrayInputStream(copyStream(zis,entry))));
					} catch (ValidationException grammarProblem) {
						invalidResult.add(UploadModelResult.invalid(grammarProblem));
					}
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {

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
	private List<UploadModelResult> safelyUpload(Collection<ModelResource> resources) throws Exception {
		List<UploadModelResult> result = new ArrayList<UploadModelResult>();
		
		for (ModelResource resource : resources) {
			result.add(UploadModelResult.valid(createUploadHandle(((ModelEMFResource)resource).toDSL(), resource.getId().getFileName()+resource.getModelType().getExtension()), resource));
		}
		
		return result;
	}
	
	public void checkinMultiple(List<ModelHandle> handles) {
		
	}
	
	private static String getDefaultExtractDirectory() {
		return FilenameUtils.normalize(FileUtils.getTempDirectory().getPath() + "/vorto", true);
	}
	
	private String createUploadHandle(byte[] content, String fileName) {
		try {
			File tmpDirectory = new File(getDefaultExtractDirectory());
			if (!tmpDirectory.exists()) {
				tmpDirectory.mkdirs();
			}
			File file = new File(FilenameUtils.normalize(getDefaultExtractDirectory() + "/" + StringUtils.getFilename(fileName)));
			IOUtils.write(content, new FileOutputStream(file));
			return file.getName();
		} catch (IOException e) {
			throw new RuntimeException("Could not create temporary file for uploaded model", e);
		}
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
