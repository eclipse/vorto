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
package org.eclipse.vorto.repository.core;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Model not found")
public class ModelNotFoundException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public ModelNotFoundException(String msg, Throwable t) {
    super(msg, t);
  }

  public ModelNotFoundException(String msg) {
    super(msg);
  }
}
