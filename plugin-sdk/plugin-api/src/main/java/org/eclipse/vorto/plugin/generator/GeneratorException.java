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
package org.eclipse.vorto.plugin.generator;

public class GeneratorException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 5850205724505089809L;


  public GeneratorException() {
    super();
  }

  public GeneratorException(String message) {
    super(message);
  }

  public GeneratorException(Throwable cause) {
    super(cause);
  }

  public GeneratorException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
