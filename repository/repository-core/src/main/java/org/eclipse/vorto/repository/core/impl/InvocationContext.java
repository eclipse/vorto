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
package org.eclipse.vorto.repository.core.impl;

import org.eclipse.vorto.repository.core.IUserContext;

public final class InvocationContext {

  private IUserContext userContext;

  public static InvocationContext create(IUserContext userContext) {
    return new InvocationContext(userContext);
  }

  private InvocationContext(IUserContext userContext) {
    this.userContext = userContext;
  }

  public IUserContext getUserContext() {
    return userContext;
  }
}
