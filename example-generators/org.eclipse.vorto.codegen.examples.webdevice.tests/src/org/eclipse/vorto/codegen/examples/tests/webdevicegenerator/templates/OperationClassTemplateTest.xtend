/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
 package org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.templates

import org.eclipse.vorto.codegen.examples.tests.TestFunctionblockModelFactory
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.OperationClassTemplate
import org.junit.Test

import static org.junit.Assert.assertEquals

class OperationClassTemplateTest {

	@Test
	def testGeneration() {
		var fbProperty = TestFunctionblockModelFactory.createFBProperty();

		var result = new OperationClassTemplate().getContent(fbProperty);
		assertEquals(fetchExpected, result);
	}

	private def String fetchExpected() {
		'''package org.eclipse.vorto.iot.fridge.model;

import java.util.List;
import java.util.ArrayList;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize
public class FridgeOperation {
	private List<String> names = new ArrayList<String>();
	
	public FridgeOperation() {
		names.add("on");
		names.add("Off");
		names.add("Toggle");
	}
	
	public List<String> getNames(){
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}
}'''
	}
}
