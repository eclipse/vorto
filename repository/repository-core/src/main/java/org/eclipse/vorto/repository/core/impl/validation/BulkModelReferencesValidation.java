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
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.InvocationContext;

/**
 * Validation class for multiple file model upload/checkin.
 * 
 * @author Nagavijay Sivakumar - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class BulkModelReferencesValidation extends ModelReferencesValidation {

  private List<ModelId> zipModelIds;

  public BulkModelReferencesValidation(IModelRepository modelRepository,
      Set<ModelInfo> modelResources) {
    super(modelRepository);
    zipModelIds =
        modelResources.stream().map(new java.util.function.Function<ModelInfo, ModelId>() {
          @Override
          public ModelId apply(ModelInfo resource) {
            return resource.getId();
          }

        }).collect(Collectors.toList());
  }

  @Override
  public void validate(ModelInfo modelResource, InvocationContext context)
      throws ValidationException {
    validateInRepository(modelResource, context);
    // Validate other references in zip files.
    validateInZipFiles(modelResource);
  }

  private List<ModelId> validateInRepository(ModelInfo modelResource, InvocationContext context) {
    List<ModelId> missingReferences = new ArrayList<ModelId>();
    try {
      super.validate(modelResource, context);
    } catch (CouldNotResolveReferenceException e) {
      return e.getMissingReferences();
    }
    return missingReferences;
  }

  private boolean isNotInRepository(ModelId modelId) {
    return getModelRepository().getById(modelId) == null;

  }

  private void validateInZipFiles(ModelInfo modelResource) {
    List<ModelId> references = modelResource.getReferences();
    List<ModelId> missingReferences = new ArrayList<ModelId>();
    for (ModelId modelId : references) {
      if (!zipModelIds.contains(modelId)) {
        if (isNotInRepository(modelId)) {
          missingReferences.add(modelId);
        }
      }
    }
    if (missingReferences.size() > 0)
      throw new CouldNotResolveReferenceException(modelResource, missingReferences);
  }
}
