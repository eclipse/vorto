package org.eclipse.vorto.mapping.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.vorto.mapping.engine.converter.JavascriptEvalProvider;
import org.eclipse.vorto.mapping.engine.converter.date.DateFunctionFactory;
import org.eclipse.vorto.mapping.engine.converter.date.SpecWithTimestamp;
import org.eclipse.vorto.mapping.engine.converter.javascript.SpecWithCustomFunction;
import org.eclipse.vorto.mapping.engine.converter.string.StringFunctionFactory;
import org.eclipse.vorto.mapping.engine.converter.types.TypeFunctionFactory;
import org.eclipse.vorto.mapping.engine.decoder.IPayloadDeserializer;
import org.eclipse.vorto.mapping.engine.decoder.JSONDeserializer;
import org.eclipse.vorto.model.runtime.EntityPropertyValue;
import org.eclipse.vorto.model.runtime.FunctionblockValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.eclipse.vorto.service.mapping.spec.SpecWithNestedEntity;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.github.noconnor.junitperf.JUnitPerfRule;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonPerformanceScenario {
	@Rule
	public JUnitPerfRule perfTestRule = new JUnitPerfRule(new HtmlReportGenerator("target/benchmark_json.html"));
	private static final int THREAD_AMOUNT_1 = 1;
	private static final int THREAD_AMOUNT_2 = 5;
	private static final int THREAD_AMOUNT_3 = 10;
	private static final int THREAD_AMOUNT_4 = 20;

	private static final int TEST_DURATION_1 = 15_000;
	private static final int TEST_DURATION_2 = 50_000;
	private static final int TEST_DURATION_3 = 150_000;
	private static final int TEST_DURATION_4 = 300_000;


	private static final int RAMP_PERIOD = 2_000;

	private static final int WARMUP_DURATION_1 = 10_000;
	private static final int WARMUP_DURATION_2 = 10_000;
	private static final int WARMUP_DURATION_3 = 10_000;
	private static final int WARMUP_DURATION_4 = 10_000;


	private static final int EXECUTIONS_PER_SECOND_1 = 10_000;
	private static final int EXECUTIONS_PER_SECOND_2 = 10_000;
	private static final int EXECUTIONS_PER_SECOND_3 = 10_000;
	private static final int EXECUTIONS_PER_SECOND_4 = 5_000;

	static IDataMapper withoutConverterMapper, builtInConverterMapper, javaScriptConverterMapper;
	static String sampleDeviceData, builtInConverterJson, javaScriptConverterJson;

	static IPayloadDeserializer deserializer;
	
	private static Gson gson = new GsonBuilder().create();
	private static final DateFormat JSON_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
	static Date timestamp = new Date();

	@BeforeClass
	public static void init() {
//Without Converter
		withoutConverterMapper = IDataMapper.newBuilder().withSpecification(new SpecWithNestedEntity()).build();
		sampleDeviceData = "{\"temperature\" : 20.3 }";
		deserializer = new JSONDeserializer();

		
//Built in Converter
		builtInConverterMapper = IDataMapper.newBuilder().withSpecification(new SpecWithTimestamp())
				.registerConverterFunction(DateFunctionFactory.createFunctions()).build();

		timestamp = new Date();
		builtInConverterJson = "{\"time\" : " + timestamp.getTime() + "}";

		
//Javascript Converter		
		javaScriptConverterMapper = IDataMapper.newBuilder().withSpecification(new SpecWithCustomFunction())
	            .registerConverterFunction(TypeFunctionFactory.createFunctions())
	            .registerConverterFunction(StringFunctionFactory.createFunctions())
	            .registerScriptEvalProvider(new JavascriptEvalProvider()).build();

		javaScriptConverterJson = "{\"clickType\" : \"DOUBLE\"}";

	}

	
//without converter

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_1, durationMs = TEST_DURATION_1, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_1, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_1)
	public void json_without_converter_1() throws Exception {
		InfomodelValue mappedOutput = withoutConverterMapper.mapSource(deserializer.deserialize(sampleDeviceData));
		EntityPropertyValue temperatureValue = (EntityPropertyValue) mappedOutput.get("outdoorTemperature")
				.getStatusProperty("value").get();
		assertEquals(20.3, temperatureValue.getValue().getPropertyValue("value").get().getValue());
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_2, durationMs = TEST_DURATION_2, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_2, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_2)
	public void json_without_converter_2() throws Exception {
		InfomodelValue mappedOutput = withoutConverterMapper.mapSource(deserializer.deserialize(sampleDeviceData));

		EntityPropertyValue temperatureValue = (EntityPropertyValue) mappedOutput.get("outdoorTemperature")
				.getStatusProperty("value").get();
		assertEquals(20.3, temperatureValue.getValue().getPropertyValue("value").get().getValue());
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_3, durationMs = TEST_DURATION_3, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_3, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_3)
	public void json_without_converter_3() throws Exception {
		InfomodelValue mappedOutput = withoutConverterMapper.mapSource(deserializer.deserialize(sampleDeviceData));

		EntityPropertyValue temperatureValue = (EntityPropertyValue) mappedOutput.get("outdoorTemperature")
				.getStatusProperty("value").get();
		assertEquals(20.3, temperatureValue.getValue().getPropertyValue("value").get().getValue());
	}
	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_4, durationMs = TEST_DURATION_4, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_4, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_4)
	public void json_without_converter_4() throws Exception {
		InfomodelValue mappedOutput = withoutConverterMapper.mapSource(deserializer.deserialize(sampleDeviceData));

		EntityPropertyValue temperatureValue = (EntityPropertyValue) mappedOutput.get("outdoorTemperature")
				.getStatusProperty("value").get();
		assertEquals(20.3, temperatureValue.getValue().getPropertyValue("value").get().getValue());
	}

	
