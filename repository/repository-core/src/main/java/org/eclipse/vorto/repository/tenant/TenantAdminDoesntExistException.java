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
package org.eclipse.vorto.repository.tenant;

public class TenantAdminDoesntExistException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -7363697265612882884L;

  public TenantAdminDoesntExistException(String userId) {
    super("User '" + userId + "' doesn't exist.");
  }

}
