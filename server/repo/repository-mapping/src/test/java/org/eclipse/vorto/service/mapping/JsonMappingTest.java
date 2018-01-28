package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.vorto.repository.api.content.Stereotype;
import org.eclipse.vorto.service.mapping.ditto.DittoData;
import org.eclipse.vorto.service.mapping.ditto.Feature;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;
import org.eclipse.vorto.service.mapping.spec.MappingSpecificationBuilder;
import org.eclipse.vorto.service.mapping.spec.MappingSpecificationProblem;
import org.eclipse.vorto.service.mapping.spec.SpecWithArrayPayload;
import org.eclipse.vorto.service.mapping.spec.SpecWithCondition;
import org.eclipse.vorto.service.mapping.spec.SpecWithCustomFunction;
import org.eclipse.vorto.service.mapping.spec.SpecWithSameFunctionblock;
import org.eclipse.vorto.service.mapping.spec.SpecWithTimestamp;
import org.eclipse.vorto.service.mapping.spec.SpecWithTypeConversion;
import org.junit.Test;

public class JsonMappingTest {

	@Test
	public void testDittoMapping() throws Exception {

		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCustomFunction())
				.buildDittoMapper();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

		DittoData mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals(true, (Boolean) buttonFeature.getStatusProperties().get("digital_input_state"));
		assertEquals(2, buttonFeature.getStatusProperties().get("digital_input_count"));

		Feature voltageFeature = mappedDittoOutput.getFeatures().get("voltage");

		assertEquals(2322f, voltageFeature.getStatusProperties().get("sensor_value"));
		assertEquals("mV", voltageFeature.getStatusProperties().get("sensor_units"));

