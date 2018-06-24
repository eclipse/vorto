package org.eclipse.vorto.repository.web.core.dto;

public class BluetoothQuery extends ResolveQuery {

	public BluetoothQuery(String modelNumber) {
		super("bluetooth", "modelNumber", modelNumber, "DeviceInfoProfile");
	}

}
