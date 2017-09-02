package org.eclipse.vorto.service.mapping;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.JXPathContext;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.IReferenceType;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.PrimitiveType;

public class JsonDataMapper implements IDataMapper<Map<String,Object>> {

	private FunctionblockModel element;
	
	private ClassFunctions function;
	
	public JsonDataMapper(FunctionblockModel element,ClassFunctions function) {
		this.element = element;
		this.function = function;
	}
	
	public Map<String,Object> map(Map<String,Object> json) {
	
		JXPathContext context = newContext(json);
		context.setFunctions(function);
		
		Map<String,Object> output = new HashMap<String, Object>();
		for (ModelProperty statusProperty : element.getStatusProperties()) {
			if (statusProperty.getMappedAttributes().containsKey("value")) {
				String value = statusProperty.getMappedAttributes().get("value");
				output.put(statusProperty.getName(), toType(value,statusProperty.getType()));
			} else {
				output.put(statusProperty.getName(), context.getValue(statusProperty.getMappedAttributes().get("xpath")));
			}
		}
		return output;
	}
	
	private Object toType(String value, IReferenceType type) {
		if (type == PrimitiveType.BOOLEAN) {
			return Boolean.parseBoolean(value);
		} else {
			throw new UnsupportedOperationException("mapped default value cannot be mapped");
		}
	}

	private JXPathContext getSharedContext() {
		JXPathContext context = JXPathContext.newContext(null);
		context.setLenient(true);		       
        return context;
	}
	
    private JXPathContext newContext(Object ctxObject) {
    	return JXPathContext.newContext(getSharedContext(),ctxObject);
    }

}
