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
package org.eclipse.vorto.editor.datatype.validation

import org.eclipse.vorto.core.api.model.datatype.Constraint
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.editor.datatype.validation.DatatypeSystemMessage

class PropertyConstraintMappingValidation {

	var errorMsg = DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID ;

	val String[] intervalArr = #["MAX", "MIN", "SCALING", "DEFAULT"]
	val String[] boolArr = #["DEFAULT"]
	val String[] strArr = #["STRLEN", "REGEX", "DEFAULT"]
	val String[] mimeArr = #["MIMETYPE"]
	val String[] byteArr = intervalArr + mimeArr

	private val valueTypeMap = <String, String[]>newHashMap(
		'int' -> intervalArr,
		'byte' -> byteArr,
		'float' -> intervalArr,
		'long' -> intervalArr,
		'short' -> intervalArr,
		'double' -> intervalArr,
		'dateTime' -> intervalArr,
		'string' -> strArr,
		'boolean' -> boolArr,
		'base64Binary' -> mimeArr
	);

	def final boolean checkPropertyConstraints(PrimitiveType type, Constraint cons) {
		var arr = valueTypeMap.get(type.literal)
		if (cons.type.literal.toUpperCase().equals("NULLABLE")) {
			return true
		}
		if (arr === null || !arr.contains(cons.type.literal)) {
			setErrorMessage(DatatypeSystemMessage.ERROR_CONSTRAINTTYPE_INVALID)
			return false
		}
		return true
	}

	def String getErrorMessage() {
		errorMsg
	}

	private def String setErrorMessage(String err) {
		errorMsg = err
	}
}