// javascript converter
	
	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_1, durationMs = TEST_DURATION_1, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_1, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_1)
	public void javascript_converter_1() throws Exception {
		 InfomodelValue mappedOutput = javaScriptConverterMapper.mapSource(gson.fromJson(javaScriptConverterJson, Object.class));
	        FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");
	        assertEquals(true, (Boolean) buttonFunctionblockData.getStatusProperty("digital_input_state")
	            .get().getValue());
	        assertEquals(2,
	            buttonFunctionblockData.getStatusProperty("digital_input_count").get().getValue());
	        FunctionblockValue voltageFunctionblockData = mappedOutput.get("voltage");
	        assertNull(voltageFunctionblockData);
	        System.out.println(gson.toJson(mappedOutput.serialize()));
	}
	
	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_2, durationMs = TEST_DURATION_2, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_2, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_2)
	public void javascript_converter_2() throws Exception {
		 InfomodelValue mappedOutput = javaScriptConverterMapper.mapSource(gson.fromJson(javaScriptConverterJson, Object.class));
	        FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");
	        assertEquals(true, (Boolean) buttonFunctionblockData.getStatusProperty("digital_input_state")
	            .get().getValue());
	        assertEquals(2,
	            buttonFunctionblockData.getStatusProperty("digital_input_count").get().getValue());
	        FunctionblockValue voltageFunctionblockData = mappedOutput.get("voltage");
	        assertNull(voltageFunctionblockData);
	        System.out.println(gson.toJson(mappedOutput.serialize()));
	}
	
	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_3, durationMs = TEST_DURATION_3, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_3, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_3)
	public void javascript_converter_3() throws Exception {
		 InfomodelValue mappedOutput = javaScriptConverterMapper.mapSource(gson.fromJson(javaScriptConverterJson, Object.class));
	        FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");
	        assertEquals(true, (Boolean) buttonFunctionblockData.getStatusProperty("digital_input_state")
	            .get().getValue());
	        assertEquals(2,
	            buttonFunctionblockData.getStatusProperty("digital_input_count").get().getValue());
	        FunctionblockValue voltageFunctionblockData = mappedOutput.get("voltage");
	        assertNull(voltageFunctionblockData);
	        System.out.println(gson.toJson(mappedOutput.serialize()));
	}
	
	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_4, durationMs = TEST_DURATION_4, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_4, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_4)
	public void javascript_converter_4() throws Exception {
		 InfomodelValue mappedOutput = javaScriptConverterMapper.mapSource(gson.fromJson(javaScriptConverterJson, Object.class));
	        FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");
	        assertEquals(true, (Boolean) buttonFunctionblockData.getStatusProperty("digital_input_state")
	            .get().getValue());
	        assertEquals(2,
	            buttonFunctionblockData.getStatusProperty("digital_input_count").get().getValue());
	        FunctionblockValue voltageFunctionblockData = mappedOutput.get("voltage");
	        assertNull(voltageFunctionblockData);
	        System.out.println(gson.toJson(mappedOutput.serialize()));
	}
	

/// With Converter
//  only built in converter
	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_1, durationMs = TEST_DURATION_1, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_1, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_1)
	public void json_builtIn_converter_1() throws Exception {
		   InfomodelValue mappedOutput = builtInConverterMapper.mapSource(gson.fromJson(builtInConverterJson, Object.class));
		    FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");
		    assertEquals(JSON_DATE_FORMAT.format(timestamp),
		        buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());
	}
	
	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_2, durationMs = TEST_DURATION_2, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_2, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_2)
	public void json_builtIn_converter_2() throws Exception {
		   InfomodelValue mappedOutput = builtInConverterMapper.mapSource(gson.fromJson(builtInConverterJson, Object.class));
		    FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");
		    assertEquals(JSON_DATE_FORMAT.format(timestamp),
		        buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());
	}
	
	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_3, durationMs = TEST_DURATION_3, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_3, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_3)
	public void json_builtIn_converter_3() throws Exception {
		   InfomodelValue mappedOutput = builtInConverterMapper.mapSource(gson.fromJson(builtInConverterJson, Object.class));
		    FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");
		    assertEquals(JSON_DATE_FORMAT.format(timestamp),
		        buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());
	}
	
	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_4, durationMs = TEST_DURATION_4, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_4, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_4)
	public void json_builtIn_converter_4() throws Exception {
		   InfomodelValue mappedOutput = builtInConverterMapper.mapSource(gson.fromJson(builtInConverterJson, Object.class));
		    FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");
		    assertEquals(JSON_DATE_FORMAT.format(timestamp),
		        buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());
	}
	
	
	/// With Converter
//  only built in converter
	
	

}
