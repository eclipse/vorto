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

import java.text.ParseException
import java.text.SimpleDateFormat
import org.eclipse.vorto.core.api.model.datatype.Constraint
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.editor.datatype.validation.DatatypeSystemMessage

class AccordinglyValueValidator extends ConstraintValueValidator {
	
	override evaluateValueType(PrimitiveType type, Constraint constraint) {
		var rawValue = constraint.constraintValues
		var typeStr = type.literal
		
		switch typeStr{
				case 'int' :
					try {
						Integer.parseInt(rawValue)
					}catch (NumberFormatException ex){
						this.setErrorMessage(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_INT)
						return false
					}
					
				case 'long' :
					try{
						Long.parseLong(rawValue)
					}catch(NumberFormatException ex){
						this.setErrorMessage(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_LONG)
						return false
					}	
					
				case 'short' :
					try{
						Short.parseShort(rawValue)
					}catch(NumberFormatException ex){
						this.setErrorMessage(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_SHORT)
						return false
					}	
					
				case 'double' :
					try{
						Double.parseDouble(rawValue)
					}catch(NumberFormatException ex){
						this.setErrorMessage(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_DOUBLE)
						return false
					}	
					
				case 'float' :
					try {
						Float.parseFloat(rawValue)
					}catch (NumberFormatException ex){
						this.setErrorMessage(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_FLOAT)
						return false
					}
					
				case 'dateTime' :
					try {
						new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(rawValue); 
					}catch (ParseException ex){
						this.setErrorMessage(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_DATETIME)
						return false
					}
				case 'byte' :
					try {
						Byte.parseByte(rawValue)
					}catch (NumberFormatException ex){
						this.setErrorMessage(DatatypeSystemMessage.ERROR_CONSTRAINT_VALUE_BYTE)
						return false
					}
		}
		true
	}
	
	
	
}