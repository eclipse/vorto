package org.eclipse.vorto.mapping.engine.converter;

import java.util.Arrays;

import org.apache.commons.jxpath.FunctionLibrary;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvaluator;
import org.eclipse.vorto.mapping.engine.functions.ScriptClassFunction;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.PrimitiveType;
import org.eclipse.vorto.repository.api.content.Stereotype;
import org.eclipse.vorto.service.mapping.spec.AbstractTestSpec;

public class SpecWithByteArrayConverter extends AbstractTestSpec {

	@Override
	protected void createFBSpec() {
		FunctionblockModel buttonModel = new FunctionblockModel(
				ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"), ModelType.Functionblock);
		ModelProperty digitalInputStateProperty = new ModelProperty();
		digitalInputStateProperty.setMandatory(true);
		digitalInputStateProperty.setName("sensor_value");
		digitalInputStateProperty.setType(PrimitiveType.STRING);

		digitalInputStateProperty.setTargetPlatformKey("iotbutton");

		digitalInputStateProperty.addStereotype(Stereotype.createWithXpath("button:convertSensorValue(conversion:byteArrayToInt(binaryString:parseHexBinary(/data),array:length(binaryString:parseHexBinary(/data))-1,0,0,1))"));

		buttonModel.setStatusProperties(
				Arrays.asList(new ModelProperty[] { digitalInputStateProperty }));
		
		addFunctionblockProperty("button", buttonModel);
	}
	
	@Override
	public FunctionLibrary getScriptFunctions(IScriptEvalProvider evalProvider) {
		FunctionLibrary library = new FunctionLibrary();
		IScriptEvaluator evaluator = evalProvider.createEvaluator("button");
		evaluator.addScriptFunction(new ScriptClassFunction("convertSensorValue","function convertSensorValue(value) { return value; }"));
		library.addFunctions(evaluator.getFunctions());
		return library;
	}

}
