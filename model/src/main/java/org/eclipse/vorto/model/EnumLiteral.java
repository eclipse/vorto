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

public class EnumLiteral {

  private String name;
  private String description;
  private ModelId parent;

  public EnumLiteral(String name, String description, ModelId parent) {
    super();
    this.name = name;
    this.description = description;
    this.parent = parent;
  }

  protected EnumLiteral() {

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
  
  public ModelId getParent() {
	return parent;
}

public void setParent(ModelId parent) {
	this.parent = parent;
}

@Override
  public String toString() {
    return "EnumLiteralDto [name=" + name + ", description=" + description + "]";
  }


}
