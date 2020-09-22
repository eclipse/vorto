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

/**
 * This is a "view" on a {@link User}'s role data, returned as an instance of {@link IUserRequestCache}
 * when invoked by {@link UserRolesRequestCache#withUser(User)} or {@link UserRolesRequestCache#withUser(String)}.<br/>
 * The latter overload will also resolve the {@link User} by name. <br/>
 * Initial invocations of {@link IUserRequestCache#getUserNamespaceRoles()} and
 * {@link IUserRequestCache#getUserRepositoryRoles()} on this object will retrieve the
 * {@link UserNamespaceRoles} and {@link UserRepositoryRoles} respectively and cache them.<br/>
 * Subsequent invocations of those methods on this object will use the cached data.
 */
public class UserRequestCache implements IUserRequestCache {

  private UserNamespaceRoleRepository userNamespaceRoleRepository;

  private UserRepositoryRoleRepository userRepositoryRoleRepository;

  private Collection<UserNamespaceRoles> userNamespaceRoles = new HashSet<>();
  private Collection<UserRepositoryRoles> userRepositoryRoles = new HashSet<>();
  private User user;

  /**
   * Invoked by {@link UserRolesRequestCache#withUser(String)}
   *
   * @param userNamespaceRoleRepository
   * @param userRepositoryRoleRepository
   * @param user
   */
  protected UserRequestCache(UserNamespaceRoleRepository userNamespaceRoleRepository,
      UserRepositoryRoleRepository userRepositoryRoleRepository, User user) {
    if (null == user) {
      throw new IllegalArgumentException("Given user is null.");
    }
    this.userNamespaceRoleRepository = userNamespaceRoleRepository;
    this.userRepositoryRoleRepository = userRepositoryRoleRepository;
    this.user = user;
  }

  /**
   * Invoked by {@link UserRolesRequestCache#withUser(String)}
   *
   * @param userNamespaceRoleRepository
   * @param userRepositoryRoleRepository
   * @param userRepository
   * @param username
   */
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

  /**
   * Caches the {@link User}'s {@link UserNamespaceRoles} when invoked for the first time, and
   * returns them.<br/>
   * Returns the cached data when invoked furtherly within the same request.
   *
   * @return
   */
  @Override
  public Collection<UserNamespaceRoles> getUserNamespaceRoles() {
    // setting UNR if not already set
    if (this.userNamespaceRoles.isEmpty()) {
      this.userNamespaceRoles.addAll(userNamespaceRoleRepository.findAllByUser(this.user));
    }
    return Collections.unmodifiableCollection(this.userNamespaceRoles);
  }

  /**
   * Caches the {@link User}'s {@link UserRepositoryRoles} when invoked for the first time, and
   * returns them.<br/>
   * Returns the cached data when invoked furtherly within the same request.
   *
   * @return
   */
  @Override
  public Collection<UserRepositoryRoles> getUserRepositoryRoles() {
    // setting URR if not already set
    if (this.userRepositoryRoles.isEmpty()) {
      userRepositoryRoleRepository.findByUser(this.user.getId())
          .map(urr -> this.userRepositoryRoles.add(urr));
    }
    return Collections.unmodifiableCollection(this.userRepositoryRoles);
  }

  /**
   * Caches the {@link User} when invoked for the first time, and returns it. <br/>
   * Returns the cached {@link User} when invoked furtherly within the same request.<br/>
   * Can be useful when the user was initially retrieved by username.
   *
   * @return
   */
  @Override
  public User getUser() {
    return this.user;
  }
}
