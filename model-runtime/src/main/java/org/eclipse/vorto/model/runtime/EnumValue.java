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
package org.eclipse.vorto.model.runtime;

import org.eclipse.vorto.model.EnumModel;

public class EnumValue {

  private EnumModel meta;
  private String value;
  
  public EnumValue(EnumModel meta) {
    super();
    this.meta = meta;
  }
  
  public EnumModel getMeta() {
    return meta;
  }
  public void setMeta(EnumModel meta) {
    this.meta = meta;
  }
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    if (!meta.getLiterals().stream().filter(literal -> literal.getName().equals(value)).findAny().isPresent()) {
      throw new IllegalArgumentException(value + " is not defined as literal in Enumeration");
    }
    this.value = value;
  }
  
  public Object serialize() {
    return value;
  }
  
}
