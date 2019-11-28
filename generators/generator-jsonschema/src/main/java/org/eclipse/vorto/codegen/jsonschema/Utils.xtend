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
package org.eclipse.vorto.codegen.jsonschema

import org.eclipse.vorto.codegen.jsonschema.templates.ConstraintTemplate
import org.eclipse.vorto.core.api.model.datatype.ConstraintRule
import org.eclipse.vorto.plugin.generator.InvocationContext

class Utils {
	var static ConstraintTemplate constraintTemplate = new ConstraintTemplate()
	
    static def getConstraintsContent(ConstraintRule constraintRule,InvocationContext invocationContext){
		'''
			«IF constraintRule !== null»
				«FOR constraint : constraintRule.constraints BEFORE ',\n' SEPARATOR ','»
					«constraintTemplate.getContent(constraint, invocationContext)»
				«ENDFOR»
			«ENDIF»
		'''
	}
}
