/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.workflow.impl.validators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.InvalidInputException;
import org.eclipse.vorto.repository.workflow.model.IAction;
import org.eclipse.vorto.repository.workflow.model.IWorkflowValidator;

public class CheckStatesOfDependenciesValidator implements IWorkflowValidator {

  private IModelRepository modelRepository;

  private Set<String> states = new HashSet<>();

  public CheckStatesOfDependenciesValidator(IModelRepository modelRepository, String state,
      String... states) {
    this.modelRepository = modelRepository;
    this.states.add(state);
    this.states.addAll(Arrays.asList(states));
  }

  @Override
  public void validate(ModelInfo model, IAction currentAction) throws InvalidInputException {
    for (ModelId referencedModelId : model.getReferences()) {
      ModelInfo referencedModel = this.modelRepository.getById(referencedModelId);
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
