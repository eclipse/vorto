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

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.vorto.core.api.model.mapping.EntitySource;
import org.eclipse.vorto.core.api.model.mapping.EnumSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSource;
import org.eclipse.vorto.core.api.model.mapping.InfomodelSource;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.Source;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.conversion.IModelConverter;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.impl.utils.DependencyManager;
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;

public class ModelIdToModelContentConverter implements IModelConverter<ModelId,ModelContent>{

  private IModelRepository repository;
  
  public ModelIdToModelContentConverter(IModelRepository repository) {
    this.repository = repository;
  }
  
  
  @Override
  public ModelContent convert(ModelId modelId, Optional<String> platformKey) {
    if (!repository.exists(modelId)) {
      throw new ModelNotFoundException("Model does not exist", null);
    }
    
    ModelContent result = new ModelContent();
    result.setRoot(modelId);

    if (platformKey.isPresent()) {
      List<ModelInfo> mappingResource = repository.getMappingModelsForTargetPlatform(modelId, platformKey.get(),Optional.empty());
      if (!mappingResource.isEmpty()) {

        IModelWorkspace workspace = getWorkspaceForModel(mappingResource.get(0).getId());

        workspace.get().stream().forEach(model -> {
          if (!(model instanceof MappingModel)) {
            Optional<Model> mappingModel =
                workspace.get().stream().filter(p -> p instanceof MappingModel)
                    .filter(p -> isMappingForModel((MappingModel) p, model)).findFirst();
            if (mappingModel.isPresent()) {
              result.getModels().put(
                  new ModelId(model.getName(), model.getNamespace(), model.getVersion()),
                  ModelDtoFactory.createResource(model,
                      Optional.of((MappingModel) mappingModel.get())));
            }
          }
        });
      } else {
        IModelWorkspace workspace = getWorkspaceForModel(modelId);

        workspace.get().stream().forEach(model -> {
          result.getModels().put(new ModelId(model.getName(), model.getNamespace(), model.getVersion()),
              ModelDtoFactory.createResource(model, Optional.empty()));
        });
      }
    } else {
      IModelWorkspace workspace = getWorkspaceForModel(modelId);

      workspace.get().stream().forEach(model -> {
        result.getModels().put(new ModelId(model.getName(), model.getNamespace(), model.getVersion()),
            ModelDtoFactory.createResource(model, Optional.empty()));
      });
    }

    return result;
  }
  
  private IModelWorkspace getWorkspaceForModel(final ModelId modelId) {
    List<ModelInfo> allModels = getModelWithAllDependencies(modelId);
    DependencyManager dm = new DependencyManager(new HashSet<>(allModels));
    allModels = dm.getSorted();

    ModelWorkspaceReader workspaceReader = IModelWorkspace.newReader();
    for (ModelInfo model : allModels) {
      FileContent modelContent = repository
          .getFileContent(model.getId(), Optional.of(model.getFileName())).get();
      workspaceReader.addFile(new ByteArrayInputStream(modelContent.getContent()), model.getType());
    }

    return workspaceReader.read();
  }


  private List<ModelInfo> getModelWithAllDependencies(ModelId modelId) {
    List<ModelInfo> modelInfos = new ArrayList<>();

    ModelInfo modelResource = repository.getById(modelId);
    modelInfos.add(modelResource);

    for (ModelId reference : modelResource.getReferences()) {
      modelInfos.addAll(getModelWithAllDependencies(reference));
    }

    return modelInfos;
  }
  
  private boolean isMappingForModel(MappingModel p, Model model) {
    if (p.getRules().isEmpty() || p.getRules().get(0).getSources().isEmpty()) {
      return false;
    }
    Source mappingSource = p.getRules().get(0).getSources().get(0);
    if (mappingSource instanceof InfomodelSource) {
      return EcoreUtil.equals(((InfomodelSource) mappingSource).getModel(), model);
    } else if (mappingSource instanceof FunctionBlockSource) {
      return EcoreUtil.equals(((FunctionBlockSource) mappingSource).getModel(), model);
    } else if (mappingSource instanceof EntitySource) {
      return EcoreUtil.equals(((EntitySource) mappingSource).getModel(), model);
    } else if (mappingSource instanceof EnumSource) {
      return EcoreUtil.equals(((EnumSource) mappingSource).getModel(), model);
    } else {
      return false;
    }
  }
}
