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

import org.eclipse.emf.common.util.EList
import org.eclipse.emf.common.util.EMap
import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.examples.coap.CoAPUtils
import org.eclipse.vorto.codegen.utils.Utils
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class CoAPServerIMRequestHandlerTemplate implements ITemplate<InformationModel> {
	var String classPackage;
	var String className;
	var String interfaceName;
	var String interfacePrefix;
	var String[] imports;
	var String primitiveTypeWrapper_suffix;
	
	new(
			String classPackage,
			String className,
			String interfaceName,
			String interfacePrefix,
			String primitiveTypeWrapper_suffix,
			String[] imports
	) {
		this.className = className;
		this.classPackage = classPackage;
		this.interfaceName = interfaceName;
		this.interfacePrefix = interfacePrefix
		this.primitiveTypeWrapper_suffix = primitiveTypeWrapper_suffix;
		this. imports = imports;		
	}
	
	override getContent(InformationModel im,InvocationContext invocationContext) {
		'''
			package «classPackage»;
			
			import org.eclipse.californium.core.coap.CoAP;
			import org.eclipse.californium.core.coap.CoAP.Code;
			import org.eclipse.californium.core.server.resources.CoapExchange;
			«FOR imprt: imports»
				«IF imprt != null»
					import «imprt».*;
				«ENDIF»
			«ENDFOR»
			
			public class «className» implements «interfaceName» {
				
				private final int contentType = 50;
				private «interfacePrefix»«im.name.toFirstUpper» «im.name.toFirstLower»;
				private JsonTransformer transformer;
				
				public «className»() {
					«im.name.toFirstLower» = new «im.name.toFirstUpper»();
					transformer = new JsonTransformer();
				}
				
				public «className»(«interfacePrefix»«im.name.toFirstUpper» «im.name.toFirstLower») {
					this.«im.name.toFirstLower» = «im.name.toFirstLower»;
					transformer = new JsonTransformer();
				}
				
				public void onRequest(CoapExchange exchange, String uri) throws Exception {
					String fbService = URIAnalyzer.getService(uri);
					String fbInstance = URIAnalyzer.getInstance(uri);
					String fbOperationName = URIAnalyzer.getOperation(uri);
					
					«val EMap<FunctionblockModel, EList<FunctionblockProperty>> map = CoAPUtils.sortByPropertyType(im.properties)»
					
					«FOR resource : map.keySet»
						if (fbService.equalsIgnoreCase("«resource.name»")) {
							«var EList<FunctionblockProperty> instances = map.get(resource)»
							«FOR instance : instances»
								if (fbInstance.equalsIgnoreCase("«instance.name»")) {
									«IF instance.type.functionblock.operations!=null»
										«FOR operation: instance.type.functionblock.operations»
											if(exchange.getRequestCode() == Code.POST) {
												if (fbOperationName.equals("«operation.name.toFirstLower»")) {
													«IF (!operation.params.empty)»
														«operation.name.toFirstUpper»ParamSet paramSet = null;

														if(exchange.getRequestPayload().length > 0) {
															final Object o = transformer.deserialize(«operation.name.toFirstUpper»ParamSet.class, exchange.getRequestPayload());
															if(o instanceof «operation.name.toFirstUpper»ParamSet) {
																paramSet = («operation.name.toFirstUpper»ParamSet) o;
															}
														}
													«ENDIF»
													«IF (operation.returnType != null)»
														«IF (operation.returnType instanceof ReturnPrimitiveType)»
															final «operation.name.toFirstUpper»«primitiveTypeWrapper_suffix» result = new «operation.name.toFirstUpper»«primitiveTypeWrapper_suffix»();
															result.setValue(«im.name.toFirstLower».get«instance.name.toFirstUpper»().«operation.name.toFirstLower»(«this.operationParams(operation)»));
														«ELSE»
															«CoAPUtils.getReturnTypeAsString(operation)» result = «im.name.toFirstLower».get«instance.name.toFirstUpper»().«operation.name.toFirstLower»(«this.operationParams(operation)»);
														«ENDIF»
														exchange.respond(
														CoAP.ResponseCode.CHANGED,
														transformer.serialize(result),
														contentType);
													«ELSE»
														exchange.respond(CoAP.ResponseCode.CHANGED);
													«ENDIF»
												}
											}
										«ENDFOR»
									«ENDIF»
									«IF instance.type.functionblock.status!=null»
										«FOR status : instance.type.functionblock.status.properties»
											if (fbOperationName.equals("«status.name.toFirstLower»")) {
												«IF Utils.isReadable(status)»
													if(exchange.getRequestCode() == Code.GET && exchange.getRequestOptions().getObserve() == null){
														exchange.respond( 
															CoAP.ResponseCode.CONTENT, 
															transformer.serialize(«im.name.toFirstLower».get«instance.name.toFirstUpper»().get«status.name.toFirstUpper»()), 
															contentType);
														return;
													}
												«ENDIF»
												«IF Utils.isWritable(status)»
													if(exchange.getRequestCode() == Code.PUT) {
														if(exchange.getRequestPayload().length > 0) {
															final Object o = transformer.deserialize(«status.name.toFirstUpper».class, exchange.getRequestPayload());
															if(o instanceof «status.name.toFirstUpper») {
																«im.name.toFirstLower».get«instance.name.toFirstUpper»().set«status.name.toFirstUpper»((«status.name.toFirstUpper») o);
																exchange.respond(CoAP.ResponseCode.CHANGED);
															} else {
																exchange.respond(CoAP.ResponseCode.BAD_REQUEST);
															}
														} else {
															exchange.respond(CoAP.ResponseCode.BAD_REQUEST);
														}
													}
												«ENDIF»
											}
										«ENDFOR»
									«ENDIF»
								}
							«ENDFOR»
						}
					«ENDFOR»
				}
			}
		'''
	}
	
	def private String operationParams(Operation op){
		var result = "";
		
		if ((op.params != null)&&(op.params.size>0)){
			for (param : op.params){
				result += "paramSet.get" + param.name.toFirstUpper + "(),\n";
			
			}
			return result.substring(0,result.length - 2);
		} else {
			return "";
		}
	}	
}