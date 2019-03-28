/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.codegen.hono.arduino

import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.model.ModelId
import org.eclipse.vorto.core.api.model.model.ModelType
import org.eclipse.vorto.codegen.testutils.GeneratorTestUtils
import org.junit.Assert
import org.junit.Test

class EntitySourceTemplateTest {
	
	@Test
	def void testSerializeEntity() {
		var template = new ArduinoEntitySoureTemplate
		
		var entity = GeneratorTestUtils.newEntity(new ModelId(ModelType.Datatype,"Temperature","org.eclipse.vorto","1.0.0"))
			.withProperty("value",PrimitiveType.STRING)
			.withProperty("unit",PrimitiveType.STRING).build()
		
		var generated = template.getContent(entity, InvocationContext.simpleInvocationContext());
		Assert.assertEquals(generated,expectedEntityTemplate);
	}
		
	def String getExpectedEntityTemplate() {
		'''
		// Temperature
		
		#include "Temperature.h"
		
		using namespace org_eclipse_vorto;
		
		Temperature::Temperature(){}
		
		void Temperature::setvalue(String value) {
		    value = value;          
		}
		
		String Temperature::getvalue() {
		    return value;
		}
		void Temperature::setunit(String value) {
		    unit = value;          
		}
		
		String Temperature::getunit() {
		    return unit;
		}
		
		String Temperature::serialize() {
		    String result = "\"Temperature\": {";
		        result += "\"value\": " + String(value) + ",";
		        result += "\"unit\": " + String(unit);
		        result += "}";
		
		    return result;
		}
		'''
	}
}