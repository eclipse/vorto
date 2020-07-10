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
import org.eclipse.vorto.repository.domain.RepositoryRole;
import org.eclipse.vorto.repository.init.DBTablesInitializer;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Yields all repository roles.<br/>
 * The repository is intended to be read-only at runtime, so no cache eviction strategy is used.<br/>
 * Still a {@link CrudRepository} because it can be populated at Vorto initialization by
 * {@link DBTablesInitializer}, if the table is found empty.
 */
@Repository
public interface RepositoryRoleRepository extends CrudRepository<RepositoryRole, Long> {

  @Query("select p from RepositoryRole p where p.name = :name")
  @Cacheable(value = "repositoryRoleCache")
  IRole find(@Param("name") String name);

  @Cacheable("repositoryRolesCache")
  Set<RepositoryRole> findAll();

  @Override
  @CacheEvict(value = {"repositoryRolesCache", "repositoryRoleCache"}, allEntries = true)
  <S extends RepositoryRole> S save(S s);

  @Override
  @CacheEvict(value = {"repositoryRolesCache", "repositoryRoleCache"}, allEntries = true)
  <S extends RepositoryRole> Iterable<S> save(Iterable<S> iterable);

  @Override
  @CacheEvict(value = {"repositoryRolesCache", "repositoryRoleCache"}, allEntries = true)
  void delete(Long aLong);

  @Override
  @CacheEvict(value = {"repositoryRolesCache", "repositoryRoleCache"}, allEntries = true)
  void delete(RepositoryRole repositoryRole);

  @Override
  @CacheEvict(value = {"repositoryRolesCache", "repositoryRoleCache"}, allEntries = true)
  void delete(Iterable<? extends RepositoryRole> iterable);

  @Override
  @CacheEvict(value = "repositoryRolesCache", allEntries = true)
  void deleteAll();
}
