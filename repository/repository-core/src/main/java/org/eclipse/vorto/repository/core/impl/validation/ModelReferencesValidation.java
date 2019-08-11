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

import java.util.ArrayList;
import java.util.List;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.InvocationContext;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ModelReferencesValidation implements IModelValidator {

  private IModelRepositoryFactory modelRepoFactory;
  
  public ModelReferencesValidation(IModelRepositoryFactory modelRepoFactory) {
    this.modelRepoFactory = modelRepoFactory;
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
      ModelInfo _referenceModelInfo =  modelRepoFactory.getRepositoryByModel(modelId).getById(modelId);
      
      if (_referenceModelInfo != null) {
        if (modelResource.getId().equals(_referenceModelInfo.getId())) {
          throw new ValidationException(
              "Cyclic dependency detected for reference '" + _referenceModelInfo.getId() + "'", modelResource);
        } else {
          if (modelResource.getType().canHandleReference(_referenceModelInfo)) {
            checkReferencesRecursive(_referenceModelInfo, accumulator);
          } else {
            throw new ValidationException(
                "Reference '" + _referenceModelInfo.getId() + "' is not valid for this model type.",
                modelResource);
          }
        }
      } else {
        accumulator.add(modelId);
      }
    }
  }

  public IModelRepositoryFactory getModelRepoFactory() {
    return modelRepoFactory;
  }
}
