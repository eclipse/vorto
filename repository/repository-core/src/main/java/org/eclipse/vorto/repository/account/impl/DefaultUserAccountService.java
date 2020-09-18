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
import javax.transaction.Transactional;
import org.eclipse.vorto.repository.core.impl.cache.UserRolesRequestCache;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.services.UserBuilder;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Service("userAccountService")
public class DefaultUserAccountService implements ApplicationEventPublisherAware {

  private UserRepository userRepository;

  private UserNamespaceRoleService userNamespaceRoleService;

  private UserRolesRequestCache cache;

  public DefaultUserAccountService(
      @Autowired UserRolesRequestCache cache,
      @Autowired UserRepository userRepository,
      @Autowired UserNamespaceRoleService userNamespaceRoleService) {
    this.cache = cache;
    this.userRepository = userRepository;
    this.userNamespaceRoleService = userNamespaceRoleService;
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
   * @param username
   * @param provider
   * @param subject
   * @return
   * @throws
   */
  @Transactional(rollbackOn = {IllegalArgumentException.class, IllegalStateException.class,
      InvalidUserException.class})
  public User createNonTechnicalUser(String username, String provider, String subject)
      throws InvalidUserException {
    if (cache.withUser(username).getUser() != null) {
      throw new IllegalArgumentException("User with given username already exists");
    }
    User saved = userRepository.save(
        new UserBuilder()
            .withName(username)
            .withAuthenticationProviderID(provider)
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

  public boolean exists(String userId) {
    return cache.withUser(userId).getUser() != null;
  }

  public User getUser(String username) {
    return cache.withUser(username).getUser();
  }

  public Collection<User> findUsers(String partial) {
    return this.userRepository.findUserByPartial(partial);
  }

}
