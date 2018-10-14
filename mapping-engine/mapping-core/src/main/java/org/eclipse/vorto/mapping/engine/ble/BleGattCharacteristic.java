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

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

public class BleGattCharacteristic {

	private UUID uuid;
	private String handle;
	private boolean updated;
	private boolean notified;

	private Short[] data;
	
	public BleGattCharacteristic()
	{
		this.notified = false;
		this.updated = false;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Short[] getData() {
		return data;
	}

	public void setData(String data)
	{
		if (data == null) {
			this.data = null;
		}
		String[] digits = data.split(" ");
		if (digits != null)
		{
			Stream<Short> bytes = Arrays.stream(digits).map(s -> Short.parseShort(s, 16));
			this.setData(bytes.toArray(Short[]::new));
		}
	}
	
	public void setData(Short[] data) {
		this.data = data;
		this.updated = true;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}
	
	public void clearUpdated()
	{
		this.updated = false;
	}
	
	public boolean isUpdated()
	{
		return updated;
	}

	public boolean isNotified() {
		return notified;
	}

	public void setNotified(boolean notified) {
		this.notified = notified;
	}

}
