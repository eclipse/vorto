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

import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.model.IWorkflowCondition;

public class IsOwnerCondition implements IWorkflowCondition {

  @Override
  public boolean passesCondition(ModelInfo model, IUserContext user) {
    return (model.getAuthor().equals(user.getHashedUsername())
        || model.getAuthor().equals(user.getUsername())) && !user.isAnonymous();
  }

}
