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

public class UserLoginException extends ModelRepositoryException {

  /**
   * 
   */
  private static final long serialVersionUID = -847932562471157259L;

  public UserLoginException(String user, Throwable cause) {
    super(user, cause);
  }

}
