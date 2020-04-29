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

import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.plugin.generator.InvocationContext

class ArduinoEntitySoureTemplate extends ArduinoTemplate<Entity>{
    override getFileName(Entity dataEntity) {
        return dataEntity.name + ".cpp";
    }
    
    override getPath(Entity dataEntity) {
        return rootPath + "/src/model/datatype/entity";
    }
    
    override getContent(Entity dataEntity, InvocationContext context) {
        '''
        // «dataEntity.name»
        
        #include "«dataEntity.name».h"
        
        using namespace «dataEntity.namespace.replace(".","_")»;
        
        «dataEntity.name»::«dataEntity.name»(){}
        
        «FOR status : dataEntity.properties»
        void «dataEntity.name»::set«status.name»(«type(status.type)» «"_"+status.name») {
            «status.name» = «"_"+status.name»;
        }
        
        «type(status.type)» «dataEntity.name»::get«status.name»() {
            return «status.name»;
        }
        «ENDFOR»
        
        String «dataEntity.name»::serialize() {
        String result = "{";
            	«var int counter = 0»
                «FOR status : dataEntity.properties SEPARATOR " + \",\";"»
                    result += "\"«status.name»\": " + String(«status.name»)«IF counter++ == dataEntity.properties.length-1»;«ENDIF»
                «ENDFOR»
                result += "}";

            return result;
        }
        '''
    }
}