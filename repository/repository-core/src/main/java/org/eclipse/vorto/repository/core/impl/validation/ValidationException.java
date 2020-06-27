/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.core.impl.validation;

import java.util.Collection;
import java.util.Objects;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.parser.ValidationIssue;
import com.google.common.collect.Lists;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ValidationException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Collection<ValidationIssue> validationIssues = Lists.newArrayList();

  private ModelInfo modelResource = null;

  public ValidationException(String msg, ModelInfo modelResource) {
    super(msg);
    this.modelResource = modelResource;
  }

  public ValidationException(String msg, ModelInfo modelResource, Throwable t) {
    super(msg, t);
    this.modelResource = modelResource;
  }

  public ValidationException(String msg, Collection<ValidationIssue> validationIssues,
      ModelInfo modelResource) {
    super(msg);
    this.modelResource = modelResource;
    this.validationIssues = Objects.requireNonNull(validationIssues);
  }

  public ModelInfo getModelResource() {
    return modelResource;
  }

  public Collection<ValidationIssue> getValidationIssues() {
    return validationIssues;
  }
}
