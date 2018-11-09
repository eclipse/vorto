package org.eclipse.vorto.model.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelProperty;

public class ModelValueFactory {

	public static PropertyValue createFBPropertyValue(FunctionblockModel model, String name, Object value) {
		
		ModelProperty property = null;
		ListIterator<ModelProperty> iterator = getAllStateProperties(model).listIterator();
		while (iterator.hasNext()){
			ModelProperty modelProperty = iterator.next();
			if(modelProperty.getName().equals(name)){
				property = modelProperty;
				break;
			}
		}

		if (property == null) {
			throw new IllegalArgumentException("No property defined with this name in Function Block Model");
		}
		
		return new PropertyValue(property,value);
	}
	
	private static List<ModelProperty> getAllStateProperties(FunctionblockModel model) {
		List<ModelProperty> properties = new ArrayList<ModelProperty>();
		properties.addAll(model.getConfigurationProperties());
		properties.addAll(model.getStatusProperties());
		return properties;
	}
}
