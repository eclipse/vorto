package org.eclipse.vorto.service.mapping.spec;

import java.util.Arrays;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.PrimitiveType;
import org.eclipse.vorto.repository.api.content.Stereotype;

public class SpecWithTypeConversion extends AbstractTestSpec {

	@Override
	protected void createFBSpec() {
		FunctionblockModel buttonModel = new FunctionblockModel(
				ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"), ModelType.Functionblock);
		ModelProperty digitalInputStateProperty = new ModelProperty();
		digitalInputStateProperty.setMandatory(true);
		digitalInputStateProperty.setName("sensor_value");
		digitalInputStateProperty.setType(PrimitiveType.STRING);
		digitalInputStateProperty.setTargetPlatformKey("iotbutton");
		digitalInputStateProperty.addStereotype(Stereotype.createWithXpath("type:convertDouble(/@lng[1])"));

		buttonModel.setStatusProperties(
				Arrays.asList(new ModelProperty[] { digitalInputStateProperty }));
		
		addFunctionblockProperty("button", buttonModel);
	}

}
