package org.eclipse.vorto.service.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BleGattService {

	private UUID uuid;
	private Map<UUID, BleGattCharacteristic> characteristics;

	public BleGattService()
	{
		characteristics = new HashMap<UUID, BleGattCharacteristic>();
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public void addCharacteristics(BleGattCharacteristic characteristic)
	{
		characteristics.put(characteristic.getUuid(), characteristic);
	}
	
	public Map<UUID, BleGattCharacteristic> getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(Map<UUID, BleGattCharacteristic> characteristics) {
		this.characteristics = characteristics;
	}
}
