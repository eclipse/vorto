package org.eclipse.vorto.repository.core.impl.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.upload.ValidationReport;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.InvocationContext;
import org.eclipse.vorto.repository.core.impl.UploadModelResultFactory;
import org.eclipse.vorto.repository.core.impl.validation.DuplicateModelValidation;
import org.eclipse.vorto.repository.core.impl.validation.IModelValidator;
import org.eclipse.vorto.repository.core.impl.validation.ModelReferencesValidation;
import org.eclipse.vorto.repository.core.impl.validation.TypeImportValidation;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;

public class ModelValidationHelper {

	private List<IModelValidator> validators = new ArrayList<IModelValidator>();
	
	public ModelValidationHelper(IModelRepository modelRepository, IUserRepository userRepository) {
		this.validators.add(new DuplicateModelValidation(modelRepository, userRepository));
		this.validators.add(new ModelReferencesValidation(modelRepository));
		this.validators.add(new TypeImportValidation());
	}
	
	public ValidationReport validate(ModelInfo model, IUserContext userContext) {
		List<ValidationException> validationExceptions = new ArrayList<ValidationException>();
		for (IModelValidator validator : validators) {
			try {
				validator.validate(model, InvocationContext.create(userContext));
			} catch (ValidationException validationException) {
				validationExceptions.add(validationException);
			}
		}

		if (validationExceptions.size() <= 0) {
			return ValidationReport.valid(model);
		} else {
			return UploadModelResultFactory
					.create(validationExceptions.toArray(new ValidationException[validationExceptions.size()])).getReport();
		}
	}
	
}
