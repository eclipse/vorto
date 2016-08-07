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

class LinkTemplate implements IFileTemplate<InformationModel> {
	
	var String targetPath;
	var String classPackage;
	
	new(String targetPath,
		String classPackage
	) {
		this.targetPath = targetPath;
		this.classPackage = classPackage;
	}
	
	override getFileName(InformationModel context) {
		return "Link.java";
	}
	
	override getPath(InformationModel context) {
		return targetPath;
	}
	
	override getContent(InformationModel context,InvocationContext invocationContext) {
		'''
			package «classPackage»;

			import org.eclipse.californium.core.coap.CoAP;
			import org.eclipse.californium.core.coap.LinkFormat;
			import org.eclipse.californium.core.coap.MediaTypeRegistry;
			import org.eclipse.californium.core.server.resources.CoapExchange;
			import org.eclipse.californium.core.server.resources.ConcurrentCoapResource;
			import org.eclipse.californium.core.server.resources.Resource;
			
			import java.util.Collection;
			
			import static org.eclipse.californium.core.coap.MediaTypeRegistry.APPLICATION_LINK_FORMAT;
			
			public class Link extends ConcurrentCoapResource {
			
			    public Link(String name) {
			        super(name, SINGLE_THREADED);
			        getAttributes().addContentType(MediaTypeRegistry.APPLICATION_LINK_FORMAT);
			
			    }
			
			    @Override
			    public void handleGET(CoapExchange exchange) {
			        final Collection<Resource> children = this.getChildren();
			        StringBuilder sb = new StringBuilder();
			
			        for (Resource child : children) {
			            sb.append(LinkFormat.serializeResource(child));
			        }
			        exchange.respond(CoAP.ResponseCode.CONTENT, sb.toString(), APPLICATION_LINK_FORMAT);
			    }
			}
		'''
	}
}
