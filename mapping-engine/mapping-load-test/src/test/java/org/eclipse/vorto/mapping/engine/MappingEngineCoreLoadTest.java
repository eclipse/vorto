package org.eclipse.vorto.mapping.engine;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Conversion;
import org.eclipse.vorto.mapping.engine.converter.JavascriptEvalProvider;
import org.eclipse.vorto.mapping.engine.converter.binary.BinaryFunctionFactory;
import org.eclipse.vorto.mapping.engine.converter.binary.BinaryMappingTest;
import org.eclipse.vorto.mapping.engine.converter.binary.SpecGattConverter;
import org.eclipse.vorto.mapping.engine.converter.binary.SpecWithByteArrayConverter;
import org.eclipse.vorto.mapping.engine.converter.binary.SpecWithConditionFunction;
import org.eclipse.vorto.mapping.engine.converter.date.DateConvertTest;
import org.eclipse.vorto.mapping.engine.converter.javascript.JsonMappingTest;
import org.eclipse.vorto.mapping.engine.converter.string.StringFunctionFactory;
import org.eclipse.vorto.mapping.engine.converter.types.TypeFunctionFactory;
import org.eclipse.vorto.mapping.engine.model.blegatt.GattCharacteristic;
import org.eclipse.vorto.mapping.engine.model.blegatt.GattDevice;
import org.eclipse.vorto.mapping.engine.model.blegatt.GattService;
import org.eclipse.vorto.model.runtime.FunctionblockValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.eclipse.vorto.service.mapping.MappingSpecJsonReaderTest;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import com.github.noconnor.junitperf.JUnitPerfRule;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MappingEngineCoreLoadTest {
	
	private static BinaryPerformanceScenario binaryPerformanceScenario = new BinaryPerformanceScenario();
	
	@Test
	public void testPerformanceBinary() throws Exception{
//		binaryPerformanceScenario.init();
//		binaryPerformanceScenario.binary_with_converter_default();
//		binaryPerformanceScenario.binary_with_converter_gatt();
//		

	}

	
	
	
	
	
	

//	@Test
//	@JUnitPerfTest(threads = THREAD_AMOUT, durationMs = TEST_DURATION, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND)
//	public void testMappingWithBinary() throws Exception {
//		binaryMappingDelegate.testMappingWithBinary();
//	}
//
//	// binary mapping of two data points
//	@Test
//	@JUnitPerfTest(threads = THREAD_AMOUT, durationMs = TEST_DURATION, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND)
//	public void testMappingBinaryContaining2DataPoints() throws Exception {
//		binaryMappingDelegate.testMappingBinaryContaining2DataPoints();
//	}
//
//	// binary mapping of custom function condition
//	@Test
//	@JUnitPerfTest(threads = THREAD_AMOUT, durationMs = TEST_DURATION, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND)
//	public void testMapWithCustomFunctionCondition() throws Exception {
//		binaryMappingDelegate.testMapWithCustomFunctionCondition();
//	}
//
//	// binary mapping of Gatt structure
//	@Test
//	@JUnitPerfTest(threads = THREAD_AMOUT, durationMs = TEST_DURATION, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND)
//	public void testMappingWithGattStructure() throws Exception {
//		binaryMappingDelegate.testMappingWithGattStructure();
//	}
//
//	// mapping from JSON (Default specification)
//	@Test
//	@JUnitPerfTest(threads = THREAD_AMOUT, durationMs = TEST_DURATION, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND)
//	public void testMappingFromJson() {
//		mappingSpecJsonReaderTestDelegate.testMappingFromJson();
//	}
//
//	// mapping with (JSON Date) convert
//	@Test
//	@JUnitPerfTest(threads = THREAD_AMOUT, durationMs = TEST_DURATION, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND)
//	public void testMappingWithDateConvert() throws Exception {
//		dateConvertTestDelegate.testMappingTimestamp();
//
//	}
//
//	
//	
//	// mapping with javascript converter
//	@Test
//	@JUnitPerfTest(threads = THREAD_AMOUT, durationMs = TEST_DURATION, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND)
//	public void testInvokeJsFunctionWithSingleByteArrayParam() {
//		jsonMappingTestDelegate.testInvokeJsFunctionWithSingleByteArrayParam();
//	}
//
//	// mapping of js with multiple params
//	@Test
//	@JUnitPerfTest(threads = THREAD_AMOUT, durationMs = TEST_DURATION, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND)
//	public void testJSonMapingJavascript() {
//		jsonMappingTestDelegate.testInvokeJsFunctionWithMultipleParams();
//	}
//	
//	// mapping of js with custom functions
//	@Test
//	@JUnitPerfTest(threads = THREAD_AMOUT, durationMs = TEST_DURATION, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND)
//	public void testMapNestedEntityWithCustomFunction() {
//		jsonMappingTestDelegate.testMapNestedEntityWithCustomFunction();
//	}
//
//	
//	// mapping of device js payload with initial value
//	@Test
//	@JUnitPerfTest(threads = THREAD_AMOUT, durationMs = TEST_DURATION, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND)
//	public void testMapDevicePayloadWithInitialValue() {
//		jsonMappingTestDelegate.testMapDevicePayloadWithInitialValue();
//	}

}
