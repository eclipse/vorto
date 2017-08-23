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

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class ArduinoImHeaderTemplate extends ArduinoTemplate<InformationModel> {
	
	override getFileName(InformationModel model) {
		return model.name + ".h";
	}
	
	override getPath(InformationModel model) {
		return model.name;
	}
	
	override getContent(InformationModel model, InvocationContext context) {
		'''
		// «model.name»
		
		#ifndef __«model.name.toUpperCase»_H__
		#define __«model.name.toUpperCase»_H__
		
		#include <WString.h>
		
		«FOR fb : model.properties»
		#include "«fb.type.name».h"
		«ENDFOR»
		
		class «model.name»
		{
            public:
                «model.name»();

                «FOR fb : model.properties»
                    «fb.type.name» «fb.name»;
                «ENDFOR»

                String serialize();
            private:
		};
		
		#endif // __«model.name.toUpperCase»_H__
		'''
	}
	
	
}