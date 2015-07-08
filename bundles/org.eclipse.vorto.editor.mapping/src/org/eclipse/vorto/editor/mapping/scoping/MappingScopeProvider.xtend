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
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock
import org.eclipse.vorto.core.api.model.mapping.ConfigurationElement
import org.eclipse.vorto.core.api.model.mapping.EventElement
import org.eclipse.vorto.core.api.model.mapping.FaultElement
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockElement
import org.eclipse.vorto.core.api.model.mapping.InformationModelElement
import org.eclipse.vorto.core.api.model.mapping.OperationElement
import org.eclipse.vorto.core.api.model.mapping.StatusElement
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.Scopes
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider

/**
 * This class contains custom scoping description.
 * 
 * see : http://www.eclipse.org/Xtext/documentation.html#scoping
 * on how and when to use it 
 *
 */
class MappingScopeProvider extends AbstractDeclarativeScopeProvider {
	
	def IScope scope_OperationElement_value(OperationElement exp, EReference ref) {
	    var FunctionBlock functionBlock =  getFunctionBlock(exp);	      
	   	return Scopes::scopeFor(functionBlock.operations);

	}
	
	def IScope scope_ConfigurationElement_value(ConfigurationElement exp, EReference ref) {
		var FunctionBlock functionBlock =  getFunctionBlock(exp);		      
	   	return Scopes::scopeFor(functionBlock.configuration.properties);
	}

	def IScope scope_StatusElement_value(StatusElement exp, EReference ref) {
		var FunctionBlock functionBlock =  getFunctionBlock(exp);		      
	   	return Scopes::scopeFor(functionBlock.status.properties);
	}
	
	def IScope scope_FaultElement_value(FaultElement exp, EReference ref) {
		var FunctionBlock functionBlock =  getFunctionBlock(exp);		      
	   	return Scopes::scopeFor(functionBlock.fault.properties);
	}
	
	def IScope scope_EventElement_value(EventElement exp, EReference ref) {
		var FunctionBlock functionBlock =  getFunctionBlock(exp);		      
	   	return Scopes::scopeFor(functionBlock.events);
	}
					
	private def FunctionBlock getFunctionBlock(FunctionBlockElement functionBlockElement) {
		var InformationModelElement informationModelElement = functionBlockElement.eContainer as InformationModelElement;
	    return informationModelElement.functionblockModel.functionblock;	
	}
}
