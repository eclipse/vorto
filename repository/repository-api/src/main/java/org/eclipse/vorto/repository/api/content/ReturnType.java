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
public class ReturnType {

  private boolean isMultiple;

  private boolean isPrimitive;

  private IReferenceType type = null;

  public boolean isMultiple() {
    return isMultiple;
  }

  public void setMultiple(boolean isMultiple) {
    this.isMultiple = isMultiple;
  }

  public IReferenceType getType() {
    return type;
  }

  public void setType(IReferenceType type) {
    this.type = type;
  }

  public boolean isPrimitive() {
    return isPrimitive;
  }

  public void setPrimitive(boolean isPrimitive) {
    this.isPrimitive = isPrimitive;
  }

  @Override
  public String toString() {
    return "ReturnTypeDto [isMultiple=" + isMultiple + ", isPrimitive=" + isPrimitive + ", type="
        + type + "]";
  }



}
