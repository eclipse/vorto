/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
 
package org.eclipse.vorto.editor.datatype.internal.validation

import org.eclipse.vorto.core.api.model.datatype.Constraint
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.editor.datatype.validation.DatatypeSystemMessage

class PropertyConstraintMappingValidation {

	var errorMsg = DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID ;

	val String[] intervalArr = #["MAX", "MIN"]
	val String[] strArr = #["STRLEN", "REGEX"]
	val String[] mimeArr = #["MIMETYPE"]
	val String[] byteArr = intervalArr + mimeArr
	val String[] empty = #[]

	private val valueTypeMap = <String, String[]>newHashMap(
		'int' -> intervalArr,
		'byte' -> byteArr,
		'float' -> intervalArr,
		'long' -> intervalArr,
		'short' -> intervalArr,
		'double' -> intervalArr,
		'dateTime' -> intervalArr,
		'string' -> strArr,
		'boolean' -> empty,
		'base64Binary' -> mimeArr
		
	);

	def final boolean checkPropertyConstraints(PrimitiveType type, Constraint cons) {
		var arr = valueTypeMap.get(type.literal)
		if (arr == null || !arr.contains(cons.type.literal)) {
			setErrorMessage(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID)
			return false
		}
		true
	}

	def String getErrorMessage() {
		errorMsg
	}

	private def String setErrorMessage(String err) {
		errorMsg = err
	}
}
