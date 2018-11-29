/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.parser.ValidationIssue;
import org.eclipse.vorto.repository.core.impl.validation.CouldNotResolveReferenceException;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;

public class ValidationReport {

  public static final StatusMessage ERROR_MODEL_ALREADY_RELEASED = new StatusMessage(
      "The model with this version is already released. Please create a new version.",
      MessageSeverity.ERROR);
  public static final StatusMessage WARNING_MODEL_ALREADY_EXISTS = new StatusMessage(
      "A model with this version already exists, and will be overwritten upon import.",
      MessageSeverity.WARNING);
  public static final StatusMessage ERROR_MODEL_ALREADY_EXISTS = new StatusMessage(
      "A model with this version has already been created by another user.", MessageSeverity.ERROR);

  private ModelInfo model = null;
  private boolean valid = false;
  private StatusMessage message = null;
  private Collection<ModelId> unresolvedReferences = new ArrayList<ModelId>();
  private Collection<ValidationIssue> validationIssues = new ArrayList<ValidationIssue>();

  public ValidationReport(ModelInfo model, boolean valid, StatusMessage message,
      Collection<ModelId> missingReferences) {
    super();
    this.model = model;
    this.valid = valid;
    this.message = message;
    this.unresolvedReferences.addAll(missingReferences);
  }

  public ValidationReport(ModelInfo model, boolean valid, StatusMessage message,
      Collection<ModelId> unresolvedReferences, Collection<ValidationIssue> validationIssues) {
    this(model, valid, message, unresolvedReferences);
    this.validationIssues = validationIssues;
  }

  public static ValidationReport invalid(ModelInfo model, String msg) {
    return new ValidationReport(model, false, new StatusMessage(msg, MessageSeverity.ERROR),
        Collections.emptyList());
  }

  public static ValidationReport invalid(String msg) {
    return new ValidationReport(null, false, new StatusMessage(msg, MessageSeverity.ERROR),
        Collections.emptyList());
  }

  public static ValidationReport invalid(ModelInfo model, String msg,
      Collection<ModelId> missingReferences) {
    return new ValidationReport(model, false, new StatusMessage(msg, MessageSeverity.ERROR),
        missingReferences);
  }

  public static ValidationReport invalid(ModelInfo model, ValidationException exception) {
    if (exception instanceof CouldNotResolveReferenceException) {
      CouldNotResolveReferenceException ex = (CouldNotResolveReferenceException) exception;
      return new ValidationReport(model, false,
          new StatusMessage(exception.getMessage(), MessageSeverity.ERROR),
          ex.getMissingReferences(), ex.getValidationIssues());
    } else {
      return new ValidationReport(model, false,
          new StatusMessage(exception.getMessage(), MessageSeverity.ERROR), Collections.emptyList(),
          exception.getValidationIssues());

    }
  }

  public static ValidationReport valid(ModelInfo model) {
    return new ValidationReport(model, true,
        new StatusMessage("The model is valid.", MessageSeverity.INFO), Collections.emptyList());
  }

  protected ValidationReport() {

  }

  public ModelInfo getModel() {
    return model;
  }

  public void setModel(ModelInfo model) {
    this.model = model;
  }

  public boolean isValid() {
    return valid;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }

  public StatusMessage getMessage() {
    return message;
  }

  public void setMessage(StatusMessage message) {
    this.message = message;
  }

  public Collection<ModelId> getUnresolvedReferences() {
    return unresolvedReferences;
  }

  public void setUnresolvedReferences(Collection<ModelId> unresolvedReferences) {
    this.unresolvedReferences = unresolvedReferences;
  }

  public Collection<ValidationIssue> getValidationIssues() {
    return validationIssues;
  }
}
