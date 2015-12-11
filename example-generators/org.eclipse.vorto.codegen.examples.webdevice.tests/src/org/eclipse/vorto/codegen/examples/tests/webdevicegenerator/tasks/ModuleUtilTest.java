/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.tasks;

import static org.junit.Assert.assertEquals;

import org.eclipse.vorto.codegen.examples.tests.TestFunctionblockModelFactory;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ModuleUtil;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.junit.Test;

public class ModuleUtilTest {

	FunctionblockModel model = TestFunctionblockModelFactory
			.createFBmodelWithProperties();

	@Test
	public void getModelPackage() {
		String packageName = ModuleUtil.getModelPackage(model);
		assertEquals("org.eclipse.vorto.iot.fridge.model", packageName);
	}

	@Test
	public void getServicePackage() {
		String packageName = ModuleUtil.getServicePackage(model);
		assertEquals("org.eclipse.vorto.iot.fridge.service", packageName);
	}

	@Test
	public void getModelPath() {
		String packageName = ModuleUtil.getModelPath(model);
		assertEquals("/src/main/java/org/eclipse/vorto/iot/fridge/model", packageName);
	}

	@Test
	public void getServicePath() {
		String packageName = ModuleUtil.getServicePath(model);
		assertEquals("/src/main/java/org/eclipse/vorto/iot/fridge/service", packageName);
	}
}
