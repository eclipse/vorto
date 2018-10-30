package org.eclipse.vorto.repository.workflow.model;

import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.core.IUserContext;

public interface IWorkflowCondition {

	boolean passesCondition(ModelInfo model, IUserContext user);
}
