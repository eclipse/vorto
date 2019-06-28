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
package org.eclipse.vorto.repository.core;

import java.util.Collection;

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.PolicyEntry.Permission;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;

public interface IModelPolicyManager {
	
    public static final String ANONYMOUS_ACCESS_POLICY = "ANONYMOUS";
  
	/**
	 * Gets a list of all policies for the given model
	 * @param modelId
	 * @return
	 * @throws NotAuthorizedException
	 */
	Collection<PolicyEntry> getPolicyEntries(ModelId modelId) throws NotAuthorizedException;

	/**
	 * Adds a policy entry for the given model
	 * @param modelId
	 * @param entries
	 */
	void addPolicyEntry(ModelId modelId, PolicyEntry... entries); 
	
	/**
     * Removes the policy entry for the given model
     * @param modelId
     * @param entryToRemove
     */
	void removePolicyEntry(ModelId modelId, PolicyEntry entryToRemove);
	
	/**
	 * checks if the current user has the given permission for the given model ID
	 * @param modelId
	 * @param permission 
	 * @return true if he/she has access, false otherwise
	 */
	boolean hasPermission(ModelId modelId, Permission permission);
}
