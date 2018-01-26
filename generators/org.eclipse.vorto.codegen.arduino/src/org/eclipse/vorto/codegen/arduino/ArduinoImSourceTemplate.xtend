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
package org.eclipse.vorto.codegen.arduino

import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ArduinoImSourceTemplate extends ArduinoTemplate<InformationModel> {
	
	override getFileName(InformationModel model) {
		return model.namespace.replace(".", "_") + "_" + model.name + ".cpp";
	}
	
	override getPath(InformationModel model) {
		return model.name;
	}
	
	override getContent(InformationModel model, InvocationContext context) {
		'''
		// «model.namespace.replace(".", "_")»_«model.name»
		
		#include "«model.namespace.replace(".", "_")»_«model.name».h"
		
		«model.namespace.replace(".", "_")»_«model.name»::«model.namespace.replace(".", "_")»_«model.name»() 
		{
		}
		
		String «model.namespace.replace(".", "_")»_«model.name»::serialize()
		{
            String result = "{";
			«var counter = 0»
            «FOR fb : model.properties»
            	«var c = counter++»
                result += "\"«fb.name»\": { ";
                result += «fb.name».serialize();
                result += " }«IF c < model.properties.length-1»,«ENDIF»";
            «ENDFOR»
		
            result += "}"; 
		
            return result;
        }
		'''
	}
	
}