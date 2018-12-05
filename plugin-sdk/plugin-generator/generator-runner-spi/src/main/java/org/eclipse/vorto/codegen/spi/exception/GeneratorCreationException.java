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
package org.eclipse.vorto.codegen.spi.exception;

public class GeneratorCreationException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 4107347635470392365L;

  public GeneratorCreationException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
