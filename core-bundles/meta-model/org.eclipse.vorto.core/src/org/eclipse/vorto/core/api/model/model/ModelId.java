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
package org.eclipse.vorto.core.api.model.model;

public class ModelId implements Comparable<ModelId> {

  private static final String UNDERSCORE = "_";

  private ModelType modelType;
  private String name;
  private String namespace;
  private String version;

  public ModelId(ModelType modelType, String name, String namespace, String version) {
    this.modelType = modelType;
    this.name = name;
    this.namespace = namespace;
    this.version = version;
  }

  public void setModelType(ModelType modelType) {
    this.modelType = modelType;
  }

  public ModelType getModelType() {
    return modelType;
  }

  public String getName() {
    return name;
  }

  public String getNamespace() {
    return namespace;
  }

  public String getVersion() {
    return version;
  }

  public ModelReference asModelReference() {
    ModelReference modelReference = ModelFactory.eINSTANCE.createModelReference();
    modelReference.setImportedNamespace(namespace + "." + name);
    modelReference.setVersion(version);
    return modelReference;
  }

  public String serialize() {
    StringBuilder sb = new StringBuilder();
    sb.append("modelType=" + modelType);
    sb.append(",namespace=" + namespace);
    sb.append(",name=" + name);
    sb.append(",version=" + version);
    return sb.toString();
  }

  @Override
  public String toString() {
    return "ModelId [modelType=" + modelType + ", name=" + name + ", namespace=" + namespace
        + ", version=" + version + "]";
  }

  @Override
  public int compareTo(ModelId o) {
    if (o == null || o.getName() == null)
      return -1;
    else
      return o.getName().compareTo(this.getName());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((modelType == null) ? 0 : modelType.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
    result = prime * result + ((version == null) ? 0 : version.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelId other = (ModelId) obj;
    if (modelType != other.modelType)
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (namespace == null) {
      if (other.namespace != null)
        return false;
    } else if (!namespace.equals(other.namespace))
      return false;
    if (version == null) {
      if (other.version != null)
        return false;
    } else if (!version.equals(other.version))
      return false;
    return true;
  }

  @Deprecated
  public String getFileName() {
    return namespace + UNDERSCORE + name + UNDERSCORE + delimitVersion(version)
        + getModelType().getExtension();
  }

  private String delimitVersion(String version) {
    return version.replaceAll("\\.", UNDERSCORE);
  }
}
