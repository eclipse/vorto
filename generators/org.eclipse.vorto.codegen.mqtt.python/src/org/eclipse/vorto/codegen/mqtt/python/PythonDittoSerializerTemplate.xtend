/*******************************************************************************
 *  Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *******************************************************************************/
package org.eclipse.vorto.codegen.mqtt.python

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.model.Model

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
	
	class DittoSerializer(object):
	    
	    def __init__(self):
	        self.payload = ""
	        self.first_prop = True
	
	    def serialize_functionblock(self, name, object, ditto_nameSpace, hono_clientId):
	        self.payload += "{\"topic\": \"" + ditto_nameSpace + "/" + hono_clientId + "/things/twin/commands/modify\","
	        self.payload += "\"headers\": {\"response-required\": false},"
	        self.payload += "\"path\": \"/features/"+name+"\",\"value\" : { \"properties\":{\"status\" : {"
	        object.serialize(self)
	        self.payload += "} }"
	        self.payload += "} }"
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
