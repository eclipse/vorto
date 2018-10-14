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
import java.util.stream.Stream;

public class BleGattInitSequenceElement {

	private BleGattCharacteristic characteristic;
	private Short[] value;
	
	public BleGattInitSequenceElement(BleGattCharacteristic characteristic, String value)
	{
		this.characteristic = characteristic;
		setValue(value);
	}
	
	public void setValue(String value)
	{
		String[] digits = value.split(" ");
		if (digits != null)
		{
			Stream<Short> bytes = Arrays.stream(digits).map(s -> Short.parseShort(s, 16));
			this.value = bytes.toArray(Short[]::new);
		}
	}
	
	public String getValue()
	{
		String result = "";
		for (Short v : value)
		{
			String digit = String.format("%02x", v);
			result = result + digit;
		}
		return result;
	}
	
	public BleGattCharacteristic getCharacteristic()
	{
		return this.characteristic;
	}
}
