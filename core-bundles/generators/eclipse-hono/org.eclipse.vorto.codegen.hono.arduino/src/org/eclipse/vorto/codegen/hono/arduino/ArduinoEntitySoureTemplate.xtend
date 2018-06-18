package org.eclipse.vorto.codegen.hono.arduino

import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.codegen.api.InvocationContext


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
        void «dataEntity.name»::set«status.name»(«type(status.type)» value) {
            «status.name» = value;          
        }
        
        «type(status.type)» «dataEntity.name»::get«status.name»() {
            return «status.name»;
        }
        «ENDFOR»
        
        String «dataEntity.name»::serialize() {
            String result = "\"«dataEntity.displayname»\": {";
                «FOR status : dataEntity.properties»
                    result += "\"«status.name»\": " + String(«status.name») + ",";
                «ENDFOR»
                result += "}";

            return result;
        }
        '''
    }
}