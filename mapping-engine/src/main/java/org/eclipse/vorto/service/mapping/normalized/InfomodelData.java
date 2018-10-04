package org.eclipse.vorto.service.mapping.normalized;

import java.util.HashMap;
import java.util.Map;

public class InfomodelData {

	private Map<String, FunctionblockData> functionblocks = new HashMap<>();
	
	public void withFunctionblock(FunctionblockData data) {
		this.functionblocks.put(data.getId(),data); 
	}
	
	public Map<String,FunctionblockData> getProperties() {
		return functionblocks;
	}
	
	public FunctionblockData get(String fbProperty) {
		return functionblocks.get(fbProperty);
	}

	@Override
	public String toString() {
		return "InfomodelData [functionblocks=" + functionblocks + "]";
	}
	
}
