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
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.utils.Utils

class MarkdownInformationModelTemplate implements ITemplate<InformationModel>{
	
	MarkdownFunctionBlockTemplate fbTemplate;
	MarkdownEntityTemplate entityTemplate;
	MarkdownEnumTemplate enumTemplate;
	
	new(MarkdownFunctionBlockTemplate fbTemplate,MarkdownEntityTemplate entityTemplate, MarkdownEnumTemplate enumTemplate) {
		this.fbTemplate = fbTemplate;
		this.entityTemplate = entityTemplate;
		this.enumTemplate = enumTemplate;
	}
	
	override getContent(InformationModel im,InvocationContext invocationContext) {
		'''
			# Information Model *«im.name»*
			
			### Unique Identification
			<table>
				<tr><td>Name:</td><td>«im.name»</td></tr>
				<tr><td>Namespace:</td><td>«im.namespace»</td></tr>
				<tr><td>Version:</td><td>«im.version»</td></tr>
			</table>
			### Description
			«im.description»
			
			### Functionblock Overview
			
			<table>
			<tr><td>Functionblock</td><td>Name</td><td>Description</td></tr>
			«FOR fbProperty : im.properties»
				<tr><td>«fbProperty.type.name»</td><td>«fbProperty.name»</td><td>«fbProperty.description»</td></tr>
			«ENDFOR»
			</table>
			
			# Functionblocks
			«FOR fbProperty : im.properties»
				«fbTemplate.getContent(fbProperty.type,invocationContext)»
				
			«ENDFOR»
			
			# Entities
			«FOR fbProperty : im.properties»
				«FOR type : Utils.getReferencedEntities(fbProperty.type.functionblock)»
					«entityTemplate.getContent(type,invocationContext)»
				«ENDFOR»
			«ENDFOR»
			
			# Enums
			«FOR fbProperty : im.properties»
				«FOR type : Utils.getReferencedEnums(fbProperty.type.functionblock)»
					«enumTemplate.getContent(type,invocationContext)»
				«ENDFOR»
			«ENDFOR»
			
		'''
	}
}