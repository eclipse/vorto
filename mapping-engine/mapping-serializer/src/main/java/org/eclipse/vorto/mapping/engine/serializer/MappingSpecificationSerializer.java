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
    ModelId rootMappingId = MappingIdUtils.getIdForInfoModel(specification.getInfoModel());
    List<IMappingSerializer> serializers = new ArrayList<IMappingSerializer>();
    for (ModelProperty fbProperty : specification.getInfoModel().getFunctionblocks()) {
      FunctionblockModel fbm = specification.getFunctionBlock(fbProperty.getName());
      ModelId mappingId = MappingIdUtils.getIdForProperty(rootMappingId, fbProperty);
      addSerializerRecursive(mappingId,fbm, fbm.getProperties(),serializers);
      serializers.add(
          new FunctionblockMappingSerializer(specification, mappingId, targetPlatform, fbProperty.getName()));
    }
    serializers.add(new InformationModelMappingSerializer(specification,rootMappingId, targetPlatform));
    return serializers.iterator();
  }

  private void addSerializerRecursive(ModelId parentId, IModel container, List<ModelProperty> properties,
      List<IMappingSerializer> serializers) {
    for (ModelProperty property : properties) {
      if (isEntityProperty(property)) {
        EntityModel entityModel = (EntityModel)property.getType();
        ModelId mappingId = MappingIdUtils.getIdForProperty(parentId, property);
        addSerializerRecursive(mappingId,entityModel, entityModel.getProperties(), serializers);
        serializers.add(new EntityMappingSerializer(specification,mappingId,targetPlatform, property.getName(), entityModel, container));
      }
    }
  }
  
  private boolean isEntityProperty(ModelProperty property) {
    return property.getType() instanceof EntityModel;
  }

}
