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