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
package org.eclipse.vorto.codegen.jsonschema.templates;

import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.ITemplate

class PrimitiveTypeTemplate implements ITemplate<PrimitiveType>{
	
	new() {
	}
	
	override getContent(PrimitiveType primitiveType, InvocationContext invocationContext) {
		'''
			«IF primitiveType == PrimitiveType.BASE64_BINARY»
				"type": "string"
				«ELSEIF primitiveType == PrimitiveType.BOOLEAN»
				"type": "boolean"
				«ELSEIF primitiveType == PrimitiveType.BYTE»
				"type": "string"
				«ELSEIF primitiveType == PrimitiveType.DATETIME»
				"type": "string"
				«ELSEIF primitiveType == PrimitiveType.DOUBLE»
				"type": "number"
				«ELSEIF primitiveType == PrimitiveType.FLOAT»
				"type": "number"
				«ELSEIF primitiveType == PrimitiveType.INT»
				"type": "integer"
				«ELSEIF primitiveType == PrimitiveType.LONG»
				"type": "number"
				«ELSEIF primitiveType == PrimitiveType.SHORT»
				"type": "integer"
				«ELSEIF primitiveType == PrimitiveType.STRING»
				"type": "string"
				«ENDIF»
		'''
	}
}
