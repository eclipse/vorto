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
