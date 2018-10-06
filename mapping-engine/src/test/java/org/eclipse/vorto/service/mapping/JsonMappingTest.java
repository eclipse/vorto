package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.vorto.service.mapping.normalized.FunctionblockData;
import org.eclipse.vorto.service.mapping.normalized.InfomodelData;
import org.eclipse.vorto.service.mapping.spec.SpecWithArrayPayload;
import org.eclipse.vorto.service.mapping.spec.SpecWithBase64Converter;
import org.eclipse.vorto.service.mapping.spec.SpecWithCondition;
import org.eclipse.vorto.service.mapping.spec.SpecWithConditionFunction;
import org.eclipse.vorto.service.mapping.spec.SpecWithConditionXpath;
import org.eclipse.vorto.service.mapping.spec.SpecWithConditionedRules;
import org.eclipse.vorto.service.mapping.spec.SpecWithConfigMapping;
import org.eclipse.vorto.service.mapping.spec.SpecWithCustomFunction;
import org.eclipse.vorto.service.mapping.spec.SpecWithMaliciousFunction;
import org.eclipse.vorto.service.mapping.spec.SpecWithSameFunctionblock;
import org.eclipse.vorto.service.mapping.spec.SpecWithTimestamp;
import org.eclipse.vorto.service.mapping.spec.SpecWithTypeConversion;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMappingTest extends AbstractMappingTest {
	
	@Test
	public void testConfigMapping() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithConfigMapping())
				.build();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		System.out.println(mappedOutput);

	}

	@Test
	public void testMapping() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCustomFunction())
				.build();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		FunctionblockData buttonFunctionblockData = mappedOutput.get("button");

		assertEquals(true, (Boolean) buttonFunctionblockData.getStatus().get("digital_input_state"));
		assertEquals(2, buttonFunctionblockData.getStatus().get("digital_input_count"));

		FunctionblockData voltageFunctionblockData = mappedOutput.get("voltage");

		assertEquals(2322f, voltageFunctionblockData.getStatus().get("sensor_value"));
		assertEquals("mV", voltageFunctionblockData.getStatus().get("sensor_units"));

		System.out.println(new ObjectMapper().writeValueAsString(mappedOutput.getProperties()));

	}
	
	@Test(expected = MappingException.class)
	public void testMappingWithMalicousScript() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithMaliciousFunction() {

			@Override
			protected String getMaliciousFunctionBody() {
				return "return quit();";
			}
			
		}).build();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

		mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
	}
	
	@Test(expected = MappingException.class)
	public void testMappingWithMalicousScript2() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithMaliciousFunction() {

			@Override
			protected String getMaliciousFunctionBody() {
				return "return exit();";
			}
			
		}).build();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

		mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
	}
	
	@Test(expected = MappingException.class)
	public void testMappingWithMalicousScript3() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithMaliciousFunction() {

			@Override
			protected String getMaliciousFunctionBody() {
				return "while (true) { }";
			}
			
		}).build();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

		mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
	}
	
	@Test(expected = MappingException.class)
	public void testMappingWithMalicousScript4() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithMaliciousFunction() {

			@Override
			protected String getMaliciousFunctionBody() {
				return "for (;;) { }";
			}
			
		}).build();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

		mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
	}
	
	@Test(expected = MappingException.class)
	public void testMappingWithMalicousScriptUsingJavaImports() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithMaliciousFunction() {

			@Override
			protected String getMaliciousFunctionBody() {
				return "load('https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.8.0/highlight.min.js')";
			}
		}).build();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

		mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
	}

	@Test
	public void testMapWithSimpleCondition() throws Exception {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCondition())
				.build();

		String json = "{\"count\" : 2 }";

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
		assertNull(mappedOutput.get("button").getStatus().get("sensor_value"));
		assertEquals(2, mappedOutput.get("button").getStatus().get("sensor_value2"));

		json = "{\"count\" : 0 }";

		mappedOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
		assertEquals(0, mappedOutput.get("button").getStatus().get("sensor_value"));
		assertNull(mappedOutput.get("button").getStatus().get("sensor_value2"));
	}

	@Test
	public void testMapWithCustomFunctionCondition() throws Exception {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithConditionFunction())
				.build();

		String json = "{\"data\" : \"aGFsbG8=\"}";

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
		assertEquals("hallo", mappedOutput.get("button").getStatus().get("sensor_value"));

	}

	@Test
	public void testMapWithJxpathCondition() throws Exception {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithConditionXpath())
				.build();

		String json = "{\"data\" : [{\"id\": 100,\"value\": \"x\"},{\"id\": 200,\"value\": \"y\"}]}";

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
		assertEquals(100.0, mappedOutput.get("button").getStatus().get("sensor_value"));

	}

	@Test
	public void testMapUsingBase64Converter() throws Exception {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithBase64Converter())
				.build();

		String json = "{\"data\" : \"" + Base64.encodeBase64String("20".getBytes()) + "\"}";

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
		assertEquals("20", new String((byte[]) mappedOutput.get("button").getStatus()
				.get("digital_input_state")));

	}

	@Test
	public void testMappingUsingListInput() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithArrayPayload())
				.build();

		String json = "[{\"clickType\" : \"DOUBLE\" }, {\"clickType\" : \"SINGLE\" }]";

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		FunctionblockData buttonFunctionblockData = mappedOutput.get("button");

		assertEquals("DOUBLE", buttonFunctionblockData.getStatus().get("sensor_value"));

		System.out.println(mappedOutput);

	}

	static final DateFormat JSON_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

	@Test
	public void testMappingTimestamp() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithTimestamp())
				.build();

		final Date timestamp = new Date();
		String json = "{\"time\" : " + timestamp.getTime() + "}";

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		FunctionblockData buttonFunctionblockData = mappedOutput.get("button");

		assertEquals(JSON_DATE_FORMAT.format(timestamp), buttonFunctionblockData.getStatus().get("sensor_value"));

		System.out.println(mappedOutput);

	}

	@Test
	public void testMappingTypeConversion() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithTypeConversion())
				.build();

		String json = "[{\"lng\" : 0.002322},{\"lng\" : 0.002222}]";

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		FunctionblockData buttonFunctionblockData = mappedOutput.get("button");

		assertEquals("0.002322", buttonFunctionblockData.getStatus().get("sensor_value"));

		System.out.println(mappedOutput);

	}

	@Test
	public void testMappingOnlyOneFunctionblockData() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCustomFunction())
				.build();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(json),
				MappingContext.functionblockProperties("button"));

		FunctionblockData buttonFunctionblockData = mappedOutput.get("button");

		assertEquals(true, (Boolean) buttonFunctionblockData.getStatus().get("digital_input_state"));
		assertEquals(2, buttonFunctionblockData.getStatus().get("digital_input_count"));

		assertNull(mappedOutput.get("voltage"));

		System.out.println(mappedOutput);

	}


	@Test
	public void testMapDevicePayloadWithInitialValue() {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCustomFunction())
				.build();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"0mV\"}";

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		FunctionblockData buttonFunctionblockData = mappedOutput.get("button");

		assertEquals(true, (Boolean) buttonFunctionblockData.getStatus().get("digital_input_state"));
		assertEquals(2, buttonFunctionblockData.getStatus().get("digital_input_count"));

		FunctionblockData voltageFunctionblockData = mappedOutput.get("voltage");

		assertEquals(0f, voltageFunctionblockData.getStatus().get("sensor_value"));
		assertEquals("mV", voltageFunctionblockData.getStatus().get("sensor_units"));

		System.out.println(mappedOutput);
	}

	@Test
	public void testMapSingleFunctionblockOfInfomodel() {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCustomFunction())
				.build();

		String json = "{\"clickType\" : \"DOUBLE\"}";

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		FunctionblockData buttonFunctionblockData = mappedOutput.get("button");

		assertEquals(true, (Boolean) buttonFunctionblockData.getStatus().get("digital_input_state"));
		assertEquals(2, buttonFunctionblockData.getStatus().get("digital_input_count"));

		FunctionblockData voltageFunctionblockData = mappedOutput.get("voltage");

		assertNull(voltageFunctionblockData);

		System.out.println(mappedOutput);
	}

	@Test
	public void testMapSingleFunctionblockOfInfomodel2() {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithConditionedRules())
				.build();
		
		final String sampleHomeConnectRESTResponse = "{\"data\" : { \"key\" : \"DoorState\", \"value\" : \"Locked\"}}";

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(sampleHomeConnectRESTResponse), MappingContext.empty());
		System.out.println(mappedOutput);
		assertNull(mappedOutput.get("operationState"));
		FunctionblockData doorStateFunctionblockData = mappedOutput.get("doorState");
		assertEquals("Locked", (String)doorStateFunctionblockData.getStatus().get("sensor_value"));
		
	}

	@Test
	public void testMappingWithInfoModelUsingSameFunctionblock() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithSameFunctionblock())
				.build();

		String json = "{\"btnvalue1\" : 2, \"btnvalue2\": 10}";

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		FunctionblockData buttonFunctionblockData = mappedOutput.get("btn1");

		assertEquals(2, buttonFunctionblockData.getStatus().get("sensor_value"));

		FunctionblockData button2FunctionblockData = mappedOutput.get("btn2");

		assertEquals(10, button2FunctionblockData.getStatus().get("sensor_value"));

		System.out.println(mappedOutput);

	}

}
