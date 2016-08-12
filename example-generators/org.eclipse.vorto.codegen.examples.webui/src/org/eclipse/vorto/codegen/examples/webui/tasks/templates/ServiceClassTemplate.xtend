/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
 package org.eclipse.vorto.codegen.examples.webui.tasks.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.codegen.api.InvocationContext

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class ServiceClassTemplate implements IFileTemplate<FunctionblockModel> {
			
	override getFileName(FunctionblockModel context) {
		return context.name+"Service.java"
	}
	
	override getPath(FunctionblockModel context) {
		return "webdevice.example/src/main/java/webdevice/example/web"
	}
	
	override getContent(FunctionblockModel context,InvocationContext invocationContext) {
		'''
		package webdevice.example.web;
		
		import java.lang.reflect.InvocationTargetException;
		import java.util.logging.Logger;
		
		import org.springframework.web.bind.annotation.RequestMapping;
		import org.springframework.web.bind.annotation.RequestMethod;
		import org.springframework.web.bind.annotation.RestController;
		
		import model.functionblocks.*;
		
		@RequestMapping("/rest/«context.name.toLowerCase»")
		@RestController
		public class «context.name»Service {	
			
			private static Logger logger = Logger.getLogger("«context.name»"); 		
			private static «context.name»Impl «context.name.toLowerCase»instance = new «context.name»Impl(); 
		
			@RequestMapping(value = "/instance", method = RequestMethod.GET)
			public «context.name»Impl getInstance(){
				return «context.name.toLowerCase»instance;
			}		
			
			«FOR operation : context.functionblock.operations»
			@RequestMapping(value = "/«operation.name»", method = RequestMethod.PUT)
			public void «operation.name»() {
				//Please handle your operation here
				logger.info("«operation.name» invoked");				
			}
			«ENDFOR»
			
			«IF context.functionblock.configuration != null»
			@RequestMapping(value = "/saveConfiguration", method = RequestMethod.PUT)
			public void saveConfiguration(«context.name»Configuration configurationData)
					throws IllegalAccessException, InvocationTargetException {
				logger.info("saveConfiguration invoked: " + configurationData);		
				«context.name.toLowerCase»instance.set«context.name»Configuration(configurationData);
			}
			«ENDIF»
		}
		'''
	}
	
}
