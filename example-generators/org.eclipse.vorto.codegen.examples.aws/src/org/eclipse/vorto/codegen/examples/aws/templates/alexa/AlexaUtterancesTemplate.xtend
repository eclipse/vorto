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
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

/**
 * @author Alexander Edelmann (Robert Bosch (SEA) Pte. Ltd)
 */
class AlexaUtterancesTemplate extends AbstractAlexaTemplate {
			
	override getFileName(InformationModel context) {
		'''«context.name.toLowerCase»Utterances.txt'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		«FOR fbProperty : element.properties»
			«FOR operation : fbProperty.type.functionblock.operations»
				«var mappedElement = context.getMappedElement(operation,STEREOTYPE_ALEXA)»
				«var commands = split(mappedElement.getAttributeValue("command",getDefaultCommand(operation).toString))»
				«FOR singleCmd : commands»
					«operation.name» «singleCmd»
				«ENDFOR»
			«ENDFOR»
			
			«IF fbProperty.type.functionblock.status != null»
				«FOR statusProperty : fbProperty.type.functionblock.status.properties»
					«var mappedElement = context.getMappedElement(statusProperty,STEREOTYPE_ALEXA)»
					«var commands = split(mappedElement.getAttributeValue("command",getDefaultCommand(statusProperty).toString))»
					«FOR singleCmd : commands»
					«statusProperty.name» «singleCmd»
					«ENDFOR»
				«ENDFOR»
			«ENDIF»
			
		«ENDFOR»
		'''
	}
	
	private def String[] split(String concatenatedValue) {
		return concatenatedValue.split(";");
	}
	
	protected def getDefaultCommand(Operation operation) {
		if (operation.params != null && operation.params.length > 0) {
			'''«FOR param : operation.params BEFORE '{' SEPARATOR ' ' AFTER '}'»«IF isAlexaSupportedParamType(param)»«param.name»«ENDIF»«ENDFOR»'''
		} else {
			return operation.name
		}
	}
	
	protected def getDefaultCommand(Property property) {
		'''get «property.name» status'''
	}
	
}
