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
package org.eclipse.vorto.repository.core.impl.cache;

import java.util.Collection;
import java.util.Collections;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserNamespaceRoles;
import org.eclipse.vorto.repository.domain.UserRepositoryRoles;

public class NullUserCache implements IRequestCache {

  @Override
  public IRequestCache withUser(User user) {
    throw new UnsupportedOperationException(
        "Do not invoke withUser multiple times within the same invocation chain.");
  }

  @Override
  public IRequestCache withUser(String username) {
    throw new UnsupportedOperationException(
        "Do not invoke withUser multiple times within the same invocation chain.");
  }

  @Override
  public Collection<UserNamespaceRoles> getUserNamespaceRoles() {
    return Collections.emptyList();
  }

  @Override
  public Collection<UserRepositoryRoles> getUserRepositoryRoles() {
    return Collections.emptyList();
  }

  @Override
  public User getUser() {
    return null;
  }
}
