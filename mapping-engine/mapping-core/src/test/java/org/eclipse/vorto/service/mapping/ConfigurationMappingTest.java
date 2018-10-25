package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Optional;

import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.MappingException;
import org.eclipse.vorto.mapping.engine.functions.ClassFunction;
import org.eclipse.vorto.mapping.engine.normalized.FunctionblockProperty;
import org.eclipse.vorto.mapping.engine.spec.IMappingSpecification;
import org.eclipse.vorto.service.mapping.spec.SpecWithConfiguration;
import org.junit.Test;

public class ConfigurationMappingTest {

	@Test
	public void testMapSimpleConfigValue() throws Exception {
		IMappingSpecification spec = new SpecWithConfiguration();
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(spec)
				.registerConverterFunction(new ClassFunction("button", ConfigurationMappingTest.class))
				.build();
		
		FunctionblockProperty newValue = FunctionblockProperty.newBuilder(spec,"button").property("enable").value(true).build();
		FunctionblockProperty oldValue = FunctionblockProperty.newBuilder(spec,"button").property("enable").value(false).build();
		
		Object mapped = mapper.mapTarget(newValue,Optional.of(oldValue));
		assertEquals("1",mapped);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testUnknownConfigProperty() throws Exception {
		IMappingSpecification spec = new SpecWithConfiguration();		
		FunctionblockProperty.newBuilder(spec,"button").property("notExistProperty").value(true).build();
	}
	
	@Test (expected = MappingException.class)
	public void testNotExistFunction() throws Exception {
		IMappingSpecification spec = new SpecWithConfiguration();
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(spec)
				.build();
		
		FunctionblockProperty newValue = FunctionblockProperty.newBuilder(spec,"button").property("enable").value(true).build();
		FunctionblockProperty oldValue = FunctionblockProperty.newBuilder(spec,"button").property("enable").value(false).build();
		
		Object mapped = mapper.mapTarget(newValue,Optional.of(oldValue));
		assertEquals("1",mapped);
	}
	
	public static Object convertEnable(Map<String,Object> ctx) {
		if (((Boolean)ctx.get("newValue")).booleanValue() == true) {
			return "1";
		} else {
			return "0";
		}
	}
	
	public static Object convertE(Map<String,Object> ctx) {
		if (((Boolean)ctx.get("newValue")).booleanValue() == true) {
			return "1";
		} else {
			return "0";
		}
	}
}
