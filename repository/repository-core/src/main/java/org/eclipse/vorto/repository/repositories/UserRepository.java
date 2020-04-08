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
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 * TODO #2265 better caching key vs eviction
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
  /**
   * Finds the user by the specified username
   * 
   * @param username
   * @return
   */
  @Cacheable("users")
  User findByUsername(String username);

  /**
   * Finds a list of users matching the given partial username.
   * @param partial
   * @return
   */
  @Cacheable("users")
  @Query("SELECT u from User u WHERE LOWER(u.username) LIKE %?1%")
  Collection<User> findUserByPartial(String partial);

  @Deprecated
  @Cacheable("users")
  @Query("SELECT u from User u, TenantUser tu, UserRole r " +
      "WHERE u.id = tu.user.id AND " +
      "tu.id = r.user.id AND " +
      "r.role = :role")
  Collection<User> findUsersWithRole(@Param("role") Role role);

  @CacheEvict(value = "users", allEntries = true)
  @Override
  <S extends User> S save(S entity);

  @CacheEvict(value = "users", allEntries = true)
  @Override
  void delete(User entity);

  @CacheEvict(value = "users", allEntries = true)
  @Override
  void delete(Long aLong);
}
