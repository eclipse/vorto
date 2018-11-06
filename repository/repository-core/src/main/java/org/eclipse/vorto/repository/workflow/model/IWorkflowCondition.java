package org.eclipse.vorto.repository.workflow.model;

import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;

public interface IWorkflowCondition {

	boolean passesCondition(ModelInfo model, IUserContext user);
}
