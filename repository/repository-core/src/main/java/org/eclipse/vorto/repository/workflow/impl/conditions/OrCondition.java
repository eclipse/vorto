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
