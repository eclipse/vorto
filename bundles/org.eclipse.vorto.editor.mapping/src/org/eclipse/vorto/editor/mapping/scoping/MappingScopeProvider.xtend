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
package org.eclipse.vorto.editor.mapping.scoping

import org.eclipse.emf.ecore.EReference
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.datatype.Type
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock
import org.eclipse.vorto.core.api.model.mapping.Attribute
import org.eclipse.vorto.core.api.model.mapping.ConfigurationElement
import org.eclipse.vorto.core.api.model.mapping.EntityExpression
import org.eclipse.vorto.core.api.model.mapping.EventElement
import org.eclipse.vorto.core.api.model.mapping.FBTypeElement
import org.eclipse.vorto.core.api.model.mapping.FBTypeProperty
import org.eclipse.vorto.core.api.model.mapping.FaultElement
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockChildElement
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement
import org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement
import org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement
import org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression
import org.eclipse.vorto.core.api.model.mapping.OperationElement
import org.eclipse.vorto.core.api.model.mapping.StatusElement
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.Scopes
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider

/**o
 * This class contains custom scoping description.
 * 
 * see : http://www.eclipse.org/Xtext/documentation.html#scoping
 * on how and when to use it 
 *
 */
class MappingScopeProvider extends AbstractDeclarativeScopeProvider {

	def IScope scope_InfoModelFbElement_functionblock(InfoModelFbElement exp, EReference ref) {
		var infoModelSourceElement = exp.eContainer as  InfoModelSourceElement;
		var model = infoModelSourceElement.infoModel;
		return Scopes::scopeFor(model.properties);
	}

	def IScope scope_OperationElement_operation(OperationElement exp, EReference ref) {
		var FunctionBlock functionBlock = getFunctionBlock(exp.eContainer as FunctionBlockChildElement);
		return Scopes::scopeFor(functionBlock.operations);
	}

	def IScope scope_FBTypeElement_property(FBTypeElement exp, EReference ref) {
		return getEntityElementProperties(exp);
	}

	
	def IScope scope_FBTypeProperty_property(FBTypeProperty exp, EReference ref) {
		var fbTypeElement = exp.eContainer as FBTypeElement;
		var property = fbTypeElement.property;
		if (property.type instanceof ObjectPropertyType) {
			var propertyType = property.type as ObjectPropertyType;
			return getScopeForType(propertyType.type as Type);
		}
		return IScope.NULLSCOPE;
	} 

	def IScope scope_EventElement_event(EventElement exp, EReference ref) {
		var FunctionBlock functionBlock = getFunctionBlock(exp);
		return Scopes::scopeFor(functionBlock.events);
	}

	private def IScope getEntityElementProperties(FBTypeElement exp) {
		var functionBlockElement = (exp.eContainer as FunctionBlockChildElement);
		var functionBlock = getFunctionBlock(functionBlockElement);

		if (functionBlockElement instanceof ConfigurationElement) {
			return Scopes::scopeFor(functionBlock.configuration.properties);
		} else if (functionBlockElement instanceof StatusElement) {
			return Scopes::scopeFor(functionBlock.status.properties);
		} else if (functionBlockElement instanceof FaultElement) {
			return Scopes::scopeFor(functionBlock.fault.properties);
		} else if (functionBlockElement instanceof EventElement) {
			var eventElement = functionBlockElement as EventElement;
			return Scopes::scopeFor(eventElement.event.properties);
		}
		return IScope.NULLSCOPE;
	}

	private def FunctionBlock getFunctionBlock(FunctionBlockChildElement functionBlockElement) {
		if (functionBlockElement.eContainer instanceof InfoModelFbElement) {
			var modelElement = functionBlockElement.eContainer as InfoModelFbElement;
			var functionblockModel = modelElement.functionblock.type
			return functionblockModel.functionblock;
		} else if (functionBlockElement.eContainer instanceof FunctionBlockSourceElement) {
			var modelElement = functionBlockElement.eContainer as FunctionBlockSourceElement;
			var functionblockModel = modelElement.functionblock;
			return functionblockModel.functionblock;
		} else {
			throw new UnsupportedOperationException(
				"functionBlockElement.eContainer type not supported for: " + functionBlockElement.eContainer);
		}
	}

	def IScope scope_NestedEntityExpression_tail(NestedEntityExpression exp, EReference ref) {
		val head = exp.ref;
		switch (head) {
			EntityExpression: getScopeForType(head.entity)
			NestedEntityExpression: {
				val tail = head.tail
				switch (tail) {
					Attribute:
						IScope::NULLSCOPE
					Property: {
						var objectPropertyType = tail.type as ObjectPropertyType;
						return getScopeForType(objectPropertyType.type as Type);
					}
					default:
						IScope::NULLSCOPE
				}
			}
			default:
				IScope::NULLSCOPE
		}
	}

	def private getScopeForType(Type type) {
		if (type instanceof Entity) {
			var entity = type as Entity;
			return Scopes::scopeFor(entity.properties);
		} else if (type instanceof Enum) {
			var enum = type as Enum;
			return Scopes::scopeFor(enum.enums);
		}
		return IScope::NULLSCOPE;
	}
}