package org.eclipse.vorto.mapping.engine;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.eclipse.vorto.mapping.engine.converter.JavascriptEvalProvider;
import org.eclipse.vorto.mapping.engine.converter.date.DateFunctionFactory;
import org.eclipse.vorto.mapping.engine.converter.date.SpecWithTimestamp;
import org.eclipse.vorto.mapping.engine.converter.javascript.SpecWithCustomFunction;
import org.eclipse.vorto.mapping.engine.converter.string.StringFunctionFactory;
import org.eclipse.vorto.mapping.engine.converter.types.TypeFunctionFactory;
import org.eclipse.vorto.mapping.engine.decoder.JSONDeserializer;
import org.eclipse.vorto.mapping.targetplatform.ditto.TwinPayloadFactory;
import org.eclipse.vorto.model.runtime.FunctionblockValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.eclipse.vorto.service.mapping.spec.SpecWithNestedEntity;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;


import com.github.noconnor.junitperf.JUnitPerfRule;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import com.google.gson.JsonObject;

public class CSVPerformanceScenario {
	@Rule
	public JUnitPerfRule perfTestRule = new JUnitPerfRule(new HtmlReportGenerator("target/benchmark_csv.html"));
	private static final int THREAD_AMOUNT_1 = 1;
	private static final int THREAD_AMOUNT_2 = 10;
	private static final int THREAD_AMOUNT_3 = 5;

	private static final int TEST_DURATION_1 = 15_000;
	private static final int TEST_DURATION_2 = 50_000;
	private static final int TEST_DURATION_3 = 150_000;

	private static final int RAMP_PERIOD = 2_000;

	private static final int WARMUP_DURATION_1 = 10_000;
	private static final int WARMUP_DURATION_2 = 15_000;
	private static final int WARMUP_DURATION_3 = 20_000;

	private static final int EXECUTIONS_PER_SECOND_1 = 500;
	private static final int EXECUTIONS_PER_SECOND_2 = 1_000;
	private static final int EXECUTIONS_PER_SECOND_3 = 1_000;
	
	static MappingEngine csvMappingEngine;
	static List<String> rawData;
	
	@BeforeClass
	public static void init() throws IOException {
		csvMappingEngine = MappingEngine
				.createFromInputStream(FileUtils.openInputStream(new File("src/test/resources/pms-mappingspec.json")));
				
				rawData = Arrays.asList("4,1573700429","-2.0632231","-0.59610987","-2.0186977","-1.1001716","-0.31627208","-1.8296678","0.72955585","4.72955585");
	}
	
	@Test
	@JUnitPerfTest(threads = THREAD_AMOUNT_1, durationMs = TEST_DURATION_1, rampUpPeriodMs = RAMP_PERIOD, warmUpMs = WARMUP_DURATION_1, maxExecutionsPerSecond = EXECUTIONS_PER_SECOND_1)
	public void csv_with_pms_motor() throws Exception {
		InfomodelValue result = csvMappingEngine.mapSource(rawData);
		System.out.println(rawData);
		JsonObject dittoPayloadToUpdateAllFeatures = TwinPayloadFactory.toDittoProtocol(result,
				"org.eclipse.vorto:4711");
				
		JsonObject actualValue = dittoPayloadToUpdateAllFeatures.getAsJsonObject("value");
		String stratorValue = actualValue.getAsJsonObject("strator_yoke").getAsJsonObject("properties").getAsJsonObject("status").get("value").getAsString();
		String stratorToothValue = actualValue.getAsJsonObject("strator_tooth").getAsJsonObject("properties").getAsJsonObject("status").get("value").getAsString();
		String motorValue = actualValue.getAsJsonObject("motor_speed").getAsJsonObject("properties").getAsJsonObject("status").get("value").getAsString();
		String stratorWindingValue = actualValue.getAsJsonObject("strator_winding").getAsJsonObject("properties").getAsJsonObject("status").get("value").getAsString();
		String torqueValue = actualValue.getAsJsonObject("torque").getAsJsonObject("properties").getAsJsonObject("status").get("value").getAsString();
		String ambientValue = actualValue.getAsJsonObject("ambient").getAsJsonObject("properties").getAsJsonObject("status").get("value").getAsString();
		String coolantValue = actualValue.getAsJsonObject("coolant").getAsJsonObject("properties").getAsJsonObject("status").get("value").getAsString();

		result.get("ambient").getStatusProperty("value").get().getValue();
		
		assertEquals(ambientValue, rawData.get(2));
		assertEquals(coolantValue, rawData.get(3));
		assertEquals(motorValue, rawData.get(4));
		assertEquals(torqueValue, rawData.get(5));
		assertEquals(stratorValue, rawData.get(6));
		assertEquals(stratorToothValue, rawData.get(7));
		assertEquals(stratorWindingValue, rawData.get(8));
		//check if json has certain value
	}
	
	

	
}
