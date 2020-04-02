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
package org.eclipse.vorto.repository.services.exceptions;

/**
 * Thrown when attempting to create a namespace for a non-sysadmin user who already has reached the
 * quota of private namespaces owned, as defined in {@code config.privateNamespaceQuota}
 * (formerly known as {@code config.restrictTenant}
 */
public class PrivateNamespaceQuotaExceededException extends Exception {

  public PrivateNamespaceQuotaExceededException(String message) {
    super(message);
  }
}
