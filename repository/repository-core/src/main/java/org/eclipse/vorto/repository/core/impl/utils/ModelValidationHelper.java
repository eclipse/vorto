/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.core.impl.utils;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.InvocationContext;
import org.eclipse.vorto.repository.core.impl.ValidationReportFactory;
import org.eclipse.vorto.repository.core.impl.validation.DuplicateModelValidation;
import org.eclipse.vorto.repository.core.impl.validation.IModelValidator;
import org.eclipse.vorto.repository.core.impl.validation.ModelReferencesValidation;
import org.eclipse.vorto.repository.core.impl.validation.TypeImportValidation;
import org.eclipse.vorto.repository.core.impl.validation.UserHasAccessToNamespaceValidation;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.importer.ValidationReport;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelValidationHelper {
  
  private IModelValidator hasAccessToNamespaceValidator = null;
  private IModelValidator duplicateModelValidator = null;
  private IModelValidator modelReferencesValidator = null;
  private IModelValidator typeImportValidator = null;
  
  private List<IModelValidator> MODEL_UPDATE_VALIDATORS = new ArrayList<IModelValidator>(3);
  private List<IModelValidator> MODEL_CREATION_VALIDATORS = new ArrayList<IModelValidator>(4);

  public ModelValidationHelper(@Autowired IModelRepositoryFactory modelRepoFactory, @Autowired IUserAccountService userRepository, 
      @Autowired ITenantService tenantService) {
    this.hasAccessToNamespaceValidator = new UserHasAccessToNamespaceValidation(userRepository, tenantService);
    this.duplicateModelValidator = new DuplicateModelValidation(modelRepoFactory);
    this.modelReferencesValidator = new ModelReferencesValidation(modelRepoFactory);
    this.typeImportValidator = new TypeImportValidation();
    
    MODEL_UPDATE_VALIDATORS.add(hasAccessToNamespaceValidator);
    MODEL_UPDATE_VALIDATORS.add(modelReferencesValidator);
    MODEL_UPDATE_VALIDATORS.add(typeImportValidator);
    
    MODEL_CREATION_VALIDATORS.add(hasAccessToNamespaceValidator);
    MODEL_CREATION_VALIDATORS.add(duplicateModelValidator);
    MODEL_CREATION_VALIDATORS.add(modelReferencesValidator);
    MODEL_CREATION_VALIDATORS.add(typeImportValidator);

  }

  public ValidationReport validateModelCreation(ModelInfo model, IUserContext userContext) {
    List<ValidationException> validationExceptions = new ArrayList<ValidationException>();
    for (IModelValidator validator : this.MODEL_CREATION_VALIDATORS) {
      try {
        validator.validate(model, InvocationContext.create(userContext));
      } catch (ValidationException validationException) {
        validationExceptions.add(validationException);
      }
    }

    if (validationExceptions.isEmpty()) {
      return ValidationReport.valid(model);
    } else {
      return ValidationReportFactory.create(
          validationExceptions.toArray(new ValidationException[validationExceptions.size()]));
    }
  }
  
  public ValidationReport validateModelUpdate(ModelInfo model, IUserContext userContext) {
    List<ValidationException> validationExceptions = new ArrayList<ValidationException>();
    for (IModelValidator validator : this.MODEL_UPDATE_VALIDATORS) {
      try {
        validator.validate(model, InvocationContext.create(userContext));
      } catch (ValidationException validationException) {
        validationExceptions.add(validationException);
      }
    }

    if (validationExceptions.isEmpty()) {
      return ValidationReport.valid(model);
    } else {
      return ValidationReportFactory.create(
          validationExceptions.toArray(new ValidationException[validationExceptions.size()]));
    }
  }

}
