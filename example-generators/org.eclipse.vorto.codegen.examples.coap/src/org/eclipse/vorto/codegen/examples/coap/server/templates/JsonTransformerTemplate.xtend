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

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class JsonTransformerTemplate implements IFileTemplate<InformationModel> {
	
	var String targetPath;
	var String classPackage;
	
	new(String targetPath,
		String classPackage
	) {
		this.targetPath = targetPath;
		this.classPackage = classPackage;
	}
	
	override getFileName(InformationModel context) {
		return "JsonTransformer.java";
	}
	
	override getPath(InformationModel context) {
		return targetPath;
	}
	
	override getContent(InformationModel context,InvocationContext invocationContext) {
		'''
			package «classPackage»;

			import com.fasterxml.jackson.core.JsonProcessingException;
			import com.fasterxml.jackson.databind.ObjectMapper;
			
			import java.io.IOException;
			
			/**
			 * This implements an Transformer for JSON (Java Simple Object Notation) Created by laj7rng on 15.12.15.
			 *
			 */
			public class JsonTransformer {
			
			    ObjectMapper mapper;
			
			    public JsonTransformer() {
			        mapper = new ObjectMapper();
			    }
			
			    public byte[] serialize(Object value) {
			
			        byte[] serialized = null;
			
			        try {
			            serialized = mapper.writeValueAsBytes(value);
			        } catch (JsonProcessingException e) {
			            e.printStackTrace();
			        }
			
			        return serialized;
			    }
			
			    public Object deserialize(Class<?> valueType, byte[] src) {
			
			        Object value = null;
			
			        try {
			            value = mapper.readValue(src, valueType);
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			
			        return value;
			    }
			}
		'''
	}
}
