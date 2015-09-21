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
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.StatusClassTemplate
import org.junit.Test

import static org.junit.Assert.assertEquals

class StatusClassTemplateTest {

	@Test
	def testGeneration() {
		var fbProperty = TestFunctionblockModelFactory.createFBProperty();

		var result = new StatusClassTemplate().getContent(fbProperty);
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
public class FridgeStatus {
	public FridgeStatus() {
		try {
			Class<?> c = Class.forName(FridgeStatus.class
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
	
	private long testLong = 0;

	public long getTestLong() {
		return testLong;
	}

	public void setTestLong(long testLong) {
		this.testLong = testLong;
	}
	
	private float testFloat = 0;

	public float getTestFloat() {
		return testFloat;
	}

	public void setTestFloat(float testFloat) {
		this.testFloat = testFloat;
	}
	
	private double testDouble = 0;

	public double getTestDouble() {
		return testDouble;
	}

	public void setTestDouble(double testDouble) {
		this.testDouble = testDouble;
	}
	
	private java.util.Date testDatetime = java.util.Calendar.getInstance().getTime();

	public java.util.Date getTestDatetime() {
		return testDatetime;
	}

	public void setTestDatetime(java.util.Date testDatetime) {
		this.testDatetime = testDatetime;
	}
	
	private byte testByte = 0;

	public byte getTestByte() {
		return testByte;
	}

	public void setTestByte(byte testByte) {
		this.testByte = testByte;
	}
	
	private byte[] testBase64 = new byte[]{};

	public byte[] getTestBase64() {
		return testBase64;
	}

	public void setTestBase64(byte[] testBase64) {
		this.testBase64 = testBase64;
	}
	
	private boolean testBoolean = false;

	public boolean getTestBoolean() {
		return testBoolean;
	}

	public void setTestBoolean(boolean testBoolean) {
		this.testBoolean = testBoolean;
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
