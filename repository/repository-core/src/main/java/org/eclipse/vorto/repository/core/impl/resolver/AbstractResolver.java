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
package org.eclipse.vorto.repository.core.impl.resolver;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.core.IModelIdResolver;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelFileContent;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.web.core.dto.ResolveQuery;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractResolver implements IModelIdResolver {

  @Autowired
  protected IModelRepository repository;

  @Override
  public ModelId resolve(final ResolveQuery query) {
    List<ModelInfo> mappings = this.repository.search(ModelType.Mapping.name());
    Optional<ModelId> foundId = mappings.stream()
        .filter(resource -> matchesServiceKey(resource, query.getTargetPlatformKey()))
        .map(r -> doResolve(r, query)).filter(modelId -> Objects.nonNull(modelId)).findFirst();
    return foundId.isPresent() ? foundId.get() : null;
  }

  private boolean matchesServiceKey(ModelInfo resource, String targetPlatformKey) {
    ModelFileContent content = this.repository.getModelContent(resource.getId(),false);
    return ((MappingModel) content.getModel()).getTargetPlatform().equals(targetPlatformKey);
  }

  protected abstract ModelId doResolve(ModelInfo mappingModelResource, ResolveQuery query);

  public IModelRepository getRepository() {
    return repository;
  }

  public void setRepository(IModelRepository repository) {
    this.repository = repository;
  }


}
