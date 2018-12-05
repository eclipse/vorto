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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.InvocationContext;

public class BulkModelDuplicateIdValidation extends ModelReferencesValidation {

  private List<ModelId> zipModelIds;

  public BulkModelDuplicateIdValidation(IModelRepository modelRepository,
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
    Set<ModelId> nonduplicates = new HashSet<>();

    for (ModelId modelId : zipModelIds) {
      if (modelResource.getId().equals(modelId) && nonduplicates.contains(modelId)) {
        throw new ValidationException("Zip contains models with duplicate Ids", modelResource);
      } else {
        nonduplicates.add(modelId);
      }
    }
  }
}
