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