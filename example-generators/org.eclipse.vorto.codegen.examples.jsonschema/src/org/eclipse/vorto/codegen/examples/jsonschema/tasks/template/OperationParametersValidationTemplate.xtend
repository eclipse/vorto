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
package org.eclipse.vorto.codegen.examples.jsonschema.tasks.template

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.Param

class OperationParametersValidationTemplate implements ITemplate<Operation>{
	
	var OperationSingleParameterValidation operationSingleParameterValidationTemplate;
	
	new(OperationSingleParameterValidation operationSingleParameterValidationTemplate) {
		this.operationSingleParameterValidationTemplate = operationSingleParameterValidationTemplate;
	}
	
	override getContent(Operation operation,InvocationContext invocationContext) {
		'''
			{
				"$schema": "http://json-schema.org/draft-04/schema#",
				"title": "Operation Parameter Validation",
				"description": "Operation Parameter Validation for «operation.name».",
				"type": "object",
				"properties": {
					«FOR Param param : operation.params SEPARATOR ', '»
						«operationSingleParameterValidationTemplate.getContent(param,invocationContext)»
					«ENDFOR»
				}
			}
		'''
	}
}