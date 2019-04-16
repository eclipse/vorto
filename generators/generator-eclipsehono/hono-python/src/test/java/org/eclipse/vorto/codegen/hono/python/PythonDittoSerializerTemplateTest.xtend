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
package org.eclipse.vorto.codegen.hono.python

import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.testutils.GeneratorTestUtils
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.model.ModelId
import org.eclipse.vorto.core.api.model.model.ModelType
import org.junit.Assert
import org.junit.Test

class PythonDittoSerializerTemplateTest {
	
	@Test
	def void testSerializeFb() {
		var template = new PythonDittoSerializerTemplate();
		
		var fbm = GeneratorTestUtils.newFunctionblock(new ModelId(ModelType.Functionblock,"Temperature","org.eclipse.vorto","1.0.0"))
		   .withStatusProperty("value",PrimitiveType.FLOAT)
		   .withStatusProperty("unit",PrimitiveType.STRING).build();	
					
		var generated = template.getContent(fbm, InvocationContext.simpleInvocationContext());
		System.out.println(generated);
		Assert.assertEquals(generated,getExpectedTemplate);
	}
	
	def String getExpectedTemplate() {
		'''
		import numbers
		
		class DittoSerializer(object):
		    
		    def __init__(self):
		        self.payload = ""
		        self.first_prop = True
		
		    def serialize_functionblock(self, name, object, ditto_topic, hono_clientId):
		        self.payload += "{\"topic\": \"" + ditto_topic + "/things/twin/commands/modify\","
		        self.payload += "\"headers\": {\"response-required\": false},"
		        self.payload += "\"path\": \"/features/"+name+"\",\"value\" : { \"properties\":{\"status\" : {"
		        object.serializeStatus(self)
		        self.payload += "}, \"configuration\" : {"
		        object.serializeConfiguration(self)
		        self.payload += "} } } }"
		        returnPayload = self.payload
		        # RESET
		        self.payload = ""
		        self.first_prop = True
		        return returnPayload
		
		    def serialize_property(self, name, value):
		        if not self.first_prop:
		            self.payload += ", "
		        else:
		            self.first_prop = False
		        if isinstance(value, numbers.Number):
		            self.payload += "\"" + name + "\": " + str(value)
		        else:
		            self.payload += "\"" + name + "\": \"" + str(value) + "\""
		'''
	}
}