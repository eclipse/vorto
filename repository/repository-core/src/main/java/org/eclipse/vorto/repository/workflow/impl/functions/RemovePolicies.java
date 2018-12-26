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
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.PolicyEntry;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemovePolicies implements IWorkflowFunction {

	private IModelPolicyManager policyManager;
	
	private static final Logger logger = LoggerFactory.getLogger(RemovePolicies.class);

	
	public RemovePolicies(IModelPolicyManager policyManager) {
		this.policyManager = policyManager;
	}
	
	@Override
	public void execute(ModelInfo model, IUserContext user) {
		logger.info("Removing permission from model " + model.getId());
		Collection<PolicyEntry> policies = policyManager.getPolicyEntries(model.getId());
		for (PolicyEntry entry : policies) {
		  logger.info("removing "+entry);
		  policyManager.removePolicyEntry(model.getId(), entry);
		}
	}
}
