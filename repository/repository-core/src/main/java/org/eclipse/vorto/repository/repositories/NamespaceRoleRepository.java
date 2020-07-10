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

import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.NamespaceRole;
import org.eclipse.vorto.repository.init.DBTablesInitializer;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Read-only repository for namespace roles.<br/>
 * Caching here is written for read-only access, as for now, namespace roles are pretty much
 * constant and not intended to be modified at runtime. <br/>
 * Still a {@link CrudRepository} because it can be populated at Vorto initialization by
 * {@link DBTablesInitializer}, if the table is found empty.
 */
@Repository
public interface NamespaceRoleRepository extends CrudRepository<NamespaceRole, Long> {

  @Query("select n from NamespaceRole n where n.name = :name")
  @Cacheable("namespaceRoleCache")
  IRole find(@Param("name") String name);

  @Query("select case when count(n) > 0 then true else false end from NamespaceRole n where n.name = :role")
  @Cacheable("namespaceRoleCache")
  boolean exists(@Param("role") String roleName);

  @Cacheable(value = "namespaceRolesCache")
  Set<NamespaceRole> findAll();

  @Override
  @CacheEvict(value = {"namespaceRolesCache","namespaceRoleCache"}, allEntries = true)
  <S extends NamespaceRole> S save(S s);

  @Override
  @CacheEvict(value = {"namespaceRolesCache","namespaceRoleCache"}, allEntries = true)
  <S extends NamespaceRole> Iterable<S> save(Iterable<S> iterable);

  @Override
  @CacheEvict(value = {"namespaceRolesCache","namespaceRoleCache"}, allEntries = true)
  void delete(Long aLong);

  @Override
  @CacheEvict(value = {"namespaceRolesCache","namespaceRoleCache"}, allEntries = true)
  void delete(Iterable<? extends NamespaceRole> iterable);

  @Override
  @CacheEvict(value = {"namespaceRolesCache","namespaceRoleCache"}, allEntries = true)
  void delete(NamespaceRole namespaceRole);

  @Override
  @CacheEvict(value = {"namespaceRolesCache","namespaceRoleCache"}, allEntries = true)
  void deleteAll();

}
