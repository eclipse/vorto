/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.model;

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
