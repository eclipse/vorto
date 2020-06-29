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
 * Thrown when a {@link org.eclipse.vorto.repository.domain.User} object is deemed invalid and
 * cannot be queried or saved by other services.
 */
public class InvalidUserException extends Exception {
  public InvalidUserException(String message) {
    super(message);
  }
}
