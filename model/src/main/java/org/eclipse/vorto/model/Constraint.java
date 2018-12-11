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

public class Constraint {

  private ConstraintType type;

  private String value;

  public Constraint(ConstraintType type, String value) {
    this.type = type;
    this.value = value;
  }

  public Constraint() {

  }

  public ConstraintType getType() {
    return type;
  }

  public void setType(ConstraintType type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


  @Override
  public String toString() {
    return "Constraint [type=" + type + ", value=" + value + "]";
  }



}
