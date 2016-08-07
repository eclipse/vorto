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

class CoAPMethodTemplate implements IFileTemplate<InformationModel> {
	
	var String targetPath;
	var String classPackage;
	
	new(String targetPath,
		String classPackage
	) {
		this.targetPath = targetPath;
		this.classPackage = classPackage;
	}
	
	override getFileName(InformationModel context) {
		return "CoapMethod.java";
	}
	
	override getPath(InformationModel context) {
		return targetPath;
	}
	
	override getContent(InformationModel context,InvocationContext invocationContext) {
		'''
			package «classPackage»;

			public enum CoapMethod {
			    GET(1),
			    POST(2),
			    PUT(3),
			    DELETE(4),
			    OBSERVE(5),
			    DISCOVER(6);
			
			    public final int value;
			
			    CoapMethod(int value) {
			        this.value = value;
			    }
			
			    public static CoapMethod valueOf(int value) {
			        switch (value) {
			            case 1:
			                return GET;
			            case 2:
			                return POST;
			            case 3:
			                return PUT;
			            case 4:
			                return DELETE;
			            case 5:
			                return OBSERVE;
			            case 6:
			                return DISCOVER;
			            default:
			                throw new IllegalArgumentException("Unknwon Method value " + value);
			        }
			    }
			}
			
		'''
	}
}
