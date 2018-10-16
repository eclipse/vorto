package org.eclipse.vorto.codegen.hono.arduino

import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.codegen.api.InvocationContext

class ArduinoEntityHeaderTemplate extends ArduinoTemplate<Entity> {
    override getFileName(Entity dataEntity) {
        return dataEntity.name + ".h";
    }
    
    override getPath(Entity dataEntity) {
        return rootPath + "/src/model/datatype/entity";
    }
    
    override getContent(Entity dataEntity, InvocationContext context) {
        '''
        // «dataEntity.name»
        
        #ifndef __ENTITY_«dataEntity.name.toUpperCase»_H__
        #define __ENTITY_«dataEntity.name.toUpperCase»_H__
        
        #include <WString.h>
        
        namespace «dataEntity.namespace.replace(".","_")» {
            class «dataEntity.name»
            {
                public:
                    «dataEntity.name»();
                    
                    «FOR status : dataEntity.properties»
                        void set«status.name»(«type(status.type)» value);
                        «type(status.type)» get«status.name»();
                    «ENDFOR»
                    
                    String serialize();
                private:
                    «FOR status : dataEntity.properties»
                        «type(status.type)» «status.name»;
                    «ENDFOR»
            };
        }
        #endif // __ENTITY_«dataEntity.name.toUpperCase»_H__
        '''
        }
}

