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
package org.eclipse.vorto.repository.core.impl.validation;

import java.util.List;
import java.util.Objects;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.ModelInfo;


public class CouldNotResolveReferenceException extends ValidationException {
  private static final long serialVersionUID = -6078848052990402848L;
  private List<ModelId> missingReferences;

  public CouldNotResolveReferenceException(ModelInfo resource, List<ModelId> missingReferences) {
    super(createErrorMessage(missingReferences), resource);
    this.missingReferences = Objects.requireNonNull(missingReferences);
    if (missingReferences.size() <= 0) {
      throw new IllegalArgumentException(
          "Trying to create a CouldNotResolveReferenceException with empty missingReferences.");
    }
  }

  public List<ModelId> getMissingReferences() {
    return missingReferences;
  }

  private static String createErrorMessage(List<ModelId> missingReferences) {
    if (missingReferences.size() > 1) {
      return "Cannot resolve multiple references.";
    } else {
      return String.format("Cannot resolve reference %s",
          missingReferences.get(0).getPrettyFormat());
    }
  }
}
