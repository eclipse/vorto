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
package org.eclipse.vorto.repository.plugin.generator;

public class GeneratorAlreadyExistsException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String existingServiceKey;

  public GeneratorAlreadyExistsException(String existingServiceKey) {
    this.existingServiceKey = existingServiceKey;
  }

  public String getExistingServiceKey() {
    return this.existingServiceKey;
  }
}
