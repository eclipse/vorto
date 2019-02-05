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
package org.eclipse.vorto.model;

import java.util.ArrayList;
import java.util.List;

public class ModelProperty extends AbstractProperty {

  private List<IPropertyAttribute> attributes = new ArrayList<IPropertyAttribute>();

  public static ModelProperty createPrimitiveProperty(String name, boolean isMandatory,
      PrimitiveType type) {
    ModelProperty property = new ModelProperty();
    property.setName(name);
    property.setType(type);
    property.setMandatory(isMandatory);
    return property;
  }

  public List<IPropertyAttribute> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<IPropertyAttribute> attributes) {
    this.attributes = attributes;
  }

  @Override
  public String toString() {
    return "ModelProperty [attributes=" + attributes + ", isMandatory=" + mandatory + ", name="
        + name + ", isMultiple=" + isMultiple + ", description=" + description + ", type=" + type
        + ", constraints=" + constraints + "]";
  }


}
