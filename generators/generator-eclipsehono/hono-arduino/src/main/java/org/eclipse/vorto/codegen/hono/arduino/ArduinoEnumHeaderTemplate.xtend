package org.eclipse.vorto.codegen.hono.arduino

import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.plugin.generator.InvocationContext

class ArduinoEnumHeaderTemplate extends ArduinoTemplate<Enum> {
	
	override getFileName(Enum dataEnum) {
		return dataEnum.name + ".h";
	}
	
	override getPath(Enum dataEnum) {
		return rootPath + "/src/model/datatype/enum";
	}
	
	override getContent(Enum dataEnum, InvocationContext context) {
		'''
		// «dataEnum.name»
		
		#ifndef __ENUM_«dataEnum.name.toUpperCase»_H__
		#define __ENUM_«dataEnum.name.toUpperCase»_H__
		
		namespace «dataEnum.namespace.replace(".","_")» {
		    enum «dataEnum.name» {
		        «FOR entries : dataEnum.enums»
		            «IF !dataEnum.enums.last.equals(entries)»
		            «entries.name»,
		            «ELSE»
		            «entries.name»
		            «ENDIF»
		        «ENDFOR»
		        };
		}
		#endif // __ENUM_«dataEnum.name.toUpperCase»_H__
		'''
	}
	
	
}