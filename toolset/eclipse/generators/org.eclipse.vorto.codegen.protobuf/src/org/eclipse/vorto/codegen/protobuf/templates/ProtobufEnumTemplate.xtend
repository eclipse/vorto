/*******************************************************************************
 * Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.protobuf.templates

import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.datatype.Enum

class ProtobufEnumTemplate extends ProtobufTemplate<Enum> {
	
	override getContent(Enum element, InvocationContext context) {
		'''
		«IF element.description != null»//«element.description»«ENDIF»
		enum «element.name» {
			
		«IF element.enums != null»
			«var counter = 0»
			«FOR literal : element.enums»
				«literal.name» = «counter++»; «IF literal.description != null»//«literal.description»«ENDIF»
			«ENDFOR»
		«ENDIF»
		}
		'''
	}
	
}