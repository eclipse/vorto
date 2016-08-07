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
package org.eclipse.vorto.codegen.examples.coap.server.templates

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.utils.Utils
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.Property

class CoAPServerPropertyTemplate implements ITemplate<Property> {
	
	new (){
	}
	
	override getContent(Property property,InvocationContext invocationContext) {
		'''
		/**
		* «property.description»
		*/
		private «Utils.getPropertyType(property)» «property.name.toFirstLower»;
		
		«IF Utils.isReadable(property)»
			«IF (property.type instanceof ObjectPropertyType) »
				public «Utils.getPropertyType(property)» get«property.name.toFirstUpper»(){
			«ELSE»
				public «Utils.getPropertyType(property)» get«property.name.toFirstUpper»() throws Exception {
			«ENDIF»
				return «property.name.toFirstLower»;
			}

		«ENDIF»
		
		«IF Utils.isWritable(property)»
			public void set«property.name.toFirstUpper»(«Utils.getPropertyType(property)» «property.name.toFirstLower»){
				this.«property.name.toFirstLower» = «property.name.toFirstLower»;
			}

		«ENDIF»
		«IF Utils.isEventable(property)»
			public boolean subscribe«property.name.toFirstUpper»(){
				
				// Add your code here ...
				
				return false;
			}
			
			public boolean unsubscribe«property.name.toFirstUpper»(){
				
				// Add your code here ...
				
				return false;
			}
		«ENDIF»
		'''
	}
}