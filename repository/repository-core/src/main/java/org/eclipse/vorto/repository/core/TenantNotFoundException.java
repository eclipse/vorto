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

public class TenantNotFoundException extends ModelRepositoryException {

  /**
   * 
   */
  private static final long serialVersionUID = 6204999779117905073L;

  public TenantNotFoundException(String tenantId, Throwable cause) {
    super("No tenant '" + tenantId + "'", cause);
  }
  
  public TenantNotFoundException(String namespace) {
    super("Tenant not found for namespace '" + namespace + "'");
  }

}
