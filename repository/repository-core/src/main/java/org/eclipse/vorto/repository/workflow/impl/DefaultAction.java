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
package org.eclipse.vorto.repository.workflow.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.eclipse.vorto.repository.workflow.model.IAction;
import org.eclipse.vorto.repository.workflow.model.IState;
import org.eclipse.vorto.repository.workflow.model.IWorkflowCondition;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.eclipse.vorto.repository.workflow.model.IWorkflowValidator;

public class DefaultAction implements IAction {

  private String name;
  private String description;
  private IState to;
  private List<IWorkflowCondition> conditions = new ArrayList<IWorkflowCondition>();
  private List<IWorkflowValidator> validators = new ArrayList<IWorkflowValidator>();
  private List<IWorkflowFunction> functions = new ArrayList<IWorkflowFunction>();

  public DefaultAction(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public DefaultAction(String name) {
    this(name, "");
  }

  public void setTo(IState to) {
    this.to = to;
  }

  public void setConditions(IWorkflowCondition... conditions) {
    this.conditions = Arrays.asList(conditions);
  }

  public void setValidators(IWorkflowValidator... validators) {
    this.validators = Arrays.asList(validators);
  }

  public void setFunctions(IWorkflowFunction... functions) {
    this.functions = Arrays.asList(functions);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public IState getTo() {
    return to;
  }

  @Override
  public String toString() {
    return "DefaultAction [name=" + name + ", description=" + description + ", to=" + to + "]";
  }

  @Override
  public List<IWorkflowCondition> getConditions() {
    return Collections.unmodifiableList(conditions);
  }

  @Override
  public List<IWorkflowValidator> getValidators() {
    return Collections.unmodifiableList(validators);
  }

  @Override
  public List<IWorkflowFunction> getFunctions() {
    return Collections.unmodifiableList(functions);
  }
}
