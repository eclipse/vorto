package org.eclipse.vorto.repository.internal.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.upload.UploadModelResult;
import org.eclipse.vorto.repository.internal.service.validation.CouldNotResolveReferenceException;
import org.eclipse.vorto.repository.internal.service.validation.ValidationException;

public class UploadModelResultFactory {

	
	public static UploadModelResult create(ValidationException... validationExceptions) {
		Objects.requireNonNull(validationExceptions);
		if (validationExceptions.length <= 0) {
			throw new IllegalArgumentException("There ought to be more than 1 validation exception for this function.");
		}
		
		StringBuffer errorMessage = new StringBuffer();
		List<ModelId> missingReferences = Collections.emptyList();
		for(int i=0; i < validationExceptions.length; i++) {
			if (validationExceptions[i] instanceof CouldNotResolveReferenceException) {
				missingReferences = ((CouldNotResolveReferenceException) validationExceptions[i]).getMissingReferences();
			}
			
			if (errorMessage.length() != 0) {
				errorMessage.append("\n");
			}
			errorMessage.append((i + 1));
			errorMessage.append(") ");
			errorMessage.append(validationExceptions[i].getMessage());
		}
		
		return new UploadModelResult(null, validationExceptions[0].getModelResource(), false,
				errorMessage.toString(), missingReferences);
	}
	
	public static UploadModelResult invalid(CouldNotResolveReferenceException validationException) {
		return new UploadModelResult(null, validationException.getModelResource(), false,
				validationException.getMessage(), validationException.getMissingReferences());
	}

	public static UploadModelResult invalid(ValidationException validationException) {
		return new UploadModelResult(null, validationException.getModelResource(), false,
				validationException.getMessage());
	}
	
}
