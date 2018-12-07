package org.eclipse.vorto.repository.core;

import java.util.Collection;

import org.eclipse.vorto.model.ModelId;

public interface IModelPolicyManager {
	
	/**
	 * Gets a list of all policy entries for the given model ID
	 * @param modelID
	 * @param user caller userID requesting the policyentries
	 * @return
	 */
	Collection<PolicyEntry> getPolicyEntries(ModelId modelId, IUserContext user);

	/**
	 * Adds a policy entry for the given model
	 * @param modelId
	 * @param entry
	 */
	void addPolicyEntry(ModelId modelId, PolicyEntry entry); 
	
}
