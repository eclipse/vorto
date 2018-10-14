package org.eclipse.vorto.mapping.engine.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.eclipse.vorto.mapping.engine.DataInput;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.MappingContext;
import org.eclipse.vorto.mapping.engine.MappingException;
import org.eclipse.vorto.mapping.engine.converter.JavascriptEvalProvider;
import org.eclipse.vorto.mapping.engine.normalized.FunctionblockData;
import org.eclipse.vorto.mapping.engine.normalized.InfomodelData;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMappingTest {
	
	@Test
	public void testConfigMapping() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithConfigMapping())
				.registerScriptEvalProvider(new JavascriptEvalProvider())
				.build();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		System.out.println(mappedOutput);

	}

	@Test
	public void testMapping() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCustomFunction())
				.registerScriptEvalProvider(new JavascriptEvalProvider())
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
			
		})
				.registerScriptEvalProvider(new JavascriptEvalProvider())
				.build();

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
			
		})
				.registerScriptEvalProvider(new JavascriptEvalProvider())
				.build();

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
			
		})
				.registerScriptEvalProvider(new JavascriptEvalProvider())
				.build();

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
			
		})
				.registerScriptEvalProvider(new JavascriptEvalProvider())
				.build();

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
		})
				.registerScriptEvalProvider(new JavascriptEvalProvider())
				.build();

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

		mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
	}

	@Test
	public void testMappingOnlyOneFunctionblockData() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithCustomFunction())
				.registerScriptEvalProvider(new JavascriptEvalProvider())
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
				.registerScriptEvalProvider(new JavascriptEvalProvider())
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
				.registerScriptEvalProvider(new JavascriptEvalProvider())
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
}
