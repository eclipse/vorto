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
package org.eclipse.vorto.repository.core.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRetrievalService;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelResource;
import com.google.common.collect.Maps;

public class ModelRetrievalService implements IModelRetrievalService {

  private Supplier<Collection<String>> tenantsSupplier;
  private Function<String, IModelRepository> modelRepoSource;
  
  public ModelRetrievalService(Supplier<Collection<String>> tenantsSupplier,
      Function<String, IModelRepository> modelRepoSource) {
    this.tenantsSupplier = tenantsSupplier;
    this.modelRepoSource = modelRepoSource;
  }

  @Override
  public Optional<Entry<String, ModelInfo>> getModel(ModelId modelId) {
    for (String tenant : tenantsSupplier.get()) {
      IModelRepository modelRepo = modelRepoSource.apply(tenant);
      ModelInfo modelInfo = modelRepo.getById(modelId);
      if (modelInfo != null) {
        return Optional.of(Maps.immutableEntry(tenant, modelInfo));
      }
    }
    
    return Optional.empty();
  }
  
  @Override
  public Optional<ModelResource> getEMFResource(String tenantId, ModelId modelId) {
    IModelRepository modelRepo = modelRepoSource.apply(tenantId);
    return Optional.ofNullable(modelRepo.getEMFResource(modelId));
  }

  @Override
  public Optional<Entry<String, FileContent>> getContent(ModelId modelId) {
    for (String tenant : tenantsSupplier.get()) {
      IModelRepository modelRepo = modelRepoSource.apply(tenant);
      Optional<FileContent> _fileContent = modelRepo.getFileContent(modelId, Optional.empty());
      if (_fileContent.isPresent()) {
        return Optional.of(Maps.immutableEntry(tenant, _fileContent.get()));
      }
    }
    
    return Optional.empty();
  }

  @Override
  public Map<String, List<ModelInfo>> getModelsReferencing(ModelId modelId) {
    Map<String, List<ModelInfo>> modelReferencesMap = new HashMap<>();
    
    for (String tenant : tenantsSupplier.get()) {
      IModelRepository modelRepo = modelRepoSource.apply(tenant);
      List<ModelInfo> modelsReferencing = modelRepo.getModelsReferencing(modelId);
      if (modelsReferencing != null && modelsReferencing.size() > 0) {
        modelReferencesMap.put(tenant, modelsReferencing);
      }
    }
    
    return modelReferencesMap;
  }

}
