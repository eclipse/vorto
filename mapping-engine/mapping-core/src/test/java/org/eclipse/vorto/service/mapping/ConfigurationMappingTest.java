package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.eclipse.vorto.mapping.engine.DataMapperBuilder;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.MappingException;
import org.eclipse.vorto.mapping.engine.functions.ClassFunction;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.model.runtime.ModelValueFactory;
import org.eclipse.vorto.model.runtime.PropertyValue;
import org.eclipse.vorto.service.mapping.spec.SpecWithConfiguration;
import org.junit.Test;

public class ConfigurationMappingTest {

	@Test
	public void testMapSimpleConfigValue() throws Exception {
		IMappingSpecification spec = new SpecWithConfiguration();
		IDataMapper mapper = new DataMapperBuilder().withSpecification(spec)
				.registerConverterFunction(new ClassFunction("button", ConfigurationMappingTest.class))
				.build();
		
		PropertyValue newValue = ModelValueFactory.createFBPropertyValue(spec.getFunctionBlock("button"), "enable", true);
		PropertyValue oldValue = ModelValueFactory.createFBPropertyValue(spec.getFunctionBlock("button"), "enable", false);
		
		Object mapped = mapper.mapTarget(newValue,oldValue,"button");
		assertEquals("1",mapped);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testUnknownConfigProperty() throws Exception {
		IMappingSpecification spec = new SpecWithConfiguration();
		ModelValueFactory.createFBPropertyValue(spec.getFunctionBlock("button"), "notExistProperty", true);
	}
	
	@Test (expected = MappingException.class)
	public void testNotExistFunction() throws Exception {
		IMappingSpecification spec = new SpecWithConfiguration();
		IDataMapper mapper = new DataMapperBuilder().withSpecification(spec)
				.build();
		
		PropertyValue newValue = ModelValueFactory.createFBPropertyValue(spec.getFunctionBlock("button"), "enable", true);
		PropertyValue oldValue = ModelValueFactory.createFBPropertyValue(spec.getFunctionBlock("button"), "enable", false);
		
		Object mapped = mapper.mapTarget(newValue,oldValue,"button");
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
