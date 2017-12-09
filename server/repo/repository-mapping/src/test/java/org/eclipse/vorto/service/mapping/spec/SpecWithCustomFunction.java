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

public class SpecWithCustomFunction extends AbstractTestSpec {

	@Override
	protected void createFBSpec() {
		addFunctionblockProperty("button", createButtonFb());
		addFunctionblockProperty("voltage", createVoltageFb());
	}
	
	private FunctionblockModel createButtonFb() {
		FunctionblockModel buttonModel = new FunctionblockModel(ModelId.fromPrettyFormat("demo.fb.PushButton:1.0.0"),
				ModelType.Functionblock);
		ModelProperty digitalInputStateProperty = new ModelProperty();
		digitalInputStateProperty.setMandatory(true);
		digitalInputStateProperty.setName("digital_input_state");
		digitalInputStateProperty.setType(PrimitiveType.BOOLEAN);

		digitalInputStateProperty.setTargetPlatformKey("iotbutton");

		digitalInputStateProperty.addStereotype(Stereotype.createWithValue("true"));

		ModelProperty digitalInputCount = new ModelProperty();
		digitalInputCount.setMandatory(true);
		digitalInputCount.setName("digital_input_count");
		digitalInputCount.setType(PrimitiveType.INT);

		digitalInputCount.setTargetPlatformKey("iotbutton");
		digitalInputCount.addStereotype(Stereotype.createWithXpath("custom:convertClickType(clickType)"));

		buttonModel.setStatusProperties(
				Arrays.asList(new ModelProperty[] { digitalInputStateProperty, digitalInputCount }));

		return buttonModel;
	}
	
	private FunctionblockModel createVoltageFb() {
		FunctionblockModel voltageModel = new FunctionblockModel(ModelId.fromPrettyFormat("demo.fb.Voltage:1.0.0"),
				ModelType.Functionblock);
		ModelProperty sensorValueProperty = new ModelProperty();
		sensorValueProperty.setMandatory(true);
		sensorValueProperty.setName("sensor_value");
		sensorValueProperty.setType(PrimitiveType.FLOAT);

		sensorValueProperty.setTargetPlatformKey("iotbutton");

		sensorValueProperty.addStereotype(Stereotype.createWithXpath(
				"number:toFloat(string:substring(batteryVoltage,0,string:length(batteryVoltage)-2))"));

		ModelProperty sensorUnitsProperty = new ModelProperty();
		sensorUnitsProperty.setMandatory(false);
		sensorUnitsProperty.setName("sensor_units");
		sensorUnitsProperty.setType(PrimitiveType.STRING);

		sensorUnitsProperty.setTargetPlatformKey("iotbutton");
		sensorUnitsProperty.addStereotype(Stereotype.createWithXpath(
				"string:substring(batteryVoltage,string:length(batteryVoltage)-2)"));
		voltageModel
				.setStatusProperties(Arrays.asList(new ModelProperty[] { sensorValueProperty, sensorUnitsProperty }));
		
		return voltageModel;
	}
	
	@Override
	public Optional<Functions> getCustomFunctions() {
		JavascriptFunctions functions = new JavascriptFunctions("custom");
		functions.addFunction("convertAccelType",
				"function convertAccelType(value) { return (value > 32768 ? value - 65536 : value) / 500;}");
		functions.addFunction("convertClickType",
				"function convertClickType(clickType) {if (clickType === 'SINGLE') return 1; else if (clickType === 'DOUBLE') return 2; else return 99;}");
		return Optional.of(functions);
	}


}
