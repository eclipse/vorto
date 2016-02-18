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

package org.eclipse.vorto.codegen.service.bosch.common

import java.util.LinkedHashSet
import java.util.Set
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.Type
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.RefParam
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType

class FbModelWrapper {
	
	private FunctionblockModel model;
	
	new(FunctionblockModel model) {
		this.model = model;
	}
	
	protected new(){}
	
	def String getLowerCaseFunctionblockName() {
		return lowerCase(model.name);	
	}
	
	private def String getVersion(){
		model.version
	}
	
	def String getMajorVersion(){
		this.getVersion.substring(0,version.indexOf('.'))
	}

	def String getMajorMinorVersion(){
		this.getVersion.substring(0,version.lastIndexOf('.'))
	}
	
	def String getFullNamespace(){
		'''http://www.bosch.com/functionblock/«getCategory()»/«getUpperCaseFunctionblockName()»/«getMajorVersion()»'''	
	}
	
	private def String getCategory() {
		model.category
	}
	
	private def String getNamespace() {
		model.namespace
	}
	
	def getJavaPackageName() {
		'''«getGroupName()».«getLowercaseFBName()»'''
	}
	
	def getLowercaseFBName() {
		model.getName().toLowerCase
	}
	
	def getDriverClassName() {
		'''«groupName».driver.«model.name»Driver'''
	}
	
	def getDriverId() {
		'''«groupName».driver_«model.name.toLowerCase»driver.«majorMinorVersion»'''
	}
	
	def getGroupName(){
		'''«getNamespace()».functionblock.«getCategory.toLowerCase().replaceAll('/','.')»'''
	}
	
	def String getFunctionBlockName(){
		model.name
	}
	
	def String getUpperCaseFunctionblockName() {
		return capitalize(model.name);
	}
	
	public def String capitalize(String line) {
	 	return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
	
	public def String lowerCase(String line) {
	 	return Character.toLowerCase(line.charAt(0)) + line.substring(1);
	}
	
	def FunctionblockModel getModel() {
		return model;
	}
	
	def getTypes() {
		var allTypes = new LinkedHashSet();
		var iterator = model.eAllContents;
		while(iterator.hasNext) {
			var current = iterator.next;
			if (current instanceof RefParam) {
				addTypeAndReferences((current as RefParam).type,allTypes);
			} else if (current instanceof ReturnObjectType) {
				addTypeAndReferences((current as ReturnObjectType).returnType,allTypes);
			} else if (current instanceof ObjectPropertyType) {
				addTypeAndReferences((current as ObjectPropertyType).type,allTypes);
			} 
		}	
		return allTypes;
	}
	
	private def addTypeAndReferences(Type type, Set<Type> container) {
		if (!container.contains(type)) {
			container.add(type);
			var references = FbModelUtils.getTypesOfType(type,container);
			container.addAll(references);
		}	
	}
	

}
