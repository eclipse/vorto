package org.eclipse.vorto.mapping.engine;

public class MappingResult<MappedPayload> {
	
	private MappedPayload value;
	
	public MappingResult(MappedPayload value) {
		this.value = value;
	}

	public MappedPayload getValue() {
		return value;
	}
	
}
