package org.eclipse.vorto.service.mapping;

import java.util.Map;

public class DataInputFactory {

	private static DataInputFactory singleton = null;
	
	private DataInputFactory() {
		
	}
	
	public static DataInputFactory getInstance() {
		if (singleton == null) {
			singleton = new DataInputFactory();
		}
		
		return singleton;
	}
	
	public DataInput fromMap(Map<String, Object> input) {
		return new DataInput() {
			
			@Override
			public Map<String, Object> getValue() {
				return input;
			}
		};
	}
	
	public DataInput fromJson(String json) {
		return new JsonInput(json);
	}
}
