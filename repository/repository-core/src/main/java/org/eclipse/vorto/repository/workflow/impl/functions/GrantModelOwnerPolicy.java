package org.eclipse.vorto.repository.workflow.impl.functions;

import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrantModelOwnerPolicy implements IWorkflowFunction {

	private IModelPolicyManager policyManager;
	
	private static final Logger logger = LoggerFactory.getLogger(GrantModelOwnerPolicy.class);

	
	public GrantModelOwnerPolicy(IModelPolicyManager policyManager) {
		this.policyManager = policyManager;
	}
	
	@Override
	public void execute(ModelInfo model, IUserContext user) {
		logger.info("Restricting permission of model " + model.getId() + " to user '"+user.getUsername()+"'");
		policyManager.grantOwnerAccess(model);
		
	}
}
