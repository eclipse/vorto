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
package org.eclipse.vorto.codegen.hono.arduino

import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.plugin.generator.InvocationContext

class ArduinoFbSourceTemplate extends ArduinoTemplate<FunctionblockModel> {
	
	override getFileName(FunctionblockModel fb) {
		return fb.name + ".cpp";
	}
	
	override getPath(FunctionblockModel fb) {
		return rootPath + "/src/model/functionblock";
	}
	
	override getContent(FunctionblockModel fb, InvocationContext context) {
		'''
		// «fb.name»
		
		#include "«fb.name».h"
		
		using namespace «fb.namespace.replace(".","_")»;
		
		«fb.name»::«fb.name»(){}
		
		«IF fb.functionblock.status !== null»
			«FOR status : fb.functionblock.status.properties»
			void «fb.name»::set«status.name»(«type(status.type)» value) {
				this.«status.name» = value;			
			}
			
			«type(status.type)» «fb.name»::get«status.name»() {
				return «status.name»;
			}
			«ENDFOR»
		«ENDIF»
		
		«IF fb.functionblock.configuration !== null»
			«FOR configuration : fb.functionblock.configuration.properties»
			void «fb.name»::set«configuration.name»(«type(configuration.type)» value) {
				«configuration.name» = value;			
			}
			
			«type(configuration.type)» «fb.name»::get«configuration.name»() {
				return «configuration.name»;
			}
			«ENDFOR»
		«ENDIF»		
		
		String «fb.name»::serialize(String ditto_topic, String hono_deviceId, String fbName) {
		    String result = "{\"topic\":\""+ ditto_topic +"/things/twin/commands/modify\",";
		    result += "\"headers\":{\"response-required\": false},";
		    result += "\"path\":\"/features/" + fbName + "/properties\",\"value\": {";
		    «IF fb.functionblock.status !== null»
		    //Status Properties
		    «var counter = 0»
		    result += "\"status\": {";
		    «FOR status : fb.functionblock.status.properties»
		        «var c = counter++»
		        «IF isNumericType(status.type)»
		            result += "\"«status.name»\" : " + String«convertNumericValue(status)» + "«IF c < fb.functionblock.status.properties.length-1»,«ENDIF»";
		         «ELSEIF isEntity(fb.functionblock, status.type)»
		            result += «status.name».serialize();
		         «ELSE»
		            result += "\"«status.name»\" : \"" + String(«status.name») + "\"«IF c < fb.functionblock.status.properties.length-1»,«ENDIF» ";
		        «ENDIF»                                    
		    «ENDFOR»
		    result += "}";
		    «ENDIF»

		    «IF fb.functionblock.configuration !== null && fb.functionblock.status !== null»
		    result +=",";
		    «ENDIF»

		    «IF fb.functionblock.configuration !== null»
		    //Configuration Properties
		    «var counter = 0»
		    result += "\"configuration\": {";
		    «FOR configuration : fb.functionblock.configuration.properties»
		        «var c = counter++»
		        «IF isNumericType(configuration.type)»
		            result += "\"«configuration.name»\" : " + String«convertNumericValue(configuration)» + "«IF c < fb.functionblock.configuration.properties.length-1»,«ENDIF»";
		         «ELSEIF isEntity(fb.functionblock, configuration.type)»
		            result += «configuration.name».serialize();
		         «ELSE»
		            result += "\"«configuration.name»\" : \"" + String(«configuration.name») + "\"«IF c < fb.functionblock.configuration.properties.length-1»,«ENDIF» ";
		        «ENDIF»                                    
		    «ENDFOR»
		    result += "}";
		    «ENDIF»
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