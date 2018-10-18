package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.MappingContext;
import org.eclipse.vorto.mapping.engine.normalized.FunctionblockData;
import org.eclipse.vorto.mapping.engine.normalized.InfomodelData;
import org.eclipse.vorto.service.mapping.spec.SpecWithArrayPayload;
import org.eclipse.vorto.service.mapping.spec.SpecWithBase64Converter;
import org.eclipse.vorto.service.mapping.spec.SpecWithCondition;
import org.eclipse.vorto.service.mapping.spec.SpecWithConditionFunction;
import org.eclipse.vorto.service.mapping.spec.SpecWithConditionXpath;
import org.eclipse.vorto.service.mapping.spec.SpecWithConditionedRules;
import org.eclipse.vorto.service.mapping.spec.SpecWithSameFunctionblock;
import org.eclipse.vorto.service.mapping.spec.SpecWithTimestamp;
import org.eclipse.vorto.service.mapping.spec.SpecWithTypeConversion;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonMappingTest {
	
	private static Gson gson = new GsonBuilder().create();

	@Test
	public void testMapWithSimpleCondition() throws Exception {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCondition())
				.build();

		String json = "{\"count\" : 2 }";

		InfomodelData mappedOutput = mapper.map(gson.fromJson(json, Object.class), MappingContext.empty());
		assertNull(mappedOutput.get("button").getStatus().get("sensor_value"));
		assertEquals(2.0, mappedOutput.get("button").getStatus().get("sensor_value2"));

		json = "{\"count\" : 0 }";

		mappedOutput = mapper.map(gson.fromJson(json, Object.class), MappingContext.empty());
		assertEquals(0.0, mappedOutput.get("button").getStatus().get("sensor_value"));
		assertNull(mappedOutput.get("button").getStatus().get("sensor_value2"));
	}

	@Test
	public void testMapWithCustomFunctionCondition() throws Exception {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithConditionFunction())
				.build();

		String json = "{\"data\" : \"aGFsbG8=\"}";

		InfomodelData mappedOutput = mapper.map(gson.fromJson(json, Object.class), MappingContext.empty());
		assertEquals("hallo", mappedOutput.get("button").getStatus().get("sensor_value"));

	}

	@Test
	public void testMapWithJxpathCondition() throws Exception {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithConditionXpath())
				.build();

		String json = "{\"data\" : [{\"id\": 100,\"value\": \"x\"},{\"id\": 200,\"value\": \"y\"}]}";

		InfomodelData mappedOutput = mapper.map(gson.fromJson(json, Object.class), MappingContext.empty());
		assertEquals(100.0, mappedOutput.get("button").getStatus().get("sensor_value"));

	}

	@Test
	public void testMapUsingBase64Converter() throws Exception {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithBase64Converter())
				.build();

		String json = "{\"data\" : \"" + Base64.encodeBase64String("20".getBytes()) + "\"}";

		InfomodelData mappedOutput = mapper.map(gson.fromJson(json, Object.class), MappingContext.empty());
		assertEquals("20", new String((byte[]) mappedOutput.get("button").getStatus()
				.get("digital_input_state")));

	}

	@Test
	public void testMappingUsingListInput() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithArrayPayload())
				.build();

		String json = "[{\"clickType\" : \"DOUBLE\" }, {\"clickType\" : \"SINGLE\" }]";

		InfomodelData mappedOutput = mapper.map(gson.fromJson(json, Object.class), MappingContext.empty());

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

		InfomodelData mappedOutput = mapper.map(gson.fromJson(json, Object.class), MappingContext.empty());

		FunctionblockData buttonFunctionblockData = mappedOutput.get("button");

		assertEquals(JSON_DATE_FORMAT.format(timestamp), buttonFunctionblockData.getStatus().get("sensor_value"));

		System.out.println(mappedOutput);

	}

	@Test
	public void testMappingTypeConversion() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithTypeConversion())
				.build();

		String json = "[{\"lng\" : 0.002322},{\"lng\" : 0.002222}]";

		InfomodelData mappedOutput = mapper.map(gson.fromJson(json, Object.class), MappingContext.empty());

		FunctionblockData buttonFunctionblockData = mappedOutput.get("button");

		assertEquals("0.002322", buttonFunctionblockData.getStatus().get("sensor_value"));

		System.out.println(mappedOutput);

	}

	@Test
	public void testMapSingleFunctionblockOfInfomodel2() {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithConditionedRules())
				.build();
		
		final String sampleHomeConnectRESTResponse = "{\"data\" : { \"key\" : \"DoorState\", \"value\" : \"Locked\"}}";

		InfomodelData mappedOutput = mapper.map(gson.fromJson(sampleHomeConnectRESTResponse,Object.class), MappingContext.empty());
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

		InfomodelData mappedOutput = mapper.map(gson.fromJson(json, Object.class), MappingContext.empty());

		FunctionblockData buttonFunctionblockData = mappedOutput.get("btn1");

		assertEquals(2.0, buttonFunctionblockData.getStatus().get("sensor_value"));

		FunctionblockData button2FunctionblockData = mappedOutput.get("btn2");

		assertEquals(10.0, button2FunctionblockData.getStatus().get("sensor_value"));

		System.out.println(mappedOutput);

	}

}
