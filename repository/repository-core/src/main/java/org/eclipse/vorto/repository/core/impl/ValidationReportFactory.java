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
package org.eclipse.vorto.repository.core.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.impl.validation.CouldNotResolveReferenceException;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.importer.ValidationReport;

public class ValidationReportFactory {


  public static ValidationReport create(ValidationException... validationExceptions) {
    Objects.requireNonNull(validationExceptions);
    if (validationExceptions.length <= 0) {
      throw new IllegalArgumentException(
          "There ought to be more than 1 validation exception for this function.");
    }

    StringBuffer errorMessage = new StringBuffer();
    List<ModelId> missingReferences = Collections.emptyList();
    for (int i = 0; i < validationExceptions.length; i++) {
      if (validationExceptions[i] instanceof CouldNotResolveReferenceException) {
        missingReferences =
            ((CouldNotResolveReferenceException) validationExceptions[i]).getMissingReferences();
      }

      if (errorMessage.length() != 0) {
        errorMessage.append("\n");
      }
      errorMessage.append((i + 1));
      errorMessage.append(") ");
      errorMessage.append(validationExceptions[i].getMessage());
    }

    return ValidationReport.invalid(validationExceptions[0].getModelResource(),
        errorMessage.toString(), missingReferences);
  }

  // public static UploadModelResult invalid(CouldNotResolveReferenceException validationException)
  // {
  // return new UploadModelResult(null,
  // ValidationReport.invalid(validationException.getModelResource(),
  // validationException.getMessage(), validationException.getMissingReferences()));
  // }
  //
  // public static UploadModelResult invalid(ValidationException validationException) {
  // return new UploadModelResult(null,
  // ValidationReport.invalid(validationException.getModelResource(),
  // validationException.getMessage()));
  // }

}
