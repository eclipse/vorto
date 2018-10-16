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
package org.eclipse.vorto.codegen.hono.arduino

import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ArduinoImHeaderTemplate extends org.eclipse.vorto.codegen.hono.arduino.ArduinoTemplate<InformationModel> {
	
	override getFileName(InformationModel model) {
		return model.name + ".h";
	}
	
	override getPath(InformationModel model) {
		return model.name + "App/src/model/infomodel";
	}
	
	override getContent(InformationModel model, InvocationContext context) {
		'''
		// «model.name»
		
		#ifndef __INFOMODEL_«model.name.toUpperCase»_H__
		#define __INFOMODEL_«model.name.toUpperCase»_H__
		
		#include <WString.h>
		
		«FOR fb : model.properties»
		#include "../functionblock/«fb.type.name».h"
		«ENDFOR»
		
		namespace «model.namespace.replace(".","_")» {
		    class «model.name»
		    {
		       public:
		            «model.name»();
		
		            «FOR fb : model.properties»
		                «fb.type.namespace.replace(".","_")»::«fb.type.name» «fb.name»;
		            «ENDFOR»
		
		            String serialize();
		        private:
		    };
		}
		
		#endif // __INFOMODEL_«model.name.toUpperCase»_H__
		'''
	}
	
	
}
