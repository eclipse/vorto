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
package org.eclipse.vorto.repository.web.core.templates

import org.eclipse.vorto.model.ModelId
import org.eclipse.vorto.model.ModelType

class ModelTemplate {
	
	def String createModelTemplate(ModelId modelId, ModelType modelType) {
		'''
		vortolang 1.0
		namespace «modelId.namespace»
		version «modelId.version»
		displayname "«modelId.name»"
		description "«modelType.name» for «modelId.name»"
		
		«keyword(modelType)» «modelId.name» {
			«IF modelType == ModelType.Mapping»
			targetplatform myplatform
			«ELSEIF modelType == ModelType.InformationModel»
			functionblocks {
				
			}
			«ENDIF»
		}
		'''
	}
	
	def keyword(ModelType type) {
		if (type == ModelType.Functionblock) {
			return "functionblock"
		} else if (type == ModelType.InformationModel) {
			return "infomodel"
		}  else if (type == ModelType.Datatype) {
			return "entity"
		}  else if (type == ModelType.Mapping) {
			return "functionblockmapping"
		}
	}
	
}