/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.codegen.aws.alexa.templates

import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class AlexaUtterancesTemplate extends AbstractAlexaTemplate {
			
	override getFileName(InformationModel context) {
		'''«context.name.toLowerCase»Utterances.txt'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		«FOR fbProperty : element.properties»
			«FOR operation : fbProperty.type.functionblock.operations»
				«var mappedElement = context.getMappedElement(operation,STEREOTYPE_ALEXA)»
				«var commands = split(mappedElement.getAttributeValue("command",getDefaultCommand(fbProperty.name, operation).toString))»
				«FOR singleCmd : commands»
					«fbProperty.name.replace("_","")»«operation.name.toFirstUpper.replace("_","")» «singleCmd.replace("_"," ")»
				«ENDFOR»
			«ENDFOR»
			
			«IF fbProperty.type.functionblock.status != null»
				«FOR statusProperty : fbProperty.type.functionblock.status.properties»
					«var mappedElement = context.getMappedElement(statusProperty,STEREOTYPE_ALEXA)»
					«var commands = split(mappedElement.getAttributeValue("command",getDefaultCommand(fbProperty.name,statusProperty).toString))»
					«FOR singleCmd : commands»
					«fbProperty.name.replace("_","")»«statusProperty.name.toFirstUpper.replace("_","")» «singleCmd.replace("_"," ")»
					«ENDFOR»
				«ENDFOR»
			«ENDIF»
			
		«ENDFOR»
		'''
	}
	
	private def String[] split(String concatenatedValue) {
		return concatenatedValue.split(";");
	}
	
	protected def getDefaultCommand(String fbPropertyName, Operation operation) {
		if (operation.params != null && operation.params.length > 0) {
			'''«FOR param : operation.params BEFORE '{' SEPARATOR ' ' AFTER '}'»«IF isAlexaSupportedParamType(param)»«param.name»«ENDIF»«ENDFOR»'''
		} else {
			return fbPropertyName+" "+operation.name
		}
	}
	
	protected def getDefaultCommand(String functionblockPropertyName, Property property) {
		'''get «functionblockPropertyName» «property.name»'''
	}
	
}
