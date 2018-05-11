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
package org.eclipse.vorto.codegen.arduino

import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class ArduinoFbSourceTemplate extends ArduinoTemplate<FunctionblockModel> {
	
	override getFileName(FunctionblockModel fb) {
		return fb.name + ".cpp";
	}
	
	override getPath(FunctionblockModel fb) {
		return rootPath;
	}
	
	override getContent(FunctionblockModel fb, InvocationContext context) {
		'''
		// «fb.name»
		
		#include "«fb.name».h"
		
		«fb.name»::«fb.name»(){}
		
		«FOR status : fb.functionblock.status.properties»
		void «fb.name»::set«status.name»(«type(status.type)» value)
		{
			«status.name» = value;			
		}
		
		«type(status.type)» «fb.name»::get«status.name»()
		{
			return «status.name»;
		}
        «ENDFOR»
		
		String «fb.name»::serialize(String nameSpace, String deviceId, String fbName)
		{
		    String result = "{\"topic\":\""+ nameSpace + "/" + deviceId +"/things/twin/commands/modify\",";
		    		result += "\"headers\":{\"response-required\": false},";
		    		result += "\"path\":\"/features/" + fbName + "/properties/status\",\"value\": {";
		    «var counter = 0»
		    «FOR status : fb.functionblock.status.properties»
            	«var c = counter++»                                
                «IF isNumericType(status.type)»
                    result += "\"«status.name»\" : " + String«convertNumericValue(status)» + "«IF c < fb.functionblock.status.properties.length-1»,«ENDIF»";
                «ELSE»
                   result += "\"«status.name»\" : \"" + String(«status.name») + "\"«IF c < fb.functionblock.status.properties.length-1»,«ENDIF» ";
                «ENDIF»                                    
            «ENDFOR»
			
    		result += "} }";

            return result;
        }
		'''
	}
	
	def String convertNumericValue(Property property)  {
		if ((property.type as PrimitivePropertyType).type == PrimitiveType.BOOLEAN) {
			return "("+property.name+" == 1 ? \"true\" : \"false\")";
		} else {
			return "("+property.name+")";
		}
	}
	
}