package org.eclipse.vorto.service.mapping.spec;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.jxpath.Functions;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.PrimitiveType;
import org.eclipse.vorto.repository.api.content.Stereotype;
import org.eclipse.vorto.service.mapping.internal.converter.JavascriptFunctions;

public abstract class SpecWithMaliciousFunction extends AbstractTestSpec {

	@Override
	protected void createFBSpec() {
		addFunctionblockProperty("button", createButtonFb());
	}
	
	private FunctionblockModel createButtonFb() {
		FunctionblockModel buttonModel = new FunctionblockModel(ModelId.fromPrettyFormat("demo.fb.PushButton:1.0.0"),
				ModelType.Functionblock);
		ModelProperty digitalInputCount = new ModelProperty();
		digitalInputCount.setMandatory(true);
		digitalInputCount.setName("digital_input_count");
		digitalInputCount.setType(PrimitiveType.INT);

		digitalInputCount.setTargetPlatformKey("iotbutton");
		digitalInputCount.addStereotype(Stereotype.createWithXpath("button:convert(clickType)"));

		buttonModel.setStatusProperties(
				Arrays.asList(new ModelProperty[] { digitalInputCount }));

		return buttonModel;
	}
	
	@Override
	public Optional<Functions> getCustomFunctions() {
		JavascriptFunctions functions = new JavascriptFunctions("button");
		functions.addFunction("convert", "function convert(value) { " + getMaliciousFunctionBody() + "}");
		return Optional.of(functions);
	}
	
	protected abstract String getMaliciousFunctionBody();


}
