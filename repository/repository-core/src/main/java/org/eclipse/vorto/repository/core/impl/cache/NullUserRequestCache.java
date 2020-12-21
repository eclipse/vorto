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

/**
 * This is a "view" on a {@link User}'s role data, when invoked by
 * {@link UserRolesRequestCache#withUser(org.eclipse.vorto.repository.web.account.dto.UserDto)}
 * or {@link UserRolesRequestCache#withSelf()} on
 * an instance of {@link UserRolesRequestCache}.<br/>
 * In this specific case, the {@link User} could not be resolved, because it was not found. <br/>
 * This matches some edge cases where user roles are queried after a {@link User} has been deleted.
 */
public class NullUserRequestCache implements IUserRequestCache {

  /**
   * @return an empty {@link java.util.List} of {@link UserNamespaceRoles}.
   */
  @Override
  public Collection<UserNamespaceRoles> getUserNamespaceRoles() {
    return Collections.emptyList();
  }

  /**
   * @return an empty {@link java.util.List} of {@link UserRepositoryRoles}.
   */
  @Override
  public Collection<UserRepositoryRoles> getUserRepositoryRoles() {
    return Collections.emptyList();
  }

  /**
   * @return {@code null}.
   */
  @Override
  public User getUser() {
    return null;
  }
}
