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
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class CoAPServerDemoAppTemplate implements IFileTemplate<InformationModel> {
	
	var String targetPath;
	var String classPackage;
	var String reqHndlImport;
	var String serverImport;
	
	new(String targetPath, 
		String classPackage,
		String reqHndlImport,
		String serverImport
	) {
		this.targetPath = targetPath;
		this.classPackage = classPackage;
		this.reqHndlImport = reqHndlImport;
		this.serverImport = serverImport;
	}
	
	override getFileName(InformationModel context) {
		return "ServerDemoApp.java";
	}
	
	override getPath(InformationModel context) {
		return targetPath;
	}
	
	override getContent(InformationModel context, InvocationContext invocationContext) {
		'''
			package «classPackage»;
			
			import «reqHndlImport».*;
			import «serverImport».*;
			
			public class ServerDemoApp {
				
				public static void main (String[] args) {
					
					CoAPRequestHandler handler = new CoAPRequestHandler();
					Server server = new Server(handler);
					
					server.start();
					
					System.out.println("CoAP-Server started ...");
					System.out.println("Now browse to 'coap://localhost:5683/' to discover your server!");
					
				}
			}
		'''
	}
}
