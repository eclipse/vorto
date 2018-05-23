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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.upload.UploadModelResult;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.impl.ITemporaryStorage;
import org.eclipse.vorto.repository.core.impl.InvocationContext;
import org.eclipse.vorto.repository.core.impl.ModelEMFResource;
import org.eclipse.vorto.repository.core.impl.UploadModelResultFactory;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.validation.BulkModelDuplicateIdValidation;
import org.eclipse.vorto.repository.core.impl.validation.BulkModelReferencesValidation;
import org.eclipse.vorto.repository.core.impl.validation.DuplicateModelValidation;
import org.eclipse.vorto.repository.core.impl.validation.IModelValidator;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.web.core.exceptions.BulkUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class BulkUploadHelper {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	public static final long TTL_TEMP_STORAGE_INSECONDS = 60 * 5;
	
	private IModelRepository repositoryService;
	
	private IUserRepository userRepository;

	private ITemporaryStorage uploadStorage;
	
	public BulkUploadHelper(IModelRepository modelRepository, ITemporaryStorage storage, IUserRepository userRepository) {
		this.repositoryService = modelRepository;
		this.uploadStorage = storage;
		this.userRepository = userRepository;
	}
	
	public List<UploadModelResult> uploadMultiple(byte[] content, String zipFileName, String callerId) {
		if (!isValid(zipFileName)) {
			throw new FatalModelRepositoryException("Filename/type is invalid", null);
		}

		/*
		 * Parse zip file
		 */
		ZipParseResult parseResult = parseZipFile(content);
		
		try {
			/*
			 * Create mapping function that will convert from a ModelInfo to an UploadModelResult using validators
			 */
			Function<ModelInfo, UploadModelResult> convertToUploadModelResult = createConvertToUploadModelResultFn(constructBulkUploadValidators(parseResult.validModels),
					InvocationContext.create(UserContext.user(callerId)));
			
			/*
			 * Convert parsed models tp UploadModelResult
			 */
			Set<UploadModelResult> validatedModelResults = parseResult.validModels.stream().map(convertToUploadModelResult).collect(Collectors.toSet()); 
			
			/*
			 * Add everything to a Set to eliminate redundancy
			 */
			Set<UploadModelResult> uploadedModelResults = new HashSet<>();
			uploadedModelResults.addAll(parseResult.invalidModels);
			uploadedModelResults.addAll(validatedModelResults);
			
			/*
			 * Return result if one is invalid, else, go for further processing
			 */
			if (uploadedModelResults.stream().anyMatch(result -> !result.isValid())) {
				return new ArrayList<>(uploadedModelResults);
			} else {
				/*
				 * Further processing involves resolving dependencies and creating an upload handle
				 */
				DependencyManager dm = new DependencyManager(parseResult.validModels);
				return dm.getSorted().stream()
						.map(this::convertToUploadModelResultWithUploadHandle)
						.filter(result -> result.isPresent())
						.map(result -> result.get())
						.collect(Collectors.toList());
			}

		} catch (Exception e) {
			throw new BulkUploadException("Invalid zip file", e);
		} 
	}
	
	private Optional<UploadModelResult> convertToUploadModelResultWithUploadHandle(ModelInfo modelInfo) {
		try {
			final String handleId = UUID.randomUUID().toString() + modelInfo.getType().getExtension();
			String key = uploadStorage.store(handleId, ((ModelEMFResource) modelInfo).toDSL(), TTL_TEMP_STORAGE_INSECONDS).getKey();
			return Optional.of(UploadModelResult.valid(key, modelInfo));
		} catch (IOException e) {
			LOGGER.error("Exception thrown when getting DSL of " + modelInfo.toString(), e);
			return Optional.empty();
		}
	}
	
	private Function<ModelInfo, UploadModelResult> createConvertToUploadModelResultFn(List<IModelValidator> bulkUploadValidators, InvocationContext context) {
		return (modelInfo) -> {
			try {
				bulkUploadValidators.stream().forEach(validator -> validator.validate(modelInfo, context));
			} catch (ValidationException validationException) {
				return UploadModelResultFactory.create(validationException);
			}
			return UploadModelResult.valid(modelInfo);
		};
	}
	
	private class ZipParseResult {
		Set<UploadModelResult> invalidModels;
		Set<ModelInfo> validModels;
	}
	
	private ZipParseResult parseZipFile(byte[] content) {
		assert(content != null);
		
		ZipParseResult parsingResult = new ZipParseResult();
		
		parsingResult.invalidModels = new HashSet<>();
		parsingResult.validModels = new HashSet<>();
		
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(content));
		ZipEntry entry = null;
		
		try {
			while ((entry = zis.getNextEntry()) != null) {
				if (!entry.isDirectory()) {
					try {
						parsingResult.validModels.add(ModelParserFactory.getParser(entry.getName()).parse(new ByteArrayInputStream(copyStream(zis,entry))));
					} catch (ValidationException grammarProblem) {
						parsingResult.invalidModels.add(UploadModelResultFactory.create(grammarProblem));
					} catch(UnsupportedOperationException fileNotSupportedException) {
						// Do nothing. Don't process the file
					}
				}
			}
		} catch (IOException e) {
			throw new BulkUploadException("IOException while getting next entry from zip file", e);
		}
		
		return parsingResult;
	}
	
	private boolean isValid(String file) {
		return !StringUtils.isEmpty(file) && StringUtils.endsWithIgnoreCase(file, ".zip");
	}

	private List<IModelValidator> constructBulkUploadValidators(Set<ModelInfo> modelResources) {
		List<IModelValidator> bulkUploadValidators = new LinkedList<IModelValidator>();
		bulkUploadValidators.add(new DuplicateModelValidation(this.repositoryService, this.userRepository));
		bulkUploadValidators.add(new BulkModelDuplicateIdValidation(this.repositoryService, modelResources));
		bulkUploadValidators.add(new BulkModelReferencesValidation(this.repositoryService, modelResources));
		return bulkUploadValidators;
	}

	protected static byte[] copyStream(ZipInputStream in, ZipEntry entry) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {			
			int size;
			byte[] buffer = new byte[2048];

			BufferedOutputStream bos = new BufferedOutputStream(out);
			
			while ((size = in.read(buffer, 0, buffer.length)) != -1) {
				bos.write(buffer, 0, size);
			}
			
			bos.flush();
			bos.close();
		} catch (IOException e) {
			throw new BulkUploadException("IOException while copying stream to ZipEntry", e); 
		}
		
		return out.toByteArray();
	}

}
