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
import org.eclipse.vorto.core.api.model.datatype.Entity

class MarkdownEntityTemplate implements ITemplate<Entity> {
	
	override getContent(Entity entity,InvocationContext invocationContext) {
		'''
			## Entity *«entity.name»*
			### Unique Identification
			<table>
				<tr><td>Name:</td><td>«entity.name»</td></tr>
				<tr><td>Namespace:</td><td>«entity.namespace»</td></tr>
				<tr><td>Version:</td><td>«entity.version»</td></tr>
			</table>
			### Description
			«entity.description»
			
			### Properties
			<table>
				<tr><td>Name</td><td>Type</td><td>Description</td></tr>
				«FOR property : entity.properties»
					<tr><td>«property.name»</td><td>«Utils.getPropertyType(property)»</td><td>«property.description»</td></tr>
				«ENDFOR»
			</table>
		'''
	}
}