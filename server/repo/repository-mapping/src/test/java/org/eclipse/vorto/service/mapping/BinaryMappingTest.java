package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Conversion;
import org.eclipse.vorto.service.mapping.ble.json.GattCharacteristic;
import org.eclipse.vorto.service.mapping.ble.json.GattDevice;
import org.eclipse.vorto.service.mapping.ditto.DittoData;
import org.eclipse.vorto.service.mapping.ditto.Feature;
import org.eclipse.vorto.service.mapping.spec.SpecGattConverter;
import org.eclipse.vorto.service.mapping.spec.SpecWithByteArrayConverter;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BinaryMappingTest {

	
	@Test
	public void testMappingWithBinary() throws Exception {

		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(new SpecWithByteArrayConverter())
				.buildDittoMapper();
		byte[] dest = new byte[6];
		byte[] value = Conversion.intToByteArray(2000, 0, dest, 3, 3);
		String json = "{\"data\" : \""+Base64.getEncoder().encodeToString(value)+"\"}";
		
		DittoData mappedDittoOutput = mapper.map(DataInputFactory.getInstance().fromJson(json), MappingContext.empty());

		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");
		
		assertEquals(20.00,buttonFeature.getStatusProperties().get("sensor_value"));

		System.out.println(mappedDittoOutput.toJson());
	}
	
	@Test
	public void testMappingWithGattStructure() throws Exception {

		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(new SpecGattConverter())
				.buildDittoMapper();
		
		byte[] dest = new byte[6];
		byte[] value = Conversion.intToByteArray(2000, 0, dest, 3, 3);
		
		GattDevice gattDevice = new GattDevice();
		Map<String, GattCharacteristic> characteristics = new HashMap<String, GattCharacteristic>();
		characteristics.put("abc", new GattCharacteristic("abc",value));
		
		gattDevice.setCharacteristics(characteristics);
		
		ObjectMapper objMapper = new ObjectMapper();
		String json = objMapper.writeValueAsString(gattDevice);
			
		
		DittoData mappedDittoOutput = mapper.map(DataInputFactory.getInstance().fromJson(json), MappingContext.empty());
		Feature buttonFeature = mappedDittoOutput.getFeatures().get("button");
		assertEquals(20.00,buttonFeature.getStatusProperties().get("sensor_value"));
		System.out.println(mappedDittoOutput.toJson());
	}
	
}
