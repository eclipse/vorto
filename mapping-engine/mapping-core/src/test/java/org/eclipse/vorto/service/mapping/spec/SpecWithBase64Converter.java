package org.eclipse.vorto.service.mapping.spec;

import java.util.Arrays;

import org.apache.commons.jxpath.FunctionLibrary;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.PrimitiveType;
import org.eclipse.vorto.repository.api.content.Stereotype;

public class SpecWithBase64Converter extends AbstractTestSpec {

	@Override
	protected void createFBSpec() {
		addFunctionblockProperty("button", createButtonFb());
	}
	
	private FunctionblockModel createButtonFb() {
		FunctionblockModel buttonModel = new FunctionblockModel(ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"),
				ModelType.Functionblock);
		ModelProperty digitalInputStateProperty = new ModelProperty();
		digitalInputStateProperty.setMandatory(true);
		digitalInputStateProperty.setName("digital_input_state");
		digitalInputStateProperty.setType(PrimitiveType.INT);

		digitalInputStateProperty.setTargetPlatformKey("iotbutton");

		digitalInputStateProperty.addStereotype(Stereotype.createWithXpath("base64:decodeString(/data)"));

		buttonModel.setStatusProperties(
				Arrays.asList(new ModelProperty[] { digitalInputStateProperty }));

		return buttonModel;
	}
		
	@Override
	public FunctionLibrary getScriptFunctions(IScriptEvalProvider evalProvider) {
		return new FunctionLibrary();
	}


}
