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
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserNamespaceRoles;
import org.eclipse.vorto.repository.domain.UserRepositoryRoles;
import org.eclipse.vorto.repository.oauth.IOAuthProviderRegistry;
import org.eclipse.vorto.repository.repositories.UserNamespaceRoleRepository;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.repositories.UserRepositoryRoleRepository;
import org.eclipse.vorto.repository.services.ServiceValidationUtil;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Request-scoped cache mapping {@link UserNamespaceRoles} and {@link UserRepositoryRoles} to
 * {@link User}s. <br/>
 * Invoked by services requiring a request instance in scope, such as
 * {@link org.eclipse.vorto.repository.services.UserNamespaceRoleService} and
 * {@link org.eclipse.vorto.repository.services.UserRepositoryRoleService} to cache data within
 * the request's lifecycle. <br/>
 * <b>Usage and specifications</b>
 * <br/>
 * An instance of {@link UserRolesRequestCache} is autowired to the service. <br/>
 * In the implementation of any method that resolves a {@link User} by username, or retrieves
 * a {@link User}'s {@link org.eclipse.vorto.repository.domain.NamespaceRole}s, or retrieves a
 * {@link User}'s {@link org.eclipse.vorto.repository.domain.RepositoryRole}s, the cache will be
 * used. <br/>
 * To avail of the cache, one must first invoke a {@code with...} method on an instance of
 * {@link UserRolesRequestCache}.<br/>
 * This will return a {@link IUserRequestCache} object, either a {@link UserRequestCache} if the
 * {@link User} is resolved, or a {@link NullUserRequestCache} if the {@link User} cannot be
 * resolved (e.g. has been deleted within the request). <br/>
 * The {@link UserRolesRequestCache#withSelf()} will also resolve the entity and cache it
 * for later invocations.<br/>
 * In turn, one can then invoke any or all of the following methods:
 * <ul>
 *   <li>
 *     {@link IUserRequestCache#getUser()} returns the mapped and resolved {@link User} - more
 *     useful if a {@code with...(String)} overload was invoked previously (rather than a
 *     {@code with...(User)} overload, since that would imply the context had already
 *     resolved the {@link User} or known about it by then).
 *   </li>
 *   <li>
 *     {@link IUserRequestCache#getUserNamespaceRoles()} returns a {@link Collection} (technically a
 *     {@link java.util.Set}) of existing {@link UserNamespaceRoles} associations for that user, and
 *     caches it when invoked for the first time ("lazy-loading"). Further invocations will use the
 *     cached data for the request's lifecycle.
 *   </li>
 *   <li>
 *     {@link IUserRequestCache#getUserRepositoryRoles()} returns a {@link Collection} (technically a
 *     {@link java.util.Set}) of existing {@link UserRepositoryRoles} associations for that user,
 *     and caches it when invoked for the first time ("lazy-loading"). Further invocations will use
 *     the cached data for the request's lifecycle.
 *   </li>
 * </ul>
 * The "main" cache (i.e. the autowired {@link UserRolesRequestCache} instance) supports caching multiple
 * {@link User}s and user-role associations within the same request, which can be useful in the
 * rare cases when multiple subsequent {@link User} and user roles resolutions are required within
 * the same request, e.g. in complex service methods involving both an acting {@link User} and a
 * target {@link User} which can be different.<br/>
 * <b>Correct example</b>
 * <br/>
 * <pre>
 * <code>
 * cache
 * // initializes or retrieves user request cache
 * .withUser(user)
 * // get user namespace roles for user
 * .getUserNamespaceRoles()
 * .stream()
 * // filters by given namespace
 * .filter(unr -> namespace.equals(unr.getNamespace()))
 * .findAny()
 * // roles found: returns roles long converted to Collection<IRole>
 * .map(unr -> roleUtil.toNamespaceRoles(unr.getRoles()))
 * // roles not found: returns an empty list
 * .orElse(Collections.emptyList());
 * </code>
 * </pre>
 * <br/>
 * <b>Incorrect example 1</b>
 * <br/>
 * <pre>
 * <code>
 * cache
 * // initializes or retrieves user request cache
 * .withUser(user1)
 * // attempts to invoke "withUser" on a UserRequestCache instance: does not compile
 * .withUser(user2)
 * ...
 * </code>
 * </pre>
 * <br/>
 * <b>Incorrect example 2</b>
 * <br/>
 * <pre>
 * <code>
 * cache
 * // attempts to retrieve roles on the main cache without user: does not compile
 * .getUserNamespaceRoles()
 * ...
 * </code>
 * </pre>
 * The same logic in this incorrect example applies to invocations of
 * {@link IUserRequestCache#getUserRepositoryRoles()} or {@link IUserRequestCache#getUser()}.
 * <br/>
 *
 * @see IUserRequestCache
 * @see UserRequestCache
 * @see NullUserRequestCache
 */
@Service
@RequestScope
public class UserRolesRequestCache {

  private IOAuthProviderRegistry registry;

  private UserNamespaceRoleRepository userNamespaceRoleRepository;

  private UserRepositoryRoleRepository userRepositoryRoleRepository;

  private UserRepository userRepository;

  /**
   * Constructor autowiring the required repositories for {@link User} resolution by name, as well
   * as {@link UserNamespaceRoles} and {@link UserRepositoryRoles} fetching by {@link User}.
   *
   * @param userNamespaceRoleRepository
   * @param userRepositoryRoleRepository
   * @param userRepository
   */
  public UserRolesRequestCache(@Autowired UserNamespaceRoleRepository userNamespaceRoleRepository,
      @Autowired UserRepositoryRoleRepository userRepositoryRoleRepository,
      @Autowired UserRepository userRepository, @Autowired IOAuthProviderRegistry registry) {
    this.userNamespaceRoleRepository = userNamespaceRoleRepository;
    this.userRepositoryRoleRepository = userRepositoryRoleRepository;
    this.userRepository = userRepository;
    this.registry = registry;
  }

  private Map<User, IUserRequestCache> cache = new ConcurrentHashMap<>();

  /**
   * @param user
   * @return either a {@link UserRequestCache} or a {@link NullUserRequestCache}
   */
  public IUserRequestCache withUser(User user) {
    if (null == user) {
      return new NullUserRequestCache();
    }
    cache.putIfAbsent(user,
        new UserRequestCache(userNamespaceRoleRepository, userRepositoryRoleRepository, user));
    return cache.get(user);
  }

  /**
   * Resolves the authenticated {@link User} by inferring the {@link Authentication} through
   * the {@link SecurityContextHolder}.<br/>
   * This usage is valid only when attempting to resolve the currently authenticated user - 
   * typically the "actor" in {@link org.eclipse.vorto.repository.services.UserNamespaceRoleService}. 
   * <br/>
   * It will also infer the correct OAuth provider ID.<br/>
   * When attempting to resolve another user (typically the "target" in 
   * {@link org.eclipse.vorto.repository.services.UserNamespaceRoleService}) instead, use 
   * {@link UserRolesRequestCache#withUser(org.eclipse.vorto.repository.web.account.dto.UserDto)}.
   *
   * @return either a {@link UserRequestCache} or a {@link NullUserRequestCache}
   */
  public IUserRequestCache withSelf() throws DoesNotExistException {
    // gets the right username and OAuth provider for the logged on user
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    String authenticationProviderID = registry
        .getByAuthentication(
          authentication
        )
        .getId();
    // attempts to resolve user if already cached
    Optional<User> maybeCachedUser = cache
        .keySet()
        .stream()
        .filter(
            u -> u.getUsername().equals(username) &&
            u.getAuthenticationProviderId().equals(authenticationProviderID)
        )
        .findAny();
    // found - returning cached value
    if (maybeCachedUser.isPresent()) {
      return cache.get(maybeCachedUser.get());
    }
    else {
      // retrieves the user entity from the repository by name and provider
      User user = userRepository
          .findByUsernameAndAuthenticationProviderId(username, authenticationProviderID)
          .orElseThrow(
              () -> new DoesNotExistException(
                  "Could not resolve a user with the given username and inferred OAuth provider ID"
              )
          );
      IUserRequestCache userCache = new UserRequestCache(
          userNamespaceRoleRepository,
          userRepositoryRoleRepository,
          user
      );
      if (null == userCache.getUser()) {
        return new NullUserRequestCache();
      }
      else {
        cache.putIfAbsent(userCache.getUser(), userCache);
      }
      return userCache;
    }
  }

  /**
   * Retrieves any given user by name and authentication provider ID, then returns the
   * {@link IUserRequestCache} associated with it.<br/>
   * Both properties of the given {@link UserDto} are validated.
   *
   * @param dto
   * @return
   */
  public IUserRequestCache withUser(UserDto dto) throws DoesNotExistException {
    // boilerplate null/empty validation
    ServiceValidationUtil.validateEmpties(dto.getUsername(), dto.getAuthenticationProvider());
    // searches for already cached users
    Optional<User> maybeCachedUser = cache
        .keySet()
        .stream()
        .filter(
          u -> u.getUsername().equals(dto.getUsername()) &&
          u.getAuthenticationProviderId().equals(dto.getAuthenticationProvider())
        )
        .findAny();
    // found - returning
    if (maybeCachedUser.isPresent()) {
      return cache.get(maybeCachedUser.get());
    }
    else {
      // retrieves the user entity from the repository by name and provider
      User user = userRepository
          .findByUsernameAndAuthenticationProviderId(dto.getUsername(), dto.getAuthenticationProvider())
          .orElseThrow(
              () -> new DoesNotExistException(
                  "Could not resolve a user with the given username and OAuth provider ID"
              )
          );
      // builds new cache
      IUserRequestCache userCache = new UserRequestCache(
          userNamespaceRoleRepository,
          userRepositoryRoleRepository,
          user
      );
      // user cannot be resolved - returns pseudo-cache
      if (null == userCache.getUser()) {
        return new NullUserRequestCache();
      }
      // user resolved - caching
      else {
        cache.putIfAbsent(userCache.getUser(), userCache);
      }
      return userCache;
    }
  }


  /**
   * @return
   * @throws IllegalStateException
   */
  public Collection<UserNamespaceRoles> getUserNamespaceRoles() {
    throw new IllegalStateException("Must invoke withUser first");
  }

  /**
   * @return
   * @throws IllegalStateException
   */
  public Collection<UserRepositoryRoles> getUserRepositoryRoles() {
    throw new IllegalStateException("Must invoke withUser first");
  }

  /**
   * @return
   * @throws IllegalStateException
   */
  public User getUser() {
    throw new IllegalStateException("Must invoke withUser first");
  }

}
