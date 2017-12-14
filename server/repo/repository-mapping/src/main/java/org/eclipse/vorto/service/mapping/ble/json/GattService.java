package org.eclipse.vorto.service.mapping.ble.json;

import java.util.Map;

public class GattService {

	private String uuid;
	private Map<String, GattCharacteristic> characteristics;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Map<String, GattCharacteristic> getCharacteristics() {
		return characteristics;
	}
	public void setCharacteristics(Map<String, GattCharacteristic> characteristics) {
		this.characteristics = characteristics;
	}
	
	
}
