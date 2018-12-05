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

public class Dictionary implements IReferenceType {

  private IReferenceType key;
  private IReferenceType value;
  private String type = "dictionary";

  public Dictionary(IReferenceType key, IReferenceType value) {
    this.key = key;
    this.value = value;
  }

  protected Dictionary() {}

  public IReferenceType getKey() {
    return key;
  }

  public void setKey(IReferenceType key) {
    this.key = key;
  }

  public IReferenceType getValue() {
    return value;
  }

  public void setValue(IReferenceType value) {
    this.value = value;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "Dictionary [key=" + key + ", value=" + value + "]";
  }



}
