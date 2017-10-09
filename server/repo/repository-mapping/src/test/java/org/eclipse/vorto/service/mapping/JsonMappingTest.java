package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.vorto.service.mapping.ditto.DittoMapper;
import org.eclipse.vorto.service.mapping.ditto.DittoOutput;
import org.eclipse.vorto.service.mapping.ditto.Feature;
import org.eclipse.vorto.service.mapping.spec.MappingSpecificationProblem;
import org.eclipse.vorto.service.mapping.spec.SpecWithArrayPayload;
import org.eclipse.vorto.service.mapping.spec.SpecWithCustomFunction;
import org.eclipse.vorto.service.mapping.spec.SpecWithTimestamp;
import org.eclipse.vorto.service.mapping.spec.SpecWithTypeConversion;
import org.junit.Test;

public class JsonMappingTest {

	@Test
	public void testDittoMapping() throws Exception {

		DittoMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCustomFunction())
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

		DittoMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithArrayPayload())
				.buildDittoMapper();

		String json = "[{\"clickType\" : \"DOUBLE\" }, {\"clickType\" : \"SINGLE\" }]";
		

		DittoOutput mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals("DOUBLE", buttonFeature.getProperty("sensor_value"));

		System.out.println(mappedDittoOutput.toJson());

	}
	
	static final DateFormat JSON_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
	@Test
	public void testDittoMappingTimestamp() throws Exception {

		DittoMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithTimestamp())
				.buildDittoMapper();

		final Date timestamp = new Date();
		String json = "{\"time\" : "+timestamp.getTime()+"}";
		
		DittoOutput mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals(JSON_DATE_FORMAT.format(timestamp), buttonFeature.getProperty("sensor_value"));

		System.out.println(mappedDittoOutput.toJson());

	}
	
	@Test
	public void testDittoMappingTypeConversion() throws Exception {

		DittoMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithTypeConversion())
				.buildDittoMapper();

		String json = "{\"lng\" : 0.002322}";
		
		DittoOutput mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");

		assertEquals("0.002322", buttonFeature.getProperty("sensor_value"));

		System.out.println(mappedDittoOutput.toJson());

	}

	@Test
	public void testDittoMappingOnlyOneFeature() throws Exception {

		DittoMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCustomFunction())
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
		DittoMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCustomFunction())
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
		DittoMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCustomFunction())
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

}
