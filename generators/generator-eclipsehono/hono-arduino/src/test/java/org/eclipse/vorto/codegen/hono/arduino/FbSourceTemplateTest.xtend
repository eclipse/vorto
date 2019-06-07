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
import org.eclipse.vorto.core.api.model.BuilderUtils
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.model.ModelId
import org.eclipse.vorto.core.api.model.model.ModelType
import org.junit.Assert
import org.junit.Test

class FbSourceTemplateTest {
	
	@Test
	def void testSerializeFb() {
		var template = new ArduinoFbSourceTemplate();
		
		var fbm = BuilderUtils.newFunctionblock(new ModelId(ModelType.Functionblock,"Temperature","org.eclipse.vorto","1.0.0"))
		   .withStatusProperty("value",PrimitiveType.FLOAT)
		   .withStatusProperty("unit",PrimitiveType.STRING).build();	
					
		var generated = template.getContent(fbm, InvocationContext.simpleInvocationContext());
		Assert.assertEquals(generated,getExpectedTemplate);
	}
	
	def String getExpectedTemplate() {
		'''
		// Temperature
		
		#include "Temperature.h"
		
		using namespace org_eclipse_vorto;
		
		Temperature::Temperature(){}
		
		void Temperature::setvalue(float value) {
			value = value;			
		}
		
		float Temperature::getvalue() {
			return value;
		}
		void Temperature::setunit(String value) {
			unit = value;			
		}
		
		String Temperature::getunit() {
			return unit;
		}
		
		
		String Temperature::serialize(String ditto_topic, String hono_deviceId, String fbName) {
		    String result = "{\"topic\":\""+ ditto_topic +"/things/twin/commands/modify\",";
		    result += "\"headers\":{\"response-required\": false},";
		    result += "\"path\":\"/features/" + fbName + "/properties\",\"value\": {";
		    //Status Properties
		    result += "\"status\": {";
		    result += "\"value\" : " + String(value) + ",";
		    result += "\"unit\" : \"" + String(unit) + "\" ";
		    result += "}";
		
		
		    result += "} } }";
		
		    return result;
		}
		'''
	}
}