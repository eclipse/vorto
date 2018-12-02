package org.eclipse.vorto.repository.core;

import org.eclipse.vorto.model.ModelId;

public interface IModelPolicyManager {
	
	/**
	 * 
	 * @param modelInfo
	 */
	void grantOwnerAccess(ModelInfo modelInfo);
	
	/**
	 * Adds a model policy for the given modelID and the given user
	 * @param modelId
	 * @param userOrRole
	 */
	void grantReadAccess(ModelId modelId, String userOrRole);

	/**
	 * Removes the model policy for the given modelId and user
	 * @param modelId
	 * @param userOrRole
	 */
	void revokeReadAccess(ModelId modelId, String userOrRole); 
	
}
