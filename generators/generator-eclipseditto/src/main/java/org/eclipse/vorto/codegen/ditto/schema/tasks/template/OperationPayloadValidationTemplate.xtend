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

import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.Param
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.ITemplate

class OperationPayloadValidationTemplate implements ITemplate<Operation>{
	
	val static OperationSingleParameterValidationTemplate operationSingleParameterValidationTemplate = new OperationSingleParameterValidationTemplate();
	
	new() {
	}
	
	override getContent(Operation operation, InvocationContext invocationContext) {
		var fbm = operation.eContainer.eContainer as FunctionblockModel;
		var definition = fbm.namespace + ":" + fbm.name + ":" + fbm.version;
		'''
			{
				"$schema": "http://json-schema.org/draft-04/schema#",
				"title": "Operation payload validation of definition <«definition»> for message subject (operation name) <«operation.name»>",
				«IF !operation.description.nullOrEmpty»"description": "«operation.description»"«IF !operation.params.empty»,«ENDIF»«ENDIF»
				«IF operation.params.size === 1»
				«calcSingleParamContent(operation.params.get(0), invocationContext)»
				«ELSEIF !operation.params.empty»
				"type": "object",
				"properties": {
					«FOR param : operation.params SEPARATOR ','»
						«operationSingleParameterValidationTemplate.getContent(param, invocationContext)»
					«ENDFOR»
				},
				"required" : [«FOR param : operation.params SEPARATOR ','»"«param.name»"«ENDFOR»]
				«ENDIF»
			}
		'''
	}
	
	static def CharSequence calcSingleParamContent(Param param, InvocationContext invocationContext) {
		val singleParamStr = OperationSingleParameterValidationTemplate.handleParam(param, invocationContext, false).toString.trim
			.replace("\"" + param.name + "\": {", "").trim
		val theStringContent = singleParamStr.substring(0, singleParamStr.length-1);
		'''
		«theStringContent»
		'''
	}
}
		