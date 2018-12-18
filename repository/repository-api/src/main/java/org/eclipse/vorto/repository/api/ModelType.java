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
