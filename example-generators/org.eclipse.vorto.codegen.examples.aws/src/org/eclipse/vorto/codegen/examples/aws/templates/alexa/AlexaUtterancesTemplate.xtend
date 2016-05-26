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

import org.eclipse.vorto.codegen.api.IMappingContext
import org.eclipse.vorto.codegen.api.utils.MappingRuleUtils
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

/**
 * @author Alexander Edelmann (Robert Bosch (SEA) Pte. Ltd)
 */
class AlexaUtterancesTemplate extends AbstractAlexaTemplate {
		
	new (IMappingContext mappingContext) {
		super(mappingContext)
	}
	
	override getFileName(InformationModel context) {
		'''«context.name.toLowerCase»Utterances.txt'''
	}
	
	override getContent(InformationModel context) {
		'''
		«FOR fbProperty : context.properties»
			«FOR operation : fbProperty.type.functionblock.operations»
			«var mappingRule = this.mappingContext.getMappingRuleByOperationAndStereoType(operation,STEREOTYPE_ALEXA)»
			«IF (mappingRule != null)»
					« var commands = split(MappingRuleUtils.getAttributeValue(mappingRule.target,"command",getDefaultCommand(operation).toString))»
					«FOR singleCmd : commands»
						«operation.name» «singleCmd»
					«ENDFOR»
			«ELSE»
				«operation.name» «operation.name» «getDefaultCommand(operation)»
			«ENDIF»
			«ENDFOR»
			
			«IF fbProperty.type.functionblock.status != null»
				«FOR statusProperty : fbProperty.type.functionblock.status.properties»
					«var mappingRule = this.mappingContext.getMappingRuleByPropertyAndStereoType(statusProperty,STEREOTYPE_ALEXA)»
					«IF (mappingRule != null)»
						« var commands = split(MappingRuleUtils.getAttributeValue(mappingRule.target,"command",getDefaultCommand(statusProperty).toString))»
						«FOR singleCmd : commands»
							«statusProperty.name» «singleCmd»
						«ENDFOR»
					«ELSE»
						«statusProperty.name»Status get «statusProperty.name» status
					«ENDIF»
				«ENDFOR»
			«ENDIF»
			
		«ENDFOR»
		'''
	}
	
	private def String[] split(String concatenatedValue) {
		return concatenatedValue.split(";");
	}
	
	protected def getDefaultCommand(Operation operation) {
		'''«FOR param : operation.params BEFORE '{' SEPARATOR ' ' AFTER '}'»«IF isAlexaSupportedParamType(param)»«param.name»«ENDIF»«ENDFOR»'''
	}
	
	protected def getDefaultCommand(org.eclipse.vorto.core.api.model.datatype.Property property) {
		'''«property.name»'''
	}
	
}
