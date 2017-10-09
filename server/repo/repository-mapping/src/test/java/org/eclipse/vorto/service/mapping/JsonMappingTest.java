package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.jxpath.Functions;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.PrimitiveType;
import org.eclipse.vorto.repository.api.content.Stereotype;
import org.eclipse.vorto.service.mapping.converters.JavascriptFunctions;
import org.eclipse.vorto.service.mapping.ditto.DittoMapper;
import org.eclipse.vorto.service.mapping.ditto.DittoOutput;
import org.eclipse.vorto.service.mapping.ditto.Feature;
import org.eclipse.vorto.service.mapping.spec.MappingSpecificationProblem;
import org.junit.Test;

public class JsonMappingTest {

	@Test
	public void testDittoMapping() throws Exception {

		DittoMapper mapper = IDataMapper.newBuilder().withSpecification(new TestMappingSpecification())
				.buildDittoMapper();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

		DittoOutput mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals(true, (Boolean) buttonFeature.getProperty("digital_input_state"));
		assertEquals(2, buttonFeature.getProperty("digital_input_count"));

		Feature voltageFeature = mappedDittoOutput.getFeatures().get("voltage");

		assertEquals(2322f, voltageFeature.getProperty("sensor_value"));
		assertEquals("mV", voltageFeature.getProperty("sensor_units"));

		System.out.println(mappedDittoOutput.toJson());

	}
	
	@Test
	public void testDittoMappingUsingListInput() throws Exception {

		DittoMapper mapper = IDataMapper.newBuilder().withSpecification(new TestMappingSpecification2())
				.buildDittoMapper();

		String json = "[{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\" }, {\"clickType\" : \"SINGLE\", \"batteryVoltage\": \"4444mV\" }]";
		
		
		
		DittoOutput mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals(true, (Boolean) buttonFeature.getProperty("digital_input_state"));
		assertEquals(2, buttonFeature.getProperty("digital_input_count"));

		Feature voltageFeature = mappedDittoOutput.getFeatures().get("voltage");

		assertEquals(2322f, voltageFeature.getProperty("sensor_value"));
		assertEquals("mV", voltageFeature.getProperty("sensor_units"));

		System.out.println(mappedDittoOutput.toJson());

	}

