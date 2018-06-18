/*******************************************************************************
 *  Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *******************************************************************************/
package org.eclipse.vorto.codegen.arduino

import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ArduinoImSourceTemplate extends org.eclipse.vorto.codegen.arduino.ArduinoTemplate<InformationModel> {
	
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
