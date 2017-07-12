/*******************************************************************************
 * Copyright (c) 2014,2016 Bosch Software Innovations GmbH and others.
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
 
 package org.eclipse.vorto.editor.datatype.validation

import org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType
import org.eclipse.vorto.editor.datatype.internal.validation.AccordinglyValueValidator
import org.eclipse.vorto.editor.datatype.internal.validation.ConstraintValueValidator
import org.eclipse.vorto.editor.datatype.internal.validation.RegexValueValidator
import org.eclipse.vorto.editor.datatype.internal.validation.ScalarValueValidator

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
				default:
					throw new IllegalArgumentException('Not supported constraint type')
			}
	}
}