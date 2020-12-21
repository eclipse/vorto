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
package org.eclipse.vorto.repository.account.impl;

import java.util.Collection;
import java.util.Optional;
import javax.transaction.Transactional;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.cache.UserRolesRequestCache;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.oauth.IOAuthProvider;
import org.eclipse.vorto.repository.oauth.IOAuthProviderRegistry;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.services.UserBuilder;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Service("userAccountService")
public class DefaultUserAccountService implements ApplicationEventPublisherAware {

  private UserRepository userRepository;

  private UserNamespaceRoleService userNamespaceRoleService;

  private UserRolesRequestCache cache;

  private IOAuthProviderRegistry registry;

  public DefaultUserAccountService(
      @Autowired UserRolesRequestCache cache,
      @Autowired UserRepository userRepository,
      @Autowired UserNamespaceRoleService userNamespaceRoleService,
      @Autowired IOAuthProviderRegistry registry
  ) {
    this.cache = cache;
    this.userRepository = userRepository;
    this.userNamespaceRoleService = userNamespaceRoleService;
    this.registry = registry;
  }

  private ApplicationEventPublisher eventPublisher = null;

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.eventPublisher = applicationEventPublisher;
  }


  /**
   * Creates and persists a new, non-technical user contextually to a new user signing up.<br/>
   * The entity will be saved twice, so the generated ID can be used to populate the
   * {@code created_by} value.<br/>
   * Will fail is the user already exists or cannot be persisted for extraneous reasons.
   *
   * @param user
   * @param subject
   * @return
   * @throws
   */
  @Transactional(rollbackOn = {IllegalArgumentException.class, IllegalStateException.class,
      InvalidUserException.class})
  public User createNonTechnicalUser(UserDto user, String subject) throws InvalidUserException {
    try {
      cache.withUser(user);
      throw new IllegalArgumentException("User with given username already exists");
    }
    catch (DoesNotExistException dnee) {
      User saved = userRepository.save(
          new UserBuilder()
              .withName(user.getUsername())
              .withAuthenticationProviderID(user.getAuthenticationProvider())
              .withAuthenticationSubject(subject)
              .build()
      );
      if (null == saved) {
        throw new IllegalStateException("Could not persist new user");
      }
      // sets "created by" field once ID is generated and saves again
      saved.setCreatedBy(saved.getId());
      return userRepository.save(saved);
    }
  }

  /**
   * Saves an existing user. <br/>
   * The user must already exist, or the operation will fail.<br/>
   * This is typically invoked when a human user changes something in their account, e.g. their
   * e-mail address, although strictly speaking, there is nothing preventing this from being
   * invoked on a technical user either.
   *
   * @param user
   */
  public void updateUser(User user) {
    this.userRepository.save(user);
  }

  /**
   *
   * @param user
   * @return
   */
  public boolean exists(UserDto user) {
    try {
      return cache.withUser(user).getUser() != null;
    }
    catch (DoesNotExistException dnee) {
      return false;
    }
  }

  /**
   *
   * @param username
   * @return whether any user exists by the given username
   */
  public boolean anyExists(String username) {
    return registry
        .list()
        .stream()
        .map(IOAuthProvider::getId)
        .filter(
          id -> exists(UserDto.of(username, id))
        )
        .findAny()
        .map(u -> true)
        .orElse(false);
  }

  /**
   *
   * @param context
   * @return
   * @see DefaultUserAccountService#exists(UserDto)
   */
  public User getUser(IUserContext context) {
    return getUser(
        UserDto.of(
          context.getUsername(),
          registry.getByAuthentication(context.getAuthentication()).getId()
        )
    );
  }

  /**
   * 
   * @param authentication
   * @return
   * @see DefaultUserAccountService#exists(UserDto)
   */
  public User getUser(Authentication authentication) {
    return getUser(
        UserDto.of(
          authentication.getName(),
          registry.getByAuthentication(authentication).getId()
        )
    );
  }

  /**
   *
   * @param user
   * @return whether a user exists with the given wrapped username and authentication provider ID.
   */
  public User getUser(UserDto user) {
    try {
      return cache.withUser(user).getUser();
    }
    catch (DoesNotExistException dnee) {
      return null;
    }
  }

  /**
   * Returns a user by name only, only if there is a single user with that username.<br/>
   * It is recommended to invoke the finer-tuned overloads of this method instead.
   * @param username
   * @return
   */
  public Optional<User> getUser(String username) {
    Collection<User> users = userRepository.findByUsername(username);
    switch(users.size()) {
      case 1: {
        return users.stream().findAny();
      }
      default: {
        return Optional.empty();
      }
    }
  }

  /**
   * Proxy for {@link UserRepository#findById(long)}
   * @param id
   * @return
   */
  public Optional<User> getUser(long id) {
    return this.userRepository.findById(id);
  }

  /**
   *
   * @param partial
   * @return
   */
  public Collection<User> findUsers(String partial) {
    return this.userRepository.findUserByPartial(partial);
  }

}
