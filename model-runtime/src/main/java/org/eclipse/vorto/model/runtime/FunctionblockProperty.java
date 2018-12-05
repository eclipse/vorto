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
package org.eclipse.vorto.model.runtime;

import java.util.Optional;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelProperty;

public class FunctionblockProperty {

  private Object propertyValue;
  private ModelProperty model;
  private String infomodelProperty;
  private FunctionblockModel parent;

  public static PropertyBuilder newBuilder(FunctionblockModel model, String infomodelProperty) {
    return new PropertyBuilder(model, infomodelProperty);
  }

  private FunctionblockProperty() {

  }

  private FunctionblockProperty(Object propertyValue, ModelProperty model,
      FunctionblockModel parent) {
    super();
    this.propertyValue = propertyValue;
    this.model = model;
    this.parent = parent;
  }

  public ModelProperty getModel() {
    return model;
  }

  public Object getPropertyValue() {
    return propertyValue;
  }

  public FunctionblockModel getParent() {
    return parent;
  }

  public String getInfomodelProperty() {
    return this.infomodelProperty;
  }

  @Override
  public String toString() {
    return "FunctionblockProperty [propertyValue=" + propertyValue + ", model=" + model
        + ", parent=" + parent + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((model == null) ? 0 : model.hashCode());
    result = prime * result + ((parent == null) ? 0 : parent.hashCode());
    result = prime * result + ((propertyValue == null) ? 0 : propertyValue.hashCode());
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
    FunctionblockProperty other = (FunctionblockProperty) obj;
    if (model == null) {
      if (other.model != null)
        return false;
    } else if (!model.equals(other.model))
      return false;
    if (parent == null) {
      if (other.parent != null)
        return false;
    } else if (!parent.equals(other.parent))
      return false;
    if (propertyValue == null) {
      if (other.propertyValue != null)
        return false;
    } else if (!propertyValue.equals(other.propertyValue))
      return false;
    return true;
  }

  public static class PropertyBuilder {
    private FunctionblockProperty prop;

    public PropertyBuilder(FunctionblockModel parent, String infomodelProp) {
      this.prop = new FunctionblockProperty();
      this.prop.parent = parent;
      this.prop.infomodelProperty = infomodelProp;
    }

    public PropertyBuilder property(String name) {
      Optional<ModelProperty> property = prop.parent.getConfigurationProperties().stream()
          .filter(p -> p.getName().equals(name)).findFirst();
      if (!property.isPresent()) {
        throw new IllegalArgumentException("Property is not defined in model");
      }
      this.prop.model = property.get();
      return this;
    }

    public PropertyBuilder value(Object value) {
      this.prop.propertyValue = value;
      return this;
    }

    public FunctionblockProperty build() {
      return prop;
    }
  }


}
