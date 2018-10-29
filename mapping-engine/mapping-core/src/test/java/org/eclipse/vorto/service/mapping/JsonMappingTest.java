package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.model.runtime.FunctionblockValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;
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

		InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));
		assertFalse(mappedOutput.get("button").getStatusProperty("sensor_value").isPresent());
		assertEquals(2.0, mappedOutput.get("button").getStatusProperty("sensor_value2").get().getValue());

		json = "{\"count\" : 0 }";

		mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));
		assertEquals(0.0, mappedOutput.get("button").getStatusProperty("sensor_value").get().getValue());
		assertFalse(mappedOutput.get("button").getStatusProperty("sensor_value2").isPresent());
	}

	@Test
	public void testMapWithCustomFunctionCondition() throws Exception {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithConditionFunction())
				.build();

		String json = "{\"data\" : \"aGFsbG8=\"}";

		InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));
		assertEquals("hallo", mappedOutput.get("button").getStatusProperty("sensor_value").get().getValue());

	}

	@Test
	public void testMapWithJxpathCondition() throws Exception {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithConditionXpath())
				.build();

		String json = "{\"data\" : [{\"id\": 100,\"value\": \"x\"},{\"id\": 200,\"value\": \"y\"}]}";

		InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));
		assertEquals(100.0, mappedOutput.get("button").getStatusProperty("sensor_value").get().getValue());

	}

	@Test
	public void testMapUsingBase64Converter() throws Exception {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithBase64Converter())
				.build();

		String json = "{\"data\" : \"" + Base64.encodeBase64String("20".getBytes()) + "\"}";

		InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));
		assertEquals("20", new String((byte[]) mappedOutput.get("button").getStatusProperty("digital_input_state").get().getValue()));

	}

	@Test
	public void testMappingUsingListInput() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithArrayPayload())
				.build();

		String json = "[{\"clickType\" : \"DOUBLE\" }, {\"clickType\" : \"SINGLE\" }]";

		InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));

		FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");

		assertEquals("DOUBLE", buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());

		System.out.println(mappedOutput);

	}

	static final DateFormat JSON_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

	@Test
	public void testMappingTimestamp() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithTimestamp())
				.build();

		final Date timestamp = new Date();
		String json = "{\"time\" : " + timestamp.getTime() + "}";

		InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));

		FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");

		assertEquals(JSON_DATE_FORMAT.format(timestamp), buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());

		System.out.println(mappedOutput);

	}

	@Test
	public void testMappingTypeConversion() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithTypeConversion())
				.build();

		String json = "[{\"lng\" : 0.002322},{\"lng\" : 0.002222}]";

		InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));

		FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");

		assertEquals("0.002322", buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());

		System.out.println(mappedOutput);

	}

	@Test
	public void testMapSingleFunctionblockOfInfomodel2() {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithConditionedRules())
				.build();
		
		final String sampleHomeConnectRESTResponse = "{\"data\" : { \"key\" : \"DoorState\", \"value\" : \"Locked\"}}";

		InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(sampleHomeConnectRESTResponse,Object.class));
		System.out.println(mappedOutput);
		assertNull(mappedOutput.get("operationState"));
		FunctionblockValue doorStateFunctionblockData = mappedOutput.get("doorState");
		assertEquals("Locked", (String)doorStateFunctionblockData.getStatusProperty("sensor_value").get().getValue());
		
	}

	@Test
	public void testMappingWithInfoModelUsingSameFunctionblock() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithSameFunctionblock())
				.build();

		String json = "{\"btnvalue1\" : 2, \"btnvalue2\": 10}";

		InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));

		FunctionblockValue buttonFunctionblockData = mappedOutput.get("btn1");

		assertEquals(2.0, buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());

		FunctionblockValue button2FunctionblockData = mappedOutput.get("btn2");

		assertEquals(10.0, button2FunctionblockData.getStatusProperty("sensor_value").get().getValue());

		System.out.println(mappedOutput);

	}

}
