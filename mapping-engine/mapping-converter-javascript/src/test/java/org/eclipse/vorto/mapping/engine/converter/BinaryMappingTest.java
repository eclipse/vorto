package org.eclipse.vorto.mapping.engine.converter;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Conversion;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.MappingContext;
import org.eclipse.vorto.mapping.engine.ble.json.GattCharacteristic;
import org.eclipse.vorto.mapping.engine.ble.json.GattDevice;
import org.eclipse.vorto.mapping.engine.ble.json.GattService;
import org.eclipse.vorto.mapping.engine.normalized.FunctionblockData;
import org.eclipse.vorto.mapping.engine.normalized.InfomodelData;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BinaryMappingTest {

	private static Gson gson = new GsonBuilder().create();
	
	@Test
	public void testMappingWithBinary() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithByteArrayConverter())
				.registerScriptEvalProvider(new JavascriptEvalProvider())
				.build();
		String x = "4f00630063007500700061006e0063007900200002";
		String json = "{\"data\" : \""+x+"\"}";
		
		InfomodelData mappedDittoOutput = mapper.map(gson.fromJson(json, Object.class), MappingContext.empty());

		FunctionblockData button = mappedDittoOutput.get("button");
		
		assertEquals(2,button.getStatus().get("sensor_value"));

		System.out.println(mappedDittoOutput);
	}
	
	@Test
	public void testMappingWithGattStructure() throws Exception {

		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecGattConverter())
				.registerScriptEvalProvider(new JavascriptEvalProvider())
				.build();

		
		GattDevice gattDevice = new GattDevice();
				
		GattService gattService = new GattService();
		List<GattCharacteristic> characteristics = new ArrayList<GattCharacteristic>();
		
		byte[] dest = new byte[6];
		byte[] value = Conversion.intToByteArray(2000, 0, dest, 3, 3);
		
		characteristics.add(new GattCharacteristic("23-D1-13-EF-5F-78-23-15-DE-EF-12-12-0D-F0-00-00",value));
		
		gattService.setCharacteristics(characteristics);
		
		List<GattService> services = new ArrayList<GattService>();
		
		services.add(gattService);
		gattDevice.setModelNumber("23-23-23");
		gattDevice.setServices(services);
		gattDevice.setCharacteristics(characteristics);
		
		String json = new Gson().toJson(gattDevice);
			
		InfomodelData mapped = mapper.map(gson.fromJson(json, Object.class), MappingContext.empty());
		assertEquals(20.00,mapped.get("button").getStatus().get("sensor_value"));
		System.out.println(mapped);
	}
	
}
