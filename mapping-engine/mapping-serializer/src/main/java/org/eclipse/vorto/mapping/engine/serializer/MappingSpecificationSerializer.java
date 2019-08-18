/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.mapping.engine.serializer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.IModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;

public class MappingSpecificationSerializer {

  private IMappingSpecification specification;
  private String targetPlatform;

  private MappingSpecificationSerializer(IMappingSpecification specification,
      String targetPlatform) {
    this.specification = specification;
    this.targetPlatform = targetPlatform;
  }

  public static MappingSpecificationSerializer create(IMappingSpecification specification,
      String targetPlaform) {
    return new MappingSpecificationSerializer(specification, targetPlaform);
  }

  public Iterator<IMappingSerializer> iterator() {
    List<IMappingSerializer> serializers = new ArrayList<IMappingSerializer>();
    for (ModelProperty fbProperty : specification.getInfoModel().getFunctionblocks()) {
      FunctionblockModel fbm = specification.getFunctionBlock(fbProperty.getName());
      addSerializerRecursive(fbm, fbm.getStatusProperties(),serializers);
      addSerializerRecursive(fbm, fbm.getConfigurationProperties(),serializers);
      serializers.add(
          new FunctionblockMappingSerializer(specification, targetPlatform, fbProperty.getName()));
    }
    serializers.add(new InformationModelMappingSerializer(specification, targetPlatform));
    return serializers.iterator();
  }

  private void addSerializerRecursive(IModel container, List<ModelProperty> properties,
      List<IMappingSerializer> serializers) {
    for (ModelProperty property : properties) {
      if (isEntityProperty(container.getId(), property)) {
        EntityModel entityModel = (EntityModel)specification.getReferencedModel(container.getId(), property.getName()).get();
        addSerializerRecursive(entityModel, entityModel.getProperties(), serializers);
        serializers.add(new EntityMappingSerializer(specification, targetPlatform, property.getName(), entityModel, container));
      }
    }
  }
  
  private boolean isEntityProperty(ModelId parentId, ModelProperty property) {
    return property.getType() instanceof ModelId && specification.getReferencedModel(parentId,property.getName()).isPresent() &&  specification.getReferencedModel(parentId,property.getName()).get() instanceof EntityModel;
}

}
