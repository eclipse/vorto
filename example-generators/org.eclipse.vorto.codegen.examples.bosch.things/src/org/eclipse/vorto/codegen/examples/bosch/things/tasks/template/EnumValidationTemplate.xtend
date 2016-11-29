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
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.codegen.api.InvocationContext

class EnumValidationTemplate implements ITemplate<Enum>{
	
	new() {
		
	}
	
	override getContent(Enum en,InvocationContext invocationContext) {
		'''
			"enum": [ «FOR literal: en.enums SEPARATOR ', '»"«literal.name»"«ENDFOR» ]
		'''
	}
}