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
package org.eclipse.vorto.repository.mapping.async;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.Callable;
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
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class BuildMappingsForModelCallable implements Callable<Entry<ModelId, AbstractModel>> {

  private final IModelRepositoryFactory factory;
  private final SecurityContext context;
  private final RequestAttributes attributes;
  private final Model root;
  private final List<ModelInfo> mappingResources;
  private final Optional<String> platformKey;

  public BuildMappingsForModelCallable(SecurityContext context, RequestAttributes attributes,
      IModelRepositoryFactory factory, Model root, List<ModelInfo> mappingResources,
      Optional<String> platformKey) {
    this.context = context;
    this.attributes = attributes;
    this.factory = factory;
    this.root = root;
    this.mappingResources = mappingResources;
    this.platformKey = platformKey;
  }

  private Optional<MappingModel> getMappingModelForModel(List<ModelInfo> mappingResources,
      Model model) {
    return mappingResources.stream().map(
        modelInfo -> (MappingModel) factory
            .getRepositoryByModelWithoutSessionHelper(modelInfo.getId())
            .getEMFResourceWithoutSessionHelper(modelInfo.getId()).getModel())
        .filter(mappingModel -> isMappingForModel((mappingModel), model)).findFirst();
  }

  private static boolean isMappingForModel(MappingModel p, Model model) {
    final ModelId modelId = new ModelId(model.getName(), model.getNamespace(), model.getVersion());
    return matchesMappingForModel(p, model) && p.getReferences().stream().filter(
        reference -> ModelId.fromReference(reference.getImportedNamespace(), reference.getVersion())
            .equals(modelId)).count() > 0;
  }

  private static boolean matchesMappingForModel(MappingModel p, Model model) {
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

  private static Model flattenHierarchy(Model model) {
    if (model instanceof FunctionblockModel) {
      return ModelConversionUtils.convertToFlatHierarchy((FunctionblockModel) model);
    } else {
      return model;
    }
  }

  @Override
  public Entry<ModelId, AbstractModel> call() throws Exception {
    SecurityContextHolder.setContext(context);
    RequestContextHolder.setRequestAttributes(attributes);

    Optional<MappingModel> mappingModel = getMappingModelForModel(mappingResources, root);
    if (mappingModel.isPresent()) {
      AbstractModel createdModel = ModelDtoFactory
          .createResource(flattenHierarchy(root), mappingModel);
      createdModel.setTargetPlatformKey(platformKey.get());
      return new AbstractMap.SimpleEntry<>(
          new ModelId(root.getName(), root.getNamespace(), root.getVersion()),
          createdModel);
    } else {
      return new AbstractMap.SimpleEntry<>(
          new ModelId(root.getName(), root.getNamespace(), root.getVersion()),
          ModelDtoFactory.createResource(flattenHierarchy(root),
              Optional.empty()));
    }
  }
}
