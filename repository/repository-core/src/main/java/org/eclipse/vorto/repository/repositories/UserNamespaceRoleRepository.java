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
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserNamespaceID;
import org.eclipse.vorto.repository.domain.UserNamespaceRoles;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Holds user-namespace role associations.
 * TODO #2265 caching and eviction are ok-ish but the eviction is a bit drastic - not easy to fine-tune due to queries ńot able to use same key
 */
@Repository
public interface UserNamespaceRoleRepository extends
    CrudRepository<UserNamespaceRoles, UserNamespaceID> {

  @Cacheable("userNamespaceRoles")
  @Override
  Iterable<UserNamespaceRoles> findAll();

  @Cacheable(value = "userNamespaceRoles")
  @Query("select unr from UserNamespaceRoles unr where unr.id.namespace = :namespace")
  Collection<UserNamespaceRoles> findAllByNamespace(@Param("namespace") Namespace namespace);

  @Cacheable(value = "userNamespaceRoles")
  @Query("select unr from UserNamespaceRoles unr where unr.id.user = :user")
  Collection<UserNamespaceRoles> findAllByUser(@Param("user") User user);

  @Cacheable(value = "userNamespaceRoles")
  @Query("select unr from UserNamespaceRoles unr where unr.id.user = :user and unr.roles = :roles")
  Collection<UserNamespaceRoles> findAllByUserAndRoles(@Param("user") User user, @Param("roles") long roles);

  @Cacheable(value = "userNamespaceRoles")
  @Query("select unr from UserNamespaceRoles unr where unr.id.namespace = :namespace and unr.roles = :roles")
  Collection<UserNamespaceRoles> findAllByNamespaceAndRoles(@Param("namespace") Namespace namespace, @Param("roles") long roles);

  @CacheEvict(value = "userNamespaceRoles", allEntries = true)
  @Override
  void delete(UserNamespaceID userNamespaceID);

  @CacheEvict(value = "userNamespaceRoles", allEntries = true)
  @Override
  <S extends UserNamespaceRoles> S save(S entity);
}
