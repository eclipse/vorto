package org.eclipse.vorto.repository.web.core.dto.mapping;

public class TestMappingResponse {

	private String mappedOutput;
	
	private boolean valid = true;
	
	private String validationError;

	public String getMappedOutput() {
		return mappedOutput;
	}

	public void setMappedOutput(String mappedOutput) {
		this.mappedOutput = mappedOutput;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getValidationError() {
		return validationError;
	}

	public void setValidationError(String validationError) {
		this.validationError = validationError;
	}
	
	
	
}
