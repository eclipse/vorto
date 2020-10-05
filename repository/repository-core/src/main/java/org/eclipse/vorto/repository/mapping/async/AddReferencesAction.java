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

import static org.eclipse.vorto.repository.mapping.impl.DefaultPayloadMappingService.initStereotypeIfMissing;
import static org.eclipse.vorto.repository.mapping.impl.DefaultPayloadMappingService.mappingMatchesModelId;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import org.apache.log4j.Logger;
import org.eclipse.vorto.core.api.model.ModelConversionUtils;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelIdFactory;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.EnumModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.IModel;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.repository.conversion.ModelIdToModelContentConverter;
import org.eclipse.vorto.repository.conversion.ModelInfoRetrieverTask;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.impl.utils.DependencyManager;
import org.eclipse.vorto.repository.utils.ModelUtils;
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Adds reference types of the given properties to the mapping specification asynchronously
 */
public class AddReferencesAction extends RecursiveAction {

  private static final Logger LOGGER = Logger.getLogger(AddReferencesAction.class);

  private final IModel root;
  private final String targetPlatformKey;
  private final IModelRepositoryFactory factory;
  private final SecurityContext context;
  private final RequestAttributes attributes;

  public AddReferencesAction(IModel root, String targetPlatformKey, IModelRepositoryFactory factory,
      SecurityContext context, RequestAttributes attributes) {
    this.root = root;
    this.targetPlatformKey = targetPlatformKey;
    this.factory = factory;
    this.context = context;
    this.attributes = attributes;
  }

  @Override
  protected void compute() {
    RequestContextHolder.setRequestAttributes(attributes, true);
    SecurityContextHolder.setContext(context);
    ForkJoinTask.invokeAll(children(root)).stream().map(ForkJoinTask::join);
  }

  private Collection<AddReferencesAction> children(IModel root) {
    Collection<AddReferencesAction> result = new ArrayList<>();

    if (root instanceof Infomodel) {
      Infomodel infomodel = (Infomodel) root;
      for (ModelProperty property : infomodel.getFunctionblocks()) {
        ModelId referenceModelId = (ModelId) property.getType();
        ModelId mappingId = property.getMappingReference();

        IModel referenceModel = null;
        if (mappingId != null) {
          referenceModel = getModelContentByModelAndMappingId(referenceModelId.getPrettyFormat(),
              mappingId.getPrettyFormat());
        } else {
          ModelContent modelContent = getModelContent(referenceModelId, targetPlatformKey);
          referenceModel = modelContent.getModels().get(modelContent.getRoot());
        }
        property.setType((FunctionblockModel) referenceModel);
        result.add(
            new AddReferencesAction(referenceModel, targetPlatformKey, factory, context, attributes)
        );
      }
      return result;

    } else if (root instanceof EntityModel) {
      EntityModel entityModel = (EntityModel) root;
      for (ModelProperty property : entityModel.getProperties()) {
        initStereotypeIfMissing(property);
        if (property.getType() instanceof ModelId) {
          ModelId referenceModelId = (ModelId) property.getType();
          ModelId mappingId = property.getMappingReference();
          IModel referenceModel = null;
          if (mappingId != null) {
            referenceModel = getModelContentByModelAndMappingId(referenceModelId.getPrettyFormat(),
                mappingId.getPrettyFormat());
          } else {
            ModelContent modelContent = getModelContent(referenceModelId, targetPlatformKey);
            referenceModel = modelContent.getModels().get(modelContent.getRoot());
          }
          if (referenceModel instanceof EntityModel) {
            property.setType((EntityModel) referenceModel);
            result.add(
                new AddReferencesAction(referenceModel, targetPlatformKey, factory, context,
                    attributes)
            );
          } else {
            property.setType((EnumModel) referenceModel);
          }
        }
      }
      return result;
    } else if (root instanceof FunctionblockModel) {
      FunctionblockModel fbModel = (FunctionblockModel) root;
      for (ModelProperty property : fbModel.getProperties()) {
        initStereotypeIfMissing(property);
        if (property.getType() instanceof ModelId) {
          ModelId referenceModelId = (ModelId) property.getType();
          ModelId mappingId = property.getMappingReference();
          IModel referenceModel = null;
          if (mappingId != null) {
            referenceModel = getModelContentByModelAndMappingId(referenceModelId.getPrettyFormat(),
                mappingId.getPrettyFormat());
          } else {
            ModelContent modelContent = getModelContent(referenceModelId, targetPlatformKey);
            referenceModel = modelContent.getModels().get(modelContent.getRoot());
          }

          if (referenceModel instanceof EntityModel) {
            property.setType((EntityModel) referenceModel);
            result.add(
                new AddReferencesAction(referenceModel, targetPlatformKey, factory, context,
                    attributes)
            );
          } else {
            property.setType((EnumModel) referenceModel);
          }
        }
      }
      return result;
    } else {
      LOGGER.debug(
          String.format(
              "Unsupported IModel type [%s] for recursive mapping reference async action - skipping",
              root.getClass().getSimpleName()
          )
      );
      return Collections.emptyList();
    }
  }

