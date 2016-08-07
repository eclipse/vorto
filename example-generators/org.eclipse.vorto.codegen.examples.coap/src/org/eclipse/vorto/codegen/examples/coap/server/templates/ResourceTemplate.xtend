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

class ResourceTemplate implements IFileTemplate<InformationModel> {
	
	var String targetPath;
	var String classPackage;
	var String imports;
	
	new(String targetPath,
		String classPackage, 
		String imports
	) {
		this.targetPath = targetPath;
		this.classPackage = classPackage;
		this.imports = imports;
	}
	
	override getFileName(InformationModel context) {
		return "Resource.java";
	}
	
	override getPath(InformationModel context) {
		return targetPath;
	}
	
	override getContent(InformationModel context,InvocationContext invocationContext) {
		'''
			package «classPackage»;


			import org.eclipse.californium.core.CoapResource;
			import org.eclipse.californium.core.coap.CoAP;
			import org.eclipse.californium.core.coap.LinkFormat;
			import org.eclipse.californium.core.network.Exchange;
			import org.eclipse.californium.core.server.resources.CoapExchange;
			import «imports».*;
			
			import java.util.Collection;
			
			import static org.eclipse.californium.core.coap.CoAP.ResponseCode.INTERNAL_SERVER_ERROR;
			import static org.eclipse.californium.core.coap.CoAP.ResponseCode.METHOD_NOT_ALLOWED;
			import static org.eclipse.californium.core.coap.CoAP.Type.NON;
			import static org.eclipse.californium.core.coap.MediaTypeRegistry.APPLICATION_LINK_FORMAT;
			
			public class Resource extends CoapResource {
			
			    private ICoAPRequestHandler handler;
			    private Configuration config;
			
			    public Resource(String name, Configuration config, ICoAPRequestHandler handler) {
			        super(name);
			
			        this.config = config;
			        this.handler = handler;
			
			        if (config.isObserve()) {
			            setObservable(true);
			            setObserveType(NON);
			        }
			    }
			
			    @Override
			    public void handleRequest(Exchange exchange) {
			        final CoapExchange coapExchange = new CoapExchange(exchange, this);
			        CoAP.Code code = exchange.getRequest().getCode();
			
			        switch (code) {
			            case GET:
			                if (config.isGet() || config.isObserve()) {
			                    forwardRequest(coapExchange);
			                } else if (config.isDiscover()) {
			                    discover(coapExchange);
			                } else {
			                    coapExchange.respond(METHOD_NOT_ALLOWED);
			                }
			                break;
			            case POST:
			                if (config.isPost()) {
			                    forwardRequest(coapExchange);
			                } else {
			                    coapExchange.respond(METHOD_NOT_ALLOWED);
			                }
			                break;
			            case PUT:
			                if (config.isPut()) {
			                    forwardRequest(coapExchange);
			                } else {
			                    coapExchange.respond(METHOD_NOT_ALLOWED);
			                }
			                break;
			            case DELETE:
			                if (config.isDelete()) {
			                    forwardRequest(coapExchange);
			                } else {
			                    coapExchange.respond(METHOD_NOT_ALLOWED);
			                }
			        }
			
			    }
			
			
			    private void forwardRequest(CoapExchange exchange) {
			        try {
			            handler.onRequest(exchange, this.getURI());
			        } catch (Exception e) {
			            LOGGER.severe(this.getClass().getName() + ": " + this.getName() + " Caused exception: " + e.toString());
			            exchange.respond(INTERNAL_SERVER_ERROR);
			        }
			    }
			
			    private void discover(CoapExchange exchange) {
			        final Collection<org.eclipse.californium.core.server.resources.Resource> children = this.getChildren();
			        StringBuilder sb = new StringBuilder();
			
			        for (org.eclipse.californium.core.server.resources.Resource child : children) {
			            sb.append(LinkFormat.serializeResource(child));
			        }
			        exchange.respond(CoAP.ResponseCode.CONTENT, sb.toString(), APPLICATION_LINK_FORMAT);
			    }
			
			
			    /**
			     * Configuration specifies which aspects of the SOME/IP Field are allowed (Get, Set, Event)
			     */
			    public static class Configuration {
			        private boolean get;
			        private boolean put;
			        private boolean post;
			        private boolean delete;
			        private boolean discover;
			        private boolean observe;
			
			        public Configuration(boolean get, boolean put, boolean post, boolean delete, boolean discover, boolean
			                observe) {
			            this.get = get;
			            this.put = put;
			            this.post = post;
			            this.delete = delete;
			            this.discover = discover;
			            this.observe = observe;
			        }
			
			        public boolean isGet() {
			            return get;
			        }
			
			        public boolean isPut() {
			            return put;
			        }
			
			        public boolean isPost() {
			            return post;
			        }
			
			        public boolean isDelete() {
			            return delete;
			        }
			
			        public boolean isDiscover() {
			            return discover;
			        }
			
			        public boolean isObserve() {
			            return observe;
			        }
			    }
			}
		'''
	}
}
