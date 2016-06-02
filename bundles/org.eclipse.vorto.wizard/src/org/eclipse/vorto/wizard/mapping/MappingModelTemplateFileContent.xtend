/*******************************************************************************
 * Copyright (c) 2015,2016 Bosch Software Innovations GmbH and others.
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
import org.eclipse.vorto.core.api.model.model.ModelId
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.datatype.Entity

class MappingModelTemplateFileContent implements ITemplate<IModelProjectContext> {
    
	val ModelId modelId;

	new(ModelId modelId) {
		this.modelId = modelId
	}

	override getContent(IModelProjectContext context) {
		var mappingWizardPage = context as MappingModellWizardPage;
		return '''
	namespace com.mycompany
	version «context.modelVersion»
	displayname "«context.modelName»"
	description "«context.modelDescription»"
	using «modelId.asModelReference.importedNamespace»;«modelId.asModelReference.version»
	
	«getMappingType(mappingWizardPage)» «context.modelName» {
		targetplatform «mappingWizardPage.targetPlatform»
	}'''
	}
	
	def String getMappingType(MappingModellWizardPage mappingWizardPage) {
		var model = mappingWizardPage.modelProject.getModelElementById(modelId).model
		if (model instanceof InformationModel) {
			return "infomodelmapping";
		} else if (model instanceof FunctionblockModel) {
			return "functionblockmapping";
		} else if (model instanceof Entity) {
			return "entitymapping";
		} else if (model instanceof org.eclipse.vorto.core.api.model.datatype.Enum) {
			return "enummapping";
		}
	}
}
