/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.account.impl;

import java.util.Collection;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public interface IUserRepository extends CrudRepository<User, Long> {
  /**
   * Finds the user by the specified username
   * 
   * @param username
   * @return
   */
  User findByUsername(String username);
  
  @Query("SELECT u from User u, TenantUser tu, UserRole r " + 
         "WHERE u.id = tu.user.id AND " +
         "tu.id = r.user.id AND " +
         "r.role = :role")
  Collection<User> findUsersWithRole(@Param("role") Role role); 
}
