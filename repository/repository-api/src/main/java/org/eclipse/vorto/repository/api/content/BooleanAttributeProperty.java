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
package org.eclipse.vorto.repository.api.content;

@Deprecated
public class BooleanAttributeProperty implements IPropertyAttribute {

  private BooleanAttributePropertyType type;

  private boolean value;

  public BooleanAttributeProperty(BooleanAttributePropertyType type, boolean value) {
    super();
    this.type = type;
    this.value = value;
  }

  public BooleanAttributeProperty() {

  }

  public BooleanAttributePropertyType getType() {
    return type;
  }

  public void setType(BooleanAttributePropertyType type) {
    this.type = type;
  }

  public boolean isValue() {
    return value;
  }

  public void setValue(boolean value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "BooleanAttributeProperty [type=" + type + ", value=" + value + "]";
  }


}
