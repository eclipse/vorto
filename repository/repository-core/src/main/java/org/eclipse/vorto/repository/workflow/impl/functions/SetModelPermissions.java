package org.eclipse.vorto.repository.workflow.impl.functions;

import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetModelPermissions implements IWorkflowFunction {

	private IModelRepository modelRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(SetModelPermissions.class);

	
	public SetModelPermissions(IModelRepository repository) {
		this.modelRepository = repository;
	}
	
	@Override
	public void execute(ModelInfo model, IUserContext user) {
		logger.info("Restricting permission of model " + model.getId() + " to user '"+user.getUsername()+"'");
		modelRepository.addModelPolicy(model.getId(), user);
		
	}
}
