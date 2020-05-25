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
import org.eclipse.vorto.repository.core.PolicyEntry.PrincipalType;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

public class RemoveRoleAccessPolicy implements IWorkflowFunction {

  private IModelRepositoryFactory repositoryFactory;

  private static final Logger LOGGER = LoggerFactory.getLogger(RemoveRoleAccessPolicy.class);
  
  private IRole roleToRemove;

  public RemoveRoleAccessPolicy(IModelRepositoryFactory repositoryFactory, IRole roleToRemove) {
    this.repositoryFactory = repositoryFactory;
    this.roleToRemove = roleToRemove;
  }

  @Override
  public void execute(ModelInfo model, IUserContext user,Map<String,Object> context) {
    IModelPolicyManager policyManager =
        repositoryFactory.getPolicyManager(user.getTenant(), user.getAuthentication());
    
    LOGGER.info("Removing full access of model to " + roleToRemove.getName() + " for " + model.getId());
    Collection<PolicyEntry> policies = policyManager.getPolicyEntries(model.getId());
    for (PolicyEntry policy : policies) {
      if (policy.getPrincipalId().equals(roleToRemove.getName()) &&
          policy.getPrincipalType() == PrincipalType.Role) {
        policyManager.removePolicyEntry(model.getId(), policy);
        break;
      }
    }
  }
}
