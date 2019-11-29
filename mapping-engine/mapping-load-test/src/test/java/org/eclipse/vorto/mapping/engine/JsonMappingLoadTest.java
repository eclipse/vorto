package org.eclipse.vorto.mapping.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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

public class JsonMappingLoadTest {
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

	private static final int EXECUTIONS_PER_SECOND_1 = 2_000;
	private static final int EXECUTIONS_PER_SECOND_2 = 5_000;
	private static final int EXECUTIONS_PER_SECOND_3 = 5_000;
	private static final int EXECUTIONS_PER_SECOND_4 = 5_000;

	static IDataMapper testCaseOneMapper, testCaseTwoMapper, testCaseThreeMapper, testCaseFourMapper;
	static String testCaseOneJsonInput[], testCaseThreeJsonInput[];
	static Double testCaseOneOutput[];
	static Integer testCaseThreeOutput[];
	static String testCaseTwoJson, testCaseThreeJson;

	static IPayloadDeserializer deserializer;

	private static Gson gson = new GsonBuilder().create();
	private static final DateFormat JSON_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
	static Date timestamp = new Date();

	@BeforeClass
	public static void init() {

		
		
//Test Case 1: No converter functions
		testCaseOneMapper = IDataMapper.newBuilder().withSpecification(new SpecWithNestedEntity()).build();
		testCaseOneJsonInput = new String[] {"{\"temperature\" : 21.3 }","{\"temperature\" : 0.1 }","{\"temperature\" : 11 }"};
	      testCaseOneOutput = new Double[] {21.3, 0.1, 11.0};


		deserializer = new JSONDeserializer();

//Test Case 2: One Built in Converter
		testCaseTwoMapper = IDataMapper.newBuilder().withSpecification(new SpecWithTimestamp())
				.registerConverterFunction(DateFunctionFactory.createFunctions()).build();

		timestamp = new Date();
		testCaseTwoJson = "{\"time\" : " + timestamp.getTime() + "}";

//Test Case 3: One Built in Converter + 1 Javascript Function
		testCaseThreeMapper = IDataMapper.newBuilder().withSpecification(new SpecWithCustomFunction())
				.registerConverterFunction(TypeFunctionFactory.createFunctions())
				.registerConverterFunction(StringFunctionFactory.createFunctions())
				.registerScriptEvalProvider(new JavascriptEvalProvider()).build();

		testCaseThreeJson = "{\"clickType\" : \"DOUBLE\"}";
		
		testCaseThreeJsonInput = new String[] {"{\"clickType\" : \"SINGLE\"}","{\"clickType\" : \"DOUBLE\"}", "{\"clickType\" : \"\"}"};
	      testCaseThreeOutput = new Integer[] {1, 2, 99};

		
	}
	
//Test Case 1: No converter functions

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_1, durationMs = TEST_DURATION_1, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_1, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_1)
	public void withoutConverter1() throws Exception {
		int r = new Random().nextInt(2 + 1);
		InfomodelValue mappedOutput = testCaseOneMapper.mapSource(deserializer.deserialize(testCaseOneJsonInput[r]));	
		EntityPropertyValue temperatureValue = (EntityPropertyValue) mappedOutput.get("outdoorTemperature")
				.getStatusProperty("value").get();
		assertEquals(testCaseOneOutput[r], temperatureValue.getValue().getPropertyValue("value").get().getValue());
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_2, durationMs = TEST_DURATION_2, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_2, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_2)
	public void withoutConverter2() throws Exception {
		int r = new Random().nextInt(2 + 1);
		InfomodelValue mappedOutput = testCaseOneMapper.mapSource(deserializer.deserialize(testCaseOneJsonInput[r]));	
		EntityPropertyValue temperatureValue = (EntityPropertyValue) mappedOutput.get("outdoorTemperature")
				.getStatusProperty("value").get();
		assertEquals(testCaseOneOutput[r], temperatureValue.getValue().getPropertyValue("value").get().getValue());
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_3, durationMs = TEST_DURATION_3, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_3, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_3)
	public void withoutConverter3() throws Exception {
		int r = new Random().nextInt(2 + 1);
		InfomodelValue mappedOutput = testCaseOneMapper.mapSource(deserializer.deserialize(testCaseOneJsonInput[r]));	
		EntityPropertyValue temperatureValue = (EntityPropertyValue) mappedOutput.get("outdoorTemperature")
				.getStatusProperty("value").get();
		assertEquals(testCaseOneOutput[r], temperatureValue.getValue().getPropertyValue("value").get().getValue());
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_4, durationMs = TEST_DURATION_4, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_4, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_4)
	public void withoutConverter4() throws Exception {
		int r = new Random().nextInt(2 + 1);
		InfomodelValue mappedOutput = testCaseOneMapper.mapSource(deserializer.deserialize(testCaseOneJsonInput[r]));	
		EntityPropertyValue temperatureValue = (EntityPropertyValue) mappedOutput.get("outdoorTemperature")
				.getStatusProperty("value").get();
		assertEquals(testCaseOneOutput[r], temperatureValue.getValue().getPropertyValue("value").get().getValue());
	}

