/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.service.mapping.mapper;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.eclipse.vorto.service.mapping.DataInput;
import org.eclipse.vorto.service.mapping.IDataMapper;
import org.eclipse.vorto.service.mapping.MappingContext;
import org.eclipse.vorto.service.mapping.spec.SpecWithCustomFunction;
import org.junit.Test;

public class CustomMapperPluginTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testSomePlatformMapping() throws Exception {

		IDataMapper<PlatformData> mapper = new SomePlatformMapper(new SpecWithCustomFunction());

		String json = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

		PlatformData result = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());

		Map<String,Object> reportedButton = (Map<String,Object>)result.getState().getReported().get("button");

		assertEquals(true, (Boolean) reportedButton.get("digital_input_state"));
		assertEquals(2, reportedButton.get("digital_input_count"));

		Map<String,Object> reportedVoltage = (Map<String,Object>)result.getState().getReported().get("voltage");

		assertEquals(2322f, reportedVoltage.get("sensor_value"));
		assertEquals("mV", reportedVoltage.get("sensor_units"));

		System.out.println(result.toJson());

	}
}