		System.out.println(mappedDittoOutput.toJson());

	}
	
	@Test
	public void testMapWithSimpleCondition() throws Exception {
		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCondition())
				.buildDittoMapper();

		String json = "{\"count\" : 2 }";

		DittoData mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
		assertNull(mappedDittoOutput.getFeatures().get("button").getStatusProperties().get("sensor_value"));
		assertEquals(2,mappedDittoOutput.getFeatures().get("button").getStatusProperties().get("sensor_value2"));
		
		json = "{\"count\" : 0 }";

		mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
		assertEquals(0,mappedDittoOutput.getFeatures().get("button").getStatusProperties().get("sensor_value"));
		assertNull(mappedDittoOutput.getFeatures().get("button").getStatusProperties().get("sensor_value2"));
	}

	
	@Test
	public void testDittoMappingUsingListInput() throws Exception {

		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(new SpecWithArrayPayload())
				.buildDittoMapper();

		String json = "[{\"clickType\" : \"DOUBLE\" }, {\"clickType\" : \"SINGLE\" }]";
		

		DittoData mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals("DOUBLE", buttonFeature.getStatusProperties().get("sensor_value"));

		System.out.println(mappedDittoOutput.toJson());

	}
	
	static final DateFormat JSON_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
	@Test
	public void testDittoMappingTimestamp() throws Exception {

		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(new SpecWithTimestamp())
				.buildDittoMapper();

		final Date timestamp = new Date();
		String json = "{\"time\" : "+timestamp.getTime()+"}";
		
		DittoData mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals(JSON_DATE_FORMAT.format(timestamp), buttonFeature.getStatusProperties().get("sensor_value"));

		System.out.println(mappedDittoOutput.toJson());

	}
	
	@Test
	public void testDittoMappingTypeConversion() throws Exception {

		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(new SpecWithTypeConversion())
				.buildDittoMapper();

		String json = "[{\"lng\" : 0.002322},{\"lng\" : 0.002222}]";
		
		DittoData mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals("0.002322", buttonFeature.getStatusProperties().get("sensor_value"));

		System.out.println(mappedDittoOutput.toJson());

	}

	@Test
	public void testDittoMappingOnlyOneFeature() throws Exception {

		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCustomFunction())
				.buildDittoMapper();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

		DittoData mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json),
				MappingContext.functionblockProperties("button"));

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals(true, (Boolean) buttonFeature.getStatusProperties().get("digital_input_state"));
		assertEquals(2, buttonFeature.getStatusProperties().get("digital_input_count"));

		assertNull(mappedDittoOutput.getFeatures().get("voltage"));

		System.out.println(mappedDittoOutput.toJson());

	}

	@Test(expected = MappingSpecificationProblem.class)
	public void testBuildMappingSpecificationForInvalidModelId() {
		MappingSpecificationBuilder.create()
			.infomodelId("devices.PhilipsLivingBloo:1.0.0")
			.targetPlatformKey("button")
			.build();
	}

	@Test
	public void testDittoMappingFromRemoteRepository() throws Exception {

		IMappingSpecification mappingSpecification = MappingSpecificationBuilder.create()
											.infomodelId("devices.aws.button.AWSIoTButton:1.0.0")
											.targetPlatformKey("devices_aws_button_AWSIoTButton_1_0_0").build();
		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(mappingSpecification).buildDittoMapper();

		Map<String, Object> input = new HashMap<String, Object>();
		input.put("clickType", "DOUBLE");
		input.put("batteryVoltage", "2322mV");

		DittoData mappedDittoOutput = mapper.map(DataInput.newInstance().fromObject(input), MappingContext.empty());

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals(true, (Boolean) buttonFeature.getStatusProperties().get("digital_input_state"));
		assertEquals(2, buttonFeature.getStatusProperties().get("digital_input_count"));

		Feature voltageFeature = mappedDittoOutput.getFeatures().get("batteryVoltage");

		assertEquals(2322f, voltageFeature.getStatusProperties().get("sensor_value"));
		assertEquals("mV", voltageFeature.getStatusProperties().get("sensor_units"));

		System.out.println(mappedDittoOutput.toJson());

	}
	
	@Test
	public void testCreateDynamicMappingSpec() throws Exception {

		IMappingSpecification mappingSpecification = MappingSpecificationBuilder.create()
											.infomodelId("com.bosch.BoschGLM100C:1.0.0").build();
		
		mappingSpecification.getFunctionBlock("distancesensor").getStatusProperty("distance").get().addStereotype(Stereotype.createWithXpath("/@dist"));
		mappingSpecification.getFunctionBlock("inclinesensor").getStatusProperty("degree").get().addStereotype(Stereotype.createWithXpath("/@incl"));
		
		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(mappingSpecification).buildDittoMapper();

		Map<String, Object> input = new HashMap<String, Object>();
		input.put("dist", 5.3);
		input.put("incl", 38.8);

		DittoData mappedDittoOutput = mapper.map(DataInput.newInstance().fromObject(input), MappingContext.empty());

		Feature distance = mappedDittoOutput.getFeatures().get("distancesensor");

		assertEquals(5.3, distance.getStatusProperties().get("distance"));

		Feature incline = mappedDittoOutput.getFeatures().get("inclinesensor");

		assertEquals(38.8, incline.getStatusProperties().get("degree"));

		System.out.println(mappedDittoOutput.toJson());

	}

	@Test
	public void testMapDevicePayloadWithInitialValue() {
		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCustomFunction())
				.buildDittoMapper();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"0mV\"}";

		DittoData mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals(true, (Boolean) buttonFeature.getStatusProperties().get("digital_input_state"));
		assertEquals(2, buttonFeature.getStatusProperties().get("digital_input_count"));

		Feature voltageFeature = mappedDittoOutput.getFeatures().get("voltage");

		assertEquals(0f, voltageFeature.getStatusProperties().get("sensor_value"));
		assertEquals("mV", voltageFeature.getStatusProperties().get("sensor_units"));

		System.out.println(mappedDittoOutput.toJson());
	}
		
	@Test
	public void testMapSingleFunctionblockOfInfomodel() {
		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCustomFunction())
				.buildDittoMapper();

		String json = "{\"clickType\" : \"DOUBLE\"}";

		DittoData mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
		
		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals(true, (Boolean) buttonFeature.getStatusProperties().get("digital_input_state"));
		assertEquals(2, buttonFeature.getStatusProperties().get("digital_input_count"));

		Feature voltageFeature = mappedDittoOutput.getFeatures().get("voltage");

		assertNull(voltageFeature);

		System.out.println(mappedDittoOutput.toJson());
	}
	
	@Test
	public void testDittoMappingWithInfoModelUsingSameFunctionblock() throws Exception {

		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(new SpecWithSameFunctionblock())
				.buildDittoMapper();

		String json = "{\"btnvalue1\" : 2, \"btnvalue2\": 10}";

		DittoData mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("btn1");

		assertEquals(2, buttonFeature.getStatusProperties().get("sensor_value"));

		Feature button2Feature = mappedDittoOutput.getFeatures().get("btn2");

		assertEquals(10, button2Feature.getStatusProperties().get("sensor_value"));

		System.out.println(mappedDittoOutput.toJson());

	}

}
