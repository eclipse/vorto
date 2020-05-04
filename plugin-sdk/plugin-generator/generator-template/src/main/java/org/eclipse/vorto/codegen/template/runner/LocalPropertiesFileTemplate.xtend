/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.codegen.template.runner

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class LocalPropertiesFileTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'application-local.yml'
	}
	
	override getPath(InformationModel context) {
		'generator-parent/generator-runner/src/main/resources'
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		server:
		  host: localhost
		  port: 8081
		  contextPath: /generatorgateway
		  serviceUrl: http://localhost:8081/generatorgateway
		  config:
		    generatorUser:
		    generatorPassword:
		
		vorto:
		  serverUrl: http://localhost:8080/infomodelrepository
		  tenantId: default
		'''
	}
	
}