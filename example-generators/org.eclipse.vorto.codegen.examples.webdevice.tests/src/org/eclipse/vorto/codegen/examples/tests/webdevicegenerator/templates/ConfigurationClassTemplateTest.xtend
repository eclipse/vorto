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
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.ConfigurationClassTemplate
import org.junit.Test

import static org.junit.Assert.assertEquals

class ConfigurationClassTemplateTest {

	@Test
	def testGeneration() {
		var fbModel = TestFunctionblockModelFactory.createFBmodelWithProperties();

		var result = new ConfigurationClassTemplate().getContent(fbModel);
		assertEquals(fetchExpected, result);
	}

	private def String fetchExpected() {
		'''package com.bosch.iot.fridge.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize
public class FridgeConfiguration {			

	private String testString = "";

	public String getTestString() {
		return testString;
	}
			
	public void setTestString(String testString) {
		this.testString = testString;
	}		

	private short testShort = 0;

	public short getTestShort() {
		return testShort;
	}
			
	public void setTestShort(short testShort) {
		this.testShort = testShort;
	}		

	private int testInt = 0;

	public int getTestInt() {
		return testInt;
	}
			
	public void setTestInt(int testInt) {
		this.testInt = testInt;
	}		
}'''
	}
}
