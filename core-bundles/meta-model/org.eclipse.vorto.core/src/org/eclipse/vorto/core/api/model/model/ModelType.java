/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.core.api.model.model;

import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;

public enum ModelType {

  Functionblock(".fbmodel", FunctionblockModel.class), InformationModel(".infomodel",
      InformationModel.class), Datatype(".type",
          Type.class), Mapping(".mapping", MappingModel.class);

  private String extension;

  private Class<?> modelClass;

  ModelType(String extension, Class<?> modelClass) {
    this.extension = extension;
    this.modelClass = modelClass;
  }

  public String getExtension() {
    return extension;
  }

  @SuppressWarnings("unchecked")
  public <M extends Model> Class<M> getModelClass() {
    return (Class<M>) modelClass;
  }

  public static ModelType create(String fileName) {
    String fileEnding = fileName.substring(fileName.indexOf("."));
    if (ModelType.Functionblock.extension.equalsIgnoreCase(fileEnding)) {
      return ModelType.Functionblock;
    } else if (ModelType.InformationModel.extension.equalsIgnoreCase(fileEnding)) {
      return ModelType.InformationModel;
    } else if (ModelType.Datatype.extension.equalsIgnoreCase(fileEnding)) {
      return ModelType.Datatype;
    } else if (ModelType.Mapping.extension.equalsIgnoreCase(fileEnding)) {
      return ModelType.Mapping;
    } else {
      throw new UnsupportedOperationException("Given filename is unknown");
    }
  }
}
