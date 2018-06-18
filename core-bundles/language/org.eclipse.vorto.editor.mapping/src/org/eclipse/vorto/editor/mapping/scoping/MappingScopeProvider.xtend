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
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.mapping.ConfigurationSource
import org.eclipse.vorto.core.api.model.mapping.EntityPropertySource
import org.eclipse.vorto.core.api.model.mapping.EnumPropertySource
import org.eclipse.vorto.core.api.model.mapping.EventSource
import org.eclipse.vorto.core.api.model.mapping.FaultSource
import org.eclipse.vorto.core.api.model.mapping.InfoModelPropertySource
import org.eclipse.vorto.core.api.model.mapping.OperationSource
import org.eclipse.vorto.core.api.model.mapping.StatusSource
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
	
	def IScope scope_EnumPropertySource_property(EnumPropertySource exp, EReference ref) {
		var enumType = exp.model as Enum;
		return Scopes::scopeFor(enumType.enums);
	} 

	def IScope scope_EntityPropertySource_property(EntityPropertySource exp, EReference ref) {
		var model = exp.model as Entity;
		return Scopes::scopeFor(model.properties);
	}

	def IScope scope_InfoModelPropertySource_property(InfoModelPropertySource exp, EReference ref) {
		var model = exp.model as InformationModel
		return Scopes::scopeFor(model.properties);
	}

	def IScope scope_ConfigurationSource_property(ConfigurationSource exp, EReference ref) {
		var model = exp.model as FunctionblockModel;
		return Scopes::scopeFor(model.functionblock.configuration.properties);
	}

	def IScope scope_StatusSource_property(StatusSource exp, EReference ref) {
		var model = exp.model as FunctionblockModel;
		return Scopes::scopeFor(model.functionblock.status.properties);
	}

	def IScope scope_FaultSource_property(FaultSource exp, EReference ref) {
		var model = exp.model as FunctionblockModel;
		return Scopes::scopeFor(model.functionblock.fault.properties);
	}

	def IScope scope_OperationSource_operation(OperationSource exp, EReference ref) {
		var model = exp.model as FunctionblockModel;
		return Scopes::scopeFor(model.functionblock.operations);
	}

	def IScope scope_EventSource_event(EventSource exp, EReference ref) {
		var model = exp.model as FunctionblockModel;
		return Scopes::scopeFor(model.functionblock.events);
	}

	def IScope scope_EventSource_eventProperty(EventSource exp, EReference ref) {
		return Scopes::scopeFor(exp.event.properties);
	}
	
	def IScope scope_OperationSource_param(OperationSource exp, EReference ref) {
		return Scopes::scopeFor(exp.operation.params);
	}
}