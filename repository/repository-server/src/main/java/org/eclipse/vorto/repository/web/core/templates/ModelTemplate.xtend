/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.repository.web.core.templates

import org.eclipse.vorto.repository.api.ModelId
import org.eclipse.vorto.repository.api.ModelType

class ModelTemplate {
	
	def String createModelTemplate(ModelId modelId, ModelType modelType) {
		'''
		namespace «modelId.namespace»
		version «modelId.version»
		displayname "«modelId.name»"
		description "«modelType.name» for «modelId.name»"
		
		«keyword(modelType)» «modelId.name» {
			
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
			return "mapping"
		}
	}
	
}