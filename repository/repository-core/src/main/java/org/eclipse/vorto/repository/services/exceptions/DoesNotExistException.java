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
 * Thrown when a referenced entity is supposed to exist already, but does not.
 */
public class DoesNotExistException extends Exception {

  public static final String DEFAULT_MESSAGE_FORMAT = "Namespace [%s] does not exist.";

  public DoesNotExistException(String message) {
    super(message);
  }

  public static DoesNotExistException withNamespace(String namespaceName) {
    return new DoesNotExistException(String.format(DEFAULT_MESSAGE_FORMAT, namespaceName));
  }
}
