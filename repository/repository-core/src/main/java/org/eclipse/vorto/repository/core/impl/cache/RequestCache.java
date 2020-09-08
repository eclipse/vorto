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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserNamespaceRoles;
import org.eclipse.vorto.repository.domain.UserRepositoryRoles;
import org.eclipse.vorto.repository.repositories.UserNamespaceRoleRepository;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.repositories.UserRepositoryRoleRepository;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Provides a request-scoped cache for:
 * <ul>
 *   <li>
 *     Read operations on user-namespace roles
 *   </li>
 *   <li>
 *     Read operations on user-repository roles
 *   </li>
 *   <li>
 *     Lookup operations on a specific user by name
 *   </li>
 * </ul>
 * <br/>
 * Only one instance of the cache (i.e. for one user) must be created within a request. <br/>
 * This implies that operations involving an acting user and a target user (typically, write
 * operations), or operations reading data for multiple users do not user the cache at all. <br/>
 * This avoids the eventuality of multiple caches per request, at the cost of making (mostly) write
 * operations more costly.<br/>
 * That is acceptable, since read operations outweigh write operations by far in frequency and
 * redundancy, due to the authorization architecture integrating table-based roles with ModeShape
 * sessions.<br/>
 * <b>Usage:</b><br/>
 * {@code cache.withUser(user).get...} or {@code cache.withUser(username).get...}.<br/>
 * The method {@link RequestCache#withUser(User)} or its overload <b>must always</b> be invoked
 * <i>prior</i> to any other operation.<br/>
 * The first invocation within a request will populate the cache. <br/>
 * Subsequent invocations will return the cached data contextually to the request's lifespan.<br/>
 * Future development of this cache could use a map of all data currently cached by {@link User},
 * so that the cache could be allowed also in operations involving multiple users (e.g. the "actor"
 * vs "target" paradigm found in write operations in the
 * {@link org.eclipse.vorto.repository.services.UserNamespaceRoleService}, or operations reading
 * roles for multiple users, etc.).
 */
@Service
@RequestScope
public class RequestCache {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestCache.class);

  private UserNamespaceRoleRepository userNamespaceRoleRepository;

  private UserRepositoryRoleRepository userRepositoryRoleRepository;

  private UserRepository userRepository;

  public RequestCache(@Autowired UserNamespaceRoleRepository userNamespaceRoleRepository,
      @Autowired UserRepositoryRoleRepository userRepositoryRoleRepository,
      @Autowired UserRepository userRepository) {
    this.userNamespaceRoleRepository = userNamespaceRoleRepository;
    this.userRepositoryRoleRepository = userRepositoryRoleRepository;
    this.userRepository = userRepository;
  }

  private Collection<UserNamespaceRoles> userNamespaceRoles = ConcurrentHashMap.newKeySet();
  private Collection<UserRepositoryRoles> userRepositoryRoles = ConcurrentHashMap.newKeySet();
  private AtomicReference<User> user = new AtomicReference<>();

  /**
   * Initializes or retrieves this cache with the given {@link User}.
   *
   * @param user
   * @return RequestCache for chained builder-style invocations.
   * @throws DoesNotExistException is the given {@link User} is not found.
   */
  public RequestCache withUser(User user) {
    // setting user if not already set
    // cannot use compareAndSet as it would eagerly invoke the repository query method
    if (this.user.get() == null) {
      this.user.set(userRepository.findOne(user.getId()));
    }
    // checks the cache is invoked with the same user as previously initialized, or fail fast
    if (!getUser().equals(user)) {
      LOGGER.error(
          "Attempting to re-initialize same request cache with a different user - arguably this should never happen."
      );
      throw new IllegalStateException("Attempting to initialize with different user");
    }
    if (null == getUser()) {
      throw new IllegalStateException("User is null");
    }
    // setting UNR if not already set
    if (this.userNamespaceRoles.isEmpty()) {
      this.userNamespaceRoles.addAll(userNamespaceRoleRepository.findAllByUser(this.user.get()));
    }
    // setting URR if not already set
    if (this.userRepositoryRoles.isEmpty()) {
      userRepositoryRoleRepository.findByUser(this.user.get().getId())
          .map(urr -> this.userRepositoryRoles.add(urr));
    }
    return this;
  }

  /**
   * Initializes or retrieves this cache with the given user name.
   *
   * @param username
   * @return
   * @throws DoesNotExistException is the {@link User} associated with the given name is not found.
   * @see RequestCache#withUser(User)
   */
  public RequestCache withUser(String username) {
    // setting user if not already set
    // cannot use compareAndSet as it would eagerly invoke the repository query method
    if (this.user.get() == null) {
      this.user.set(userRepository.findByUsername(username));
    }
    return withUser(this.user.get());
  }

  /**
   * @return an unmodifiable collection of user-namespace roles.
   */
  public Collection<UserNamespaceRoles> getUserNamespaceRoles() {
    if (null == getUser()) {
      LOGGER.error(
          "User is null. Either this was invoked without a prior invocation of 'withUser', or the latter has thrown DoesNotExistException and was not properly intercepted"
      );
      throw new IllegalStateException("User is null");
    }
    return Collections.unmodifiableCollection(this.userNamespaceRoles);
  }

  /**
   * @return an unmodifiable collection of user-repository roles.
   */
  public Collection<UserRepositoryRoles> getUserRepositoryRoles() {
    if (null == getUser()) {
      LOGGER.error(
          "User is null. Either this was invoked without a prior invocation of 'withUser', or the latter has thrown DoesNotExistException and was not properly intercepted"
      );
      throw new IllegalStateException("User is null");
    }
    return Collections.unmodifiableCollection(this.userRepositoryRoles);
  }

  /**
   * @return the looked-up user.
   */
  public User getUser() {
    return this.user.get();
  }

}
