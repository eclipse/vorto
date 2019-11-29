package org.eclipse.vorto.mapping.engine;

import static org.junit.Assert.assertEquals;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.Conversion;
import org.eclipse.vorto.mapping.engine.converter.JavascriptEvalProvider;
import org.eclipse.vorto.mapping.engine.converter.binary.BinaryFunctionFactory;
import org.eclipse.vorto.mapping.engine.converter.binary.SpecBinaryConverter;
import org.eclipse.vorto.mapping.engine.converter.binary.SpecWithBase64Converter;
import org.eclipse.vorto.mapping.engine.converter.binary.SpecWithByteArrayConverter;
import org.eclipse.vorto.mapping.engine.converter.string.StringFunctionFactory;
import org.eclipse.vorto.mapping.engine.converter.types.TypeFunctionFactory;
import org.eclipse.vorto.mapping.engine.model.binary.BinaryData;
import org.eclipse.vorto.model.runtime.FunctionblockValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.github.noconnor.junitperf.JUnitPerfRule;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BinaryMappingLoadTest {

	@Rule
	public JUnitPerfRule perfTestRule = new JUnitPerfRule(new HtmlReportGenerator("target/benchmark_binary.html"));
	private static final int THREAD_AMOUNT_1 = 1;
	private static final int THREAD_AMOUNT_2 = 5;
	private static final int THREAD_AMOUNT_3 = 10;
	private static final int THREAD_AMOUNT_4 = 15;

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
	private static final int EXECUTIONS_PER_SECOND_2 = 1_000;
	private static final int EXECUTIONS_PER_SECOND_3 = 1_000;
	private static final int EXECUTIONS_PER_SECOND_4 = 1_000;

	//// Benchmark tests Scenario 1: Binary Data Mapping
	static IDataMapper testCaseOneMapper, testCaseTwoMapper, testCaseThreeMapper;

	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	static String testCaseOneJson;
	static BinaryData testCaseTwoInput = new BinaryData();
	static String testCaseThreeJson;
	static String testCaseFourJson;

	@BeforeClass
	public static void init() {

		// Test Case 1
		testCaseOneMapper = IDataMapper.newBuilder().withSpecification(new SpecWithBase64Converter())
				.registerConverterFunction(BinaryFunctionFactory.createFunctions()).build();

		testCaseOneJson = "{\"data\" : \"" + Base64.encodeBase64String("20".getBytes()) + "\"}";


		// Test Case 2
		testCaseTwoMapper = IDataMapper.newBuilder().withSpecification(new SpecBinaryConverter())
				.registerConverterFunction(BinaryFunctionFactory.createFunctions())
				.registerScriptEvalProvider(new JavascriptEvalProvider()).build();

		byte[] dest = new byte[4]; // 2 byte temperature (Byte 1-2), 2 byte humidity (Byte 3-4)
		byte[] value = Conversion.intToByteArray(2000, 0, dest, 0, 2);
		value = Conversion.intToByteArray(8819, 0, dest, 2, 2);

		testCaseTwoInput = new BinaryData(value);


		// Test Case 3
		testCaseThreeMapper = IDataMapper.newBuilder().withSpecification(new SpecWithByteArrayConverter())
				.registerConverterFunction(StringFunctionFactory.createFunctions())
				.registerConverterFunction(TypeFunctionFactory.createFunctions())
				.registerConverterFunction(BinaryFunctionFactory.createFunctions())
				.registerScriptEvalProvider(new JavascriptEvalProvider()).build();
		String x = "4f00630063007500700061006e0063007900200002";
		testCaseThreeJson = "{\"data\" : \"" + x + "\"}";

	}

//// Test Case 1: 1 Built-in (Java) Converter Function

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_1, durationMs = TEST_DURATION_1, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_1, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_1)
	public void builtInConverter1() throws Exception {

		InfomodelValue mappedOutput = testCaseOneMapper.mapSource(gson.fromJson(testCaseOneJson, Object.class));
		assertEquals("20", new String(
				(byte[]) mappedOutput.get("button").getStatusProperty("digital_input_state").get().getValue()));
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_2, durationMs = TEST_DURATION_2, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_2, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_2)
	public void builtInConverter2() throws Exception {
		InfomodelValue mappedOutput = testCaseOneMapper.mapSource(gson.fromJson(testCaseOneJson, Object.class));
		assertEquals("20", new String(
				(byte[]) mappedOutput.get("button").getStatusProperty("digital_input_state").get().getValue()));
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_3, durationMs = TEST_DURATION_3, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_3, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_3)
	public void builtInConverter3() throws Exception {
		InfomodelValue mappedOutput = testCaseOneMapper.mapSource(gson.fromJson(testCaseOneJson, Object.class));
		assertEquals("20", new String(
				(byte[]) mappedOutput.get("button").getStatusProperty("digital_input_state").get().getValue()));
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_4, durationMs = TEST_DURATION_4, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_4, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_4)
	public void builtInConverter4() throws Exception {
		InfomodelValue mappedOutput = testCaseOneMapper.mapSource(gson.fromJson(testCaseOneJson, Object.class));
		assertEquals("20", new String(
				(byte[]) mappedOutput.get("button").getStatusProperty("digital_input_state").get().getValue()));
	}

