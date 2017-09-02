package org.eclipse.vorto.service.mapping;

import java.util.Map;

public interface DataInput {

	Map<String, Object> getValue();
	
	static DataInputFactory newInstance() {
		return DataInputFactory.getInstance();
	}
}
