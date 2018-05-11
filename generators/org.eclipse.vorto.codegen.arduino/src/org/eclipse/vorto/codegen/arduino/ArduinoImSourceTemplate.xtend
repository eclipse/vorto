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
		return "infomodel_" + model.name + ".cpp";
	}
	
	override getPath(InformationModel model) {
		return model.name + "App";
	}
	
	override getContent(InformationModel model, InvocationContext context) {
		'''
		// infomodel_«model.name»
		
		#include "infomodel_«model.name».h"
		
		infomodel_«model.name»::infomodel_«model.name»() 
		{
		}
		
		String infomodel_«model.name»::serialize() {}
		'''
	}
	
}
