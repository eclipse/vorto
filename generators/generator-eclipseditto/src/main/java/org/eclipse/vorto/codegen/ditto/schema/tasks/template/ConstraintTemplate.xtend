/*******************************************************************************
 * Copyright (c) 2017 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.ditto.schema.tasks.template;

import org.eclipse.vorto.core.api.model.datatype.Constraint
import org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.ITemplate

class ConstraintTemplate implements ITemplate<Constraint>{
	
	new() {
	}
	
	override getContent(Constraint constraint, InvocationContext invocationContext) {
		'''«IF (!getJsonConstraint(constraint.type).isNullOrEmpty)»«getJsonConstraint(constraint.type)»«constraint.constraintValues»«ENDIF»'''
	}
	
	def getJsonConstraint(ConstraintIntervalType type) {
		if(type == ConstraintIntervalType.STRLEN){
			return '''"maxLength": '''
		} else if(type == ConstraintIntervalType.REGEX) {
			return '''"pattern": '''
		} else if(type == ConstraintIntervalType.MIN) {
			return '''"minimum": '''
		} else if(type == ConstraintIntervalType.MAX) {
			return '''"maximum": '''
		} else if(type == ConstraintIntervalType.SCALING) {
			return '''"multipleOf": '''
		} else if(type == ConstraintIntervalType.DEFAULT) {
			return '''"default": '''
		}
		
		return null
	}	
}
