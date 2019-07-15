/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.TenantUser;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserRole;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.notification.message.DeleteAccountMessage;
import org.eclipse.vorto.repository.tenant.repository.ITenantRepository;
import org.eclipse.vorto.repository.tenant.repository.ITenantUserRepo;
import org.eclipse.vorto.repository.utils.PreConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Service("userAccountService")
public class DefaultUserAccountService
    implements IUserAccountService, ApplicationEventPublisherAware {

  @Value("${server.admin:#{null}}")
  private String[] admins;

  @Autowired
  private IUserRepository userRepository;

  @Autowired
  private INotificationService notificationService;

  @Autowired
  private ITenantRepository tenantRepo;

  @Autowired
  private ITenantUserRepo tenantUserRepo;

  private ApplicationEventPublisher eventPublisher = null;

  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.eventPublisher = applicationEventPublisher;
  }

  public boolean removeUserFromTenant(String tenantId, String userId) {
    PreConditions.notNullOrEmpty(tenantId, "tenantId");
    PreConditions.notNullOrEmpty(userId, "userId");

    Tenant tenant = tenantRepo.findByTenantId(tenantId);

    PreConditions.notNull(tenant, "Tenant with given tenantId doesnt exists");

    Optional<TenantUser> maybeUser = tenant.getUser(userId);
    if (maybeUser.isPresent()) {
      tenant.removeUser(maybeUser.get());
    }

    tenantRepo.save(tenant);

    return true;
  }

  public boolean addUserToTenant(String tenantId, String userId, Role... roles) {
    PreConditions.notNullOrEmpty(tenantId, "tenantId");
    PreConditions.notNullOrEmpty(userId, "userId");
    PreConditions.notNullOrEmpty(roles, "roles should not be empty");

    Tenant tenant = tenantRepo.findByTenantId(tenantId);

    PreConditions.notNull(tenant, "Tenant with given tenantId doesnt exists");

    Optional<TenantUser> maybeUser = tenant.getUser(userId);
    if (maybeUser.isPresent()) {
      TenantUser user = maybeUser.get();
      
      Set<UserRole> oldUserRolesToRemove =
          user.getRoles().stream()
          	.filter(userRole -> userRole.getRole() != Role.SYS_ADMIN)
          	.collect(Collectors.toSet());
      
      oldUserRolesToRemove.forEach(_userRole -> user.removeRole(_userRole));
      
      user.addRoles(roles);
      tenantUserRepo.save(user);
      eventPublisher.publishEvent(new AppEvent(this, userId, EventType.USER_MODIFIED));
    } else {
      User user = userRepository.findByUsername(userId);
      TenantUser tenantUser = TenantUser.createTenantUser(tenant, user, roles);
      tenantUserRepo.save(tenantUser);
      eventPublisher.publishEvent(new AppEvent(this, userId, EventType.USER_ADDED));
    }
    
    return true;
  }

  public Collection<Tenant> getTenantsOfUser(String userId) {
    PreConditions.notNullOrEmpty(userId, "userId");

    User user = getUser(userId);
    if (user == null) {
      return Collections.emptyList();
    }

    return user.getTenantUsers().stream().map(tenantUser -> tenantUser.getTenant())
        .collect(Collectors.toList());
  }
  
  public boolean hasRole(String tenantId, Authentication authentication, String role) {
    PreConditions.notNull(authentication, "authentication should not be null");
    return hasRole(tenantId, authentication.getName(), role);
  }
  
  public boolean hasRole(String tenantId, String username, String role) {
    PreConditions.notNullOrEmpty(tenantId, "tenantId");
    PreConditions.notNullOrEmpty(role, "role");
    PreConditions.notNullOrEmpty(username, "username");
    
    if (!Role.exist(role)) {
      throw new IllegalArgumentException("role must be in Role enum");
    }
    
    Tenant tenant = tenantRepo.findByTenantId(tenantId);
    PreConditions.notNull(tenant, "Tenant with tenantId" + tenantId + " doesnt exists");
    
    Optional<TenantUser> user = tenant.getUser(username);
    if (user.isPresent()) {
      TenantUser tenantUser = user.get();
      return tenantUser.hasRole(Role.valueOf(role.replace(Role.rolePrefix, "")));
    }
    
    return false;
  }

  @Transactional
  public User create(String username) {
    if (userRepository.findByUsername(username) != null) {
      throw new IllegalArgumentException("User with given username already exists");
    }

    return userRepository.save(User.create(username));
  }

  @Transactional
  public User create(String username, String tenantId, Role... userRoles)
      throws RoleNotSupportedException {
    
    PreConditions.notNullOrEmpty(username, "username");
    PreConditions.notNullOrEmpty(tenantId, "username");
    
    Tenant tenant = tenantRepo.findByTenantId(tenantId);

    PreConditions.notNull(tenant, "Tenant with given tenantId doesnt exists");
    
    User existingUser = userRepository.findByUsername(username);

    if (existingUser != null) {
      addUserToTenant(tenantId, username, userRoles);
      return existingUser;
    } else {
      User user = create(username);
      TenantUser tenantUser = TenantUser.createTenantUser(tenant, userRoles);
      user.addTenantUser(tenantUser);
      return userRepository.save(user);
    }
  }

  @Transactional
  public User removeUserRole(String userName, String tenantId, List<Role> roles) {

    User user = userRepository.findByUsername(userName);
    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException("User Not Found: " + userName);
    }
    Set<UserRole> userRoles = user.getRoles(tenantId);

    roles.forEach(role -> {
      userRoles.removeIf(e -> role == e.getRole());
    });

    user.setRoles(tenantId, userRoles);

    return userRepository.save(user);
  }

  @Override
  @Transactional
  public void delete(final String userId) {
    User userToDelete = userRepository.findByUsername(userId);

    if (userToDelete != null) {
      userRepository.delete(userToDelete);
      eventPublisher.publishEvent(new AppEvent(this, userId, EventType.USER_DELETED));
      if (userToDelete.hasEmailAddress()) {
        notificationService.sendNotification(new DeleteAccountMessage(userToDelete));
      }
    }
  }

  @Override
  public boolean exists(String userId) {
    return userRepository.findByUsername(userId) != null;
  }

  @Override
  public User getUser(String username) {
    return this.userRepository.findByUsername(username);
  }

  @Override
  public void saveUser(User user) {
    this.userRepository.save(user);
  }
  
  @Override
  public Collection<User> getSystemAdministrators() {
    return userRepository.findUsersWithRole(Role.SYS_ADMIN);
  }

  public IUserRepository getUserRepository() {
    return userRepository;
  }

  public void setUserRepository(IUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public INotificationService getNotificationService() {
    return notificationService;
  }
  
  public void setNotificationService(INotificationService notificationService) {
    this.notificationService = notificationService;
  }

  public void setTenantUserRepo(ITenantUserRepo tenantUserRepo) {
    this.tenantUserRepo = tenantUserRepo;
  }

  public void setTenantRepo(ITenantRepository tenantRepo) {
    this.tenantRepo = tenantRepo;
  }
}
