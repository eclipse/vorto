package org.eclipse.vorto.codegen.examples.leshan.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.codegen.examples.leshan.utils.TypeMapper
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType

/**
 * @author Ying Shaodong - Robert Bosch (SEA) Pte. Ltd.
 */
class FunctionBlockLeshanTemplate implements IFileTemplate<FunctionblockModel> {

	override getFileName(FunctionblockModel model) {
		return '''Fb«model.name».java'''
	}
	
	override getPath(FunctionblockModel model) {
		return "/generated_code/"
	}
	override getContent(FunctionblockModel model,InvocationContext context) {
	    
'''
package org.eclipse.leshan.client.demo;

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
            «var mappedElement = context.getMappedElement(statusProperty, "Resources")»
            «var obj_id = mappedElement.getAttributeValue("ID", null)»
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
            «var mappedElement = context.getMappedElement(operation, "Resources")»
            «var obj_id = mappedElement.getAttributeValue("ID", null)»
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
