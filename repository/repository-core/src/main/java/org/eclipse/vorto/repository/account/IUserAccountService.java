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

import java.util.Collection;
import java.util.List;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.springframework.security.core.Authentication;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public interface IUserAccountService {
  
  /**
   * 
   * @return users who are system administrators
   */
  Collection<User> getSystemAdministrators();
  
  /**
   * 
   * @param tenantId the tenant from which to remove the user
   * @param userId the user to be removed
   * @return
   */
  boolean removeUserFromTenant(String tenantId, String userId);
  
  /**
   * Adds the specific userId with the given roles to the given tenant.
   * @param tenantId the tenant to add this user to
   * @param userId the user id
   * @param roles the roles to be given to the user
   * @return status if the user was added successfully
   */
  boolean addUserToTenant(String tenantId, String userId, Role ... roles);
  
  /**
   * Returns if the particular user as the role in the Tenant
   * 
   * @param tenantId the tenant to check 
   * @param userId the user id
   * @param role the role to check (e.g ROLE_TENANT_ADMIN, ROLE_USER,..)
   * @return
   */
  boolean hasRole(String tenantId, Authentication authentication, String role);
  
  /**
   * Returns if the particular user as the role in the Tenant
   * 
   * @param tenantId the tenant to check 
   * @param username the userId of the user
   * @param role the role to check (e.g ROLE_TENANT_ADMIN, ROLE_USER,..)
   * @return
   */
  boolean hasRole(String tenantId, String username, String role);
  
  /**
   * Gets all tenant for this user
   * 
   * @param userId
   * @return
   */
  Collection<Tenant> getTenantsOfUser(String userId);
  
  /**
   * creates a new account in the Vorto Repository, under the Playground tenant
   * 
   * @param username
   * @return createdUser
   */
  User create(String username);

  /**
   * create a new user with roles in Vorto Repository, under the Playground tenant
   * 
   * @param username
   * @param userRoles
   * @return
   */
  public User create(String username, String tenantId, Role... userRoles);
  
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
   * Remove role from user
   * 
   * @param userName
   * @param roles
   * @return
   */
  User removeUserRole(String userName, String tenantId, List<Role> roles);
}
