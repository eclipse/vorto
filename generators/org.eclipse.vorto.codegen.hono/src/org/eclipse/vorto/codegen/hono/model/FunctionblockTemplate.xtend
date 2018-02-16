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
package org.eclipse.vorto.codegen.hono.model

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.hono.TypeMapper
import org.eclipse.vorto.codegen.hono.Utils
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class FunctionblockTemplate implements IFileTemplate<FunctionblockModel> {

	private InformationModel informationModelContext;
	
	new(InformationModel context) {
		this.informationModelContext = context;
	}

    override getFileName(FunctionblockModel model) {
        return model.getName()+".java"
    }
    
    override getPath(FunctionblockModel model) {
    	'''«Utils.getJavaPackageBasePath(informationModelContext)»/model'''
    }
    

    override getContent(FunctionblockModel model,InvocationContext context) {
		'''
		package «Utils.getJavaPackage(informationModelContext)».model;
		
		import com.google.gson.annotations.SerializedName;
		
		public class «model.getName» {
		    «FOR statusProperty : model.functionblock.status.properties»
		    @SerializedName("«statusProperty.name»")
		    private «TypeMapper.mapSimpleDatatype(statusProperty.type)» «TypeMapper.checkKeyword(statusProperty.name)»;
		    «ENDFOR»
		    
		    «FOR statusProperty : model.functionblock.status.properties»
		    /**
		     * Getter for «statusProperty.name».
		     */
		    public «TypeMapper.mapSimpleDatatype(statusProperty.type)» get«TypeMapper.checkKeyword(statusProperty.name).toFirstUpper»() {
		        return «TypeMapper.checkKeyword(statusProperty.name)»;
		    }
		    
		    /**
		     * Setter for «statusProperty.name».
		     */
		    public void set«TypeMapper.checkKeyword(statusProperty.name).toFirstUpper»(«TypeMapper.mapSimpleDatatype(statusProperty.type)» «TypeMapper.checkKeyword(statusProperty.name)») {
		        this.«TypeMapper.checkKeyword(statusProperty.name)» = «TypeMapper.checkKeyword(statusProperty.name)»;
		    }
		    
		    «ENDFOR»
		
		}
		'''
	}

}
