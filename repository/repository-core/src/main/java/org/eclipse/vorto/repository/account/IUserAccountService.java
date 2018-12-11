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
package org.eclipse.vorto.repository.account;

import java.util.List;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public interface IUserAccountService {

  /**
   * creates a new account in the Vorto Repository
   * 
   * @param username
   * @return createdUser
   */
  User create(String username);

  /**
   * create a new user with roles in Vorto Repository
   * 
   * @param username
   * @param userRoles
   * @return
   */
  public User create(String username, Role... userRoles);

  /**
   * 
   * @param username
   * @return
   */
  User getUser(String username);

  /**
   * 
   * @param user
   */
  void saveUser(User user);

  /**
   * @post Information Models that had been created by the user are made anonymous
   * 
   *       Removes the account for the given user
   * @param userId
   */
  void delete(String userId);

  /**
   * Checks if the account for the given user exists.
   * 
   * @param userId
   * @return
   */
  boolean exists(String userId);

  /**
   * 
   * @return
   */
  String getAnonymousUserId();

  /**
   * Remove role from user
   * 
   * @param userName
   * @param roles
   * @return
   */
  User removeUserRole(String userName, List<Role> roles);
}
