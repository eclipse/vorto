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
package org.eclipse.vorto.codegen.webui.templates.web

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ThingMessageControllerTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''«context.name»MessageController.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/web'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».web;
		
		import org.slf4j.Logger;
		import org.slf4j.LoggerFactory;
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.messaging.handler.annotation.MessageMapping;
		import org.springframework.messaging.simp.SimpMessagingTemplate;
		import org.springframework.stereotype.Controller;
		
		import com.example.iot.«element.name.toLowerCase».config.WebSocketConfig;
		«IF context.configurationProperties.getOrDefault("persistence","false").equalsIgnoreCase("true")»
		import com.example.iot.«element.name.toLowerCase».dao.«element.name»Repository;
		«ENDIF»
		import com.example.iot.«element.name.toLowerCase».model.«element.name»;
		import com.example.iot.«element.name.toLowerCase».service.DataService;
		import com.example.iot.«element.name.toLowerCase».service.DataService.DataCallback;
		
		@Controller
		public class «element.name»MessageController {
					
			private Logger log = LoggerFactory.getLogger(«element.name»MessageController.class);
			
			@Autowired
			private SimpMessagingTemplate messagingTemplate;
				
			@Autowired
			private DataService dataService;
			
			«IF context.configurationProperties.getOrDefault("persistence","false").equalsIgnoreCase("true")»
			@Autowired
			private «element.name»Repository crudRepository;
			«ENDIF»
				
			@MessageMapping(WebSocketConfig.ENDPOINT+"/subscribe")
			public String subscribe(String thingId) throws Exception {
				try {
					log.info("Subscribing for thingId : " + thingId);
				
					final SimpMessagingTemplate tmp = messagingTemplate;
					dataService.registerAsyncCallback(thingId, new DataCallback() {
							
						@Override
						public void onChange(«element.name» thing) {
							«IF context.configurationProperties.getOrDefault("persistence","false").equalsIgnoreCase("true")»
							crudRepository.save(thing);
							«ENDIF»
							tmp.convertAndSend( "/topic/device/" + thingId, thing);	
						}
					});
				} catch (Throwable e) {
					log.error("Error while subcribing to device", e);
				}
				
				return thingId;
			}
				
			@MessageMapping(WebSocketConfig.ENDPOINT+"/unsubscribe")
			public String unsubscribe(String thingId) throws Exception {
				
				try {
					log.info("Unsubscribing for thingId : " + thingId);
					dataService.deregisterAsyncCallback(thingId);
				} catch (Throwable e) {
					log.error("Error while unsubcribing to thing", e);
				}
				
				return thingId;
			}
		}
		'''
	}
	
}
