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
package org.eclipse.vorto.repository.workflow.model;

import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.InvalidInputException;

public interface IWorkflowValidator {

  /**
   * validates the given model for the currently executed action
   * 
   * @param model
   * @param currentAction
   * @throws InvalidInputException
   */
  void validate(ModelInfo model, IAction currentAction) throws InvalidInputException;
}
