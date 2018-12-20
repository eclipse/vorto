package org.eclipse.vorto.repository.core;

import java.util.Collection;

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;

public interface IModelPolicyManager {
	
	/**
	 * Gets a list of all policy entries for the given model ID
	 * @param modelID
	 * @param user caller userID requesting the policyentries
	 * @return
	 */
	Collection<PolicyEntry> getPolicyEntries(ModelId modelId, IUserContext user);
	
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
	 * checks if the current user has access to the given model
	 * @param modelId
	 * @param user
	 * @return true if he/she has access, false otherwise
	 */
	boolean isAccessAllowed(ModelId modelId);
}
