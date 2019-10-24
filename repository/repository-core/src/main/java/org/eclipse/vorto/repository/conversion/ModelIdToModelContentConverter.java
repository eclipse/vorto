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
package org.eclipse.vorto.repository.conversion;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.eclipse.vorto.core.api.model.ModelConversionUtils;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.EntityMappingModel;
import org.eclipse.vorto.core.api.model.mapping.EnumMappingModel;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingModel;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.model.AbstractModel;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.conversion.IModelConverter;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.core.impl.utils.DependencyManager;
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;

public class ModelIdToModelContentConverter implements IModelConverter<ModelId,ModelContent>{

  private IModelRepositoryFactory repositoryFactory;
  
  public ModelIdToModelContentConverter(IModelRepositoryFactory repositoryFactory) {
    this.repositoryFactory = repositoryFactory;
  }
  
  @Override
  public ModelContent convert(ModelId modelId, Optional<String> platformKey) {
    if (!repositoryFactory.getRepositoryByModel(modelId).exists(modelId)) {
      throw new ModelNotFoundException("Model does not exist", null);
    }
    
    ModelWorkspaceReader workspaceReader = getWorkspaceForModel(modelId);
    
    ModelContent result = new ModelContent();
    result.setRoot(modelId);

    if (platformKey.isPresent()) {
      final List<ModelInfo> mappingResources = repositoryFactory.getRepositoryByModel(modelId).getMappingModelsForTargetPlatform(modelId, platformKey.get(),Optional.empty());
      if (!mappingResources.isEmpty()) {
        
        // adding to workspace reader in order to resolve cross linking between mapping models correctly
        mappingResources.forEach(mapping -> {
          workspaceReader.addFile(new ByteArrayInputStream(((ModelResource)mapping).toDSL()), org.eclipse.vorto.model.ModelType.Mapping);
        });
        
        final IModelWorkspace workspace = workspaceReader.read();

        workspace.get().stream().forEach(model -> {
          Optional<MappingModel> mappingModel = getMappingModelForModel(mappingResources,model);
          if (mappingModel.isPresent()) {
            AbstractModel createdModel = ModelDtoFactory.createResource(flattenHierarchy(model),
                Optional.of((MappingModel) mappingModel.get()));
            createdModel.setTargetPlatformKey(platformKey.get());
            result.getModels().put(
                new ModelId(model.getName(), model.getNamespace(), model.getVersion()),createdModel);
          } else {
            result.getModels().put(
                new ModelId(model.getName(), model.getNamespace(), model.getVersion()),ModelDtoFactory.createResource(flattenHierarchy(model),
                    Optional.empty()));
          }
        });
      } else {
        final IModelWorkspace workspace = workspaceReader.read();
        workspace.get().stream().forEach(model -> {
          AbstractModel createdModel = ModelDtoFactory.createResource(flattenHierarchy(model), Optional.empty());
          createdModel.setTargetPlatformKey(platformKey.get());
          result.getModels().put(new ModelId(model.getName(), model.getNamespace(), model.getVersion()),createdModel);
        });
      }
    } else {
      final IModelWorkspace workspace = workspaceReader.read();

      workspace.get().stream().forEach(model -> {
        AbstractModel createdModel = ModelDtoFactory.createResource(flattenHierarchy(model), Optional.empty());
        result.getModels().put(new ModelId(model.getName(), model.getNamespace(), model.getVersion()),createdModel);
      });
    }

    return result;
  }
  
  private Optional<MappingModel> getMappingModelForModel(List<ModelInfo> mappingResources, Model model) {   
    return  mappingResources.stream()
        .filter(mappingModel -> isMappingForModel(((ModelResource) mappingModel), model)).map(t -> (MappingModel)((ModelResource) t).getModel()).findFirst();
  }


  private Model flattenHierarchy(Model model) {
    if (model instanceof FunctionblockModel) {
      return ModelConversionUtils.convertToFlatHierarchy((FunctionblockModel)model);
    } else {
      return model;
    }
  }


  private ModelWorkspaceReader getWorkspaceForModel(final ModelId modelId) {
    List<ModelInfo> allModels = getModelWithAllDependencies(modelId);
    DependencyManager dm = new DependencyManager(new HashSet<>(allModels));
    allModels = dm.getSorted();

    ModelWorkspaceReader workspaceReader = IModelWorkspace.newReader();
    for (ModelInfo model : allModels) {
      FileContent modelContent = repositoryFactory.getRepositoryByModel(model.getId())
          .getFileContent(model.getId(), Optional.of(model.getFileName())).get();
      workspaceReader.addFile(new ByteArrayInputStream(modelContent.getContent()), model.getType());
    }

    return workspaceReader;
  }


  private List<ModelInfo> getModelWithAllDependencies(ModelId modelId) {
    List<ModelInfo> modelInfos = new ArrayList<>();

    ModelInfo modelResource = repositoryFactory.getRepositoryByModel(modelId).getById(modelId);
    modelInfos.add(modelResource);

    for (ModelId reference : modelResource.getReferences()) {
      modelInfos.addAll(getModelWithAllDependencies(reference));
    }

    return modelInfos;
  }
  
  private boolean isMappingForModel(ModelResource resource, Model model) {
    MappingModel p = (MappingModel)resource.getModel();
    
    final ModelId modelId = new ModelId(model.getName(),model.getNamespace(),model.getVersion());

    return matchesMappingForModel(p,model) && p.getReferences().stream().filter(reference -> ModelId.fromReference(reference.getImportedNamespace(), reference.getVersion()).equals(modelId)).count() > 0;
    
//    p.getReferences().forEach(reference -> {
//      ModelId referencedId = ModelId.fromReference(reference.getImportedNamespace(), reference.getVersion());
//      if (referencedId.equals(modelId)) {
//        return true;
//      }
//    });
//    
//    if (p.getRules().isEmpty() || p.getRules().get(0).getSources().isEmpty()) {
//      return false;
//    }
//    Source mappingSource = p.getRules().get(0).getSources().get(0);
//    if (mappingSource instanceof InfomodelSource) {
//      return EcoreUtil.equals(((InfomodelSource) mappingSource).getModel(), model);
//    } else if (mappingSource instanceof FunctionBlockSource) {
//      return EcoreUtil.equals(((FunctionBlockSource) mappingSource).getModel(), model);
//    } else if (mappingSource instanceof EntitySource) {
//      return EcoreUtil.equals(((EntitySource) mappingSource).getModel(), model);
//    } else if (mappingSource instanceof EnumSource) {
//      return EcoreUtil.equals(((EnumSource) mappingSource).getModel(), model);
//    } else {
//      return false;
//    }
  }

  private boolean matchesMappingForModel(MappingModel p, Model model) {
    if (model instanceof InformationModel && p instanceof InfoModelMappingModel) {
      return true;
    } else if (model instanceof FunctionblockModel && p instanceof FunctionBlockMappingModel) {
      return true;
    } else if (model instanceof Entity && p instanceof EntityMappingModel) {
      return true;
    } else if (model instanceof org.eclipse.vorto.core.api.model.datatype.Enum && p instanceof EnumMappingModel) {
      return true;
    } else {
      return false;
    }
  }
}
