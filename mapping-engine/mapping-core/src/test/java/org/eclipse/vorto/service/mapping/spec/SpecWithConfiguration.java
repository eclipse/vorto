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

public class SpecWithConfiguration extends AbstractTestSpec {

	@Override
	protected void createFBSpec() {
		FunctionblockModel buttonModel = new FunctionblockModel(
				ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"), ModelType.Functionblock);
		
		
		ModelProperty buttonEnableProperty = new ModelProperty();
		buttonEnableProperty.setMandatory(true);
		buttonEnableProperty.setName("enable");
		buttonEnableProperty.setType(PrimitiveType.BOOLEAN);

		buttonEnableProperty.setTargetPlatformKey("iotbutton");

		buttonEnableProperty.addStereotype(Stereotype.createTarget());
		
		buttonModel.setConfigurationProperties(
				Arrays.asList(new ModelProperty[] { buttonEnableProperty }));
		
		addFunctionblockProperty("button", buttonModel);
	}
	
	@Override
	public FunctionLibrary getScriptFunctions(IScriptEvalProvider evalProvider) {
		return new FunctionLibrary();
	}

}
