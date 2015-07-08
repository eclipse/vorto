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

import org.apache.log4j.Logger
import org.eclipse.vorto.core.api.model.datatype.Constraint
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.editor.datatype.validation.DatatypeSystemMessage

class ScalarValueValidator extends ConstraintValueValidator {
	
	private static final Logger logger = Logger.getLogger(ScalarValueValidator.canonicalName)
	
	override evaluateValueType(PrimitiveType type, Constraint constraint) {
		try {
			Integer.parseInt(constraint.constraintValues)
		}catch (NumberFormatException ex){
			logger.info(ex.message)
			this.setErrorMessage(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_INT)
			return false
		}
		
		true
	}
	
}