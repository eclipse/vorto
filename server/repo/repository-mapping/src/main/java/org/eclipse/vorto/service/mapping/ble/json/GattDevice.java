package org.eclipse.vorto.service.mapping.ble.json;

import java.util.Map;

public class GattDevice {

	private Map<String, GattCharacteristic> characteristics;

	public Map<String, GattCharacteristic> getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(Map<String, GattCharacteristic> characteristics) {
		this.characteristics = characteristics;
	}
}
