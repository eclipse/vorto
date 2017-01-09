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

import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

/**
 * @author Alexander Edelmann (Robert Bosch (SEA) Pte. Ltd)
 */
class AlexaIndentSchemaTemplate extends AbstractAlexaTemplate {
		
	override getFileName(InformationModel context) {
		return "IntendSchema.json";
	}
	
	override getContent(InformationModel element, InvocationContext context) {
	'''
		{
		  "intents": [
				«FOR fbProperty : element.properties»
				«FOR operation : fbProperty.type.functionblock.operations SEPARATOR ','»
					{
					"intent": "«operation.name»"«IF operation.params != null && operation.params.length > 0»,
					"slots": [
						«FOR param : operation.params SEPARATOR ','»
							«IF isAlexaSupportedParamType(param)»
								{
								"name": "«param.name»",
								"type": "«mapToAlexaSupportedType(param)»"
								}
							«ENDIF»
						«ENDFOR»
					]
					«ENDIF»
					}
				«ENDFOR»
				«IF fbProperty.type.functionblock.status != null»
					«IF fbProperty.type.functionblock.operations.length > 0»,«ENDIF»
					«FOR statusProperty : fbProperty.type.functionblock.status.properties SEPARATOR ','»
						{
						"intent": "«statusProperty.name»Status"
						}
					«ENDFOR»
				«ENDIF»
		«ENDFOR»
		  ]
		}
	'''
	}
	
}
