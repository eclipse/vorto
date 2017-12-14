package org.eclipse.vorto.service.mapping.ble.json;

public class GattCharacteristic {

	private String uuid;
	private byte[] data;
	
	protected GattCharacteristic() {	
	}
	
	public GattCharacteristic(String uuid, byte[] data) {
		this.uuid = uuid;
		this.data = data;
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}

}
