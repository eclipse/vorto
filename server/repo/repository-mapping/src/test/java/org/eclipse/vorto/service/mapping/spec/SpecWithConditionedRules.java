package org.eclipse.vorto.service.mapping.spec;

import java.util.Arrays;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.PrimitiveType;
import org.eclipse.vorto.repository.api.content.Stereotype;

public class SpecWithConditionedRules extends AbstractTestSpec {
	
	@Override
	protected void createFBSpec() {
		FunctionblockModel doorState = new FunctionblockModel(
				ModelId.fromPrettyFormat("demo.fb.PushButton:1.0.0"), ModelType.Functionblock);
		ModelProperty digitalInputStateProperty = new ModelProperty();
		digitalInputStateProperty.setMandatory(true);
		digitalInputStateProperty.setName("sensor_value");
		digitalInputStateProperty.setType(PrimitiveType.STRING);
		
		digitalInputStateProperty.setTargetPlatformKey("homeconnect");
		digitalInputStateProperty.addStereotype(Stereotype.createWithConditionalXpath("data.key == 'DoorState'","data/value"));
		
		doorState.setStatusProperties(
				Arrays.asList(new ModelProperty[] { digitalInputStateProperty}));
		
		FunctionblockModel operationState = new FunctionblockModel(
				ModelId.fromPrettyFormat("demo.fb.PushButton:1.0.0"), ModelType.Functionblock);
		ModelProperty digitalInputStateProperty1 = new ModelProperty();
		digitalInputStateProperty1.setMandatory(true);
		digitalInputStateProperty1.setName("sensor_value");
		digitalInputStateProperty1.setType(PrimitiveType.STRING);
		
		digitalInputStateProperty1.setTargetPlatformKey("homeconnect");
		digitalInputStateProperty1.addStereotype(Stereotype.createWithConditionalXpath("data.key == 'OperationState'","data/value"));
		
		operationState.setStatusProperties(
				Arrays.asList(new ModelProperty[] { digitalInputStateProperty1 }));
		
		addFunctionblockProperty("doorState", doorState);
		addFunctionblockProperty("operationState", operationState);
	}

}
