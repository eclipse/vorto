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
