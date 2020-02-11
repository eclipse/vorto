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
 * This is a nearly identical copy of nested class
 * {@link org.eclipse.vorto.repository.web.tenant.TenantManagementController#Result}, for usage in
 * the new {@link org.eclipse.vorto.repository.web.api.v1.NamespaceController}.<br/>
 * It simply provides a serializable response to common namespace CRUD operations, to work with the
 * front-end.
 */
public class NamespaceOperationResult {

    private boolean success;
    private String errorMessage;

  /**
   * Generates an empty success {@link NamespaceOperationResult} if the given flag is {@code true}
   * (typically the outcome of a service operation), or a failure result with the given optional
   * message otherwise.
   * @param success
   * @param errorMessageIfFailure
   * @return
   */
    public static NamespaceOperationResult generate(boolean success, Optional<String> errorMessageIfFailure) {
      if (success) {
        return success();
      }
      else {
        return failure(errorMessageIfFailure.orElse(null));
      }
    }

    public static NamespaceOperationResult success() {
      return new NamespaceOperationResult(true, null);
    }

    public static NamespaceOperationResult failure(String msg) {
      return new NamespaceOperationResult(false, msg);
    }

    private NamespaceOperationResult(boolean success, String errorMessage) {
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
