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
package org.eclipse.vorto.repository.workflow.impl.validators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.InvalidInputException;
import org.eclipse.vorto.repository.workflow.model.IAction;
import org.eclipse.vorto.repository.workflow.model.IWorkflowValidator;

public class CheckStatesOfDependenciesValidator implements IWorkflowValidator {

  private IModelRepositoryFactory modelRepositoryFactory;

  private Set<String> states = new HashSet<>();

  public CheckStatesOfDependenciesValidator(IModelRepositoryFactory modelRepositoryFactory, String state,
      String... states) {
    this.modelRepositoryFactory = modelRepositoryFactory;
    this.states.add(state);
    this.states.addAll(Arrays.asList(states));
  }

  @Override
  public void validate(ModelInfo model, IAction currentAction, IUserContext user) throws InvalidInputException {
    for (ModelId referencedModelId : model.getReferences()) {
      ModelInfo referencedModel = modelRepositoryFactory.getRepository(user.getTenant(), user.getAuthentication())
          .getById(referencedModelId);
      if (referencedModel == null) {
        throw new InvalidInputException(
            "Referenced Model with ID '" + referencedModelId + "' does not exist in repository.",
            model);
      } else {
        final String referencedModelState = referencedModel.getState();
        if (!states.contains(referencedModelState)) {
          throw new InvalidInputException("Referenced Model with ID '" + referencedModelId
              + "' is not in state(s) [" + this.states + "]", model);

        }
      }
    }
  }

}
