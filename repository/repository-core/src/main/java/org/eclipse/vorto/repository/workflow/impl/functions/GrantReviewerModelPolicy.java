package org.eclipse.vorto.repository.workflow.impl.functions;

import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.PolicyEntry;
import org.eclipse.vorto.repository.core.PolicyEntry.PrincipalType;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrantReviewerModelPolicy implements IWorkflowFunction {

	private IModelPolicyManager policyManager;
	
	private static final Logger logger = LoggerFactory.getLogger(GrantReviewerModelPolicy.class);

	
	public GrantReviewerModelPolicy(IModelPolicyManager policyManager) {
		this.policyManager = policyManager;
	}
	
	@Override
	public void execute(ModelInfo model, IUserContext caller) {
		logger.info("Granting permission of model " + model.getId() + " to reviewer role");
		policyManager.addPolicyEntry(model.getId(), PolicyEntry.of("ROLE_MODEL_REVIEWER",PrincipalType.Role,true,false));
		
	}
}