  private ModelContent getModelContent(ModelId modelId, String targetPlatformKey) {
    ModelIdToModelContentConverter converter = new ModelIdToModelContentConverter(factory);
    return converter.convert(modelId, Optional.of(targetPlatformKey));
  }

  private IModel getModelContentByModelAndMappingId(String modelIDString, String mappingIDString) {

    final ModelId modelID = ModelId.fromPrettyFormat(modelIDString);
    final ModelId mappingID = ModelId.fromPrettyFormat(mappingIDString);

    ModelInfo vortoModelInfo = factory.getRepositoryByModelWithoutSessionHelper(modelID)
        .getById(modelID);
    ModelInfo mappingModelInfo = factory.getRepositoryByModelWithoutSessionHelper(mappingID)
        .getById(mappingID);

    if (vortoModelInfo == null) {
      throw new ModelNotFoundException(
          String.format("Could not find vorto model with ID: [%s]", modelID));
    } else if (mappingModelInfo == null) {
      throw new ModelNotFoundException(
          String.format("Could not find mapping with ID: [%s]", mappingIDString));
    }

    IModelWorkspace mappingWorkspace = getWorkspaceForModel(mappingModelInfo.getId());

    Optional<Model> model = mappingWorkspace
        .get()
        .stream()
        .filter(
            m ->
                ModelUtils
                    .fromEMFModelId(ModelIdFactory.newInstance(m))
                    .equals(vortoModelInfo.getId()))
        .findFirst();

    if (model.isPresent()) {

      final Model flattenedModel = ModelConversionUtils.convertToFlatHierarchy(model.get());
      return ModelDtoFactory
          .createResource(
              flattenedModel,
              Optional.of(
                  (MappingModel) mappingWorkspace
                      .get()
                      .stream()
                      .filter(
                          m -> m instanceof MappingModel
                              &&
                              mappingMatchesModelId(
                                  (MappingModel) m, vortoModelInfo
                              )
                      )
                      .findFirst()
                      .get()
              )
          );
    } else {
      return null;
    }
  }

  private IModelWorkspace getWorkspaceForModel(final ModelId modelId) {
    List<ModelInfo> allModels = getModelWithAllDependencies(modelId);
    DependencyManager dm = new DependencyManager(new HashSet<>(allModels));
    allModels = dm.getSorted();

    ModelWorkspaceReader workspaceReader = IModelWorkspace.newReader();

    for (ModelInfo model : allModels) {
      FileContent modelContent = factory
          .getRepositoryByModelWithoutSessionHelper(model.getId())
          .getFileContent(model.getId(), Optional.of(model.getFileName()))
          .get();
      workspaceReader.addFile(
          new ByteArrayInputStream(modelContent.getContent()),
          model.getType()
      );
    }

    return workspaceReader.read();
  }

  private List<ModelInfo> getModelWithAllDependencies(ModelId modelId) {
    return new ArrayList<>(ForkJoinPool.commonPool()
        .invoke(
            new ModelInfoRetrieverTask(RequestContextHolder.getRequestAttributes(),
                SecurityContextHolder.getContext(), factory, modelId)));
  }

}
