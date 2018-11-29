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

import java.util.ArrayList;
import java.util.List;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.InvocationContext;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ModelReferencesValidation implements IModelValidator {

  private IModelRepository modelRepository;

  public ModelReferencesValidation(IModelRepository modelRepository) {
    this.modelRepository = modelRepository;
  }

  public IModelRepository getModelRepository() {
    return modelRepository;
  }

  @Override
  public void validate(ModelInfo modelResource, InvocationContext context)
      throws ValidationException {
    List<ModelId> missingReferences = new ArrayList<ModelId>();
    if (!modelResource.getReferences().isEmpty()) {
      checkReferencesRecursive(modelResource, missingReferences);
    }

    if (!missingReferences.isEmpty()) {
      throw new CouldNotResolveReferenceException(modelResource, missingReferences);
    }
  }

  private void checkReferencesRecursive(ModelInfo modelResource, List<ModelId> accumulator) {
    for (ModelId modelId : modelResource.getReferences()) {
      ModelInfo reference = modelRepository.getById(modelId);
      if (reference == null) {
        accumulator.add(modelId);
      } else if (modelResource.getId().equals(reference.getId())) {
        throw new ValidationException(
            "Cyclic dependency detected for reference '" + reference.getId() + "'", modelResource);
      } else {
        if (modelResource.getType().canHandleReference(reference)) {
          checkReferencesRecursive(reference, accumulator);
        } else {
          throw new ValidationException(
              "Reference '" + reference.getId() + "' is not valid for this model type.",
              modelResource);
        }
      }
    }
  }
}
