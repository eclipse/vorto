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
import org.eclipse.vorto.repository.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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
  User findByUsername(String username);

  /**
   * Finds a list of users matching the given partial username.
   * @param partial
   * @return
   */
  @Query("SELECT u from User u WHERE LOWER(u.username) LIKE %?1%")
  Collection<User> findUserByPartial(String partial);

  @Override
  <S extends User> S save(S entity);

  @Override
  void delete(User entity);

  @Override
  void delete(Long aLong);

  @Override
  Collection<User> findAll();
}
