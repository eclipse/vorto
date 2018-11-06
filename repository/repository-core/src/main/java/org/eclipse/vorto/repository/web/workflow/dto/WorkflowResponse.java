package org.eclipse.vorto.repository.web.workflow.dto;

import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.WorkflowException;

public class WorkflowResponse {

	private String errorMessage;
	private boolean hasErrors = false;
	
	private ModelInfo model; 
	
	public static WorkflowResponse create(ModelInfo model) {
		WorkflowResponse response = new WorkflowResponse();
		response.model = model;
		return response;
	}
	
	public static WorkflowResponse withErrors(WorkflowException exception) {
		WorkflowResponse response = new WorkflowResponse();
		response.setModel(exception.getModel());
		response.setErrorMessage(exception.getMessage());
		return response;
	}
	
	protected WorkflowResponse() {
		
	}
	
	public void setErrorMessage(String message) {
		this.errorMessage = message;
		this.hasErrors = true;
	}

	public boolean isHasErrors() {
		return hasErrors;
	}

	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}

	public ModelInfo getModel() {
		return model;
	}

	public void setModel(ModelInfo model) {
		this.model = model;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	
}
