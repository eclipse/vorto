package org.eclipse.vorto.service.mapping;

public interface DataInput {

	Object getValue();
	
	static DataInputFactory newInstance() {
		return DataInputFactory.getInstance();
	}
}
