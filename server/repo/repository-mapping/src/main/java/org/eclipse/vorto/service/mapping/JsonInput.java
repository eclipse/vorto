package org.eclipse.vorto.service.mapping;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonInput implements DataInput {

	private Object value;
	
	public JsonInput(String json) {
		this.value = parseJson(json);
	}
	
	private Object parseJson(String json) {
		TypeReference<Object> typeRef = new TypeReference<Object>() {};

		ObjectMapper mapper = new ObjectMapper(); 
		try {
			return mapper.readValue(json, typeRef);
		} catch (Exception e) {
			throw new IllegalArgumentException("Provided json not valid");
		}
	}
	
	@Override
	public Object getValue() {
		return this.value;
	}

}
