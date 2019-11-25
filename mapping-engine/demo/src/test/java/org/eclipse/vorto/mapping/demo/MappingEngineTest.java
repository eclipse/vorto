package org.eclipse.vorto.mapping.demo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.eclipse.vorto.mapping.engine.MappingEngine;
import org.eclipse.vorto.mapping.engine.RawData;
import org.eclipse.vorto.mapping.targetplatform.ditto.TwinPayloadFactory;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.junit.Before;
import org.junit.Test;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class MappingEngineTest {
	
	private MappingEngine engineWithSimpleSpec, engineWithCoordnSpec = null;
	private List[] rawData;
	
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	Random randomPayloadIndex = new Random();
	Map<String, Object> rawDataCoordnObj = new HashMap<String, Object>(1);

	private RawData rawDataCoordn;

	@Before
	public void setup() throws Exception {

// simple spec:
		engineWithSimpleSpec = MappingEngine
				.createFromInputStream(FileUtils.openInputStream(new File("src/test/resources/pms-mappingspec.json")));
		rawData = createPayloadData(1);

// coordinate spec:
		engineWithCoordnSpec = MappingEngine
		.createFromInputStream(FileUtils.openInputStream(new File("src/test/resources/vorto.private.tmtnckff.assets_MappingTestDevice_1.0.0-mappingspec.json")));
		rawDataCoordn = new RawData(2, 3, 4);
	}
	
	
	@Test
	public void testSimpleMappingSpec() throws Exception {		
		InfomodelValue result = engineWithSimpleSpec.mapSource(rawData[0]);
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

		assertEquals(ambientValue, rawData[0].get(2));
		assertEquals(coolantValue, rawData[0].get(3));
		assertEquals(motorValue, rawData[0].get(4));
		assertEquals(torqueValue, rawData[0].get(5));
		assertEquals(stratorValue, rawData[0].get(6));
		assertEquals(stratorToothValue, rawData[0].get(7));
		assertEquals(stratorWindingValue, rawData[0].get(8));
		//check if json has certain value
	}
	
	//@Test
//	public void testCordsMappingSpec() throws Exception {	
//		InfomodelValue result = engineWithCoordnSpec.mapSource(rawDataCoordn);	
//	   	 System.out.println(result);

//		JsonObject dittoPayloadToUpdateSingleFeature = TwinPayloadFactory.toDittoProtocol(result.get("acceleration"), "ambient","org.eclipse.vorto:4712");

		
		
		
//		JsonObject actualValue = dittoPayloadToUpdateSingleFeature.getAsJsonObject("value").getAsJsonObject("status").getAsJsonObject("value");

//		JSONAssert.assertEquals("2.0", actualValue.get("x").getAsString(), true); 	
//		JSONAssert.assertEquals("3.0", actualValue.get("y").getAsString(), true);
//		JSONAssert.assertEquals("4.0", actualValue.get("z").getAsString(), true);
//	}
	
	
	
	private List[] createPayloadData(int amount) {
		List res[];
		res = new List[amount];
		for (int i = 0; i < amount; i++) {
			res[i] = Arrays.asList("123", randFloat(), randFloat(), randFloat(), randFloat(), randFloat(), randFloat(),
					randFloat(), randFloat());
		}
		return res;
	}

	public static String randFloat() {
		Random rand = new Random();
		return String.valueOf(rand.nextFloat() * 2 - 1);
	}
	
}