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

public class ModelEvent {

  private String name;
  private List<ModelProperty> properties = new ArrayList<ModelProperty>();

  public static Builder Builder(String name) {
    return new Builder(name);
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<ModelProperty> getProperties() {
    return properties;
  }

  public void setProperties(List<ModelProperty> properties) {
    this.properties = properties;
  }

  @Override
  public String toString() {
    return "ModelEventDto [name=" + name + ", properties=" + properties + "]";
  }

  public static class Builder {
    private ModelEvent event;
    public Builder(String name) {
      this.event = new ModelEvent();
    }
    
    public Builder withProperty(ModelProperty property) {
      this.event.getProperties().add(property);
      return this;
    }
    
    public ModelEvent build() {
      return event;
    }
  }

}
