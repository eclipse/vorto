/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
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
