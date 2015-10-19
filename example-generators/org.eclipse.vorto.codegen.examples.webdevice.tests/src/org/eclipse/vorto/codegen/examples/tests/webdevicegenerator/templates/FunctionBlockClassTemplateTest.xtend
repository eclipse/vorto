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
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.FunctionBlockClassTemplate
import org.junit.Test

import static org.junit.Assert.assertEquals

class FunctionBlockClassTemplateTest {

	@Test
	def testGeneration() {
		var fbProperty = TestFunctionblockModelFactory.createFBProperty();

		var result = new FunctionBlockClassTemplate().getContent(fbProperty);
		assertEquals(fetchExpected, result);
	}

	private def String fetchExpected() {
		return '''package org.eclipse.vorto.iot.fridge.model;

public class Fridge {
	
	private String displayName = "Fridge Function Block";
	private String description = "A Simple Fridge Functionblock for tester";
	private String namespace = "www.bosch.com";
	private String category = "demo";
	private String version = "1.2.3";
	
	private FridgeConfiguration configuration = new FridgeConfiguration();
	
	private FridgeStatus status = new FridgeStatus();
	
	private FridgeFault fault = new FridgeFault();
	
	private FridgeOperation operation = new FridgeOperation();
	
	public String getDisplayName() {
		return displayName;
	}

	public String getDescription() {
		return description;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getCategory() {
		return category;
	}

	public String getVersion() {
		return version;
	}	
			
	public FridgeConfiguration getConfiguration() {
		return configuration;
	}
	
	public void setConfiguration(FridgeConfiguration configuration) {
		this.configuration = configuration;
	}
	
	public FridgeStatus getStatus() {
		return status;
	}
	
	public FridgeFault getFault() {
		return fault;
	}
	
	public FridgeOperation getOperation() {
		return operation;
	}
}'''
	}
}

