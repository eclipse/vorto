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
