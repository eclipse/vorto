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

class ICoAPRequestHandlerTemplate implements IFileTemplate<InformationModel> {
	
	var String targetPath;
	var String classPackage;
	
	new(String targetPath,
		String classPackage
	) {
		this.targetPath = targetPath;
		this.classPackage = classPackage;
	}
	
	override getFileName(InformationModel context) {
		return "ICoAPRequestHandler.java";
	}
	
	override getPath(InformationModel context) {
		return targetPath;
	}
	
	override getContent(InformationModel context,InvocationContext invocationContext) {
		'''
			package «classPackage»;

			import org.eclipse.californium.core.server.resources.CoapExchange;
			
			public interface ICoAPRequestHandler {
			
			    /**
			     * This operation reacts on a request which was received by a corresponding
			     * CoAP server.
			     *
			     * @param exchange see {@link CoapExchange}
			     * @param uri      identifies the requested resource (uniform resource identifier)
			     * @throws Exception
			     */
			    void onRequest(CoapExchange exchange, String uri) throws Exception;
			}
		'''
	}
}
