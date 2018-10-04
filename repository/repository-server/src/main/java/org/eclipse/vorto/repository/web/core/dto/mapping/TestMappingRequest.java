package org.eclipse.vorto.repository.web.core.dto.mapping;

public class TestMappingRequest {

	private String sourceJson;
	private MappingSpecification specification;
	
	public String getSourceJson() {
		return sourceJson;
	}
	public void setSourceJson(String sourceJson) {
		this.sourceJson = sourceJson;
	}
	public MappingSpecification getSpecification() {
		return specification;
	}
	public void setSpecification(MappingSpecification specification) {
		this.specification = specification;
	}

}
