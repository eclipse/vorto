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
import org.eclipse.vorto.core.api.model.datatype.Enum

class LatexEnumTemplate implements ITemplate<Enum> {
	
	override getContent(Enum en,InvocationContext invocationContext) {
		'''
			## Enum *«en.name»*
			### Unique Identification
			<table>
				<tr><td>Name:</td><td>«en.name»</td></tr>
				<tr><td>Namespace:</td><td>«en.namespace»</td></tr>
				<tr><td>Version:</td><td>«en.version»</td></tr>
			</table>
			### Description
			«en.description»
			
			### Literals
			<table>
				<tr><td>Literal</td><td>Description</td></tr>
			«FOR literal:en.enums»
				<tr><td>«literal.name»</td><td>«literal.description»</td></tr>
			«ENDFOR»
			</table>
		'''
	}
}