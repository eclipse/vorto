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

import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.eclipse.vorto.repository.domain.*;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.notification.message.DeleteAccountMessage;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.services.RoleService;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.eclipse.vorto.repository.tenant.repository.ITenantRepository;
import org.eclipse.vorto.repository.tenant.repository.ITenantUserRepo;
import org.eclipse.vorto.repository.utils.PreConditions;
import org.eclipse.vorto.repository.web.api.v1.dto.ICollaborator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

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
  private UserRepository userRepository;

  @Autowired
  private INotificationService notificationService;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private ITenantRepository tenantRepo;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private ITenantUserRepo tenantUserRepo;

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

  @Override
  /**
   * Attempts to remove user with given userId from "tenant". <br/>
   * @param tenantId the tenant from which to remove the user
   * @param userId the user to be removed
   * @return {@literal true} if the user was present, {@literal false} otherwise.
   */
  @Deprecated
  public boolean removeUserFromTenant(String tenantId, String userId) {
    PreConditions.notNullOrEmpty(tenantId, "tenantId");
    PreConditions.notNullOrEmpty(userId, "userId");

    Tenant tenant = tenantRepo.findByTenantId(tenantId);

    PreConditions.notNull(tenant, "Tenant with given tenantId doesnt exists");

    Optional<TenantUser> maybeUser = tenant.getUser(userId);

    boolean userIsPresent = false;

    if (maybeUser.isPresent()) {
      userIsPresent = true;
      tenant.removeUser(maybeUser.get());
    }

    tenantRepo.save(tenant);

    return userIsPresent;
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
   * @param user
   * @param roles
   * @return
   */
  @Override
  public boolean createTechnicalUserAndAddToTenant(String tenantId, String userId, ICollaborator user, Role... roles) {

    String authenticationProviderId = user.getAuthenticationProviderId();
    String subject = user.getSubject();

    Tenant tenant = validateAndReturnTenant(tenantId, userId, authenticationProviderId, roles);

    // additional validation for authentication provider id and subject
    PreConditions.notNullOrEmpty(authenticationProviderId, "authenticationProviderId");
    PreConditions.withPattern(AUTHENTICATION_SUBJECT_VALIDATION_PATTERN, subject, "subject");

    // this creates and persists the technical user
    User userToCreate = User.create(userId, authenticationProviderId, subject, true);
    userRepository.save(userToCreate);

    // this creates and persists the "tenant user"
    TenantUser tenantUserToCreate = TenantUser.createTenantUser(tenant, userToCreate, roles);
    tenantUserRepo.save(tenantUserToCreate);

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
   * sibling method here {@link DefaultUserAccountService#createTechnicalUserAndAddToTenant(String, String, ICollaborator, Role...)},
   * upon administrative user confirmation that they want to create a technical user instead.
   * @param tenantId the tenant to add this user to
   * @param userId the user id
   * @param roles the roles to be given to the user
   * @return
   */
  @Override
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

      oldUserRolesToRemove.forEach(user::removeRole);

      user.addRoles(roles);
      tenantUserRepo.save(user);
      eventPublisher.publishEvent(new AppEvent(this, userId, EventType.USER_MODIFIED));
    }
    else {
      User user = userRepository.findByUsername(userId);
      // at this point the user cannot be null
      if (user == null) {
        LOGGER.warn(
          String.format(
            "Aborting operation to add existing user [%s] to the [%s] namespace, because the user does not exist.", userId, tenant.getDefaultNamespace()
          )
        );
        return false;
      }
      TenantUser tenantUser = TenantUser.createTenantUser(tenant, user, roles);
      tenantUserRepo.save(tenantUser);
      eventPublisher.publishEvent(new AppEvent(this, userId, EventType.USER_ADDED));
    }
    
    return true;
  }

  @Override
  public Collection<Tenant> getTenantsOfUser(String userId) {
    PreConditions.notNullOrEmpty(userId, "userId");

    User user = getUser(userId);
    if (user == null) {
      return Collections.emptyList();
    }

    return user.getTenantUsers().stream().map(TenantUser::getTenant)
            .collect(Collectors.toList());
  }

  @Deprecated
  @Override
  public boolean hasRole(String tenantId, Authentication authentication, String role) {
    PreConditions.notNull(authentication, "authentication should not be null");
    return hasRole(tenantId, authentication.getName(), role);
  }

  @Deprecated
  @Override
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

  @Override
  @Transactional
  public User create(String username, String provider, String subject) {
    return create(username, provider, subject, false);
  }

  @Override
  @Transactional
  public User create(String username, String provider, String subject, boolean isTechnicalUser) {
    if (userRepository.findByUsername(username) != null) {
      throw new IllegalArgumentException("User with given username already exists");
    }

    return userRepository.save(User.create(username, provider, subject, isTechnicalUser));
  }

  @Override
  @Transactional
  public User createOrUpdate(String username, String provider, String subject, String tenantId, IRole... userRoles)
          throws RoleNotSupportedException {
    return createOrUpdate(username, provider, subject, false, tenantId, userRoles);
  }

  @Override
  @Transactional
  public User createOrUpdate(String username, String provider, String subject, boolean isTechnicalUser, String workspaceId,
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
    User actor = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    Arrays.stream(userRoles).forEach(role -> {
      try {
        userNamespaceRoleService.addRole(actor, existingUser, namespace, role);
      } catch (OperationForbiddenException | DoesNotExistException e) {
        throw new IllegalStateException(e);
      }
    });
  }

  @Override
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

  @Override
  @Transactional
  public Collection<Tenant> getTenants(User user) {
    return userRepository.findOne(user.getId()).getTenants();
  }

  @Override
  @Transactional
  public Set<Role> getRoles(User user, String tenantId) {
    return userRepository.findOne(user.getId()).getUserRoles(tenantId);
  }

  @Override
  @Transactional
  public Set<Role> getAllRoles(User user) {
    return userRepository.findOne(user.getId()).getAllRoles();
  }

  private boolean deleteWillOrphanTenants(User userToDelete) {
    for (Tenant tenant : tenantRepo.findAll()) {
      if (tenant != null &&
              tenant.hasTenantAdmin(userToDelete.getUsername()) &&
              tenant.getTenantAdmins().size() <= 1) {
        return true;
      }
    }
    return false;
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

  public void setTenantUserRepo(ITenantUserRepo tenantUserRepo) {
    this.tenantUserRepo = tenantUserRepo;
  }

  public void setTenantRepo(ITenantRepository tenantRepo) {
    this.tenantRepo = tenantRepo;
  }
}
