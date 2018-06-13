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
package org.eclipse.vorto.repository.importer.impl.vorto;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.upload.StagingResult;
import org.eclipse.vorto.repository.core.impl.InvocationContext;
import org.eclipse.vorto.repository.core.impl.StorageItem;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.validation.CouldNotResolveReferenceException;
import org.eclipse.vorto.repository.core.impl.validation.DuplicateModelValidation;
import org.eclipse.vorto.repository.core.impl.validation.IModelValidator;
import org.eclipse.vorto.repository.core.impl.validation.ModelReferencesValidation;
import org.eclipse.vorto.repository.core.impl.validation.TypeImportValidation;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.importer.CommittedModel;
import org.eclipse.vorto.repository.importer.impl.AbstractModelImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VortoModelImporter extends AbstractModelImporter {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	public static final long TTL_TEMP_STORAGE_INSECONDS = 60 * 5;

	private List<IModelValidator> validators = new LinkedList<IModelValidator>();
	
	@Override
	public String getId() {
		return "vorto";
	}
	
	@Override
	public boolean canHandle(byte[] file, String filename) {
		return ModelParserFactory.hasParserFor(filename);
	}
	
	@Override
	public ModelId getModelId(byte[] content, String filename) {
		ModelInfo resource = ModelParserFactory.getParser(filename).parse(new ByteArrayInputStream(content));
		return resource.getId();
	}

	@Override
	public StagingResult stageModel(byte[] content, String fileName, UserContext userContext) {
		LOGGER.info("Staging the model with filename: " + fileName);
		
		ModelInfo resource = ModelParserFactory.getParser(fileName).parse(new ByteArrayInputStream(content));

		List<ValidationException> validationExceptions = new ArrayList<ValidationException>();
		for (IModelValidator validator : validators) {
			try {
				validator.validate(resource, InvocationContext.create(userContext));
			} catch (ValidationException validationException) {
				validationExceptions.add(validationException);
			}
		}

		if (validationExceptions.size() <= 0) {
			// If no validation error, we can now stage the content
			String stagingId = stageContent(content, fileName);
			
			return StagingResult.success(getId(), stagingId, new VortoStagingDetails(resource));
		} else {
			return StagingResult.fail(getId(), getErrorMessage(validationExceptions), 
					new VortoStagingDetails(resource, getMissingReferences(validationExceptions)));
		}
	}
	
	private List<ModelId> getMissingReferences(List<ValidationException> validationExceptions) {
		return validationExceptions.stream()
			.filter(validationException -> validationException instanceof CouldNotResolveReferenceException)
			.findFirst()
			.map(validationException -> ((CouldNotResolveReferenceException) validationException).getMissingReferences())
			.orElse(Collections.emptyList());
	}

	public String getErrorMessage(List<ValidationException> validationExceptions) {
		StringBuffer errorMessage = new StringBuffer();
		
		for(int i=0; i < validationExceptions.size(); i++) {
			if (errorMessage.length() != 0) {
				errorMessage.append("\n");
			}
			errorMessage.append((i + 1));
			errorMessage.append(") ");
			errorMessage.append(validationExceptions.get(i).getMessage());
		}
		
		return errorMessage.toString();
	}

	@Override
	public CommittedModel commitModel(String stagingId, UserContext userContext) {
		LOGGER.info("Committing staged file: " + stagingId);
		StorageItem uploadedItem = getUploadStorage().get(stagingId);
		
		final ModelInfo resource = ModelParserFactory.getParser(stagingId).parse(new ByteArrayInputStream((byte [])uploadedItem.getValue()));
		
		getModelRepository().save(resource.getId(), (byte []) uploadedItem.getValue(), resource.getId().getName() + resource.getType().getExtension(), userContext);
		
		getUploadStorage().remove(stagingId);
		
		return new CommittedModel(getId(), resource.getId(), resource);
	}
	
	@PostConstruct
	public void createValidators() {
		this.validators.add(new DuplicateModelValidation(getModelRepository(), getUserRepository()));
		this.validators.add(new ModelReferencesValidation(getModelRepository()));
		this.validators.add(new TypeImportValidation());
	}
}
