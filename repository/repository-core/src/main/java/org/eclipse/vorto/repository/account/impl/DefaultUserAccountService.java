/**
 * Copyright (c) 2018, 2019 Contributors to the Eclipse Foundation
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

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private IUserRepository userRepository;

  @Autowired
  private INotificationService notificationService;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private ITenantRepository tenantRepo;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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

  /**
   * This method does to extraneous things - ugly but convenient to centralize some validation logic:
   * <ul>
   *   <li>
   *     Validates all "id" string parameters and fails if any invalid ({@literal null} or empty).
   *   </li>
   *   <li>
   *     Validates that the given roles are not empty.
   *   </li>
   *   <li>
   *     Attempts to find an existing tenant (namespace) with the given id, and fails if not found.
   *   </li>
   *   <li>
   *     If found, returns the tenant (namespace).
   *   </li>
   * </ul>
   * With the current usage, it's probably not worth re-writing / separating scopes.
   *
   * @param tenantId
   * @param userId
   * @param authenticationProviderId
   * @param roles
   * @return
   */
  private Tenant validateAndReturnTenant(String tenantId, String userId, String authenticationProviderId, Role... roles) {
    PreConditions.notNullOrEmpty(tenantId, "tenantId");
    PreConditions.notNullOrEmpty(userId, "userId");
    PreConditions.notNullOrEmpty(roles, "roles should not be empty");
    if (authenticationProviderId != null) {
      if (authenticationProviderId.trim().isEmpty()) {
        throw new IllegalArgumentException("Given authentication provider cannot be empty.");
      }
    }
    Tenant tenant = tenantRepo.findByTenantId(tenantId);
    PreConditions.notNull(tenant, "Tenant with given tenantId does not exist");
    return tenant;
  }

  /**
   * This is invoked through a {@code POST} request in the {@link org.eclipse.vorto.repository.web.account.AccountController},
   * when an administrator wants to add a new technical user to a given namespace. <br/>
   * @see DefaultUserAccountService#addUserToTenant(String, String, Role...) for situations where the
   * user exists already, instead.
   * @param tenantId
   * @param userId
   * @param authenticationProviderId
   * @param roles
   * @return
   */
  public boolean createTechnicalUserAndAddToTenant(String tenantId, String userId, String authenticationProviderId, Role... roles) {

    Tenant tenant = validateAndReturnTenant(tenantId, userId, authenticationProviderId, roles);

    // additional validation for authentication provider id
    PreConditions.notNullOrEmpty(authenticationProviderId, "authenticationProviderId");

    // this creates and persists the technical user
    User user = User.create(userId, authenticationProviderId, null, true);
    userRepository.save(user);
    // this creates and persists the "tenant user"
    TenantUser tenantUser = TenantUser.createTenantUser(tenant, user, roles);
    tenantUserRepo.save(tenantUser);
    eventPublisher.publishEvent(new AppEvent(this, userId, EventType.USER_ADDED));
    return true;
  }

  /**
   * As the name implies, adds a given user to the given tenant/namespace, with the given roles.<br/>
   * As the name does <b>not</b> imply, the given user represented by the {@code userId} parameter
   * must exist.<br/>
   * For the purpose of ensuring the user exists, the front-end will first invoke a user search. <br/>
   * If the user is found, then this method gets eventually invoked after the {@code PUT} method is
   * called in the {@link org.eclipse.vorto.repository.web.account.AccountController}.<br/>
   * If the user search returns {@literal 404}, then the {@code POST} method is invoked instead in
   * the {@link org.eclipse.vorto.repository.web.account.AccountController}, which may end up invoking
   * sibling method here {@link DefaultUserAccountService#createTechnicalUserAndAddToTenant(String, String, String, Role...)},
   * upon administrative user confirmation that they want to create a technical user instead.
   * @param tenantId the tenant to add this user to
   * @param userId the user id
   * @param roles the roles to be given to the user
   * @return
   */
  public boolean addUserToTenant(String tenantId, String userId, Role... roles) {

    // cannot validate authentication provider within context
    Tenant tenant = validateAndReturnTenant(tenantId, userId, null, roles);

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
    PreConditions.notNull(tenant, "Tenant with tenantId " + tenantId + " doesnt exists");
    
    Optional<TenantUser> user = tenant.getUser(username);
    if (user.isPresent()) {
      TenantUser tenantUser = user.get();
      return tenantUser.hasRole(Role.valueOf(role.replace(Role.rolePrefix, "")));
    }
    
    return false;
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
  public User createOrUpdate(String username, String provider, String subject, String tenantId, Role... userRoles)
      throws RoleNotSupportedException {
    return createOrUpdate(username, provider, subject, false, tenantId, userRoles);
  }
  
  @Transactional
  public User createOrUpdate(String username, String provider, String subject, boolean isTechnicalUser, String tenantId, Role... userRoles)
      throws RoleNotSupportedException {
    
    PreConditions.notNullOrEmpty(username, "username");
    PreConditions.notNullOrEmpty(tenantId, "tenantId");
    
    Tenant tenant = tenantRepo.findByTenantId(tenantId);

    PreConditions.notNull(tenant, "Tenant with given tenantId doesnt exists");
    
    User existingUser = userRepository.findByUsername(username);

    if (existingUser != null) {
      addUserToTenant(tenantId, username, userRoles);
      return existingUser;
    } else {
      User user = create(username, provider, subject, isTechnicalUser);
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
      
      if (deleteWillOrphanTenants(userToDelete)) {
        throw AccountDeletionNotAllowed.reason("Deleting this user will orphan some tenants.");
      }
      
      eventPublisher.publishEvent(new AppEvent(this, userId, EventType.USER_DELETED));
      userRepository.delete(userToDelete);
      if (userToDelete.hasEmailAddress()) {
        notificationService.sendNotification(new DeleteAccountMessage(userToDelete));
      }
    }
  }

  private boolean deleteWillOrphanTenants(User userToDelete) {
    for(Tenant tenant : tenantRepo.findAll()) {
      if (tenant != null &&
          tenant.hasTenantAdmin(userToDelete.getUsername()) &&
          tenant.getTenantAdmins().size() <= 1) {
        return true;
      }
    }
    
    return false;
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
  public Collection<User> findUsers(String partial) {
    return this.userRepository.findUserByPartial(partial);
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
