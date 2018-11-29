/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
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
