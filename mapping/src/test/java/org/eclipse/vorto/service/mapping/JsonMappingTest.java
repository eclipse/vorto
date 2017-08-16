package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.ClassFunctions;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.PrimitiveType;
import org.junit.Test;

public class JsonMappingTest {

	@Test
	public void testSingleFbMapping() throws Exception {
		FunctionblockModel fbModel = new FunctionblockModel(ModelId.fromPrettyFormat("demo.fb.PushButton:1.0.0"), ModelType.Functionblock);
		ModelProperty digitalInputStateProperty = new ModelProperty();
		digitalInputStateProperty.setMandatory(true);
		digitalInputStateProperty.setName("digital_input_state");
		digitalInputStateProperty.setType(PrimitiveType.BOOLEAN);
		
		digitalInputStateProperty.setTargetPlatformKey("iotbutton");
		digitalInputStateProperty.setStereotype("source");
		Map<String, String> a1 = new HashMap<String, String>();
		a1.put("value", "true");
		digitalInputStateProperty.setMappedAttributes(a1);
		
		ModelProperty digitalInputCount = new ModelProperty();
		digitalInputCount.setMandatory(true);
		digitalInputCount.setName("digital_input_count");
		digitalInputCount.setType(PrimitiveType.INT);
		
		digitalInputCount.setTargetPlatformKey("iotbutton");
		digitalInputCount.setStereotype("source");
		Map<String, String> a2 = new HashMap<String, String>();
		a2.put("xpath", "custom:clickType(clickType)"); //SINGLE -> 1, DOUBLE -> 2
		digitalInputCount.setMappedAttributes(a2);
		
		fbModel.setStatusProperties(Arrays.asList(new ModelProperty[] {digitalInputStateProperty,digitalInputCount}));
		
		JsonDataMapper mapping = new JsonDataMapper(fbModel,new ClassFunctions(CustomFunctions.class, "custom"));
		
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("clickType", "DOUBLE");
		
		Map<String,Object> mappedOutput = mapping.map(input);
		assertEquals(true,(Boolean)mappedOutput.get("digital_input_state"));
		assertEquals(2,mappedOutput.get("digital_input_count"));
	}
	
	/**
	 * 
	 * Custom functions for data mappers
	 *
	 */
	public static class CustomFunctions {
		
		public static int clickType(String clickType) {
			if (clickType.equalsIgnoreCase("single")) {
				return 1;
			} else if (clickType.equalsIgnoreCase("double")) {
				return 2;
			} else {
				return -1;
			}
		}
	}
}
