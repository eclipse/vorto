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
package org.eclipse.vorto.mapping.engine.model.spec;

import org.eclipse.vorto.model.IModel;
import org.eclipse.vorto.model.ModelId;

public class Reference {

  private String propertyName;
  private ModelId parent;
  private IModel type;
  
  public Reference(String propertyName, ModelId parent, IModel type) {
    this.propertyName = propertyName;
    this.parent = parent;
    this.type = type;
  }
  
  public Reference() {
    
  }

  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  public ModelId getParent() {
    return parent;
  }

  public void setParent(ModelId parent) {
    this.parent = parent;
  }

  public IModel getType() {
    return type;
  }

  public void setType(IModel type) {
    this.type = type;
  }

  public static Reference of(ModelId parent, IModel type, String propertyName) {
    return new Reference(propertyName, parent, type);
  }

  @Override
  public String toString() {
    return "Reference [propertyName=" + propertyName + ", parent=" + parent + ", type=" + type
        + "]";
  }
  
  
}
