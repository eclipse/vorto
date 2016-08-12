/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.webui.tasks.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class DeviceInfoServiceTemplate implements IFileTemplate<InformationModel> {
	
	override getContent(InformationModel model,InvocationContext invocationContext) {
		'''
		package webdevice.example.web;
		
		import webdevice.example.model.DeviceInfo;
		import org.springframework.web.bind.annotation.RequestMapping;
		import org.springframework.web.bind.annotation.RequestMethod;
		import org.springframework.web.bind.annotation.RestController;
		
		@RequestMapping("/rest/deviceInfo")
		@RestController
		public class DeviceInfoService {
			
			private DeviceInfo infomodelInstance = new DeviceInfo();
				
			@RequestMapping(method = RequestMethod.GET)
			public DeviceInfo getInstance(){
				return infomodelInstance;
			}	
		}
		'''
	}
	
	override getFileName(InformationModel context) {
		return "DeviceInfoService.java";
	}
	
	override getPath(InformationModel context) {
		return "webdevice.example/src/main/java/webdevice/example/web"
	}
	
}
