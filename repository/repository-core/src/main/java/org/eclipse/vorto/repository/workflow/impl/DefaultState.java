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
import java.util.Optional;
import org.eclipse.vorto.repository.workflow.model.IAction;
import org.eclipse.vorto.repository.workflow.model.IState;

public class DefaultState implements IState {

  private String name;
  private String description;

  private List<IAction> actions = new ArrayList<IAction>();

  public DefaultState(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public DefaultState(String name) {
    this(name, "");
  }

  public void setActions(IAction... actions) {
    this.actions = Arrays.asList(actions);
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
  public List<IAction> getActions() {
    return Collections.unmodifiableList(actions);
  }

  @Override
  public Optional<IAction> getAction(String name) {
    return actions.stream().filter(action -> action.getName().equalsIgnoreCase(name)).findFirst();
  }

  @Override
  public String toString() {
    return "DefaultState [name=" + name + ", description=" + description + ", actions=" + actions
        + "]";
  }


}
