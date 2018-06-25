package org.eclipse.vorto.repository.importer.ipso

import org.eclipse.vorto.repository.api.ModelInfo

class FunctionblockTemplate {
	
	def create(LWM2M.Object source, ModelInfo modelInfo) {
		'''
		namespace «modelInfo.id.namespace»
		version «modelInfo.id.version»
		displayname "«modelInfo.displayName»"
		description "«modelInfo.description»"
		
		functionblock «modelInfo.id.name» {
			
			configuration {
				
				«FOR item : source.getResources.item»
					«IF item.operations == "RW" || item.operations == "W"»
						«item.mandatory.toFirstLower» «IF item.multipleInstances != 'Single'»multiple «ENDIF»«parseName(item.name).toFirstLower» as «parseType(item.type)» «constraints(item.rangeEnumeration)» with {readable : «IF item.operations == "RW"»true«ELSE»false«ENDIF», writable: true} "«item.description»"
					«ENDIF»
				«ENDFOR»
			}
			
			status {
				«FOR item : source.getResources.item»
					«IF item.operations == "R"»
						«item.mandatory.toFirstLower» «IF item.multipleInstances != 'Single'»multiple «ENDIF»«parseName(item.name).toFirstLower» as «parseType(item.type)» «constraints(item.rangeEnumeration)» with {readable : true, writable: false } "«item.description»"
					«ENDIF»
				«ENDFOR»
			}
			
			operations {
				«FOR item : source.getResources.item»
					«IF item.operations == "E"»
						«parseName(item.name).toFirstLower»() "«item.description»"
					«ENDIF»
				«ENDFOR»
			}
		}
		'''
	}
	
	def parseName(String name) {
		return name.replace(" ","").replace("/","").replace("-","")
	}
	
	def constraints(String rangeEnumeration) {
		if (!rangeEnumeration.nullOrEmpty) {
			var minMax = rangeEnumeration.split("-");
			return "<MIN "+minMax.get(0) + ", MAX "+minMax.get(1)+">";
		} else {
			return "";
		}
	}
	
	def parseType(String dataType) {
	    if (dataType == "string")
	        return "string"
	    else if (dataType == "float")
	        return "float"
	   else if (dataType == "integer")
	        return "int"
	    else if (dataType == "opaque")
	        return "base64Binary"
	    else if (dataType == "time")
	        return "dateTime"
	    else if (dataType == "boolean")
	        return "boolean"
	    else if (dataType == "objlnk")
	        return "string"
	    else {
	    	return "string"
	    }
	}

	
}