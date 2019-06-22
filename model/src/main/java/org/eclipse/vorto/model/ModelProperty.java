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
import java.util.Map;

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
  
  public static Builder Builder(String name, IReferenceType type) {
    return new Builder(name, type);
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

  public static class Builder {
    private ModelProperty property;
    public Builder(String name, IReferenceType type) {
      this.property = new ModelProperty();
      this.property.setMandatory(true);
      this.property.setName(name);
      this.property.setType(type);
    }
    
    public Builder optional() {
      this.property.setMandatory(false);
      return this;
    }
    
    public Builder multiple() {
      this.property.setMultiple(true);
      return this;
    }
    
    public Builder withConstraint(ConstraintType type, String value) {
      this.property.getConstraints().add(new Constraint(type, value));
      return this;
    }
    
    public Builder withAttributeMeasurementUnit(EnumLiteral literal) {
      this.property.getAttributes().add(new EnumAttributeProperty(EnumAttributePropertyType.MEASUREMENT_UNIT, literal));
      return this;
    }
    
    public Builder withStereotype(String stereoTypeName, Map<String,String> attributes,String targetPlatformKey) {
      property.setTargetPlatformKey(targetPlatformKey);
      property.addStereotype(Stereotype.create(stereoTypeName, attributes));
      return this;
    }
    
    public ModelProperty build() {
      return property;
    }
  }
  

}
