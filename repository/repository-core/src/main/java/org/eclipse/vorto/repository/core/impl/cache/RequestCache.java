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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserNamespaceRoles;
import org.eclipse.vorto.repository.domain.UserRepositoryRoles;
import org.eclipse.vorto.repository.repositories.UserNamespaceRoleRepository;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.repositories.UserRepositoryRoleRepository;
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
// TODO map by users and don't fail fast if a request involves > 1 user
@Service
@RequestScope
public class RequestCache implements IRequestCache {

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

  private Map<User, IRequestCache> cache = new ConcurrentHashMap<>();

  @Override
  public IRequestCache withUser(User user) {
    if (null == user) {
      return new NullUserCache();
    }
    cache.putIfAbsent(user,
        new UserRequestCache(userNamespaceRoleRepository, userRepositoryRoleRepository, user));
    return cache.get(user);
  }

  @Override
  public IRequestCache withUser(String username) {
    IRequestCache userCache = new UserRequestCache(userNamespaceRoleRepository,
        userRepositoryRoleRepository, userRepository, username);
    if (null == userCache.getUser()) {
      return new NullUserCache();
    }
    cache.putIfAbsent(userCache.getUser(), userCache);
    return cache.get(userCache.getUser());
  }

  @Override
  public Collection<UserNamespaceRoles> getUserNamespaceRoles() {
    throw new UnsupportedOperationException("Must invoke withUser first");
  }

  @Override
  public Collection<UserRepositoryRoles> getUserRepositoryRoles() {
    throw new UnsupportedOperationException("Must invoke withUser first");
  }

  @Override
  public User getUser() {
    throw new UnsupportedOperationException("Must invoke withUser first");
  }

}
