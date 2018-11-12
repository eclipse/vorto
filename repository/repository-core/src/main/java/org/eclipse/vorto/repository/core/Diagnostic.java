package org.eclipse.vorto.repository.core;

import org.eclipse.vorto.model.ModelId;

public class Diagnostic {
	private ModelId modelId;
	private String diagnosticMessage;

	public Diagnostic(ModelId modelId, String diagnosticMessage) {
		this.modelId = modelId;
		this.diagnosticMessage = diagnosticMessage;
	}

	public ModelId getModelId() {
		return modelId;
	}

	public void setModelId(ModelId modelId) {
		this.modelId = modelId;
	}

	public String getDiagnosticMessage() {
		return diagnosticMessage;
	}

	public void setDiagnosticMessage(String diagnosticMessage) {
		this.diagnosticMessage = diagnosticMessage;
	}
	
	@Override
	public String toString() {
		return "Diagnostic [modelId=" + modelId + ", diagnosticMessage=" + diagnosticMessage + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diagnosticMessage == null) ? 0 : diagnosticMessage.hashCode());
		result = prime * result + ((modelId == null) ? 0 : modelId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Diagnostic other = (Diagnostic) obj;
		if (diagnosticMessage == null) {
			if (other.diagnosticMessage != null)
				return false;
		} else if (!diagnosticMessage.equals(other.diagnosticMessage))
			return false;
		if (modelId == null) {
			if (other.modelId != null)
				return false;
		} else if (!modelId.equals(other.modelId))
			return false;
		return true;
	}
}
