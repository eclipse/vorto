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
package org.eclipse.vorto.repository.server.ui.util;

/**
 * Utility builder with model renaming parametrization
 */
public class RenameModelParams {
  private String newSubNamespace;
  private String newName;

  public RenameModelParams() {}

  public RenameModelParams(String newSubNamespace, String newName) {
    this.newSubNamespace = newSubNamespace;
    this.newName = newName;
  }

  public RenameModelParams withNewSubNamespace(String newSubNamespace) {
    this.newSubNamespace = newSubNamespace;
    return this;
  }

  public RenameModelParams withNewName(String newName) {
    this.newName = newName;
    return this;
  }

  public String getNewSubNamespace() {
    return newSubNamespace;
  }

  public void setNewSubNamespace(String newSubNamespace) {
    this.newSubNamespace = newSubNamespace;
  }

  public String getNewName() {
    return newName;
  }

  public void setNewName(String newName) {
    this.newName = newName;
  }
}
