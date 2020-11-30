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
package org.eclipse.vorto.repository.services;

import javax.transaction.Transactional;
import org.eclipse.vorto.repository.core.impl.cache.UserRolesRequestCache;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.RepositoryRole;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserRepositoryRoles;
import org.eclipse.vorto.repository.repositories.RepositoryRoleRepository;
import org.eclipse.vorto.repository.repositories.UserRepositoryRoleRepository;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryRoleService {

  private static final Logger LOGGER = Logger.getLogger(UserRepositoryRoleService.class);

  @Autowired
  private RepositoryRoleRepository repositoryRoleRepository;

  @Autowired
  private UserRepositoryRoleRepository userRepositoryRoleRepository;

  @Autowired
  private UserRolesRequestCache cache;

  public IRole sysadmin() {
    return repositoryRoleRepository.find(RepositoryRole.SYS_ADMIN.getName());
  }

  /**
   * @param user
   * @return whether the given {@link User} has the {@literal sysadmin} role.
   */
  public boolean isSysadmin(User user) {
    return
        cache
            .withUser(user)
            .getUserRepositoryRoles()
            .stream()
            .findAny()
            .map(urr -> (urr.getRoles() & sysadmin().getRole()) == sysadmin().getRole())
            .orElse(false);
  }

  /**
   *
   * @param dto
   * @return whether the target user has the {@literal sysadmin} repository role.
   * @see UserRepositoryRoleService#isSysadmin(User)
   */
  public boolean isSysadmin(UserDto dto) {
    try {
      return isSysadmin(cache.withUser(dto).getUser());
    }
    catch (DoesNotExistException dnee) {
      LOGGER.warn("Could not resolve user", dnee);
      return false;
    }
  }

  /**
   *
   * @return whether the authenticated user has the {@literal sysadmin} repository role.
   * @see UserRepositoryRoleService#isSysadmin(UserDto) for other users.
   */
  public boolean isAuthenticatedUserSysadmin() {
    try {
      return isSysadmin(cache.withSelf().getUser());
    }
    catch (DoesNotExistException dnee) {
      LOGGER.warn("Could not resolve user", dnee);
      return false;
    }
  }

  /**
   * Sets a user as {@literal sysadmin}. <br/>
   * Requires the acting user to be {@literal sysadmin}.
   *
   * @param dto
   * @return
   */
  @Transactional(rollbackOn = {DoesNotExistException.class, OperationForbiddenException.class})
  public void setSysadmin(UserDto dto) throws DoesNotExistException, OperationForbiddenException {
    User actor = cache.withSelf().getUser();
    if (!isSysadmin(actor)) {
      throw new OperationForbiddenException("Acting user is not sysadmin");
    }
    User user = cache.withUser(dto).getUser();
    if (isSysadmin(user)) {
      return;
    }
    updateOrInsertSysadminRole(user);
  }

  private void updateOrInsertSysadminRole(User user) {
    UserRepositoryRoles roles = cache
        .withUser(user)
        .getUserRepositoryRoles()
        .stream()
        .findAny()
        .orElseGet(
            () -> {
              UserRepositoryRoles result = new UserRepositoryRoles();
              result.setUser(user);
              return result;
            }
        );
    roles.setRoles(RepositoryRole.SYS_ADMIN.getRole());
    userRepositoryRoleRepository.save(roles);
  }
}
