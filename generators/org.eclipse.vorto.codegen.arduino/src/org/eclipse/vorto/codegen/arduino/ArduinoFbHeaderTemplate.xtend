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
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.model.Model

class ArduinoFbHeaderTemplate extends ArduinoTemplate<FunctionblockModel> {
	
	override getFileName(FunctionblockModel fb) {
		return fb.name + ".h";
	}
	
	override getPath(FunctionblockModel fb) {
		return rootPath;
	}
	
	override getContent(FunctionblockModel fb, InvocationContext context) {
		'''
		// «fb.name»
		
		#ifndef __«fb.name.toUpperCase»_H__
		#define __«fb.name.toUpperCase»_H__
		
		#include <WString.h>
		
		class «fb.name»
		{
            public:
                «fb.name»();
				
                «FOR status : fb.functionblock.status.properties»
                void set«status.name»(«type(status.type)» value);
                «type(status.type)» get«status.name»();
                «ENDFOR»

                String serialize();
            private:
                «FOR status : fb.functionblock.status.properties»
                    «type(status.type)» «status.name»;
                    bool «status.name»Updated;
                «ENDFOR»				
        };
		
        #endif // __«fb.name.toUpperCase»_H__
		'''
	}
	
}