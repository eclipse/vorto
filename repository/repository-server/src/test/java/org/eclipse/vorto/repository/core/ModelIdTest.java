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
package org.eclipse.vorto.repository.core;

import static org.junit.Assert.assertEquals;

import org.eclipse.vorto.repository.api.ModelId;
import org.junit.Test;

public class ModelIdTest {

	@Test (expected=IllegalArgumentException.class)
	public void testInvalidModelId() {
		String modelId = "Test";
		ModelId.fromPrettyFormat(modelId);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testInvalidModelId2() {
		String modelId = "com.bosch.Test";
		ModelId.fromPrettyFormat(modelId);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testInvalidModelId3() {
		String modelId = "com.bosch.test:1.0.0";
		ModelId.fromPrettyFormat(modelId);
	}
	
	@Test
	public void testValidModelId() {
		String modelId = "com.bosch.Test:1.0.0";
		assertEquals(new ModelId("Test", "com.bosch", "1.0.0"),ModelId.fromPrettyFormat(modelId));
	}
	
	@Test
	public void testValidModelId2() {
		String modelId = "com.Test:1.0.0";
		assertEquals(new ModelId("Test", "com", "1.0.0"),ModelId.fromPrettyFormat(modelId));
	}
	
	@Test
	public void testValidModelId3() {
		String modelId = "com.bosch.si.Test:1.0.2";
		assertEquals(new ModelId("Test", "com.bosch.si", "1.0.2"),ModelId.fromPrettyFormat(modelId));
	}
}
