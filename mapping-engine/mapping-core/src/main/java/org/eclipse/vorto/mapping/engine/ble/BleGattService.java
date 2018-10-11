/**
 * Copyright (c) 2017 Oliver Meili
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * - Oliver Meili <omi@ieee.org>
 */
package org.eclipse.vorto.mapping.engine.ble;

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
