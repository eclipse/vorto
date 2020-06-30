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
package org.eclipse.vorto.repository.repositories;

import org.eclipse.vorto.repository.domain.RepositoryRole;
import org.eclipse.vorto.repository.domain.UserRepositoryRoles;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Yields all user-repository role associations, which is read-only at runtime and limited to
 * expressing sysadmin users at this time.<br/>
 * The repository is intended to be read-only at runtime after initialization, so no cache eviction
 * strategy is used.
 */
@Repository
@Cacheable("userRepositoryRoles")
public interface UserRepositoryRoleRepository extends CrudRepository<UserRepositoryRoles, Long> {
}
