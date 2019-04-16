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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.core.IModelIdResolver;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IModelSearchService;
import org.eclipse.vorto.repository.core.ModelFileContent;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.web.core.dto.ResolveQuery;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class AbstractResolver implements IModelIdResolver {

  @Autowired
  protected IModelRepositoryFactory repositoryFactory;

  @Override
  public ModelId resolve(final ResolveQuery query) {
    IModelSearchService searchService = repositoryFactory.getModelSearchService();
    Map<String, List<ModelInfo>> result = searchService.search(ModelType.Mapping.name());
    
    //List<ModelInfo> mappings = this.repository.search(ModelType.Mapping.name());
    List<Map.Entry<String, ModelInfo>> mappings = result.entrySet().stream()
        .map(entry -> entry.getValue().stream()
            .map(modelInfo -> Maps.immutableEntry(entry.getKey(), modelInfo))
            .collect(Collectors.toList()))
        .reduce(Lists.newArrayList(), (a, b) -> {
          a.addAll(b);
          return a;
        });
    
    Optional<ModelId> foundId = mappings.stream()
        .filter(resource -> matchesServiceKey(resource.getKey(), resource.getValue(), query.getTargetPlatformKey()))
        .map(r -> doResolve(r.getKey(), r.getValue(), query))
        .filter(modelId -> Objects.nonNull(modelId))
        .findFirst();
    
    return foundId.isPresent() ? foundId.get() : null;
  }

  private boolean matchesServiceKey(String tenantId, ModelInfo resource, String targetPlatformKey) {
    ModelFileContent content = repositoryFactory.getRepository(tenantId).getModelContent(resource.getId(), false);
    return ((MappingModel) content.getModel()).getTargetPlatform().equals(targetPlatformKey);
  }

  protected abstract ModelId doResolve(String tenantId, ModelInfo mappingModelResource, ResolveQuery query);

  public IModelRepositoryFactory getRepositoryFactory() {
    return repositoryFactory;
  }

  public void setRepositoryFactory(IModelRepositoryFactory repositoryFactory) {
    this.repositoryFactory = repositoryFactory;
  }
}
