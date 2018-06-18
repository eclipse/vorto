/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.codegen.webui.templates.config

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class WebSocketConfigTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''WebSocketConfig.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/config'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».config;
		
		import org.springframework.context.annotation.Configuration;
		import org.springframework.messaging.simp.config.MessageBrokerRegistry;
		import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
		import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
		import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
		
		@Configuration
		@EnableWebSocketMessageBroker
		public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
		
			public static final String ENDPOINT = "/«element.name.toLowerCase»endpoint";
			
			@Override
			public void registerStompEndpoints(StompEndpointRegistry registry)  {
				registry.addEndpoint(ENDPOINT).withSockJS();
			}
		
			@Override
			public void configureMessageBroker(MessageBrokerRegistry registry) {
				registry.setApplicationDestinationPrefixes("/«element.name.toLowerCase»");
				registry.enableSimpleBroker("/topic/device");
			}
		
		}
		'''
	}
}