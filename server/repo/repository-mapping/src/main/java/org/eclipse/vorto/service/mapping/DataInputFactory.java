package org.eclipse.vorto.service.mapping;

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
		
	public DataInput fromObject(Object obj) {
		return new DataInput() {
			
			@Override
			public Object getValue() {
				return obj;
			}
		};
	}
	
	public DataInput fromJson(String json) {
		return new JsonInput(json);
	}
}
