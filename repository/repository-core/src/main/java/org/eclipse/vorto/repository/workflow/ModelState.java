/**
 * Copyright (c) 2018, 2019 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.workflow;

public enum ModelState {
  Draft("Draft"),
  InReview("InReview"),
  Released("Released"),
  Deprecated("Deprecated");
  
  private String name;

  ModelState(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}
