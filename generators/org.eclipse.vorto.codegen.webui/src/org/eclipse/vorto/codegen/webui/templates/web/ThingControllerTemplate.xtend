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

class ThingControllerTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''«context.name»Controller.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/web'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».web;
		
		import java.util.List;
		import java.util.concurrent.ExecutionException;
		
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.http.HttpStatus;
		import org.springframework.http.MediaType;
		import org.springframework.web.bind.annotation.ExceptionHandler;
		import org.springframework.web.bind.annotation.PathVariable;
		import org.springframework.web.bind.annotation.RequestMapping;
		import org.springframework.web.bind.annotation.RequestMethod;
		import org.springframework.web.bind.annotation.ResponseStatus;
		import org.springframework.web.bind.annotation.RestController;
		
		import com.example.iot.«element.name.toLowerCase».model.«element.name»;
		import com.example.iot.«element.name.toLowerCase».service.DataService;
		import com.example.iot.«element.name.toLowerCase».service.Query;
		
		@RestController
		@RequestMapping("/rest/devices")
		public class «element.name»Controller {
		
			@Autowired
			private DataService dataService;
							
			@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
			public List<«element.name»> searchApplewatchThings() throws ExecutionException, InterruptedException  {		
				Query query = dataService.newQuery();
				return dataService.queryThings(query);
			}
			
			@RequestMapping(value = "/{thingId:.+}", produces = {
					MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
			public «element.name» get«element.name»Thing(@PathVariable("thingId") final String thingId) throws ExecutionException, InterruptedException {
				return dataService.getThing(thingId);
			}
				
			@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason = "Problem accessing Backend IoT Cloud")
			@ExceptionHandler(ExecutionException.class)
			public void executionError(final ExecutionException ex){
				// handle this error 
			}
					
			@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason = "Problem accessing Backend IoT Cloud")
			@ExceptionHandler(InterruptedException.class)
			public void interruptedError(final InterruptedException ex){
				// handle this error
			}
		}
		'''
	}
	
}
