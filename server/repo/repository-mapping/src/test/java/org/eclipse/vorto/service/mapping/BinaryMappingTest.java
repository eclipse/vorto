package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Conversion;
import org.eclipse.vorto.service.mapping.ble.json.GattCharacteristic;
import org.eclipse.vorto.service.mapping.ble.json.GattDevice;
import org.eclipse.vorto.service.mapping.ble.json.GattService;
import org.eclipse.vorto.service.mapping.ditto.DittoData;
import org.eclipse.vorto.service.mapping.ditto.Feature;
import org.eclipse.vorto.service.mapping.spec.SpecGattConverter;
import org.eclipse.vorto.service.mapping.spec.SpecWithByteArrayConverter;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BinaryMappingTest {

	
	@Test
	public void testMappingWithBinary() throws Exception {

		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(new SpecWithByteArrayConverter())
				.buildDittoMapper();
		String json = "{\"data\" : [0, 0, 0, -48, 7, 0]}";
		
		DittoData mappedDittoOutput = mapper.map(DataInputFactory.getInstance().fromJson(json), MappingContext.empty());

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");
		
		assertEquals(20.00,buttonFeature.getStatusProperties().get("sensor_value"));

		System.out.println(mappedDittoOutput.toJson());
	}
	
	@Test
	public void testMappingWithGattStructure() throws Exception {

		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(new SpecGattConverter())
				.buildDittoMapper();

		
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
			
		DittoData mappedDittoOutput = mapper.map(DataInputFactory.getInstance().fromJson(json), MappingContext.empty());
		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");
		assertEquals(20.00,buttonFeature.getStatusProperties().get("sensor_value"));
		System.out.println(mappedDittoOutput.toJson());
	}
	
}
