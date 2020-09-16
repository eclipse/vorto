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

import org.eclipse.vorto.core.api.model.ModelConversionUtils;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.*;
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
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class ModelIdToModelContentConverter implements IModelConverter<ModelId, ModelContent> {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ModelIdToModelContentConverter.class);

  private IModelRepositoryFactory repositoryFactory;

  public ModelIdToModelContentConverter(IModelRepositoryFactory repositoryFactory) {
    this.repositoryFactory = repositoryFactory;
  }

  @Override
  public ModelContent convert(ModelId modelId, Optional<String> platformKey) {
    modelId = repositoryFactory.getRepositoryByNamespace(modelId.getNamespace())
        .getLatestModelVersionIfLatestTagIsSet(modelId);
    if (!repositoryFactory.getRepositoryByModel(modelId).exists(modelId)) {
      throw new ModelNotFoundException(
          String.format("Model [%s] does not exist", modelId.getPrettyFormat()), null);
    }

    ModelWorkspaceReader workspaceReader = getWorkspaceForModel(modelId);

    ModelContent result = new ModelContent();
    result.setRoot(modelId);

    if (platformKey.isPresent()) {
      final List<ModelInfo> mappingResources = repositoryFactory.getRepositoryByModel(modelId)
          .getMappingModelsForTargetPlatform(modelId, platformKey.get(), Optional.empty());
      if (!mappingResources.isEmpty()) {
        // adding to workspace reader in order to resolve cross linking between mapping models correctly
        mappingResources.forEach(mapping -> workspaceReader.addFile(new ByteArrayInputStream(
                repositoryFactory.getRepositoryByModel(mapping.getId())
                    .getFileContent(mapping.getId(), Optional.empty()).get().getContent()),
            org.eclipse.vorto.model.ModelType.Mapping));

        final IModelWorkspace workspace = workspaceReader.read();
        workspace.get().forEach(model -> {
          Optional<MappingModel> mappingModel = getMappingModelForModel(mappingResources, model);
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

  private static class ModelInfoRetrieverTask extends RecursiveTask<List<ModelInfo>> {

    private static final long serialVersionUID = 3056763240970631787L;
    private List<ModelInfo> modelInfos = new ArrayList<>();
    private ModelId rootID;
    private IModelRepositoryFactory factory;
    private SecurityContext securityContext;
    private RequestAttributes requestAttributes;

    public ModelInfoRetrieverTask(RequestAttributes requestAttributes,
        SecurityContext securityContext, IModelRepositoryFactory factory, ModelId rootID) {
      this.requestAttributes = requestAttributes;
      RequestContextHolder.setRequestAttributes(requestAttributes);
      this.securityContext = securityContext;
      SecurityContextHolder.setContext(securityContext);
      this.factory = factory;
      this.rootID = rootID;
      /*LOGGER.warn(String
          .format("%s%n%s%n%s%n", Thread.currentThread().getName(), requestAttributes,
              securityContext));*/
    }

    @Override
    protected List<ModelInfo> compute() {
      RequestContextHolder.setRequestAttributes(requestAttributes, true);
      SecurityContextHolder.setContext(securityContext);
      /*LOGGER.warn(String
          .format("%s%n%s%n%s%n", Thread.currentThread().getName(), requestAttributes,
              securityContext));*/
      ModelInfo modelInfo =
          factory.getRepositoryByModelWithoutSessionHelper(rootID).getById(rootID);
      if (null != modelInfo) {
        modelInfos.add(modelInfo);
        if (modelInfo.getReferences().isEmpty()) {
          return modelInfos;
        } else {
          return ForkJoinTask.invokeAll(children(modelInfo)).stream().map(ForkJoinTask::join)
              .map(e -> {
                e.addAll(modelInfos);
                return e;
              }).flatMap(Collection::stream).collect(Collectors.toList());
        }
      }
      return modelInfos;
    }

    private Collection<ModelInfoRetrieverTask> children(ModelInfo root) {
      return root.getReferences().stream()
          .map(r -> new ModelInfoRetrieverTask(RequestContextHolder.getRequestAttributes(),
              SecurityContextHolder.getContext(), factory, r)).collect(
              Collectors.toList());
    }
  }

  private List<ModelInfo> getModelWithAllDependencies(ModelId modelId) {
    final List<ModelInfo> modelInfos = new ArrayList<>();

    ForkJoinPool pool = ForkJoinPool.commonPool();
    modelInfos.addAll(pool.invoke(
        new ModelInfoRetrieverTask(RequestContextHolder.getRequestAttributes(),
            SecurityContextHolder.getContext(), repositoryFactory, modelId)));
    return modelInfos;
    /*List<ModelInfo> modelInfos = new ArrayList<>();

    ModelInfo modelResource = repositoryFactory.getRepositoryByModel(modelId).getById(modelId);
    modelInfos.add(modelResource);

    for (ModelId reference : modelResource.getReferences()) {
      modelInfos.addAll(getModelWithAllDependencies(reference));
    }
    return modelInfos;*/

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
