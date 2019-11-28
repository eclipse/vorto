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
