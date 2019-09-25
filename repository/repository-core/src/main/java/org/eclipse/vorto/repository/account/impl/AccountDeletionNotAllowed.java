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
package org.eclipse.vorto.repository.account.impl;

public class AccountDeletionNotAllowed extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 2931565983082782093L;

  public AccountDeletionNotAllowed(String arg0) {
    super(arg0);
  }

  public static AccountDeletionNotAllowed reason(String reason) {
    return new AccountDeletionNotAllowed(reason);
  }
  
}