//Test Case 2: Built-In Converter

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_1, durationMs = TEST_DURATION_1, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_1, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_1)
	public void builtInConverter1() throws Exception {
		InfomodelValue mappedOutput = testCaseTwoMapper.mapSource(gson.fromJson(testCaseTwoJson, Object.class));
		FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");
		assertEquals(JSON_DATE_FORMAT.format(timestamp),
				buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());

	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_2, durationMs = TEST_DURATION_2, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_2, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_2)
	public void builtInConverter2() throws Exception {
		InfomodelValue mappedOutput = testCaseTwoMapper.mapSource(gson.fromJson(testCaseTwoJson, Object.class));
		FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");
		assertEquals(JSON_DATE_FORMAT.format(timestamp),
				buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_3, durationMs = TEST_DURATION_3, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_3, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_3)
	public void builtInConverter3() throws Exception {
		InfomodelValue mappedOutput = testCaseTwoMapper.mapSource(gson.fromJson(testCaseTwoJson, Object.class));
		FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");
		assertEquals(JSON_DATE_FORMAT.format(timestamp),
				buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_4, durationMs = TEST_DURATION_4, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_4, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_4)
	public void builtInConverter4() throws Exception {
		InfomodelValue mappedOutput = testCaseTwoMapper.mapSource(gson.fromJson(testCaseTwoJson, Object.class));
		FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");
		assertEquals(JSON_DATE_FORMAT.format(timestamp),
				buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());
	}

/// With Converter
//Test Case 3: Built-in Converter Function + 1 Javascript Function

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_1, durationMs = TEST_DURATION_1, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_1, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_1)
	public void builtInConverterAndJs1() throws Exception {
		int r = new Random().nextInt(2 + 1);
		InfomodelValue mappedOutput = testCaseThreeMapper.mapSource(gson.fromJson(testCaseThreeJsonInput[r], Object.class));
		FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");
		assertEquals(true, (Boolean) buttonFunctionblockData.getStatusProperty("digital_input_state").get().getValue());
		assertEquals(testCaseThreeOutput[r], buttonFunctionblockData.getStatusProperty("digital_input_count").get().getValue());
		FunctionblockValue voltageFunctionblockData = mappedOutput.get("voltage");
		assertNull(voltageFunctionblockData);
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_2, durationMs = TEST_DURATION_2, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_2, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_2)
	public void builtInConverterAndJs2() throws Exception {
		int r = new Random().nextInt(2 + 1);
		InfomodelValue mappedOutput = testCaseThreeMapper.mapSource(gson.fromJson(testCaseThreeJsonInput[r], Object.class));
		FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");
		assertEquals(true, (Boolean) buttonFunctionblockData.getStatusProperty("digital_input_state").get().getValue());
		assertEquals(testCaseThreeOutput[r], buttonFunctionblockData.getStatusProperty("digital_input_count").get().getValue());
		FunctionblockValue voltageFunctionblockData = mappedOutput.get("voltage");
		assertNull(voltageFunctionblockData);
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_3, durationMs = TEST_DURATION_3, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_3, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_3)
	public void builtInConverterAndJs3() throws Exception {
		int r = new Random().nextInt(2 + 1);
		InfomodelValue mappedOutput = testCaseThreeMapper.mapSource(gson.fromJson(testCaseThreeJsonInput[r], Object.class));
		FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");
		assertEquals(true, (Boolean) buttonFunctionblockData.getStatusProperty("digital_input_state").get().getValue());
		assertEquals(testCaseThreeOutput[r], buttonFunctionblockData.getStatusProperty("digital_input_count").get().getValue());
		FunctionblockValue voltageFunctionblockData = mappedOutput.get("voltage");
		assertNull(voltageFunctionblockData);
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_4, durationMs = TEST_DURATION_4, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_4, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_4)
	public void builtInConverterAndJs4() throws Exception {
		int r = new Random().nextInt(2 + 1);
		InfomodelValue mappedOutput = testCaseThreeMapper.mapSource(gson.fromJson(testCaseThreeJsonInput[r], Object.class));
		FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");
		assertEquals(true, (Boolean) buttonFunctionblockData.getStatusProperty("digital_input_state").get().getValue());
		assertEquals(testCaseThreeOutput[r], buttonFunctionblockData.getStatusProperty("digital_input_count").get().getValue());
		FunctionblockValue voltageFunctionblockData = mappedOutput.get("voltage");
		assertNull(voltageFunctionblockData);
	}
	
}
