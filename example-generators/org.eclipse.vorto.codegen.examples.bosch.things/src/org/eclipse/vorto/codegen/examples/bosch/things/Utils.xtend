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
package org.eclipse.vorto.codegen.examples.bosch.things

import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.datatype.ConstraintRule
import org.eclipse.vorto.codegen.examples.bosch.things.tasks.template.ConstraintTemplate

class Utils {
	var static ConstraintTemplate constraintTemplate = new ConstraintTemplate()
	
	public static def getConstraintsContent(ConstraintRule constraintRule,InvocationContext invocationContext){
		return 
		'''
		«IF constraintRule != null»
			«FOR constraint : constraintRule.constraints BEFORE ',\n' SEPARATOR ', '»
				«constraintTemplate.getContent(constraint, invocationContext)»
			«ENDFOR»
		«ENDIF»
		'''

	}
}