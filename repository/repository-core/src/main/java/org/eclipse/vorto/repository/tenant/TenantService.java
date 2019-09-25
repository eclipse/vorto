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
package org.eclipse.vorto.repository.tenant;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.eclipse.vorto.repository.domain.AuthenticationProvider;
import org.eclipse.vorto.repository.domain.AuthorizationProvider;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.TenantUser;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.tenant.repository.INamespaceRepository;
import org.eclipse.vorto.repository.tenant.repository.ITenantRepository;
import org.eclipse.vorto.repository.utils.PreConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Component
public class TenantService implements ITenantService, ApplicationEventPublisherAware {

  private static final String TENANT_ID = "tenantId";

  private static final Logger logger = LoggerFactory.getLogger(TenantService.class);

  private ITenantRepository tenantRepo;

  private INamespaceRepository namespaceRepo;

  private IUserAccountService userAccountService;
  
  private ApplicationEventPublisher eventPublisher = null;
  
  @Value("${config.restrictTenant}")
  private String restrictTenantConfig;

  public TenantService(@Autowired ITenantRepository tenantRepo,
      @Autowired INamespaceRepository namespaceRepo,
      @Autowired IUserAccountService accountService) {
    this.tenantRepo = tenantRepo;
    this.namespaceRepo = namespaceRepo;
    this.userAccountService = accountService;
  }

  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.eventPublisher = applicationEventPublisher;
  }

  public boolean tenantExist(String tenantId) {
    return tenantRepo.findByTenantId(tenantId) != null;
  }

  public boolean conflictsWithExistingNamespace(String namespace) {
    List<Namespace> namespaces = Lists.newArrayList(namespaceRepo.findAll());
    return namespaces.stream().anyMatch(ns -> ns.isInConflictWith(namespace));
  }

  @Transactional
  public Tenant createOrUpdateTenant(String tenantId, String defaultNamespace,
      Set<String> tenantAdmins, Optional<Set<String>> namespaces,
      Optional<String> authenticationProvider, Optional<String> authorizationProvider,
      IUserContext userContext) {

    PreConditions.notNullOrEmpty(tenantId, TENANT_ID);
    PreConditions.notNull(userContext, "userContext should not be null");
    PreConditions.notNullOrEmpty(userContext.getUsername(), "userContext.getUsername()");
    PreConditions.notNullOrEmpty(defaultNamespace, "defaultNamespace");
    PreConditions.notNullOrEmpty(tenantAdmins, "tenantAdmins should not be null or empty.");
    
    for (String tenantAdmin : tenantAdmins) {
      PreConditions.notNullOrEmpty(tenantAdmin, "tenantAdmin");
      if (!userAccountService.exists(tenantAdmin)) {
        throw new TenantAdminDoesntExistException(tenantAdmin);
      }
    }

    User owner = userAccountService.getUser(userContext.getUsername());
    PreConditions.notNull(owner, "owner does not exist!");

    Tenant tenant = tenantRepo.findByTenantId(tenantId);
    EventType eventType = EventType.TENANT_ADDED;
    
    if (tenant == null) {
      logger.info("Adding new tenant '{}'", tenantId);
      if (!userContext.isSysAdmin() && overTenantLimit(owner, restrictTenantConfig)) {
        throw new RestrictTenantPerOwnerException(owner.getUsername(), restrictTenantConfig);
      }
      
      tenant = newTenant(tenantId, defaultNamespace, namespaces,
          authenticationProvider.orElse(null), authorizationProvider.orElse(null), userContext);
      tenantAdmins.add(owner.getUsername());
      Set<TenantUser> newTenantAdmins = createNewTenantAdmins(tenantAdmins, tenant);
      tenant.setUsers(newTenantAdmins);
    } else {
      if (!userContext.isSysAdmin() && !tenant.hasTenantAdmin(userContext.getUsername())) {
        throw new UpdateNotAllowedException(userContext.getUsername(), tenant.getTenantId());
      }

      logger.info("Updating tenant '{}' - {}", tenantId, tenant);
      updateTenant(tenant, defaultNamespace, namespaces,
          authenticationProvider.orElse(null), authorizationProvider.orElse(null), userContext);

      updateTenantAdmins(tenantAdmins, tenant);
      eventType = EventType.TENANT_UPDATED;
    }

    tenant = tenantRepo.save(tenant);

    logger.info("Sending update/create event for {}", tenantId);
    eventPublisher.publishEvent(new AppEvent(this, tenant, userContext, eventType));

    return tenant;
  }
  
  private boolean overTenantLimit(User owner, String restrictTenantConfig) {
    if (restrictTenantConfig != null) {
      return getTenantCountOfUser(owner) >= Integer.parseInt(restrictTenantConfig);
    }
    return false;
  }

  private int getTenantCountOfUser(User owner) {
    Collection<Tenant> tenants = getTenants();
    return (int) tenants.stream()
        .filter(tenant -> tenant.hasTenantAdmin(owner.getUsername()))
        .count();
  }

  private void updateTenantAdmins(Set<String> newTenantAdmins, Tenant tenant) {
    Set<String> tenantUsers = tenant.getUsers().stream().map(user -> user.getUser().getUsername())
        .collect(Collectors.toSet());
    Set<String> oldTenantAdmins =
        tenant.getTenantAdmins().stream().map(User::getUsername).collect(Collectors.toSet());
    Set<String> tenantUserButNotAdmin = Sets.difference(tenantUsers, oldTenantAdmins);
    Set<String> totallyNewAdmins = Sets.difference(newTenantAdmins, tenantUsers);
    Set<String> upgradedToAdmins = Sets.intersection(newTenantAdmins, tenantUserButNotAdmin);
    Set<String> demotedToUsers = Sets.difference(oldTenantAdmins, newTenantAdmins);

    totallyNewAdmins.forEach(admin -> {
      logger.info("Adding new tenant_admin '{}' to Tenant '{}'", admin, tenant.getTenantId());
      User user = userAccountService.getUser(admin);
      tenant.addUser(TenantUser.createTenantUser(user, Role.TENANT_ADMIN, Role.USER,
          Role.MODEL_CREATOR, Role.MODEL_PROMOTER, Role.MODEL_REVIEWER));
    });

    upgradedToAdmins.forEach(admin -> {
      logger.info("Upgrading user '{}' in Tenant '{}' to tenant_admin", admin,
          tenant.getTenantId());
      Optional<TenantUser> upgradedUser = tenant.getUser(admin);
      if (upgradedUser.isPresent()) {
        upgradedUser.get().addRoles(Role.TENANT_ADMIN);
      }
    });

    demotedToUsers.forEach(user -> {
      logger.info("Removing tenant_admin rights from user '{}' in Tenant '{}'", user,
          tenant.getTenantId());
      Optional<TenantUser> maybeDemotedUser = tenant.getUser(user);
      if (maybeDemotedUser.isPresent()) {
        TenantUser demotedUser = maybeDemotedUser.get();
        demotedUser.getRoles().removeIf(userRole -> userRole.getRole() == Role.TENANT_ADMIN);
        if (demotedUser.getRoles().isEmpty()) {
          tenant.removeUser(demotedUser);
        }
      }
    });
  }

  private Set<TenantUser> createNewTenantAdmins(Collection<String> tenantAdmins, Tenant tenant) {
    Set<TenantUser> tenantAdminUsers = Sets.newHashSet();
    for (String tenantAdminId : tenantAdmins) {
      User user = userAccountService.getUser(tenantAdminId);
      tenantAdminUsers.add(TenantUser.createTenantUser(tenant, user, Role.TENANT_ADMIN, Role.USER,
          Role.MODEL_CREATOR, Role.MODEL_PROMOTER, Role.MODEL_REVIEWER));
    }
    return tenantAdminUsers;
  }

  public Optional<Tenant> getTenant(String tenantId) {
    if (tenantId == null) {
      throw new IllegalArgumentException("tenantId should not be null");
    }

    return Optional.ofNullable(tenantRepo.findByTenantId(tenantId));
  }

  public Optional<Tenant> getTenantFromNamespace(String namespace) {
    PreConditions.notNullOrEmpty(namespace, "namespace");
    return getTenants().stream().filter(tn -> tn.owns(namespace)).findFirst();
  }

  @Transactional
  public Collection<Tenant> getTenants() {
    return Lists.newArrayList(tenantRepo.findAll());
  }

  public boolean updateTenantNamespaces(String tenantId, Set<String> namespaces, IUserContext userContext) {
    PreConditions.notNullOrEmpty(tenantId, TENANT_ID);
    PreConditions.notNullOrEmpty(namespaces, "namespaces");

    Tenant tenant = tenantRepo.findByTenantId(tenantId);
    if (tenant != null) {
      updateTenantNamespace(tenant, namespaces, userContext);

      tenantRepo.save(tenant);

      return true;
    }

    return false;
  }
  
  public void updateTenantNamespace(Tenant tenant, Set<String> namespaces, IUserContext user) {
    PreConditions.notNull(tenant, TENANT_ID);
    PreConditions.notNull(namespaces, "namespaces");
    
    Set<String> oldNamespaces =
        tenant.getNamespaces().stream().map(Namespace::getName).collect(Collectors.toSet());
    Set<String> removedNamespaces = Sets.difference(oldNamespaces, namespaces);
    Set<String> newNamespaces = Sets.difference(namespaces, oldNamespaces);
    
    if (!user.isSysAdmin()) {
      checkForPrivatePrefix(newNamespaces);
    }
    
    // We should check if new namespaces is superset of old namespaces to make sure
    // we are not removing any namespace from the old namespaces that are already
    // being used by the models of the users of this tenant
    checkIfSuperset(namespaces, tenant);

    // Make sure namespaces are unique across tenants
    checkForConflict(namespaces, namespaceOwnedByAnotherTenant(tenant));
    
    removedNamespaces.forEach(ns -> tenant.removeNamespace(ns));
    newNamespaces.forEach(ns -> tenant.addNamespace(Namespace.newNamespace(ns)));
  }

  public boolean addNamespacesToTenant(String tenantId, Set<String> namespaces) {
    PreConditions.notNullOrEmpty(tenantId, TENANT_ID);
    PreConditions.notNullOrEmpty(namespaces, "namespaces");

    Tenant tenant = tenantRepo.findByTenantId(tenantId);
    if (tenant != null) {
      Set<String> oldNamespaces =
          tenant.getNamespaces().stream().map(Namespace::getName).collect(Collectors.toSet());
      
      Set<String> commonNamespaces = Sets.intersection(namespaces, oldNamespaces);
      if (!commonNamespaces.isEmpty()) {
        throw new NamespaceExistException(commonNamespaces);
      }
      
      Set<String> newNamespaces =
          checkForConflict(namespaces, namespaceOwnedByAnotherTenant(tenant));
      tenant.getNamespaces().addAll(Namespace.toNamespace(newNamespaces, tenant));
      tenantRepo.save(tenant);
    }

    return false;
  }

  public boolean deleteTenant(Tenant tenant, IUserContext userContext) {
    PreConditions.notNull(tenant, "Tenant should not be null");
        
    eventPublisher.publishEvent(new AppEvent(this, tenant, userContext, EventType.TENANT_DELETED));
    
    tenantRepo.delete(tenant);

    return true;
  }

  private Tenant updateTenant(Tenant tenant, String defaultNamespace,
      Optional<Set<String>> namespaces, String authenticationProvider, String authorizationProvider,
      IUserContext user) {

    Set<String> tenantNamespaces = combine(namespaces, defaultNamespace);

    updateTenantNamespace(tenant, tenantNamespaces, user);

    tenant.setDefaultNamespace(defaultNamespace);
    tenant.setAuthenticationProvider(
        getAuthenticationProvider(authenticationProvider, tenant.getAuthenticationProvider()));
    tenant.setAuthorizationProvider(
        getAuthorizationProvider(authorizationProvider, tenant.getAuthorizationProvider()));
    return tenant;
  }

  private Predicate<String> namespaceOwnedByAnotherTenant(Tenant tenant) {
    return namespace -> !tenant.hasNamespace(namespace)
        && conflictsWithExistingNamespace(namespace);
  }

  private Tenant newTenant(String tenantId, String defaultNamespace,
      Optional<Set<String>> namespaces, String authenticationProvider, String authorizationProvider,
      IUserContext user) {

    Set<String> tenantNamespaces = combine(namespaces, defaultNamespace);
    if (!user.isSysAdmin()) {
      checkForPrivatePrefix(tenantNamespaces);
    }
    
    checkForConflict(tenantNamespaces, this::conflictsWithExistingNamespace);

    Tenant tenant = Tenant.newTenant(tenantId, defaultNamespace, tenantNamespaces);
    tenant.setAuthenticationProvider(
        getAuthenticationProvider(authenticationProvider, AuthenticationProvider.GITHUB));
    tenant.setAuthorizationProvider(
        getAuthorizationProvider(authorizationProvider, AuthorizationProvider.DB));

    return tenant;
  }

  private void checkForPrivatePrefix(Set<String> tenantNamespaces) {
    List<String> invalidNamespaces = tenantNamespaces.stream().filter(ns -> !isPrivateNamespace(ns))
        .collect(Collectors.toList());

    if (!invalidNamespaces.isEmpty()) {
      throw new NewNamespaceNotPrivateException(invalidNamespaces);
    }
  }

  private boolean isPrivateNamespace(String namespace) {
    return namespace.startsWith(Namespace.PRIVATE_NAMESPACE_PREFIX)
        && !namespace.equals(Namespace.PRIVATE_NAMESPACE_PREFIX);
  }

  private Set<String> checkIfSuperset(Set<String> newNamespaces, Tenant tenant) {
    List<String> oldNamespaces =
        tenant.getNamespaces().stream().map(Namespace::getName).collect(Collectors.toList());
    if (!newNamespaces.containsAll(oldNamespaces)) {
      throw new NewNamespacesNotSupersetException();
    }
    return newNamespaces;
  }

  private Set<String> checkForConflict(Set<String> namespaces, Predicate<String> conflictFn) {
    Optional<String> conflictingNamespace = namespaces.stream().filter(conflictFn).findAny();
    if (conflictingNamespace.isPresent()) {
      throw new NamespaceExistException(conflictingNamespace.get());
    }
    return namespaces;
  }

  private Set<String> combine(Optional<Set<String>> namespaces, String anotherNamespace) {
    Set<String> combined = new HashSet<>(namespaces.orElse(Collections.emptySet()));
    try {
      combined.add(anotherNamespace);
    } catch (Exception e) {
      // Do nothing. Caught a duplicate maybe.
    }
    return combined;
  }

  private AuthorizationProvider getAuthorizationProvider(String authorizationProvider,
      AuthorizationProvider defaultVal) {
    try {
      return AuthorizationProvider.valueOf(authorizationProvider);
    } catch (Exception e) {
      return defaultVal;
    }
  }

  private AuthenticationProvider getAuthenticationProvider(String authenticationProvider,
      AuthenticationProvider defaultVal) {
    try {
      return AuthenticationProvider.valueOf(authenticationProvider);
    } catch (Exception e) {
      return defaultVal;
    }
  }
}