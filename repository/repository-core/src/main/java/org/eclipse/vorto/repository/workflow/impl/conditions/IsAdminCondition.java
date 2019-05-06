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
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.workflow.model.IWorkflowCondition;
import org.springframework.security.core.Authentication;

public class IsAdminCondition implements IWorkflowCondition {

  @Override
  public boolean passesCondition(ModelInfo model, IUserContext user) {
    Authentication authentication = user.getAuthentication();
    return authentication.getAuthorities().stream()
        .anyMatch(ga -> ga.getAuthority().equals(Role.rolePrefix + Role.SYS_ADMIN));
  }

}
