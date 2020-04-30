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
package org.eclipse.vorto.editor.datatype.internal.validation

import org.eclipse.vorto.core.api.model.datatype.Constraint
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType

abstract class ConstraintValueValidator {
	
	var errorMsg = "" ;
	
	def boolean evaluateValueType(PrimitiveType type,Constraint constraint)
	
	def String getErrorMessage(){
		errorMsg
	}
	
	protected def String setErrorMessage(String err){
		errorMsg = err
	}
}