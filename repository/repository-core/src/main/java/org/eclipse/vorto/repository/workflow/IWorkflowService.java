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
package org.eclipse.vorto.repository.workflow;

import java.util.List;
import java.util.Optional;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.model.IState;

public interface IWorkflowService {

  /**
   * Starts the workflow for the given model
   * 
   * @param model
   * @return
   * @throws InvalidInputException
   */
  ModelId start(ModelId model) throws WorkflowException;

  /**
   * Transitions the given models to the next possible state
   * 
   * @param model
   * @param user
   * @param action
   * 
   * @throws WorkflowException
   */
  ModelInfo doAction(ModelId model, IUserContext user, String action) throws WorkflowException;

  /**
   * Retrieves possible actions for the given model
   * 
   * @param model
   * @param user
   * @return
   */
  List<String> getPossibleActions(ModelId model, IUserContext user);

  /**
   * Retrieves all models for the given state
   * 
   * @param state
   * @return
   */
  List<ModelInfo> getModelsByState(String state);

  /**
   * Retrieves the state model of the current state
   * 
   * @param model
   * @return
   */
  Optional<IState> getStateModel(ModelId model);

}
