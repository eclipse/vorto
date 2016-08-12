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
package org.eclipse.vorto.codegen.examples.coap.client.templates

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.examples.coap.CoAPUtils
import org.eclipse.vorto.codegen.utils.Utils
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.Property

class CoAPClientPropertyTemplate implements ITemplate<Property> {
	
	var String primTypeSuffix;
	var String paramSetSuffx;
	
	new (String primTypeSuffix,
		String paramSetSuffx
	){
		this.primTypeSuffix = primTypeSuffix;
		this.paramSetSuffx = paramSetSuffx;

	}
	
	override getContent(Property property,InvocationContext invocationContext) {
		'''
		/**
		* «property.description»
		*/
		«IF Utils.isReadable(property)»
		«IF (property.type instanceof ObjectPropertyType) »
			public «CoAPUtils.getPropertyTypeAsString(property)» get«property.name.toFirstUpper»(){
		«ELSE»
			public «CoAPUtils.getPropertyTypeAsString(property)» get«property.name.toFirstUpper»() throws Exception {
		«ENDIF»
			final Response response = Client.sendRequest(BASE_URI + "/" + "«property.name.toFirstLower»", CoapMethod.GET);
			
			if(Client.isValidResponseWithPayload(response)) {
				«IF (property.type instanceof ObjectPropertyType)»
					final Object o = Client.deserializePayload(«CoAPUtils.getPropertyTypeAsString(property)».class, response.getPayload());
					if(o instanceof «CoAPUtils.getPropertyTypeAsString(property)»){
						return («CoAPUtils.getPropertyTypeAsString(property)») o;
					}
				«ELSE»
					final Object o = Client.deserializePayload(«property.name.toFirstUpper»«primTypeSuffix».class, response.getPayload());
					if(o instanceof «property.name.toFirstUpper»«primTypeSuffix»){
						final «property.name.toFirstUpper»«primTypeSuffix» result = («property.name.toFirstUpper»«primTypeSuffix») o;
						return result.getValue();
					}
				«ENDIF»
			}
			«IF (property.type instanceof ObjectPropertyType) »
				return null;
			«ELSE»
				throw new Exception("Request Failed!");
			«ENDIF»
		}
		«ENDIF»
		«IF Utils.isWritable(property)»
		
		public void set«property.name.toFirstUpper»(«CoAPUtils.getPropertyTypeAsString(property)» «property.name.toFirstLower»){
			«IF (property.type instanceof PrimitivePropertyType)»
				«property.name.toFirstUpper»«paramSetSuffx» paramSet = new «property.name.toFirstUpper»«paramSetSuffx»();
				paramSet.setValue(«property.name.toFirstLower»);
				byte[] payload = Client.serializePayload(paramSet);
			«ELSE»
				byte[] payload = Client.serializePayload(«property.name.toFirstLower»);
			«ENDIF»
			Client.sendRequest(BASE_URI + "/" + "«property.name.toFirstLower»", CoapMethod.PUT, payload);
		}
		«ENDIF»
		
		'''
	}
}