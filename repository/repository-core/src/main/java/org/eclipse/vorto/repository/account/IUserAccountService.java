/**
 * Copyright (c) 2018, 2019 Contributors to the Eclipse Foundation
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
import java.util.Set;

import javax.transaction.Transactional;

import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.web.account.dto.TenantTechnicalUserDto;
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
   * Finds a list of users matching the given partial username.
   * @param partial
   * @return
   */
  Collection<User> findUsers(String partial);

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
   * Creates a technical user with the given id, authentication provider and roles, then adds it to
   * the given tenant.
   * @param tenantId
   * @param userId
   * @param user
   * @param roles
   * @return always {@literal true} (fails with runtime exception if some operation failed).
   */
  boolean createTechnicalUserAndAddToTenant(String tenantId, String userId, TenantTechnicalUserDto user, Role... roles);

  /**
   * Returns if the particular user as the role in the Tenant
   *
   * @param tenantId the tenant to check 
   * @param authentication the authentication
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
   * Creates a new NON-TECHNICAL user in the Vorto repository but without membership in any namespace
   *
   * @param username
   * @param provider
   * @param subject
   * @return
   */
  User create(String username, String provider, String subject);

  /**
   * Creates a new user in the Vorto repository but without membership in any namespace
   *
   * @param username
   * @param provider
   * @param subject
   * @param isTechnicalUser
   * @return
   */
  User create(String username, String provider, String subject, boolean isTechnicalUser);

  /**
   * Creates a new NON-TECHNICAL user in the Vorto repository, and adds him as a collaborator to @tenantId with the roles @userRoles
   *
   * @param username
   * @param tenantId
   * @param provider
   * @param userRoles
   * @return
   */
  User createOrUpdate(String username, String provider, String subject, String tenantId, Role... userRoles);

  /**
   * Creates a new user in the Vorto repository, and adds him as a collaborator to @tenantId with the roles @userRoles
   *
   * @param username
   * @param provider
   * @param subject
   * @param isTechnicalUser
   * @param tenantId
   * @param userRoles
   * @return
   */
  User createOrUpdate(String username, String provider, String subject, boolean isTechnicalUser, String tenantId, Role... userRoles);

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
   */
  User removeUserRole(String userName, String tenantId, List<Role> roles);

  Collection<Tenant> getTenants(User user);

  Set<Role> getRoles(User user, String tenantId);

  @Transactional
  Set<Role> getAllRoles(User user);
}
