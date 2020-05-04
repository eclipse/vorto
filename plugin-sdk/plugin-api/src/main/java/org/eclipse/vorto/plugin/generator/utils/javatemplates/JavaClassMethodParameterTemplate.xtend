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
package org.eclipse.vorto.plugin.generator.utils.javatemplates

import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.functionblock.Param
import org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam
import org.eclipse.vorto.core.api.model.functionblock.RefParam
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.ITemplate

class JavaClassMethodParameterTemplate implements ITemplate<Param>{
	
	override getContent(Param property,InvocationContext invocationContext) {
		'''
			«IF property instanceof PrimitiveParam»
				«ValueMapper.mapSimpleDatatype((property as PrimitiveParam).type as PrimitiveType)» «property.name»
			«ELSEIF property instanceof RefParam»
				«var RefParam object = property as RefParam»
				«IF object.type instanceof Entity» 
					«(object.type as Entity).name.toFirstUpper» «property.name»
				«ELSEIF object.type instanceof Enum»
					«(object.type as Enum).name.toFirstUpper» «property.name»
				«ENDIF»
			«ENDIF»
		'''
	}
}
