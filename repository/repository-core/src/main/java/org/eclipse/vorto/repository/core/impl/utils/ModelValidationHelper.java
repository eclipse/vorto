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
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.importer.ValidationReport;

public class ModelValidationHelper {

  private List<IModelValidator> validators = new ArrayList<IModelValidator>();

  public ModelValidationHelper(IModelRepositoryFactory modelRepoFactory, IUserAccountService userRepository) {
    this.validators.add(new DuplicateModelValidation(modelRepoFactory, userRepository));
    this.validators.add(new ModelReferencesValidation(modelRepoFactory));
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
      return ValidationReportFactory.create(
          validationExceptions.toArray(new ValidationException[validationExceptions.size()]));
    }
  }

}
