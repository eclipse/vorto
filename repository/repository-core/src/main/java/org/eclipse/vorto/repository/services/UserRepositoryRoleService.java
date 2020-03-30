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
import org.eclipse.vorto.repository.repositories.RepositoryRoleRepository;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.repositories.UserRepositoryRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryRoleService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RepositoryRoleRepository repositoryRoleRepository;

  @Autowired
  private UserRepositoryRoleRepository userRepositoryRoleRepository;

  //

  public boolean isSysadmin(User user) {
    /*UserRepositoryRoles roles = new UserRepositoryRoles();
    roles.setUser(user);
    roles.setRoles(sysadmin.getRole());*/

    return false;//userRepositoryRoleRepository.getByUserandRole(user, sysadmin) != null;
  }

}
