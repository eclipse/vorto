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
      serializers.add(
          new FunctionblockMappingSerializer(specification, targetPlatform, fbProperty.getName()));
    }
    serializers.add(new InformationModelMappingSerializer(specification, targetPlatform));
    return serializers.iterator();
  }

}
