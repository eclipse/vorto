/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.examples.bosch.things.tasks.template

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.datatype.Constraint
import org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType

class ConstraintTemplate implements ITemplate<Constraint>{
	
	override getContent(Constraint constraint, InvocationContext invocationContext) {
		'''«getJsonConstraint(constraint.type)»«constraint.constraintValues»'''
		
	}
	
	def getJsonConstraint(ConstraintIntervalType type) {
		if(type == ConstraintIntervalType.STRLEN){
			return '''"maxLength" : '''
		} else if(type == ConstraintIntervalType.REGEX) {
			return '''"pattern" : '''
		} else if(type == ConstraintIntervalType.MIN) {
			return '''"minimum" : '''
		} else if(type == ConstraintIntervalType.MAX) {
			return '''"maximum" : '''
		} else if(type == ConstraintIntervalType.SCALING) {
			return '''"multipleOf" : '''
		}
		return ''''''
	}	
}