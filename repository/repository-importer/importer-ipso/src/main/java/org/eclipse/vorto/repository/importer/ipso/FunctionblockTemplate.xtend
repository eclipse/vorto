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
package org.eclipse.vorto.repository.importer.ipso

import org.eclipse.vorto.repository.core.ModelInfo
import org.slf4j.LoggerFactory

class FunctionblockTemplate {
	
	private static var logger = LoggerFactory.getLogger(typeof(FunctionblockTemplate));
	
	def String create(LWM2M.Object source, ModelInfo modelInfo) {
		'''
		namespace «modelInfo.id.namespace»
		version «modelInfo.id.version»
		displayname "«modelInfo.displayName»"
		description "«modelInfo.description»"
		
		functionblock «modelInfo.id.name» {
			
			configuration {
				
				«FOR item : source.getResources.item»
					«IF item.operations == "RW" || item.operations == "W"»
						«item.mandatory.toFirstLower» «IF item.multipleInstances != 'Single'»multiple «ENDIF»«parseName(item.name).toFirstLower» as «parseType(item.type)» with {readable : «IF item.operations == "RW"»true«ELSE»false«ENDIF», writable: true} «constraints(item.type, item.rangeEnumeration)»  "«item.description.replace("\"","'")»"
					«ENDIF»
				«ENDFOR»
			}
			
			status {
				«FOR item : source.getResources.item»
					«IF item.operations == "R"»
						«item.mandatory.toFirstLower» «IF item.multipleInstances != 'Single'»multiple «ENDIF»«parseName(item.name).toFirstLower» as «parseType(item.type)» with {readable : true, writable: false } «constraints(item.type, item.rangeEnumeration)»  "«item.description.replace("\"","'")»"
					«ENDIF»
				«ENDFOR»
			}
			
			operations {
				«FOR item : source.getResources.item»
					«IF item.operations == "E"»
						«parseName(item.name).toFirstLower»() "«item.description.replace("\"","'")»"
					«ENDIF»
				«ENDFOR»
			}
		}
		'''
	}
	
	def parseName(String name) {
		return name.replace(" ","").replace("/","").replace("-","")
	}
	
	def constraints(String dataType, String rangeEnumeration) {
		logger.debug("Processing range enumeration "+rangeEnumeration);
		if (!rangeEnumeration.nullOrEmpty && 
			(dataType.equalsIgnoreCase("time") || 
			 dataType.equalsIgnoreCase("integer") ||
 			 dataType.equalsIgnoreCase("float"))) {
			var minMax = rangeEnumeration.split("-");
			if (minMax.length == 2) {
				return "<MIN "+minMax.get(0) + ", MAX "+minMax.get(1)+">";
			} 
		}
		
		return "";
	}
	
	def parseType(String dataType) {
	    if (dataType.equalsIgnoreCase("string"))
	        return "string"
	    else if (dataType.equalsIgnoreCase("float"))
	        return "float"
	   else if (dataType.equalsIgnoreCase("integer"))
	        return "int"
	    else if (dataType.equalsIgnoreCase("opaque"))
	        return "base64Binary"
	    else if (dataType.equalsIgnoreCase("time"))
	        return "dateTime"
	    else if (dataType.equalsIgnoreCase("boolean"))
	        return "boolean"
	    else if (dataType.equalsIgnoreCase("objlnk"))
	        return "string"
	    else {
	    	return "string"
	    }
	}

	
}