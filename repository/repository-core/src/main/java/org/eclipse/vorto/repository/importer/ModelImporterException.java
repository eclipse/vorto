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
package org.eclipse.vorto.repository.importer;

public class ModelImporterException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -7641141207722584032L;

  public ModelImporterException() {}

  public ModelImporterException(String message) {
    super(message);
  }

  public ModelImporterException(String message, Throwable t) {
    super(message, t);
  }

}
