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