package org.eclipse.vorto.service.mapping.internal.converter;

public class Base64 {

	public static byte[] decodeString(String value) {
		return org.apache.commons.codec.binary.Base64.decodeBase64(value);
	}
	
	public static byte[] decodeByteArray(byte[] value) {
		return org.apache.commons.codec.binary.Base64.decodeBase64(value);
	}
}
