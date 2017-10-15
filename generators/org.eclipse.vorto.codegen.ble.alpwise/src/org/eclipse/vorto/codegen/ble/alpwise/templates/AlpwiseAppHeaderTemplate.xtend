/*******************************************************************************
 *  Copyright (c) 2017 Oliver Meili
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
 *  Oliver Meili <omi@ieee.org>
 *******************************************************************************/
package org.eclipse.vorto.codegen.ble.alpwise.templates

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class AlpwiseAppHeaderTemplate extends AlpwiseTemplate<InformationModel> {
	
	override getFileName(InformationModel model) {
		return "BleApp.h";
	}
	
	override getPath(InformationModel model) {
		return rootPath
	}
	
	override getContent(InformationModel model, InvocationContext context) {
'''
/* BLE Application Template generated by Vorto */

void BleApp_Init(void);

void BleApp_Deinit(void);
'''
	}		
}