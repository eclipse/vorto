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

import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.utils.Utils

class ArduinoFbHeaderTemplate extends ArduinoTemplate<FunctionblockModel> {
	
	override getFileName(FunctionblockModel fb) {
		return fb.name + ".h";
	}
	
	override getPath(FunctionblockModel fb) {
		return rootPath + "/src/model/functionblock";
	}
	
	override getContent(FunctionblockModel fb, InvocationContext context) {
		'''
		// «fb.name»
		
		#ifndef __MODEL_«fb.name.toUpperCase»_H__
		#define __MODEL_«fb.name.toUpperCase»_H__
		
		#include <WString.h>
		
		«IF fb.functionblock.status !== null»
		    «FOR dataEnum : Utils.getReferencedEnums(fb.functionblock)»
		        #include "../datatype/enum/«dataEnum.name».h"
		    «ENDFOR»
		    «FOR dataEntity : Utils.getReferencedEntities(fb.functionblock)»
		         #include "../datatype/entity/«dataEntity.name».h"
		    «ENDFOR»
		«ENDIF»
		
		namespace «fb.namespace.replace(".","_")» {
		    class «fb.name»
		    {
		        public:
		            «fb.name»();
		            
		            «IF fb.functionblock.status !== null»
		                «FOR status : fb.functionblock.status.properties»
		                    void set«status.name»(«type(status.type)» value);
		                    «type(status.type)» get«status.name»();
		                «ENDFOR»
		            «ENDIF»
		            «IF fb.functionblock.configuration !== null»
		                «FOR configuration : fb.functionblock.configuration.properties»
		                    void set«configuration.name»(«type(configuration.type)» value);
		                    «type(configuration.type)» get«configuration.name»();
		                «ENDFOR»
		            «ENDIF»

		            String serialize(String ditto_namespace, String hono_deviceId, String fbName);
		        private:
		            «IF fb.functionblock.status !== null»
		                «FOR status : fb.functionblock.status.properties»
		                    «type(status.type)» «status.name»;
		                «ENDFOR»
		            «ENDIF»
		            «IF fb.functionblock.configuration !== null»
		                «FOR configuration : fb.functionblock.configuration.properties»
		                    «type(configuration.type)» «configuration.name»;
		                «ENDFOR»
		            «ENDIF»
		    };
		}
		
		#endif // __MODEL_«fb.name.toUpperCase»_H__
		'''
	}
	
}
