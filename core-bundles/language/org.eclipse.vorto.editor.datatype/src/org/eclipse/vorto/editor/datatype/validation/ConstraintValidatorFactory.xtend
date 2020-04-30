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

import org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType
import org.eclipse.vorto.editor.datatype.internal.validation.AccordinglyValueValidator
import org.eclipse.vorto.editor.datatype.internal.validation.ConstraintValueValidator
import org.eclipse.vorto.editor.datatype.internal.validation.RegexValueValidator
import org.eclipse.vorto.editor.datatype.internal.validation.ScalarValueValidator
import org.eclipse.vorto.editor.datatype.internal.validation.NullableValueValidator

class ConstraintValidatorFactory {
	
	def static ConstraintValueValidator getValueValidator(ConstraintIntervalType type){
		var typeStr = type.literal.toString
		switch typeStr{
				case 'MAX' :
					new AccordinglyValueValidator
				case 'MIN' :
					new AccordinglyValueValidator
				case 'REGEX' :
					new RegexValueValidator
				case 'STRLEN' :
					new ScalarValueValidator
				case 'MIMETYPE' :
					new RegexValueValidator
				case 'SCALING' :
					new ScalarValueValidator 
				case 'DEFAULT' :
					new AccordinglyValueValidator
				case 'NULLABLE' :
					new NullableValueValidator
				default:
					throw new IllegalArgumentException('Not supported constraint type')
			}
	}
}