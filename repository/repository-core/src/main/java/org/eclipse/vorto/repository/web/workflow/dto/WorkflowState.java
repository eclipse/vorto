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
package org.eclipse.vorto.repository.web.workflow.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.workflow.model.IState;

public class WorkflowState {

  private String name;
  private String description;

  private List<WorkflowAction> actions = new ArrayList<>();

  public WorkflowState(IState state) {
    this.name = state.getName();
    this.description = state.getDescription();
    this.actions.addAll(state.getActions().stream().map(action -> new WorkflowAction(action))
        .collect(Collectors.toList()));

  }

  protected WorkflowState() {

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<WorkflowAction> getActions() {
    return actions;
  }

  public void setActions(List<WorkflowAction> actions) {
    this.actions = actions;
  }


}
