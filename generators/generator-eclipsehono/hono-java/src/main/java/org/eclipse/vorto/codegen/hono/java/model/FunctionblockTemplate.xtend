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
package org.eclipse.vorto.codegen.hono.java.model

import org.eclipse.vorto.codegen.hono.java.Utils
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.IFileTemplate
import org.eclipse.vorto.plugin.generator.utils.javatemplates.JavaClassFieldGetterTemplate
import org.eclipse.vorto.plugin.generator.utils.javatemplates.JavaClassFieldSetterTemplate
import org.eclipse.vorto.plugin.generator.utils.javatemplates.JavaClassFieldTemplate

class FunctionblockTemplate implements IFileTemplate<FunctionblockModel> {

	InformationModel informationModelContext;
	
	JavaClassFieldTemplate propertyTemplate;
	JavaClassFieldSetterTemplate propertySetterTemplate;
	JavaClassFieldGetterTemplate propertyGetterTemplate;
	
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
