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
package org.eclipse.vorto.repository.upgrade;

import java.util.Optional;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.core.impl.JcrModelRepository;

public abstract class AbstractUpgradeTask implements IUpgradeTask {

  private IModelRepository modelRepository;

  public AbstractUpgradeTask(IModelRepository repository) {
    this.modelRepository = repository;
  }

  public Optional<IUpgradeTaskCondition> condition() {
    return Optional.empty();
  }

  protected IModelRepository getModelRepository() {
    return modelRepository;
  }

  protected ModelResource getModel(ModelId modelId) {
    return ((JcrModelRepository) modelRepository).getEMFResource(modelId);
  }

  protected void saveModel(ModelResource resource) {
    ((JcrModelRepository) modelRepository).saveModel(resource);
  }
}
