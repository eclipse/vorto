/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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

import org.eclipse.vorto.core.api.model.model.Model
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.IFileTemplate

class PythonDittoSerializerTemplate implements IFileTemplate<Model> {
	
	override getFileName(Model model) {
		return "DittoSerializer.py";
	}
	
	override getPath(Model model) {
		return "model";
	}
	
	override getContent(Model fb, InvocationContext context) {
	'''
	import numbers
	import json
	
	class DittoSerializer(object):
	    
	    def __init__(self):
	        self.payload = ""
	        self.first_prop = True
	
	    def serialize_functionblock(self, name, object, ditto_topic, hono_clientId):
	        self.payload += "{\"topic\": \"" + ditto_topic + "/things/twin/commands/modify\","
	        self.payload += "\"headers\": {\"response-required\": false},"
	        self.payload += "\"path\": \"/features/"+name+"/properties\",\"value\" : {\"status\" : {"
	        object.serializeStatus(self)
	        self.payload += "}, \"configuration\" : {"
	        object.serializeConfiguration(self)
	        self.payload += "} } }"
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
	        elif isinstance(value,dict):
	        	self.payload += "\"" + name + "\": " + json.dumps(value)
	        else:
	            self.payload += "\"" + name + "\": \"" + str(value) + "\""
		'''
	}
	
}
