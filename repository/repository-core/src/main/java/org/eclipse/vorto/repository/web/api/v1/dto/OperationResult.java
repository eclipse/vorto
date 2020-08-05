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
package org.eclipse.vorto.repository.web.api.v1.dto;

import java.util.Optional;

/**
 * It simply provides a serializable response to common namespace CRUD operations, to work with the
 * front-end.
 */
public class OperationResult {

  private boolean success;
  private String errorMessage;

  /**
   * Generates an empty success {@link OperationResult} if the given flag is {@code true}
   * (typically the outcome of a service operation), or a failure result with the given optional
   * message otherwise.
   *
   * @param success
   * @param errorMessageIfFailure
   * @return
   */
  public static OperationResult generate(boolean success,
      Optional<String> errorMessageIfFailure) {
    if (success) {
      return success();
    } else {
      return failure(errorMessageIfFailure.orElse(null));
    }
  }

  public static OperationResult success() {
    return new OperationResult(true, null);
  }

  public static OperationResult success(String message) {
    return new OperationResult(true, message);
  }

  public static OperationResult failure(String msg) {
    return new OperationResult(false, msg);
  }

  private OperationResult(boolean success, String errorMessage) {
    this.success = success;
    this.errorMessage = errorMessage;
  }

  public boolean isSuccess() {
    return success;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

}
