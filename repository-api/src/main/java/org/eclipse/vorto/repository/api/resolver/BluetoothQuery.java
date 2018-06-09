package org.eclipse.vorto.repository.api.resolver;

public class BluetoothQuery extends ResolveQuery {

	public BluetoothQuery(String modelNumber) {
		super("bluetooth", "modelNumber", modelNumber, "DeviceInfoProfile");
	}

}
