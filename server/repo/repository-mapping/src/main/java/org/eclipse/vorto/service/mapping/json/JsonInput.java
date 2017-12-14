/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.service.mapping.json;

import java.util.HashMap;

import org.eclipse.vorto.service.mapping.DataInput;
import org.eclipse.vorto.service.mapping.binary.BinaryData;
import org.eclipse.vorto.service.mapping.ble.json.GattDevice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonInput implements DataInput {

	private Object value;
	
	public JsonInput(String json) {
		this.value = parseJson(json);
	}
	
	@SuppressWarnings("unchecked")
	private Object parseJson(String json) {
		TypeReference<Object> typeRef = new TypeReference<Object>() {};

		ObjectMapper mapper = new ObjectMapper(); 
		try {
			Object result = mapper.readValue(json, typeRef);
			if (result instanceof HashMap) {
				HashMap<String,Object> map = (HashMap<String, Object>)result;
				if (map.containsKey("data")) {
					return mapper.readValue(json, BinaryData.class);
				} else if (map.containsKey("characteristics")) {
					return mapper.readValue(json, GattDevice.class);
				} else {
					return map;
				}
			} else {
				return result;
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Provided json not valid");
		}
	}
	
	@Override
	public Object getValue() {
		return this.value;
	}

}
