package org.eclipse.vorto.service.mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

public class BleGattDevice {

	private Map<UUID, BleGattService> services;
	private Map<String, BleGattCharacteristic> characteristics;
	private String modelNumber;
	private List<BleGattInitSequenceElement> initSequence;

	public BleGattDevice()
	{
		services = new HashMap<UUID, BleGattService>();
		characteristics = new HashMap<String, BleGattCharacteristic>();
		initSequence = new Vector<BleGattInitSequenceElement>();
	}

	public Map<UUID, BleGattService> getServices() {
		return services;
	}

	public void addService(BleGattService service) {
		this.services.put(service.getUuid(), service);
		for (BleGattCharacteristic ch : service.getCharacteristics().values())
		{
			this.addCharacteristic(ch);
		}
	}
	
	public void setServices(Map<UUID, BleGattService> services) {
		this.services = services;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public void addCharacteristic(BleGattCharacteristic characteristic)
	{
		this.characteristics.put(characteristic.getUuid().toString(), characteristic);
	}
	
	public Map<String, BleGattCharacteristic> getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(Map<String, BleGattCharacteristic> characteristics) {
		this.characteristics = characteristics;
	}

	public void addInitSequenceElement(BleGattInitSequenceElement elt)
	{
		this.initSequence.add(elt);
	}
	
	public List<BleGattInitSequenceElement> getInitSequence()
	{
		return this.initSequence;
	}
	
	public boolean isNotificationComplete()
	{
		boolean result = true;
		
		for (BleGattCharacteristic ch : this.characteristics.values())
		{
			if (ch.isNotified())
			{
				result &= ch.isUpdated();
			}
		}
		
		return result;
	}

}
