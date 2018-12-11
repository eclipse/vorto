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
package org.eclipse.vorto.repository.workflow.impl.conditions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.model.IWorkflowCondition;

public class OrCondition implements IWorkflowCondition {

  private Set<IWorkflowCondition> conditions = new HashSet<>();

  public OrCondition(IWorkflowCondition condition, IWorkflowCondition... otherConditions) {
    this.conditions.add(condition);
    this.conditions.addAll(Arrays.asList(otherConditions));

  }

  @Override
  public boolean passesCondition(ModelInfo model, IUserContext user) {
    for (IWorkflowCondition condition : conditions) {
      if (condition.passesCondition(model, user)) {
        return true;
      }
    }
    return false;
  }

}
