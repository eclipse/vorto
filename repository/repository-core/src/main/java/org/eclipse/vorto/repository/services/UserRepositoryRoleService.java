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

import org.eclipse.vorto.repository.core.impl.cache.RequestCache;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.RepositoryRole;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserRepositoryRoles;
import org.eclipse.vorto.repository.repositories.RepositoryRoleRepository;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.repositories.UserRepositoryRoleRepository;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
public class UserRepositoryRoleService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RepositoryRoleRepository repositoryRoleRepository;

  @Autowired
  private UserRepositoryRoleRepository userRepositoryRoleRepository;

  @Autowired
  private RequestCache cache;

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
   * @param username
   * @return
   * @see UserRepositoryRoleService#isSysadmin(User)
   */
  public boolean isSysadmin(String username) {
    return isSysadmin(cache.withUser(username).getUser());
  }

  /**
   * Sets a user as sysadmin.
   *
   * @param username
   * @return
   */
  @Transactional
  public void setSysadmin(String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new IllegalArgumentException("User is null");
    }
    if (isSysadmin(user)) {
      return;
    }
    updateOrInsertSysadminRole(user);
  }

  private void updateOrInsertSysadminRole(User user) {
    UserRepositoryRoles roles = userRepositoryRoleRepository.findOne(user.getId());
    if (roles == null) {
      roles = new UserRepositoryRoles();
      roles.setRoles(RepositoryRole.SYS_ADMIN.getRole());
      roles.setUser(user);
    } else {
      roles.setRoles(RepositoryRole.SYS_ADMIN.getRole());
    }
    userRepositoryRoleRepository.save(roles);
  }
}
