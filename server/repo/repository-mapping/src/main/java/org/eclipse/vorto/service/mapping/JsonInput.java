package org.eclipse.vorto.service.mapping;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonInput implements DataInput {

	private Map<String,Object> value;
	
	public JsonInput(String json) {
		this.value = parseJson(json);
	}
	
	private Map<String,Object> parseJson(String json) {
		TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};

		ObjectMapper mapper = new ObjectMapper(); 
		try {
			return mapper.readValue(json, typeRef);
		} catch (Exception e) {
			throw new IllegalArgumentException("Provided json not valid");
		}
	}
	
	@Override
	public Map<String, Object> getValue() {
		return this.value;
	}

}
