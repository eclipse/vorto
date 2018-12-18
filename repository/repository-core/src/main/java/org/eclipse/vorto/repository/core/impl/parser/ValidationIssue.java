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
package org.eclipse.vorto.repository.core.impl.parser;

public class ValidationIssue {
  private int lineNumber;
  private String msg;

  public ValidationIssue(int lineNumber, String msg) {
    this.lineNumber = lineNumber;
    this.msg = msg;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public String getMsg() {
    return msg;
  }

  public String toString() {
    return "On line number " + lineNumber + " : " + msg;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + lineNumber;
    result = prime * result + ((msg == null) ? 0 : msg.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ValidationIssue other = (ValidationIssue) obj;
    if (lineNumber != other.lineNumber)
      return false;
    if (msg == null) {
      if (other.msg != null)
        return false;
    } else if (!msg.equals(other.msg))
      return false;
    return true;
  }
}
