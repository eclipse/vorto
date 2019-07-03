/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.core.refactoring;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.refactor.ChangeSet;
import org.eclipse.vorto.model.refactor.RefactoringTask;
import org.eclipse.vorto.repository.core.IModelRefactoring;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelAlreadyExistsException;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.core.impl.utils.DependencyManager;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.tenant.NewNamespacesNotSupersetException;
import org.eclipse.vorto.repository.utils.ModelUtils;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;

public class DefaultModelRefactoring implements IModelRefactoring {

  private IModelRepository repository;
  
  private ModelId oldModelId;

  private ModelId newModelId;
  
  private Tenant tenant;

  public DefaultModelRefactoring(IModelRepository repository, Tenant tenant, ModelId oldModelId) {
    this.repository = repository;
    this.oldModelId = oldModelId;
    this.newModelId = ModelId.fromPrettyFormat(oldModelId.getPrettyFormat());
    this.tenant = tenant;
  }

  @Override
  public IModelRefactoring newId(ModelId newId) throws ModelAlreadyExistsException, NewNamespacesNotSupersetException {
    
    if (repository.getById(newId) != null) {
      throw new ModelAlreadyExistsException();
    } else if (!newId.getNamespace().startsWith(this.tenant.getDefaultNamespace())) {
      throw new NewNamespacesNotSupersetException();
    }
    this.newModelId = newId;
    return this;
  }
    
  @Override
  public RefactoringResult execute(IUserContext userContext) throws RefactoringProblem {
    ModelWorkspaceReader reader = IModelWorkspace.newReader();

    try {
      ModelResource resource = this.repository.getEMFResource(oldModelId);
      reader.addFile(new ByteArrayInputStream(resource.toDSL()), resource.getType());

      this.repository.getModelsReferencing(oldModelId).forEach(reference -> {
        ModelResource referencingModel = this.repository.getEMFResource(reference.getId());
        try {
          reader.addFile(new ByteArrayInputStream(referencingModel.toDSL()),
              referencingModel.getType());
        } catch (IOException e) {
          throw new RefactoringProblem(
              "Could not read DSL of referencing model " + resource.getId(), e);
        }
      });

      ChangeSet changeSet = RefactoringTask.from(reader.read())
          .toModelId(ModelUtils.toEMFModelId(resource.getId(), resource.getType()),
              ModelUtils.toEMFModelId(newModelId, resource.getType()))
          .execute();

      List<ModelInfo> changedModels = saveChangeSet(changeSet, userContext);

      return RefactoringResult.of(
          changedModels.stream().filter(m -> m.getId().equals(newModelId)).findAny().get(),
          changedModels.stream().filter(m -> !m.getId().equals(newModelId))
              .collect(Collectors.toList()));

    } catch (IOException problem) {
      throw new RefactoringProblem(
          "Could not read content of model that is in scope of refactoring", problem);
    }


  }

  /**
   * Saves the changes in the right order into the model repository
   * 
   * @param changeSet
   * @param userContext
   * @return list of models that were saved
   */
  private List<ModelInfo> saveChangeSet(ChangeSet changeSet, IUserContext userContext) {
    List<ModelInfo> result = new ArrayList<>();

    DependencyManager dm = new DependencyManager();

    changeSet.getChanges().stream().forEach(model -> {
      dm.addResource(new ModelResource(model));
    });

    dm.getSorted().forEach(sortedModel -> {
      result.add(this.repository.save((ModelResource) sortedModel, userContext));
    });

    return result;
  }

}
