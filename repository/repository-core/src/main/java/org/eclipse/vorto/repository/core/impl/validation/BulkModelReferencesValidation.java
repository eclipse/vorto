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
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IUserContext;
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

  public BulkModelReferencesValidation(IModelRepositoryFactory modelRepoFactory,
      Set<ModelInfo> modelResources) {
    super(modelRepoFactory);
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
    validateInZipFiles(modelResource, context);
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

  private boolean isNotInRepository(ModelId modelId, IUserContext userContext) {
    return !getModelRepoFactory().getModelRetrievalService(userContext).getModel(modelId).isPresent();
  }

  private void validateInZipFiles(ModelInfo modelResource, InvocationContext context) {
    List<ModelId> references = modelResource.getReferences();
    List<ModelId> missingReferences = new ArrayList<ModelId>();
    for (ModelId modelId : references) {
      if (!zipModelIds.contains(modelId) && isNotInRepository(modelId, context.getUserContext())) {
        missingReferences.add(modelId);
      }
    }
    if (missingReferences.size() > 0)
      throw new CouldNotResolveReferenceException(modelResource, missingReferences);
  }
}
