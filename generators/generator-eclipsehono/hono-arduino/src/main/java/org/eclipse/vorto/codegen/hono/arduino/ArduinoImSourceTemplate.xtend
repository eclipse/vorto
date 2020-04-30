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
package org.eclipse.vorto.codegen.hono.arduino

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.plugin.generator.InvocationContext

class ArduinoImSourceTemplate extends ArduinoTemplate<InformationModel> {
	
	override getFileName(InformationModel model) {
		return model.name + ".cpp";
	}
	
	override getPath(InformationModel model) {
		return model.name + "App/src/model/infomodel";
	}
	
	override getContent(InformationModel model, InvocationContext context) {
		'''
		// «model.name»
		
		#include "«model.name».h"
		
		using namespace «model.namespace.replace(".","_")»;
		
		«model.name»::«model.name»() {}
		
		String «model.name»::serialize() {}
		'''
	}
	
}
