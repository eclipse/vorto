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

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ClientTemplate implements IFileTemplate<InformationModel> {
	
	var String targetPath;
	var String classPackage;
	
	new(String targetPath,
		String classPackage
	) {
		this.targetPath = targetPath;
		this.classPackage = classPackage;
	}
	
	override getFileName(InformationModel context) {
		return "Client.java";
	}
	
	override getPath(InformationModel context) {
		return targetPath;
	}
	
	override getContent(InformationModel context,InvocationContext invocationContext) {
		'''
			package «classPackage»;

			import com.fasterxml.jackson.core.JsonProcessingException;
			import com.fasterxml.jackson.databind.ObjectMapper;
			
			import org.eclipse.californium.core.CoapResponse;
			import org.eclipse.californium.core.coap.MediaTypeRegistry;
			import org.eclipse.californium.core.coap.Request;
			import org.eclipse.californium.core.coap.Response;
			
			import java.io.IOException;
			
			
			public class Client {
			
			    public static int ct = MediaTypeRegistry.APPLICATION_JSON;
			
			
			    public static Response sendRequest(String uri, CoapMethod method, byte[] payload){
			
			        final Request request = newRequest(method);
			        
			        if (request == null) {
			            // TODO: 02.02.16 define error code
			            return null;
			        }
			        
			        request.setURI(uri);
			        request.setPayload(payload);
			        request.getOptions().setContentFormat(ct);
			        request.send();
			
			        Response response;
			
			        try {
			            response = request.waitForResponse();
			        } catch (InterruptedException e) {
			            e.printStackTrace();
			            // TODO: 02.02.16 define error handling
			            return null;
			        }
			
			        return response;
			    }
			    
			    public static Response sendRequest(String uri, CoapMethod method){
			
			        final Request request = newRequest(method);
			        
			        if (request == null) {
			            // TODO: 02.02.16 define error code
			            return null;
			        }
			        
			        request.setURI(uri);
			        request.send();
			
			        Response response;
			
			        try {
			            response = request.waitForResponse();
			        } catch (InterruptedException e) {
			            e.printStackTrace();
			            // TODO: 02.02.16 define error handling
			            return null;
			        }
			
			        return response;
			    }
			
			    public static boolean isValidResponseWithPayload(Response response){
			        if(response != null) {
			            if (response.getCode().value < 100) {
			                if (response.getPayload() != null) {
			                    if(response.getPayloadSize() >0 ){
			                        return true;
			                    }
			                }
			            }
			        }
			        return false;
			    }
			
			    public static boolean isValidResponseWithPayload(CoapResponse response){
			        if(response != null) {
			            if (response.getCode().value < 100) {
			                if (response.getPayload() != null) {
			                    return true;
			                }
			            }
			        }
			        return false;
			    }
			
			    private static Request newRequest(CoapMethod method) {
			
			        switch (method) {
			
			            case GET:
			                return Request.newGet();
			            case POST:
			                return Request.newPost();
			            case PUT:
			                return Request.newPut();
			            case DELETE:
			                return Request.newDelete();
			            case OBSERVE:
			                Request request = Request.newGet();
			                request.setObserve();
			                return request;
			            case DISCOVER:
			                return Request.newGet();
			        }
			
			        return null;
			    }
			
			
			    /**
			     * Collection of available transformer to deserialize payload for CoAP
			     *
			     * @param valueType   POJO class
			     * @param payload     serialized data
			     * @return object representation of data
			     */
			    public static Object deserializePayload(Class<?> valueType, byte[] payload) {
			
			        ObjectMapper mapper = new ObjectMapper();
			        Object value = null;
			
			        try {
			            value = mapper.readValue(payload, valueType);
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			
			        return value;
			    }
			
			
			    /**
			     * Collection of available transformers to serialize payload for CoAP
			     *
			     * @param value       object representation of data
			     * @return serialized representation of data
			     */
			    public static byte[] serializePayload(Object value) {
			
			        ObjectMapper mapper = new ObjectMapper();
			        byte[] serialized = null;
			
			        try {
			            serialized = mapper.writeValueAsBytes(value);
			        } catch (JsonProcessingException e) {
			            e.printStackTrace();
			        }
			
			        return serialized;
			    }
			}
		'''
	}
}
