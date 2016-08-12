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
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType

class LatexEntityTemplate implements ITemplate<Entity> {
	LatexSimplePropertyConstraintTemplate constraintTemplate;
	LatexSimplePropertyTemplate simpleTemplate;
	LatexComplexPropertyTemplate complexTemplate;
	
	new (LatexSimplePropertyConstraintTemplate constraintTemplate,
		LatexSimplePropertyTemplate simpleTemplate,
		LatexComplexPropertyTemplate complexTemplate){
		this.constraintTemplate=constraintTemplate;
		this.simpleTemplate = simpleTemplate;
		this.complexTemplate = complexTemplate;
	}
	
	override getContent(Entity entity,InvocationContext invocationContext) {
		'''
		\subsection{«entity.displayname»}
			«entity.description»
			The properties of the data type «entity.displayname»\footnote{Name: «entity.name», Namespace: «entity.namespace», Version: «entity.version».} are described below:\\\\
			«FOR property:entity.properties»
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
		'''
	}
}