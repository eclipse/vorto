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
package org.eclipse.vorto.codegen.api;

public class VortoCodeGeneratorException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 5850205724505089809L;


  public VortoCodeGeneratorException() {
    super();
  }

  public VortoCodeGeneratorException(String message) {
    super(message);
  }

  public VortoCodeGeneratorException(Throwable cause) {
    super(cause);
  }

  public VortoCodeGeneratorException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
