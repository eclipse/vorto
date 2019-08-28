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

import java.util.Collection;
import java.util.Map;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.PolicyEntry;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveModelReviewerPolicy implements IWorkflowFunction {

  private IModelRepositoryFactory repositoryFactory;

  private static final Logger logger = LoggerFactory.getLogger(RemoveModelReviewerPolicy.class);


  public RemoveModelReviewerPolicy(IModelRepositoryFactory repositoryFactory) {
    this.repositoryFactory = repositoryFactory;
  }

  @Override
  public void execute(ModelInfo model, IUserContext user,Map<String,Object> context) {
    IModelPolicyManager policyManager =
        repositoryFactory.getPolicyManager(user.getTenant(), user.getAuthentication());
    
    logger.info("Removing full access of model to ROLE_MODEL_REVIEWER for " + model.getId());
    Collection<PolicyEntry> policies = policyManager.getPolicyEntries(model.getId());
    for (PolicyEntry policy : policies) {
      if (policy.getPrincipalId().equals(Role.MODEL_REVIEWER.name())) {
        policyManager.removePolicyEntry(model.getId(), policy);
        break;
      }
    }
  }
}
