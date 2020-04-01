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

import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserRepositoryRoles;
import org.eclipse.vorto.repository.repositories.RepositoryRoleRepository;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.repositories.UserRepositoryRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryRoleService {

  @Autowired
  private UserRepository userRepository;

  private RepositoryRoleRepository repositoryRoleRepository;

  @Autowired
  private UserRepositoryRoleRepository userRepositoryRoleRepository;

  private IRole sysadmin;

  @Autowired
  public UserRepositoryRoleService(RepositoryRoleRepository repositoryRoleRepository) {
    this.repositoryRoleRepository = repositoryRoleRepository;
    sysadmin = repositoryRoleRepository.find("sysadmin");
  }

  /**
   * @param user
   * @return whether the given {@link User} has the {@literal sysadmin} role.
   */
  public boolean isSysadmin(User user) {

    UserRepositoryRoles userRepositoryRoles = userRepositoryRoleRepository.findOne(user.getId());
    // no explicit repository roles for this user - they are not sysadmin
    if (userRepositoryRoles == null) {
      return false;
    }
    // should never happen until the table is broken
    if (sysadmin != null) {
      return (userRepositoryRoles.getRoles() & sysadmin.getRole()) == sysadmin.getRole();
    }
    return false;
  }

  /**
   * @param username
   * @return
   * @see UserRepositoryRoleService#isSysadmin(User)
   */
  public boolean isSysadmin(String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new IllegalArgumentException("User is null");
    }
    return isSysadmin(user);
  }

}
