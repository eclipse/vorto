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

import java.util.Collection;
import java.util.Set;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserNamespaceID;
import org.eclipse.vorto.repository.domain.UserNamespaceRoles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Holds user-namespace role associations.
 * TODO #2265 better caching key vs eviction
 */
@Repository
public interface UserNamespaceRoleRepository extends
    CrudRepository<UserNamespaceRoles, UserNamespaceID> {

  @Query("select unr from UserNamespaceRoles unr where unr.id.namespace = :namespace")
  Collection<UserNamespaceRoles> findAllByNamespace(@Param("namespace") Namespace namespace);

  @Query("select unr from UserNamespaceRoles unr where unr.id.user = :user")
  Collection<UserNamespaceRoles> findAllByUser(@Param("user") User user);

  @Query("select unr from UserNamespaceRoles unr where unr.id.namespace = :namespace and unr.roles = :roles")
  Collection<UserNamespaceRoles> findAllByNamespaceAndRoles(@Param("namespace") Namespace namespace,
      @Param("roles") long roles);

  Set<UserNamespaceRoles> findAll();
}
