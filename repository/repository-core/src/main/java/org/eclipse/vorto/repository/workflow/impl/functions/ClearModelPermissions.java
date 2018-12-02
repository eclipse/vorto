package org.eclipse.vorto.repository.workflow.impl.functions;

import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClearModelPermissions implements IWorkflowFunction {

	private IModelRepository modelRepository;

	private static final Logger logger = LoggerFactory.getLogger(ClearModelPermissions.class);

	public ClearModelPermissions(IModelRepository repository) {
		this.modelRepository = repository;
	}

	@Override
	public void execute(ModelInfo model, IUserContext user) {
		logger.info("Clearing permissions of model " + model.getId());
		modelRepository.removeModelPolicy(model.getId(), user);
	}
}
