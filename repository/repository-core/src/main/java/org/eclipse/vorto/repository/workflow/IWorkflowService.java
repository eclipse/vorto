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
