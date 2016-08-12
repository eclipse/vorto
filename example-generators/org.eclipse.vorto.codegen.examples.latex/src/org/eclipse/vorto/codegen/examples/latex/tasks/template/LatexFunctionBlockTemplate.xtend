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
package org.eclipse.vorto.codegen.examples.latex.tasks.template

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.utils.Utils
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class LatexFunctionBlockTemplate implements ITemplate<FunctionblockModel>{
	
	LatexSimplePropertyConstraintTemplate constraintTemplate;
	LatexSimplePropertyTemplate simpleTemplate;
	LatexComplexPropertyTemplate complexTemplate;
	LatexOperationTemplate operationTemplate;
	
	new (LatexSimplePropertyConstraintTemplate constraintTemplate,
		LatexSimplePropertyTemplate simpleTemplate,
		LatexComplexPropertyTemplate complexTemplate,
		LatexOperationTemplate operationTemplate){
		this.constraintTemplate=constraintTemplate;
		this.simpleTemplate = simpleTemplate;
		this.complexTemplate = complexTemplate;
		this.operationTemplate = operationTemplate;
	}
	
	override getContent(FunctionblockModel fb,InvocationContext invocationContext) {
		'''
			\subsection{«fb.displayname»}
			«fb.description» The details of the Functionblock «fb.displayname»
			\footnote{Name: «fb.name», Namespace: «fb.namespace», Version: «fb.version».}
			are described below:
			«IF fb.functionblock.status != null»
				\subsubsection{Status Properties}
				«FOR property : fb.functionblock.status.properties»
					«IF (Utils.isSimpleNumeric(property))»
						«constraintTemplate.getContent(property,invocationContext)»
					«ELSE»
						«IF (property.type instanceof PrimitivePropertyType)»
							«simpleTemplate.getContent(property,invocationContext)»
						«ELSE»
							«complexTemplate.getContent(property,invocationContext)»
						«ENDIF»
					«ENDIF»
				«ENDFOR»
			«ENDIF»
			
			«IF fb.functionblock.configuration != null»
				\subsubsection{Configuration Properties}
				«FOR property : fb.functionblock.configuration.properties»
					«IF (Utils.isSimpleNumeric(property))»
						«constraintTemplate.getContent(property,invocationContext)»
					«ELSE»
						«IF (property.type instanceof PrimitivePropertyType)»
							«simpleTemplate.getContent(property,invocationContext)»
						«ELSE»
							«complexTemplate.getContent(property,invocationContext)»
						«ENDIF»
					«ENDIF»
				«ENDFOR»
			«ENDIF»
			
			«IF fb.functionblock.fault != null»
				\subsubsection{Fault Properties}
				«FOR property : fb.functionblock.fault.properties»
					«IF (Utils.isSimpleNumeric(property))»
						«constraintTemplate.getContent(property,invocationContext)»
					«ELSE»
						«IF (property.type instanceof PrimitivePropertyType)»
							«simpleTemplate.getContent(property,invocationContext)»
						«ELSE»
							«complexTemplate.getContent(property,invocationContext)»
						«ENDIF»
					«ENDIF»
				«ENDFOR»
			«ENDIF»
			
			«IF (fb.functionblock.operations != null)&&(fb.functionblock.operations.size>0)»
				\subsubsection{Operations}
				«FOR operation : fb.functionblock.operations»
					«operationTemplate.getContent(operation,invocationContext)»
				«ENDFOR»
			«ENDIF»
		'''
	}
	
	
	
}