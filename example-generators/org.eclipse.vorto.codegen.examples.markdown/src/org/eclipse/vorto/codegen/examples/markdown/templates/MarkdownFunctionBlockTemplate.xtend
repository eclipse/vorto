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
package org.eclipse.vorto.codegen.examples.markdown.templates

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.examples.markdown.utils.Utils
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class MarkdownFunctionBlockTemplate implements ITemplate<FunctionblockModel>{
	

	
	override getContent(FunctionblockModel fb,InvocationContext invocationContext) {
		'''
			## Functionblock *«fb.name»*
			### Unique Identification
			<table>
				<tr><td>Name:</td><td>«fb.name»</td></tr>
				<tr><td>Namespace:</td><td>«fb.namespace»</td></tr>
				<tr><td>Version:</td><td>«fb.version»</td></tr>
			</table>
			### Description
			«fb.description»
			«IF fb.functionblock.status != null»
				### Status Properties
				<table>
				<tr><td>Name</td><td>Type</td><td>Description</td></tr>
				«FOR property : fb.functionblock.status.properties»
					<tr><td>«property.name»</td><td>«Utils.getPropertyType(property)»</td><td>«property.description»</td></tr>
				«ENDFOR»
				</table>
			«ENDIF»
			
			«IF fb.functionblock.configuration != null»
				### Configuration Properties
				<table>
					<tr><td>Name</td><td>Type</td><td>Description</td></tr>
					«FOR property : fb.functionblock.configuration.properties»
						<tr><td>«property.name»</td><td>«Utils.getPropertyType(property)»</td><td>«property.description»</td></tr>
					«ENDFOR»
				</table>
			«ENDIF»
			
			«IF fb.functionblock.fault != null»
				### Fault Properties
				<table>
				<tr><td>Name</td><td>Type</td><td>Description</td></tr>
				«FOR property : fb.functionblock.fault.properties»
					<tr><td>«property.name»</td><td>«Utils.getPropertyType(property)»</td><td>«property.description»</td></tr>
				«ENDFOR»
				</table>
			«ENDIF»
			«FOR operation : fb.functionblock.operations»
				### Operation *«operation.name»*
				<table>
					<tr><td>Name</td><td>«operation.name»</td></tr>
					<tr><td>Description</td><td>«operation.description»</td></tr>
					«IF operation.returnType!=null»
						<tr><td>Return Type</td><td>«Utils.getReturnType(operation.returnType)»</td></tr>
					«ENDIF»
				</table>
				
			«ENDFOR»
		'''
	}
	
	
	
}