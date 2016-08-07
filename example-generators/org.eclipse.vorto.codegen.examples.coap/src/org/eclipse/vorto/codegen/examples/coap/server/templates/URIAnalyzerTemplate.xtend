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

class URIAnalyzerTemplate implements IFileTemplate<InformationModel> {
	
	var String targetPath;
	var String classPackage;
	
	new(String targetPath,
		String classPackage
	) {
		this.targetPath = targetPath;
		this.classPackage = classPackage;
	}
	
	override getFileName(InformationModel context) {
		return "URIAnalyzer.java";
	}
	
	override getPath(InformationModel context) {
		return targetPath;
	}
	
	override getContent(InformationModel context,InvocationContext invocationContext) {
		'''
			package «classPackage»;

			public class URIAnalyzer {
				
				public static String getService(String uri) {
			        String tokens[] = uri.split("/");
			        if (tokens.length > 2) {
			            return tokens[tokens.length - 3];
			        } else {
			            return "";
			        }
			    }
				
			    public static String getInstance(String uri) {
			        String tokens[] = uri.split("/");
			        if (tokens.length > 1) {
			            return tokens[tokens.length - 2];
			        } else {
			            return "";
			        }
			    }
			
			    public static String getOperation(String uri) {
			        String tokens[] = uri.split("/");
			        if (tokens.length > 0) {
			            return tokens[tokens.length - 1];
			        } else {
			            return "";
			        }
			    }
			}
		'''
	}
}
