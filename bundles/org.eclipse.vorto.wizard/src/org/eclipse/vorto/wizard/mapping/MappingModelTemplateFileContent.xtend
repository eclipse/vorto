/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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

package org.eclipse.vorto.wizard.mapping

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.ui.context.IModelProjectContext

class MappingModelTemplateFileContent implements ITemplate<IModelProjectContext> {
    
	override getContent(IModelProjectContext context) {
		var mappingWizardPage = context as MappingModellWizardPage;
		return '''
	namespace com.mycompany
	version «context.modelVersion»
	displayname "«context.modelName»"
	description "«context.modelDescription»"
	infomodelmapping «context.modelName» {
		targetplatform myplatform
	}	
		'''
	}
	
//	def String getImportStr(){
//		var model = getProjectFromSelection().model;
//		return model.namespace + "." + model.name + ";" + model.version;
//	}
	
//	def String getMappingType(){
//		var model = getProjectFromSelection().model;
//		if(model instanceof InformationModel){
//			return "infomodelmapping";
//		}else if(model instanceof FunctionblockModel){
//			return "functionblockmapping";
//		}else if(model instanceof Entity){
//			return "entitymapping";
//		}else if(model instanceof Enum){
//			return "enummapping";
//		}
//		
//		throw new UnsupportedOperationException("Not support type :" + model.class);
//	}
//	
//	def private IModelProject getProjectFromSelection(){
//		return ModelProjectServiceFactory.getDefault().getProjectFromSelection();
//	} 
}
