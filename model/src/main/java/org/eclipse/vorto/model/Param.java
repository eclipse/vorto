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

public class Param extends AbstractProperty {

  public static Builder Builder(String name, IReferenceType type) {
    return new Builder(name, type);
  }
  
  @Override
  public String toString() {
    return "Param [isMandatory=" + mandatory + ", name=" + name + ", isMultiple=" + isMultiple
        + ", description=" + description + ", type=" + type + ", constraints=" + constraints + "]";
  }

  public static class Builder {
    private Param param;
    public Builder(String name, IReferenceType type) {
      this.param = new Param();
      this.param.setMandatory(true);
      this.param.setName(name);
      this.param.setType(type);
    }
    
    public Builder optional() {
      this.param.setMandatory(false);
      return this;
    }
    
    public Builder multiple() {
      this.param.setMultiple(true);
      return this;
    }
    
    public Builder withConstraint(ConstraintType type, String value) {
      this.param.getConstraints().add(new Constraint(type, value));
      return this;
    }
 
    public Param build() {
      return param;
    }
  }
}
