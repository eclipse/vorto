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
		var fbProperty = TestFunctionblockModelFactory.createFBProperty();

		var result = new FaultClassTemplate().getContent(fbProperty);
		assertEquals(fetchExpected, result);
	}

	private def String fetchExpected() {
		'''package org.eclipse.vorto.iot.fridge.model;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize
public class FridgeFault {
	public FridgeFault() {
		try {
			Class<?> c = Class.forName(FridgeFault.class
				.getCanonicalName());
			BeanInfo beanInfo = Introspector.getBeanInfo(c);
			PropertyDescriptor propertyDescriptor[] = beanInfo
				.getPropertyDescriptors();

			for (int i = 0; i < propertyDescriptor.length; i++) {
				if (!propertyDescriptor[i].getName().equals("configData")
					&& !propertyDescriptor[i].getName().equals("class")) {
					Class<?> type = propertyDescriptor[i].getPropertyType();
					String typeName = (type instanceof Class && ((Class<?>) type)
						.isEnum()) ? "enum_" + type.getSimpleName() : type.getSimpleName();
					configData.put(propertyDescriptor[i].getName(), typeName);
				}

			}
		} catch (Exception e) {
			System.out.println("Exception caught. " + e);
			}
	}

	private boolean isFault = false;

	public boolean getIsFault() {
		return isFault;
	}
			
	public void setIsFault(boolean isFault) {
		this.isFault = isFault;
	}		
	private Map<String, String> configData = new HashMap<String, String>();

	public Map<String, String> getConfigData() {
		return configData;
	}

	public void setConfigData(Map<String, String> configData) {
		this.configData = configData;
	}		
}'''
	}
}
