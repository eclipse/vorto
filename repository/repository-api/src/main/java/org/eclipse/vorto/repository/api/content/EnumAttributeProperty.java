/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
public class EnumAttributeProperty implements IPropertyAttribute {

  private EnumAttributePropertyType type;
  private EnumLiteral value;

  public EnumAttributeProperty(EnumAttributePropertyType type, EnumLiteral value) {
    super();
    this.type = type;
    this.value = value;
  }

  public EnumAttributeProperty() {

  }

  public EnumAttributePropertyType getType() {
    return type;
  }

  public void setType(EnumAttributePropertyType type) {
    this.type = type;
  }

  public EnumLiteral getValue() {
    return value;
  }

  public void setValue(EnumLiteral value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "EnumAttributeProperty [type=" + type + ", value=" + value + "]";
  }


}
