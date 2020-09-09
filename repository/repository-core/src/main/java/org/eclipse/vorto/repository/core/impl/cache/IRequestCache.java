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
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserNamespaceRoles;
import org.eclipse.vorto.repository.domain.UserRepositoryRoles;

/**
 * Common interface for all {@link User} resolution, {@link UserNamespaceRoles} and
 * {@link UserRepositoryRoles} retrieval caches. <br/>
 * Implemented by {@link UserRequestCache} for {@link User}-specific windows and
 * {@link NullUserRequestCache} for {@link User}-specific windows when the user does not exist
 * (anymore).<br/>
 * See {@link RequestCache} implementation for usage details.
 */
public interface IRequestCache {

  Collection<UserNamespaceRoles> getUserNamespaceRoles();

  Collection<UserRepositoryRoles> getUserRepositoryRoles();

  User getUser();
}
