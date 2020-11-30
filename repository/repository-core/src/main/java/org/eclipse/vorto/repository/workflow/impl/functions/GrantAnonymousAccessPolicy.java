/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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

import org.eclipse.vorto.repository.core.*;
import org.eclipse.vorto.repository.core.PolicyEntry.Permission;
import org.eclipse.vorto.repository.core.PolicyEntry.PrincipalType;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GrantAnonymousAccessPolicy implements IWorkflowFunction {

  private IModelRepositoryFactory repositoryFactory;

  private static final Logger logger = LoggerFactory.getLogger(GrantAnonymousAccessPolicy.class);


  public GrantAnonymousAccessPolicy(IModelRepositoryFactory repositoryFactory) {
    this.repositoryFactory = repositoryFactory;
  }

  @Deprecated
  @Override
  public void execute(ModelInfo model, IUserContext user,Map<String,Object> context) {
    IModelPolicyManager policyManager =
        repositoryFactory.getPolicyManager(user.getWorkspaceId());

    logger.info("Adding access of model " + model.getId() + " to non-tenant member users");
    policyManager.addPolicyEntry(model.getId(),
        PolicyEntry.of(IModelPolicyManager.ANONYMOUS_ACCESS_POLICY, PrincipalType.User, Permission.READ));
  }
}
