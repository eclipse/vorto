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
package org.eclipse.vorto.repository.core.impl.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserNamespaceRoles;
import org.eclipse.vorto.repository.domain.UserRepositoryRoles;
import org.eclipse.vorto.repository.repositories.UserNamespaceRoleRepository;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.repositories.UserRepositoryRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRequestCache implements IRequestCache {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserRequestCache.class);

  private UserNamespaceRoleRepository userNamespaceRoleRepository;

  private UserRepositoryRoleRepository userRepositoryRoleRepository;

  private Collection<UserNamespaceRoles> userNamespaceRoles = new HashSet<>();
  private Collection<UserRepositoryRoles> userRepositoryRoles = new HashSet<>();
  private User user;

  protected UserRequestCache(UserNamespaceRoleRepository userNamespaceRoleRepository,
      UserRepositoryRoleRepository userRepositoryRoleRepository, User user) {
    if (null == user) {
      throw new IllegalArgumentException("Given user is null.");
    }
    this.userNamespaceRoleRepository = userNamespaceRoleRepository;
    this.userRepositoryRoleRepository = userRepositoryRoleRepository;
    this.user = user;
  }

  protected UserRequestCache(UserNamespaceRoleRepository userNamespaceRoleRepository,
      UserRepositoryRoleRepository userRepositoryRoleRepository, UserRepository userRepository,
      String username) {
    if (null == username) {
      throw new IllegalArgumentException("Given username is null.");
    }
    this.userNamespaceRoleRepository = userNamespaceRoleRepository;
    this.userRepositoryRoleRepository = userRepositoryRoleRepository;
    this.user = userRepository.findByUsername(username);
  }

  @Override
  public IRequestCache withUser(User user) {
    throw new UnsupportedOperationException(
        "Do not invoke withUser multiple times within the same invocation chain.");
  }

  @Override
  public IRequestCache withUser(String username) {
    throw new UnsupportedOperationException(
        "Do not invoke withUser multiple times within the same invocation chain.");
  }

  @Override
  public Collection<UserNamespaceRoles> getUserNamespaceRoles() {
    // setting UNR if not already set
    if (this.userNamespaceRoles.isEmpty()) {
      this.userNamespaceRoles.addAll(userNamespaceRoleRepository.findAllByUser(this.user));
    }
    return Collections.unmodifiableCollection(this.userNamespaceRoles);
  }

  @Override
  public Collection<UserRepositoryRoles> getUserRepositoryRoles() {
    // setting URR if not already set
    if (this.userRepositoryRoles.isEmpty()) {
      userRepositoryRoleRepository.findByUser(this.user.getId())
          .map(urr -> this.userRepositoryRoles.add(urr));
    }
    return Collections.unmodifiableCollection(this.userRepositoryRoles);
  }

  @Override
  public User getUser() {
    return this.user;
  }
}