	@Test
	public void testDittoMappingOnlyOneFeature() throws Exception {

		DittoMapper mapper = IDataMapper.newBuilder().withSpecification(new TestMappingSpecification())
				.buildDittoMapper();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

		DittoOutput mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json),
				MappingContext.functionblockProperties("button"));

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals(true, (Boolean) buttonFeature.getProperty("digital_input_state"));
		assertEquals(2, buttonFeature.getProperty("digital_input_count"));

		assertNull(mappedDittoOutput.getFeatures().get("voltage"));

		System.out.println(mappedDittoOutput.toJson());

	}

	@Test(expected = MappingSpecificationProblem.class)
	public void testBuildMappingSpecificationForInvalidModelId() {
		IMappingSpecification.newBuilder().modelId("devices.PhilipsLivingBloo:1.0.0").build();
	}

	@Test
	public void testDittoMappingFromRemoteRepository() throws Exception {

		DittoMapper mapper = IDataMapper.newBuilder()
				.withSpecification(
						IMappingSpecification.newBuilder().modelId("devices.aws.button.AWSIoTButton:1.0.0").build())
				.buildDittoMapper();

		Map<String, Object> input = new HashMap<String, Object>();
		input.put("clickType", "DOUBLE");
		input.put("batteryVoltage", "2322mV");

		DittoOutput mappedDittoOutput = mapper.map(DataInput.newInstance().fromObject(input), MappingContext.empty());

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals(true, (Boolean) buttonFeature.getProperty("digital_input_state"));
		assertEquals(2, buttonFeature.getProperty("digital_input_count"));

		Feature voltageFeature = mappedDittoOutput.getFeatures().get("batteryVoltage");

		assertEquals(2322f, voltageFeature.getProperty("sensor_value"));
		assertEquals("mV", voltageFeature.getProperty("sensor_units"));

		System.out.println(mappedDittoOutput.toJson());

	}

	@Test
	public void testMapDevicePayloadWithInitialValue() {
		DittoMapper mapper = IDataMapper.newBuilder().withSpecification(new TestMappingSpecification())
				.buildDittoMapper();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"0mV\"}";

		DittoOutput mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals(true, (Boolean) buttonFeature.getProperty("digital_input_state"));
		assertEquals(2, buttonFeature.getProperty("digital_input_count"));

		Feature voltageFeature = mappedDittoOutput.getFeatures().get("voltage");

		assertEquals(0f, voltageFeature.getProperty("sensor_value"));
		assertEquals("mV", voltageFeature.getProperty("sensor_units"));

		System.out.println(mappedDittoOutput.toJson());
	}
		
	@Test
	public void testMapSingleFunctionblockOfInfomodel() {
		DittoMapper mapper = IDataMapper.newBuilder().withSpecification(new TestMappingSpecification())
				.buildDittoMapper();

		String json = "{\"clickType\" : \"DOUBLE\"}";

		DittoOutput mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
		
		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals(true, (Boolean) buttonFeature.getProperty("digital_input_state"));
		assertEquals(2, buttonFeature.getProperty("digital_input_count"));

		Feature voltageFeature = mappedDittoOutput.getFeatures().get("voltage");

		assertNull(voltageFeature);

		System.out.println(mappedDittoOutput.toJson());
	}

	private static class TestMappingSpecification implements IMappingSpecification {

		private static Map<ModelId, FunctionblockModel> FBS = new HashMap<ModelId, FunctionblockModel>(2);

		static {
			FunctionblockModel buttonModel = new FunctionblockModel(
					ModelId.fromPrettyFormat("demo.fb.PushButton:1.0.0"), ModelType.Functionblock);
			ModelProperty digitalInputStateProperty = new ModelProperty();
			digitalInputStateProperty.setMandatory(true);
			digitalInputStateProperty.setName("digital_input_state");
			digitalInputStateProperty.setType(PrimitiveType.BOOLEAN);

			digitalInputStateProperty.setTargetPlatformKey("iotbutton");

			digitalInputStateProperty.addStereotype(Stereotype.createWithValue("source", "true"));

			ModelProperty digitalInputCount = new ModelProperty();
			digitalInputCount.setMandatory(true);
			digitalInputCount.setName("digital_input_count");
			digitalInputCount.setType(PrimitiveType.INT);

			digitalInputCount.setTargetPlatformKey("iotbutton");
			digitalInputCount.addStereotype(Stereotype.createWithXpath("source", "custom:convertClickType(clickType)"));// SINGLE
																														// ->
																														// 1,
																														// DOUBLE
																														// ->
																														// 2

			buttonModel.setStatusProperties(
					Arrays.asList(new ModelProperty[] { digitalInputStateProperty, digitalInputCount }));

			FBS.put(buttonModel.getId(), buttonModel);

			// ################# VOLTAGE Model ####################

			FunctionblockModel voltageModel = new FunctionblockModel(ModelId.fromPrettyFormat("demo.fb.Voltage:1.0.0"),
					ModelType.Functionblock);
			ModelProperty sensorValueProperty = new ModelProperty();
			sensorValueProperty.setMandatory(true);
			sensorValueProperty.setName("sensor_value");
			sensorValueProperty.setType(PrimitiveType.FLOAT);

			sensorValueProperty.setTargetPlatformKey("iotbutton");

			sensorValueProperty.addStereotype(Stereotype.createWithXpath("source",
					"number:toFloat(string:substring(batteryVoltage,0,string:length(batteryVoltage)-2))"));

			ModelProperty sensorUnitsProperty = new ModelProperty();
			sensorUnitsProperty.setMandatory(false);
			sensorUnitsProperty.setName("sensor_units");
			sensorUnitsProperty.setType(PrimitiveType.STRING);

			sensorUnitsProperty.setTargetPlatformKey("iotbutton");
			sensorUnitsProperty.addStereotype(Stereotype.createWithXpath("source",
					"string:substring(batteryVoltage,string:length(batteryVoltage)-2)"));
			voltageModel.setStatusProperties(
					Arrays.asList(new ModelProperty[] { sensorValueProperty, sensorUnitsProperty }));

			FBS.put(voltageModel.getId(), voltageModel);

		}

		@Override
		public Infomodel getInfoModel() {

			Infomodel infomodel = new Infomodel(ModelId.fromPrettyFormat("devices.AWSIoTButton:1.0.0"),
					ModelType.InformationModel);

			ModelProperty buttonProperty = new ModelProperty();
			buttonProperty.setName("button");
			buttonProperty.setType(ModelId.fromPrettyFormat("demo.fb.PushButton:1.0.0"));
			infomodel.getFunctionblocks().add(buttonProperty);

			ModelProperty voltageProperty = new ModelProperty();
			voltageProperty.setName("voltage");
			voltageProperty.setType(ModelId.fromPrettyFormat("demo.fb.Voltage:1.0.0"));
			infomodel.getFunctionblocks().add(voltageProperty);

			return infomodel;
		}

		@Override
		public FunctionblockModel getFunctionBlock(ModelId modelId) {
			return FBS.get(modelId);
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
	
	private static class TestMappingSpecification2 implements IMappingSpecification {

		private static final String PREFIX = "/@";
		
		private static Map<ModelId, FunctionblockModel> FBS = new HashMap<ModelId, FunctionblockModel>(2);

		static {
			FunctionblockModel buttonModel = new FunctionblockModel(
					ModelId.fromPrettyFormat("demo.fb.PushButton:1.0.0"), ModelType.Functionblock);
			ModelProperty digitalInputStateProperty = new ModelProperty();
			digitalInputStateProperty.setMandatory(true);
			digitalInputStateProperty.setName("digital_input_state");
			digitalInputStateProperty.setType(PrimitiveType.BOOLEAN);

			digitalInputStateProperty.setTargetPlatformKey("iotbutton");

			digitalInputStateProperty.addStereotype(Stereotype.createWithValue("source", "true"));

			ModelProperty digitalInputCount = new ModelProperty();
			digitalInputCount.setMandatory(true);
			digitalInputCount.setName("digital_input_count");
			digitalInputCount.setType(PrimitiveType.INT);

			digitalInputCount.setTargetPlatformKey("iotbutton");
			digitalInputCount.addStereotype(Stereotype.createWithXpath("source", "custom:convertClickType("+PREFIX+"clickType[1])"));// SINGLE
																														// ->
																														// 1,
																														// DOUBLE
																														// ->
																														// 2

			buttonModel.setStatusProperties(
					Arrays.asList(new ModelProperty[] { digitalInputStateProperty, digitalInputCount }));

			FBS.put(buttonModel.getId(), buttonModel);

			// ################# VOLTAGE Model ####################

			FunctionblockModel voltageModel = new FunctionblockModel(ModelId.fromPrettyFormat("demo.fb.Voltage:1.0.0"),
					ModelType.Functionblock);
			ModelProperty sensorValueProperty = new ModelProperty();
			sensorValueProperty.setMandatory(true);
			sensorValueProperty.setName("sensor_value");
			sensorValueProperty.setType(PrimitiveType.FLOAT);

			sensorValueProperty.setTargetPlatformKey("iotbutton");

			sensorValueProperty.addStereotype(Stereotype.createWithXpath("source",
					"number:toFloat(string:substring("+PREFIX+"batteryVoltage[1],0,string:length("+PREFIX+"batteryVoltage[1])-2))"));

			ModelProperty sensorUnitsProperty = new ModelProperty();
			sensorUnitsProperty.setMandatory(false);
			sensorUnitsProperty.setName("sensor_units");
			sensorUnitsProperty.setType(PrimitiveType.STRING);

			sensorUnitsProperty.setTargetPlatformKey("iotbutton");
			sensorUnitsProperty.addStereotype(Stereotype.createWithXpath("source",
					"string:substring("+PREFIX+"batteryVoltage[1],string:length("+PREFIX+"batteryVoltage[1])-2)"));
			voltageModel.setStatusProperties(
					Arrays.asList(new ModelProperty[] { sensorValueProperty, sensorUnitsProperty }));

			FBS.put(voltageModel.getId(), voltageModel);

		}

		@Override
		public Infomodel getInfoModel() {

			Infomodel infomodel = new Infomodel(ModelId.fromPrettyFormat("devices.AWSIoTButton:1.0.0"),
					ModelType.InformationModel);

			ModelProperty buttonProperty = new ModelProperty();
			buttonProperty.setName("button");
			buttonProperty.setType(ModelId.fromPrettyFormat("demo.fb.PushButton:1.0.0"));
			infomodel.getFunctionblocks().add(buttonProperty);

			ModelProperty voltageProperty = new ModelProperty();
			voltageProperty.setName("voltage");
			voltageProperty.setType(ModelId.fromPrettyFormat("demo.fb.Voltage:1.0.0"));
			infomodel.getFunctionblocks().add(voltageProperty);

			return infomodel;
		}

		@Override
		public FunctionblockModel getFunctionBlock(ModelId modelId) {
			return FBS.get(modelId);
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
}
