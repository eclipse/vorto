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
package org.eclipse.vorto.repository.api;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Deprecated
public enum ModelType {
  Functionblock(".fbmodel"), InformationModel(".infomodel"), Datatype(".type"), Mapping(".mapping");

  private String extension;

  ModelType(String extension, String... referenceTypes) {
    this.extension = extension;
  }

  public String getExtension() {
    return extension;
  }

  public static ModelType fromFileName(String fileName) {
    String type = fileName.substring(fileName.lastIndexOf("."));
    if (type.equals(ModelType.Functionblock.getExtension())) {
      return ModelType.Functionblock;
    } else if (type.equals(ModelType.InformationModel.getExtension())) {
      return ModelType.InformationModel;
    } else if (type.equals(ModelType.Datatype.getExtension())) {
      return ModelType.Datatype;
    } else if (type.equals(ModelType.Mapping.getExtension())) {
      return ModelType.Mapping;
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public static boolean containsType(String type) {
    for (ModelType mType : ModelType.values()) {
      if (mType.name().equals(type)) {
        return true;
      }
    }
    return false;
  }

  public boolean canHandleReference(IModel reference) {
    if (this == ModelType.InformationModel && reference.getType() == ModelType.Functionblock) {
      return true;
    } else if (this == ModelType.Functionblock && (reference.getType() == ModelType.Functionblock
        || reference.getType() == ModelType.Datatype)) {
      return true;
    } else if (this == ModelType.Datatype && reference.getType() == ModelType.Datatype) {
      return true;
    } else if (this == ModelType.Mapping) { // mapping allow all reference types
      return true;
    } else {
      return false;
    }
  }
}
