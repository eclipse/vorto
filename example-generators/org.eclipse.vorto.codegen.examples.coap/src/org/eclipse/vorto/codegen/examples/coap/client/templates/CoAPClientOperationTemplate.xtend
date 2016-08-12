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
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.Param
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType

class CoAPClientOperationTemplate implements ITemplate<Operation>{
	
	var String primitiveTypeWrapper_suffix;
	var String paramSet_suffix;
	var ITemplate<Param> parameterTemplate
	
	new(
		String primitiveTypeWrapper_suffix,
		String paramSet_suffix, 
		ITemplate<Param> parameterTemplate
	) {
		this.primitiveTypeWrapper_suffix = primitiveTypeWrapper_suffix;
		this.paramSet_suffix = paramSet_suffix;
		this.parameterTemplate = parameterTemplate;
	}
	
	override getContent(Operation op,InvocationContext invocationContext) {
		'''
			/**
			* «op.description»
			*/
			«IF (op.returnType instanceof ReturnObjectType || op.returnType == null)»
			public «CoAPUtils.getReturnTypeAsString(op)» «op.name»(«getParameterString(op,invocationContext)») {
			«ELSE»
			public «CoAPUtils.getReturnTypeAsString(op)» «op.name»(«getParameterString(op,invocationContext)») throws Exception {
			«ENDIF»
			
				«IF (!op.params.empty)»
				«op.name.toFirstUpper»«paramSet_suffix» paramSet = new «op.name.toFirstUpper»ParamSet();
				«FOR param : op.params»
					paramSet.set«param.name.toFirstUpper»(«param.name»);
				«ENDFOR»
			
				byte[] payload = Client.serializePayload(paramSet);
				«IF op.returnType != null»
					final Response response = Client.sendRequest(BASE_URI + "/" + "«op.name.toFirstLower»", CoapMethod.POST, payload);
				«ELSE»
					Client.sendRequest(BASE_URI + "/" + "«op.name.toFirstLower»", CoapMethod.POST, payload);
				«ENDIF»
				«ELSE»
				«IF op.returnType != null»
					final Response response = Client.sendRequest(BASE_URI + "/" + "«op.name.toFirstLower»", CoapMethod.POST);
				«ELSE»
					Client.sendRequest(BASE_URI + "/" + "«op.name.toFirstLower»", CoapMethod.POST, payload);
				«ENDIF»
				«ENDIF»
			
				«IF op.returnType != null»
				if(response != null) {
					if(response.getCode().value < 100) {
						if(response.getPayloadSize() > 0) {
							«IF (op.returnType instanceof ReturnObjectType) »
								«var String returnType = CoAPUtils.getReturnTypeAsString(op)»
								final Object o = Client.deserializePayload(«returnType».class, response.getPayload());
								if(o instanceof «returnType»){
									return («returnType») o;
								}
							«ELSE»
								final Object o = Client.deserializePayload(«op.name.toFirstUpper»«primitiveTypeWrapper_suffix».class, response.getPayload());
								if(o instanceof «op.name.toFirstUpper»«primitiveTypeWrapper_suffix»){
									final «op.name.toFirstUpper»«primitiveTypeWrapper_suffix» result = («op.name.toFirstUpper»«primitiveTypeWrapper_suffix») o;
									return result.getValue();
								}
							«ENDIF»
						}
					}
				}
				«IF op.returnType instanceof ReturnObjectType»
					return null;
				«ELSE»
					throw new Exception("Request failed!");
				«ENDIF»
				«ENDIF»
			}
		'''
	}
	
	def String getParameterString(Operation op,InvocationContext invocationContext) {
		var String result="";
		for (param : op.params) {
			result =  result + ", " + parameterTemplate.getContent(param,invocationContext);
		}
		if (result.isNullOrEmpty) {
			return "";
		}
		else {
			return result.substring(2, result.length).replaceAll("\n", "").replaceAll("\r", "");
		}
	}

	

}