package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;

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
import org.eclipse.vorto.service.mapping.converters.JavascriptFunctions;
import org.eclipse.vorto.service.mapping.ditto.DittoOutput;
import org.eclipse.vorto.service.mapping.ditto.Feature;
import org.eclipse.vorto.service.mapping.ditto.JsonToDittoMapper;
import org.eclipse.vorto.service.mapping.loader.RepositoryLoader;
import org.junit.Test;

public class JsonMappingTest {

	@Test
	public void testDittoMapping() throws Exception {
			
		JsonToDittoMapper mapper = IDataMapper.newBuilder()
									.withModelLoader(new DummyModelLoader()).buildDittoMapper();
		
		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";
		
		DittoOutput mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json));
		
		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");
				
		assertEquals(true,(Boolean)buttonFeature.getProperty("digital_input_state"));
		assertEquals(2,buttonFeature.getProperty("digital_input_count"));	
		
		Feature voltageFeature = mappedDittoOutput.getFeatures().get("voltage");
		
		assertEquals(2322f,voltageFeature.getProperty("sensor_value"));
		assertEquals("mV",voltageFeature.getProperty("sensor_units"));
		
		System.out.println(mappedDittoOutput.toJson());
		
	}
	
	@Test
	public void testDittoMappingFromRemoteRepository() throws Exception {
			
		JsonToDittoMapper mapper = IDataMapper.newBuilder()
									.withModelLoader(new RepositoryLoader(ModelId.fromPrettyFormat("devices.aws.button.AWSIoTButton:1.0.0"), "awsiotbutton")).buildDittoMapper();
		
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("clickType", "DOUBLE");
		input.put("batteryVoltage", "2322mV");
		
		DittoOutput mappedDittoOutput = mapper.map(DataInput.newInstance().fromMap(input));
		
		
		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");
				
		assertEquals(true,(Boolean)buttonFeature.getProperty("digital_input_state"));
		assertEquals(2,buttonFeature.getProperty("digital_input_count"));	
		
		Feature voltageFeature = mappedDittoOutput.getFeatures().get("batteryVoltage");
		
		assertEquals(2322f,voltageFeature.getProperty("sensor_value"));
		assertEquals("mV",voltageFeature.getProperty("sensor_units"));
		
		System.out.println(mappedDittoOutput.toJson());
		
	}

	private static class DummyModelLoader implements IModelLoader {

		private static Map<ModelId, FunctionblockModel> FBS = new HashMap<ModelId, FunctionblockModel>(2);
		
		static {
			FunctionblockModel buttonModel = new FunctionblockModel(ModelId.fromPrettyFormat("demo.fb.PushButton:1.0.0"), ModelType.Functionblock);
			ModelProperty digitalInputStateProperty = new ModelProperty();
			digitalInputStateProperty.setMandatory(true);
			digitalInputStateProperty.setName("digital_input_state");
			digitalInputStateProperty.setType(PrimitiveType.BOOLEAN);
			
			digitalInputStateProperty.setTargetPlatformKey("iotbutton");
			digitalInputStateProperty.setStereotype("source");
			Map<String, String> a1 = new HashMap<String, String>();
			a1.put("value", "true");
			digitalInputStateProperty.setMappedAttributes(a1);
			
			ModelProperty digitalInputCount = new ModelProperty();
			digitalInputCount.setMandatory(true);
			digitalInputCount.setName("digital_input_count");
			digitalInputCount.setType(PrimitiveType.INT);
			
			digitalInputCount.setTargetPlatformKey("iotbutton");
			digitalInputCount.setStereotype("source");
			Map<String, String> a2 = new HashMap<String, String>();
			a2.put("xpath", "custom:convertClickType(clickType)"); //SINGLE -> 1, DOUBLE -> 2
			digitalInputCount.setMappedAttributes(a2);
			
			buttonModel.setStatusProperties(Arrays.asList(new ModelProperty[] {digitalInputStateProperty,digitalInputCount}));
			
			FBS.put(buttonModel.getId(), buttonModel);
			
			
			//################# VOLTAGE Model ####################
			
			FunctionblockModel voltageModel = new FunctionblockModel(ModelId.fromPrettyFormat("demo.fb.Voltage:1.0.0"), ModelType.Functionblock);
			ModelProperty sensorValueProperty = new ModelProperty();
			sensorValueProperty.setMandatory(true);
			sensorValueProperty.setName("sensor_value");
			sensorValueProperty.setType(PrimitiveType.FLOAT);
			
			sensorValueProperty.setTargetPlatformKey("iotbutton");
			sensorValueProperty.setStereotype("source");
			Map<String, String> sensorValueAttributes = new HashMap<String, String>();
			sensorValueAttributes.put("xpath", "vorto:toFloat(vorto:substring(batteryVoltage,0,vorto:length(batteryVoltage)-2))");
			sensorValueProperty.setMappedAttributes(sensorValueAttributes);
			
			ModelProperty sensorUnitsProperty = new ModelProperty();
			sensorUnitsProperty.setMandatory(false);
			sensorUnitsProperty.setName("sensor_units");
			sensorUnitsProperty.setType(PrimitiveType.STRING);
			
			sensorUnitsProperty.setTargetPlatformKey("iotbutton");
			sensorUnitsProperty.setStereotype("source");
			Map<String, String> sensorUnitsAttributes = new HashMap<String, String>();
			sensorUnitsAttributes.put("xpath", "vorto:substring(batteryVoltage,vorto:length(batteryVoltage)-2)");
			sensorUnitsProperty.setMappedAttributes(sensorUnitsAttributes);
			
			voltageModel.setStatusProperties(Arrays.asList(new ModelProperty[] {sensorValueProperty,sensorUnitsProperty}));
			
			FBS.put(voltageModel.getId(), voltageModel);
			
		}
		@Override
		public Infomodel getInfoModel() {
			
			Infomodel infomodel = new Infomodel(ModelId.fromPrettyFormat("devices.AWSIoTButton:1.0.0"), ModelType.InformationModel);

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
			return Optional.of(new JavascriptFunctions("custom","convertClickType","function convertClickType(clickType) {if (clickType === 'SINGLE') return 1; else if (clickType === 'DOUBLE') return 2; else return 99;}"));
		}
		
	}
	
	/**
	 * 
	 * Custom functions for data mappers
	 *
	 */
	public static class MyConverterFunctions {
		
		public static int clickType(String clickType) {
			if (clickType.equalsIgnoreCase("single")) {
				return 1;
			} else if (clickType.equalsIgnoreCase("double")) {
				return 2;
			} else {
				return -1;
			}
		}
	}
}
