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
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.transaction.Transactional;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserRole;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.notification.message.DeleteAccountMessage;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.services.RoleService;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.eclipse.vorto.repository.utils.PreConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Service("userAccountService")
public class DefaultUserAccountService implements ApplicationEventPublisherAware {

  @Value("${server.admin:#{null}}")
  private String[] admins;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private INotificationService notificationService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private UserNamespaceRoleService userNamespaceRoleService;

  @Autowired
  private NamespaceService namespaceService;

  private ApplicationEventPublisher eventPublisher = null;

  private static final Logger LOGGER = Logger.getLogger(DefaultUserAccountService.class);

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


  @Deprecated
  public boolean hasRole(String tenantId, Authentication authentication, String role) {
    PreConditions.notNull(authentication, "authentication should not be null");
    return hasRole(tenantId, authentication.getName(), role);
  }

  @Deprecated
  public boolean hasRole(String workspaceId, String username, String role) {
    PreConditions.notNullOrEmpty(workspaceId, "workspaceId");
    PreConditions.notNullOrEmpty(role, "role");
    PreConditions.notNullOrEmpty(username, "username");

    String roleWithoutPrefix = role.replace(Role.rolePrefix, "");
    if (!roleService.findAnyByName(roleWithoutPrefix).isPresent()) {
      return false;
    }
    String namespace = namespaceService.findNamespaceByWorkspaceId(workspaceId).getName();

    try {
      return userNamespaceRoleService.getRoles(username, namespace)
          .stream()
          .map(IRole::getName)
          .anyMatch(roleWithoutPrefix::equals);
    } catch (DoesNotExistException e) {
      throw new IllegalStateException("Error retrieving roles.", e);
    }
  }


  @Transactional
  public User create(String username, String provider, String subject) {
    return create(username, provider, subject, false);
  }


  @Transactional
  public User create(String username, String provider, String subject, boolean isTechnicalUser) {
    if (userRepository.findByUsername(username) != null) {
      throw new IllegalArgumentException("User with given username already exists");
    }

    return userRepository.save(User.create(username, provider, subject, isTechnicalUser));
  }

  @Transactional
  public User createOrUpdate(String username, String provider, String subject, String tenantId,
      IRole... userRoles)
      throws RoleNotSupportedException {
    return createOrUpdate(username, provider, subject, false, tenantId, userRoles);
  }

  @Transactional
  public User createOrUpdate(String username, String provider, String subject,
      boolean isTechnicalUser, String workspaceId,
      IRole... userRoles)
      throws RoleNotSupportedException {

    PreConditions.notNullOrEmpty(username, "username");
    PreConditions.notNullOrEmpty(workspaceId, "workspaceId");

    Namespace namespace = namespaceService.findNamespaceByWorkspaceId(workspaceId);
    PreConditions.notNull(namespace, "Namespace with given workspaceId does not exist");

    User existingUser = userRepository.findByUsername(username);
    if (existingUser != null) {
      addRolesOnNamespace(namespace, existingUser, userRoles);
      return existingUser;
    } else {
      User user = create(username, provider, subject, isTechnicalUser);
      addRolesOnNamespace(namespace, user, userRoles);
      return userRepository.save(user);
    }
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

  @Transactional
  public User removeUserRole(String userName, String tenantId, List<Role> roles) {
    User user = userRepository.findByUsername(userName);
    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException("User Not Found: " + userName);
    }
    Set<UserRole> userRoles = user.getRoles(tenantId);
    roles.forEach(role -> userRoles.removeIf(e -> role == e.getRole()));
    user.setRoles(tenantId, userRoles);

    return userRepository.save(user);
  }

  public boolean exists(String userId) {
    return userRepository.findByUsername(userId) != null;
  }

  public User getUser(String username) {
    return this.userRepository.findByUsername(username);
  }

  public Collection<User> findUsers(String partial) {
    return this.userRepository.findUserByPartial(partial);
  }

  public void saveUser(User user) {
    this.userRepository.save(user);
  }

  public Collection<User> getSystemAdministrators() {
    return userRepository.findUsersWithRole(Role.SYS_ADMIN);
  }

  @Transactional
  public Collection<Tenant> getTenants(User user) {
    return userRepository.findOne(user.getId()).getTenants();
  }

  @Transactional
  public Set<Role> getRoles(User user, String tenantId) {
    return userRepository.findOne(user.getId()).getUserRoles(tenantId);
  }

  @Transactional
  public Set<Role> getAllRoles(User user) {
    return userRepository.findOne(user.getId()).getAllRoles();
  }

  public UserRepository getUserRepository() {
    return userRepository;
  }

  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public INotificationService getNotificationService() {
    return notificationService;
  }

  public void setNotificationService(INotificationService notificationService) {
    this.notificationService = notificationService;
  }
}
