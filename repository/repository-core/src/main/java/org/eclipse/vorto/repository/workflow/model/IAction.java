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

import java.util.List;

public interface IAction extends IWorkflowElement {

  /**
   * @return the state the the action is pointing to
   */
  IState getTo();

  /**
   * 
   * @return all conditions for the action
   */
  List<IWorkflowCondition> getConditions();

  /**
   * 
   * @return all validators for the action
   */
  List<IWorkflowValidator> getValidators();

  /**
   * 
   * @return all functions for the action
   */
  List<IWorkflowFunction> getFunctions();
}
