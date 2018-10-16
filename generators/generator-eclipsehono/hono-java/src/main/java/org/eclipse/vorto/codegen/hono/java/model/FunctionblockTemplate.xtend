/*******************************************************************************
 * Copyright (c) 2018 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.hono.java.model

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.hono.java.Utils
import org.eclipse.vorto.codegen.templates.java.JavaClassFieldGetterTemplate
import org.eclipse.vorto.codegen.templates.java.JavaClassFieldSetterTemplate
import org.eclipse.vorto.codegen.templates.java.JavaClassFieldTemplate
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class FunctionblockTemplate implements IFileTemplate<FunctionblockModel> {

	private InformationModel informationModelContext;
	
	private JavaClassFieldTemplate propertyTemplate;
	private JavaClassFieldSetterTemplate propertySetterTemplate;
	private JavaClassFieldGetterTemplate propertyGetterTemplate;
	
	new(InformationModel context) {
		this.informationModelContext = context;
		
		this.propertyTemplate = new JavaClassFieldTemplate() {
			protected override addFieldAnnotations(Property property) {
			'''
			@com.google.gson.annotations.SerializedName("«property.name»")
			'''
			}
			
			protected override getNamespaceOfDatatype() {
				'''«Utils.getJavaPackage(informationModelContext)».model.datatypes.'''
			}
		};
		this.propertySetterTemplate = new JavaClassFieldSetterTemplate("set") {
			protected override getNamespaceOfDatatype() {
				'''«Utils.getJavaPackage(informationModelContext)».model.datatypes.'''
			}
		};
		this.propertyGetterTemplate = new JavaClassFieldGetterTemplate("get") {
			protected override getNamespaceOfDatatype() {
				'''«Utils.getJavaPackage(informationModelContext)».model.datatypes.'''
			}
		};
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
		
		import java.util.HashMap;
		import java.util.Map;
		
		public class «model.getName» {
		    «var fb = model.functionblock»	
		    «IF fb.status !== null»
		    
		    /** Status properties */
		    
		    «FOR property : model.functionblock.status.properties»
		    	«propertyTemplate.getContent(property,context)»
		    «ENDFOR»
		    «ENDIF»
		    «IF fb.configuration !== null»
		    
		    /** Configuration properties */
		    
		    «FOR property : model.functionblock.configuration.properties»
		    	«propertyTemplate.getContent(property,context)»
		    «ENDFOR»
		    «ENDIF»
		    	
		    «IF fb.status !== null»	
		    	«FOR property : model.functionblock.status.properties»
		    		«propertySetterTemplate.getContent(property,context)»
		    		«propertyGetterTemplate.getContent(property,context)»
		    	«ENDFOR»
		    «ENDIF»
		    «IF fb.configuration !== null»	
		    	«FOR property : model.functionblock.configuration.properties»
		    		«propertySetterTemplate.getContent(property,context)»
		    		«propertyGetterTemplate.getContent(property,context)»
		    	«ENDFOR»
		    «ENDIF»
		    
		    public Map getStatusProperties() {
		        Map<String, Object> status = new HashMap<String, Object>();
		        «IF fb.status !== null»	
		        	«FOR property : model.functionblock.status.properties»
		        		status.put("«property.name»", this.«property.name»);
		        	«ENDFOR»
		        «ENDIF»
		    	return status;
		    }
		    public Map getConfigurationProperties() {
		        Map<String, Object> configuration = new HashMap<String, Object>();
		        «IF fb.configuration !== null»	
		        	«FOR property : model.functionblock.configuration.properties»
		        		configuration.put("«property.name»", this.«property.name»);
		        	«ENDFOR»
		        «ENDIF»
		        return configuration;
		    }
		}
		'''
	}

}
