package org.eclipse.vorto.repository.core.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRetrievalService;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.common.collect.Maps;

@Component
public class ModelRetrievalService extends AbstractModelService implements IModelRetrievalService {

  public ModelRetrievalService(@Autowired ITenantService tenantService, 
      @Autowired ModelRepositoryFactory repoFactory) {
    super(tenantService, repoFactory);
  }

  @Override
  public Optional<Entry<String, ModelInfo>> getModel(ModelId modelId) {
    for (String tenant : getTenants()) {
      IModelRepository modelRepo = getRepository(tenant);
      ModelInfo modelInfo = modelRepo.getById(modelId);
      if (modelInfo != null) {
        return Optional.of(Maps.immutableEntry(tenant, modelInfo));
      }
    }
    
    return Optional.empty();
  }
  
  @Override
  public Optional<ModelResource> getEMFResource(String tenantId, ModelId modelId) {
    IModelRepository modelRepo = getRepository(tenantId);
    return Optional.ofNullable(modelRepo.getEMFResource(modelId));
  }

  @Override
  public Optional<Entry<String, FileContent>> getContent(ModelId modelId) {
    for (String tenant : getTenants()) {
      IModelRepository modelRepo = getRepository(tenant);
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
    
    for (String tenant : getTenants()) {
      IModelRepository modelRepo = getRepository(tenant);
      List<ModelInfo> modelsReferencing = modelRepo.getModelsReferencing(modelId);
      if (modelsReferencing != null && modelsReferencing.size() > 0) {
        modelReferencesMap.put(tenant, modelsReferencing);
      }
    }
    
    return modelReferencesMap;
  }

}
