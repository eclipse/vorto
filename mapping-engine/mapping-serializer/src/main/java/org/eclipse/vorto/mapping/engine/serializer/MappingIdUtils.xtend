package org.eclipse.vorto.mapping.engine.serializer

import org.eclipse.vorto.model.Infomodel
import org.eclipse.vorto.model.ModelId
import org.eclipse.vorto.model.ModelProperty

class MappingIdUtils {
	def static ModelId getIdForInfoModel(Infomodel infomodel) {
    return new ModelId(infomodel.getId().getName()+"PayloadMapping",infomodel.getId().getNamespace()+"."+infomodel.getId().getName().toLowerCase(),infomodel.getId().getVersion());
  }
  
  def static ModelId getIdForProperty(ModelId parentId, ModelProperty property) {
    return new ModelId(property.getName().toFirstUpper+"PayloadMapping",parentId.getNamespace()+"."+property.name.toLowerCase,parentId.getVersion());
  }
}