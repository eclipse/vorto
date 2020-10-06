/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
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
import org.eclipse.vorto.repository.core.impl.utils.DependencyManager;
import org.eclipse.vorto.repository.mapping.async.BuildMappingsForModelCallable;
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class ModelIdToModelContentConverter implements IModelConverter<ModelId, ModelContent> {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ModelIdToModelContentConverter.class);

  private IModelRepositoryFactory repositoryFactory;

  public ModelIdToModelContentConverter(IModelRepositoryFactory repositoryFactory) {
    this.repositoryFactory = repositoryFactory;
  }

  @Override
  public ModelContent convert(ModelId modelId, Optional<String> platformKey) {
    final ModelId modelIdWithLatest = repositoryFactory
        .getRepositoryByNamespace(modelId.getNamespace())
        .getLatestModelVersionIfLatestTagIsSet(modelId);
    if (!repositoryFactory.getRepositoryByModel(modelIdWithLatest).exists(modelIdWithLatest)) {
      throw new ModelNotFoundException(
          String.format("Model [%s] does not exist", modelIdWithLatest.getPrettyFormat()), null);
    }

    ModelWorkspaceReader workspaceReader = getWorkspaceForModel(modelIdWithLatest);

    ModelContent result = new ModelContent();
    result.setRoot(modelIdWithLatest);

    if (platformKey.isPresent()) {
      final List<ModelInfo> mappingResources = repositoryFactory
          .getRepositoryByModel(modelIdWithLatest)
          .getMappingModelsForTargetPlatform(modelIdWithLatest, platformKey.get(),
              Optional.empty());

      if (!mappingResources.isEmpty()) {
        // adding to workspace reader in order to resolve cross linking between mapping models correctly
        mappingResources.forEach(mapping -> workspaceReader.addFile(new ByteArrayInputStream(
                repositoryFactory.getRepositoryByModel(mapping.getId())
                    .getFileContent(mapping.getId(), Optional.empty()).get().getContent()),
            org.eclipse.vorto.model.ModelType.Mapping));

        final IModelWorkspace workspace = workspaceReader.read();
        // starts spawning threads to retrieve models mappings
        final ExecutorService executor = Executors.newCachedThreadPool();
        final List<Future<Entry<ModelId, AbstractModel>>> futures = new ArrayList<>();
        SecurityContext context = SecurityContextHolder.getContext();
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        workspace.get().forEach(model -> {
          futures.add(
              executor.submit(
                  new BuildMappingsForModelCallable(context, attributes, repositoryFactory, model,
                      mappingResources, platformKey)
              )
          );
          /*Optional<MappingModel> mappingModel = getMappingModelForModel(mappingResources, model);
          if (mappingModel.isPresent()) {
            AbstractModel createdModel = ModelDtoFactory
                .createResource(flattenHierarchy(model), mappingModel);
            createdModel.setTargetPlatformKey(platformKey.get());
            result.getModels()
                .put(new ModelId(model.getName(), model.getNamespace(), model.getVersion()),
                    createdModel);
          } else {
            result.getModels().put(
                new ModelId(model.getName(), model.getNamespace(), model.getVersion()),
                ModelDtoFactory.createResource(flattenHierarchy(model),
                    Optional.empty()));
          }*/
        });
        executor.shutdown();
        // iterates over futures starting with tasks already finished, and adds the mapping to the
        // root model content
        Comparator<Future> comp = Comparator.comparing(Future::isDone);
        futures.stream().sorted(comp.reversed()).forEach(f -> {
          try {
            Map.Entry<ModelId, AbstractModel> entry = f.get();
            result.getModels().put(entry.getKey(), entry.getValue());
          } catch (InterruptedException | ExecutionException e) {
            LOGGER.error(
                String.format(
                    "Failed to build mapping for model [%s]]", modelIdWithLatest.getPrettyFormat())
                ,
                e
            );
          }
        });
      } else {
        final IModelWorkspace workspace = workspaceReader.read();
        workspace.get().forEach(model -> {
          AbstractModel createdModel = ModelDtoFactory
              .createResource(flattenHierarchy(model), Optional.empty());
          createdModel.setTargetPlatformKey(platformKey.get());
          result.getModels()
              .put(new ModelId(model.getName(), model.getNamespace(), model.getVersion()),
                  createdModel);
        });
      }
    } else {
      final IModelWorkspace workspace = workspaceReader.read();
      workspace.get().forEach(model -> {
        AbstractModel createdModel = ModelDtoFactory
            .createResource(flattenHierarchy(model), Optional.empty());
        result.getModels()
            .put(new ModelId(model.getName(), model.getNamespace(), model.getVersion()),
                createdModel);
      });
    }
    return result;
  }

  private Optional<MappingModel> getMappingModelForModel(List<ModelInfo> mappingResources,
      Model model) {
    return mappingResources.stream().map(
        modelInfo -> (MappingModel) repositoryFactory.getRepositoryByModel(modelInfo.getId())
            .getEMFResource(modelInfo.getId()).getModel())
        .filter(mappingModel -> isMappingForModel((mappingModel), model)).findFirst();
  }


  private Model flattenHierarchy(Model model) {
    if (model instanceof FunctionblockModel) {
      return ModelConversionUtils.convertToFlatHierarchy((FunctionblockModel) model);
    } else {
      return model;
    }
  }


  private ModelWorkspaceReader getWorkspaceForModel(final ModelId modelId) {
    Collection<ModelInfo> allModels = getModelWithAllDependencies(modelId);
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
    return new ArrayList<>(ForkJoinPool.commonPool()
        .invoke(
            new ModelInfoRetrieverTask(RequestContextHolder.getRequestAttributes(),
                SecurityContextHolder.getContext(), repositoryFactory, modelId)));
  }

  private boolean isMappingForModel(MappingModel p, Model model) {
    final ModelId modelId = new ModelId(model.getName(), model.getNamespace(), model.getVersion());
    return matchesMappingForModel(p, model) && p.getReferences().stream().filter(
        reference -> ModelId.fromReference(reference.getImportedNamespace(), reference.getVersion())
            .equals(modelId)).count() > 0;
  }

  private boolean matchesMappingForModel(MappingModel p, Model model) {
    if (model instanceof InformationModel && p instanceof InfoModelMappingModel) {
      return true;
    } else if (model instanceof FunctionblockModel && p instanceof FunctionBlockMappingModel) {
      return true;
    } else if (model instanceof Entity && p instanceof EntityMappingModel) {
      return true;
    } else if (model instanceof org.eclipse.vorto.core.api.model.datatype.Enum
        && p instanceof EnumMappingModel) {
      return true;
    } else {
      return false;
    }
  }
}
