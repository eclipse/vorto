package org.eclipse.vorto.service.mapping;

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
