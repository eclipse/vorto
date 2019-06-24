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
package org.eclipse.vorto.repository.workflow.impl.functions;

import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.PolicyEntry;
import org.eclipse.vorto.repository.core.PolicyEntry.Permission;
import org.eclipse.vorto.repository.core.PolicyEntry.PrincipalType;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrantRoleAccessPolicy implements IWorkflowFunction {

  private IModelRepositoryFactory repositoryFactory;

  private static final Logger logger = LoggerFactory.getLogger(GrantRoleAccessPolicy.class);

  private Role roleToGiveAccess;

  public GrantRoleAccessPolicy(IModelRepositoryFactory repositoryFactory, Role role) {
    this.repositoryFactory = repositoryFactory;
    this.roleToGiveAccess = role;
  }

  @Override
  public void execute(ModelInfo model, IUserContext user) {
    logger.info("Granting permission of model " + model.getId() + " to " + roleToGiveAccess.name() + " role");
    repositoryFactory.getPolicyManager(user.getTenant(), user.getAuthentication()).addPolicyEntry(
        model.getId(),
        PolicyEntry.of(roleToGiveAccess.name(), PrincipalType.Role, Permission.FULL_ACCESS));

  }
}
