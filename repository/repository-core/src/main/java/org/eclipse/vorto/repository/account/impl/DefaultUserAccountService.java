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

import java.util.Arrays;
import java.util.Collection;
import javax.transaction.Transactional;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.core.impl.cache.UserRolesRequestCache;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.services.RoleService;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.core.context.SecurityContextHolder;
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

  /**
   * Defines the minimum validation requirement for a subject string. <br/>
   * Set arbitrarily to 4+ alphanumeric characters for now. <br/>
   * This is and should be reflected in the front-end validation - see resource
   * {@literal createTechnicalUser.html}.
   */
  public static final String AUTHENTICATION_SUBJECT_VALIDATION_PATTERN = "^[a-zA-Z0-9]{4,}$";

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.eventPublisher = applicationEventPublisher;
  }


  @Transactional
  public User create(String username, String provider, String subject) {
    return create(username, provider, subject, false);
  }


  @Transactional
  public User create(String username, String provider, String subject, boolean isTechnicalUser) {
    if (cache.withUser(username).getUser() != null) {
      throw new IllegalArgumentException("User with given username already exists");
    }

    return userRepository.save(User.create(username, provider, subject, isTechnicalUser));
  }

  protected void addRolesOnNamespace(Namespace namespace, User existingUser, IRole[] userRoles) {
    User actor = userRepository
        .findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    Arrays.stream(userRoles).forEach(role -> {
      try {
        userNamespaceRoleService.addRole(actor, existingUser, namespace, role);
      } catch (OperationForbiddenException | DoesNotExistException e) {
        throw new IllegalStateException(e);
      }
    });
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

  public void saveUser(User user) {
    this.userRepository.save(user);
  }

}
