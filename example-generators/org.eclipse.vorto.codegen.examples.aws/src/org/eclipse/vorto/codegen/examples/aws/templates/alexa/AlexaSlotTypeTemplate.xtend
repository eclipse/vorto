/*******************************************************************************
 * Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.examples.aws.templates.alexa

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.codegen.api.InvocationContext

class AlexaSlotTypeTemplate implements IFileTemplate<Enum> {
	
	override getFileName(Enum context) {
		return context.name+"_CustomSlotType.txt"
	}
	
	override getPath(Enum context) {
		return "aws/alexa";
	}
	
	override getContent(Enum element, InvocationContext context) {
		'''
		«FOR literal : element.enums»
		«literal.name»
		«ENDFOR»
		'''
	}
	
}