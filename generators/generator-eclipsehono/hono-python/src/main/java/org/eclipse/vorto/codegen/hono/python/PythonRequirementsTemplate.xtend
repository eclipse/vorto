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
package org.eclipse.vorto.codegen.hono.python

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.IFileTemplate

class PythonRequirementsTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''requirements.txt'''
	}
	
	override getPath(InformationModel context) {
		return ""
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		netifaces
		paho-mqtt
		'''
	}
	
}