////// Test Case 2 - Double Nested Converter Functions (Built-in (Java) + Javascript)

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_1, durationMs = TEST_DURATION_1, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_1, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_1)
	public void doubleNestedConverter1() throws Exception {
		InfomodelValue mapped = testCaseTwoMapper.mapSource(testCaseTwoInput);
		assertEquals(20.00, mapped.get("temperature").getStatusProperty("value").get().getValue());
		assertEquals(88.19, mapped.get("humidity").getStatusProperty("value").get().getValue());
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_2, durationMs = TEST_DURATION_2, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_2, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_2)
	public void doubleNestedConverter2() throws Exception {
		InfomodelValue mapped = testCaseTwoMapper.mapSource(testCaseTwoInput);
		assertEquals(20.00, mapped.get("temperature").getStatusProperty("value").get().getValue());
		assertEquals(88.19, mapped.get("humidity").getStatusProperty("value").get().getValue());
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_3, durationMs = TEST_DURATION_3, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_3, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_3)
	public void doubleNestedConverter3() throws Exception {
		InfomodelValue mapped = testCaseTwoMapper.mapSource(testCaseTwoInput);
		assertEquals(20.00, mapped.get("temperature").getStatusProperty("value").get().getValue());
		assertEquals(88.19, mapped.get("humidity").getStatusProperty("value").get().getValue());
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_4, durationMs = TEST_DURATION_4, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_4, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_4)
	public void doubleNestedConverter4() throws Exception {
		InfomodelValue mapped = testCaseTwoMapper.mapSource(testCaseTwoInput);
		assertEquals(20.00, mapped.get("temperature").getStatusProperty("value").get().getValue());
		assertEquals(88.19, mapped.get("humidity").getStatusProperty("value").get().getValue());
	}

////Test Case 3: Triple Nested Converter Functions (Built-in + Built-in + Javascript)

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_1, durationMs = TEST_DURATION_1, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_1, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_1)
	public void tripleNestedConverter1() throws Exception {
		InfomodelValue mappedDittoOutput = testCaseThreeMapper
				.mapSource(gson.fromJson(testCaseThreeJson, Object.class));
		FunctionblockValue button = mappedDittoOutput.get("button");
		assertEquals(2, button.getStatusProperty("sensor_value").get().getValue());
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_2, durationMs = TEST_DURATION_2, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_2, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_2)
	public void tripleNestedConverter2() throws Exception {
		InfomodelValue mappedDittoOutput = testCaseThreeMapper
				.mapSource(gson.fromJson(testCaseThreeJson, Object.class));
		FunctionblockValue button = mappedDittoOutput.get("button");
		assertEquals(2, button.getStatusProperty("sensor_value").get().getValue());
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_3, durationMs = TEST_DURATION_3, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_3, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_3)
	public void tripleNestedConverter3() throws Exception {
		InfomodelValue mappedDittoOutput = testCaseThreeMapper
				.mapSource(gson.fromJson(testCaseThreeJson, Object.class));
		FunctionblockValue button = mappedDittoOutput.get("button");
		assertEquals(2, button.getStatusProperty("sensor_value").get().getValue());
	}

	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_4, durationMs = TEST_DURATION_4, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_4, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_4)
	public void tripleNestedConverter4() throws Exception {
		InfomodelValue mappedDittoOutput = testCaseThreeMapper
				.mapSource(gson.fromJson(testCaseThreeJson, Object.class));
		FunctionblockValue button = mappedDittoOutput.get("button");
		assertEquals(2, button.getStatusProperty("sensor_value").get().getValue());
	}
}
