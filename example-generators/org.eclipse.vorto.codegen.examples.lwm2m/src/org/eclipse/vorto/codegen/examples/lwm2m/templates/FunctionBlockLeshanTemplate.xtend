/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.lwm2m.templates

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.examples.lwm2m.utils.TypeMapper
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

/**
 * @author Shaodong Ying (Robert Bosch (SEA) Pte. Ltd)
 */
class FunctionBlockLeshanTemplate extends LWM2MConstants implements ITemplate<FunctionblockModel> {

	override getContent(FunctionblockModel model,InvocationContext context) {
	    
'''
package examples.leshan.client;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;

public class «model.name» extends BaseInstanceEnabler {

    «FOR statusProperty : model.functionblock.status.properties»
    /**
     * «statusProperty.description»
     */
    private «TypeMapper.mapSimpleDatatype((statusProperty.type as PrimitivePropertyType).type as PrimitiveType)» «statusProperty.name» = «TypeMapper.getInitialValue((statusProperty.type as PrimitivePropertyType).type as PrimitiveType)»;
    
    «ENDFOR»
        
    «FOR statusProperty : model.functionblock.status.properties»
    /**
     * Getter for «statusProperty.name».
     */
    public «TypeMapper.mapSimpleDatatype((statusProperty.type as PrimitivePropertyType).type as PrimitiveType)» get«statusProperty.name.toFirstUpper»() {
        return this.«statusProperty.name»;
    }
    
    «ENDFOR»

    «FOR statusProperty : model.functionblock.status.properties»
    /**
     * Setter for «statusProperty.name».
     */
    public void set«statusProperty.name.toFirstUpper»(«TypeMapper.mapSimpleDatatype((statusProperty.type as PrimitivePropertyType).type as PrimitiveType)» «statusProperty.name») {
        this.«statusProperty.name» = «statusProperty.name»;
    }
    
    «ENDFOR»

    /**
     * Gets the current value of one of this LWM2M object instance's resources.
     */
    @Override
    public ReadResponse read(int resourceid) {
        switch (resourceid) {
        «FOR statusProperty : model.functionblock.status.properties»
            «var mappedElement = context.getMappedElement(statusProperty, STEREOTYPE_RESOURCE)»
            «var obj_id = mappedElement.getAttributeValue(ATTRIBUTE_ID, null)»
            case «obj_id»:
                return ReadResponse.success(resourceid, get«statusProperty.name.toFirstUpper»());
        «ENDFOR»
        default:
            return super.read(resourceid);
        }
    }
    
    /**
     * Executes the operation represented by one of this LWM2M object instance's resources.
     */
    @Override
    public ExecuteResponse execute(int resourceid, String params) {
        switch (resourceid) {
        «FOR operation : model.functionblock.operations»
            «var mappedElement = context.getMappedElement(operation, STEREOTYPE_RESOURCE)»
            «var obj_id = mappedElement.getAttributeValue(ATTRIBUTE_ID, null)»
            case «obj_id»:
                // TODO: Implement execution code here
                // ...
                return ExecuteResponse.success();
        «ENDFOR»
        default:
            return ExecuteResponse.success();
        }

    }

}

'''
	}
}
