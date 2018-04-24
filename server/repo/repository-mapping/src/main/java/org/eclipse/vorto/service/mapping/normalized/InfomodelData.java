package org.eclipse.vorto.service.mapping.normalized;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfomodelData {

	private List<FunctionblockData> functionblocks = new ArrayList<>();
	
	public void withFunctionblock(FunctionblockData data) {
		this.functionblocks.add(data); 
	}

	public List<FunctionblockData> getFunctionblockData() {
		return Collections.unmodifiableList(functionblocks);
	}

	@Override
	public String toString() {
		return "InfomodelData [functionblocks=" + functionblocks + "]";
	}

	public Map<String, Object> toMap() {
		Map<String,Object> data = new HashMap<String, Object>();
		for (FunctionblockData fbData : this.getFunctionblockData()) {
			data.put(fbData.getId(),fbData.toMap());
		}
		return data;
	}
	
	
	
}
