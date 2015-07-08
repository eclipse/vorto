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
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.FaultClassTemplate
import org.junit.Test

import static org.junit.Assert.assertEquals

class FaultClassTemplateTest {

	@Test
	def testGeneration() {
		var model = TestFunctionblockModelFactory.createFBmodelWithProperties();

		var result = new FaultClassTemplate().getContent(model);
		assertEquals(fetchExpected, result);
	}

	private def String fetchExpected() {
		'''package com.bosch.iot.fridge.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize
public class FridgeFault {			

	private boolean isFault = false;

	public boolean getIsFault() {
		return isFault;
	}
			
	public void setIsFault(boolean isFault) {
		this.isFault = isFault;
	}		
}'''
	}
}
