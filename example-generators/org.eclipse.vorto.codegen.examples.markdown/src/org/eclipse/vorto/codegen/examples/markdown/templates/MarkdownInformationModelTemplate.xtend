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

import org.eclipse.emf.common.util.BasicEList
import org.eclipse.emf.common.util.EList
import org.eclipse.vorto.codegen.api.tasks.ITemplate
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.Type
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class MarkdownInformationModelTemplate implements ITemplate<InformationModel>{
	
	MarkdownFunctionBlockTemplate fbTemplate;
	MarkdownEntityTemplate entityTemplate;
	MarkdownEnumTemplate enumTemplate;
	
	new(MarkdownFunctionBlockTemplate fbTemplate,MarkdownEntityTemplate entityTemplate, MarkdownEnumTemplate enumTemplate) {
		this.fbTemplate = fbTemplate;
		this.entityTemplate = entityTemplate;
		this.enumTemplate = enumTemplate;
	}
	
	override getContent(InformationModel im) {
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
				«fbTemplate.getContent(fbProperty.type)»
				
			«ENDFOR»
			
			# Entities
			«FOR fbProperty : im.properties»
				«FOR type : getReferencedEntities(fbProperty.type.functionblock)»
					«entityTemplate.getContent(type)»
				«ENDFOR»
			«ENDFOR»
			
			# Enums
			«FOR fbProperty : im.properties»
				«FOR type : getReferencedEnums(fbProperty.type.functionblock)»
					«enumTemplate.getContent(type)»
				«ENDFOR»
			«ENDFOR»
			
		'''
	}
	
	def EList<Entity> getReferencedEntities(FunctionBlock fb) {
		var entities = new BasicEList<Entity>();
		for (Type type : getReferencedTypes(fb)) {
			if (type instanceof Entity) {
				entities.add(type);
			}
		}
		return entities;
	}
	
	def EList<Enum> getReferencedEnums(FunctionBlock fb) {
		var enums = new BasicEList<Enum>();
		for (Type type : getReferencedTypes(fb)) {
			if (type instanceof Enum) {
				enums.add(type);
			}
		}
		return enums;
	}
	
	def EList<Type> getReferencedTypes(Entity entity) {
		var types = new BasicEList<Type>();
			for (org.eclipse.vorto.core.api.model.datatype.Property property : entity.getProperties()) {
				types.addAll(getReferencedTypes(property));
			}
			types.add(entity.getSuperType());
		return types;
	}
	
	def EList<Type> getReferencedTypes(org.eclipse.vorto.core.api.model.datatype.Property property) {
		var types = new BasicEList<Type>();
		if (property.getType() instanceof ObjectPropertyType) {
			var objectType = property.getType() as ObjectPropertyType;
			types.add(objectType.getType());
			if (objectType.getType() instanceof Entity) {
				types.addAll(getReferencedTypes(objectType.getType() as Entity));
			}
		}
		return types;
	}
	
	def EList<Type> getReferencedTypes(FunctionBlock fb) {
		var types = new BasicEList<Type>();
		if (fb != null) {
			// Analyze the status properties...
			if (fb.getStatus() != null) {
				for (org.eclipse.vorto.core.api.model.datatype.Property property : fb.getStatus().getProperties()) {
					types.addAll(getReferencedTypes(property));
				}
			}
			// Analyze the configuration properties...
			if (fb.getConfiguration() != null) {
				for (org.eclipse.vorto.core.api.model.datatype.Property property : fb.getConfiguration().getProperties()) {
					types.addAll(getReferencedTypes(property));
				}
			}
			// Analyze the fault properties...
			if (fb.getFault() != null) {
				for (org.eclipse.vorto.core.api.model.datatype.Property property : fb.getFault().getProperties()) {
					types.addAll(getReferencedTypes(property));
				}
			}
		}
		return types;
	}
}