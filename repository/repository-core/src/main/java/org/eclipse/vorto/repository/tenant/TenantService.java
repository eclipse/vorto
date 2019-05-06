/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Component
public class TenantService implements ITenantService, ApplicationEventPublisherAware {

  private static final Logger logger = LoggerFactory.getLogger(TenantService.class);

  private ITenantRepository tenantRepo;

  private INamespaceRepository namespaceRepo;

  private IUserAccountService userAccountService;

  private ApplicationEventPublisher eventPublisher = null;

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

  public Tenant createOrUpdateTenant(String tenantId, String defaultNamespace,
      Set<String> tenantAdmins, Optional<Set<String>> namespaces,
      Optional<String> authenticationProvider, Optional<String> authorizationProvider,
      IUserContext userContext) {

    PreConditions.notNullOrEmpty(tenantId, "tenantId");
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
      tenant = newTenant(tenantId, defaultNamespace, namespaces,
          authenticationProvider.orElse(null), authorizationProvider.orElse(null));
      Set<TenantUser> newTenantAdmins = createNewTenantAdmins(tenantAdmins, tenant);
      tenant.setUsers(newTenantAdmins);
      tenant.setOwner(owner);
    } else {
      if (!userContext.isSysAdmin() && !tenant.hasTenantAdmin(userContext.getUsername())) {
        throw new UpdateNotAllowedException(userContext.getUsername(), tenant.getTenantId());
      }

      logger.info("Updating tenant '{}' - {}", tenantId, tenant);
      tenant = updateTenant(tenant, defaultNamespace, namespaces,
          authenticationProvider.orElse(null), authorizationProvider.orElse(null));

      updateTenantAdmins(tenantAdmins, tenant);
      eventType = EventType.TENANT_UPDATED;
    }

    tenant = tenantRepo.save(tenant);

    logger.info("Sending event for {}", tenantId);
    eventPublisher.publishEvent(new AppEvent(this, tenant, eventType));

    return tenant;
  }

  private void updateTenantAdmins(Set<String> newTenantAdmins, Tenant tenant) {
    Set<String> tenantUsers = tenant.getUsers().stream().map(user -> user.getUser().getUsername())
        .collect(Collectors.toSet());
    Set<String> oldTenantAdmins = tenant.getTenantAdmins().stream().map(user -> user.getUsername())
        .collect(Collectors.toSet());
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
      Optional<TenantUser> upgradedUser = tenant.getUsers().stream()
          .filter(user -> user.getUser().getUsername().equals(admin)).findFirst();
      if (upgradedUser.isPresent()) {
        upgradedUser.get().addRoles(Role.TENANT_ADMIN);
      }
    });

    demotedToUsers.forEach(user -> {
      logger.info("Removing tenant_admin rights from user '{}' in Tenant '{}'", user,
          tenant.getTenantId());
      Optional<TenantUser> _demotedUser = tenant.getUsers().stream()
          .filter(_user -> _user.getUser().getUsername().equals(user)).findFirst();
      if (_demotedUser.isPresent()) {
        TenantUser demotedUser = _demotedUser.get();
        demotedUser.getRoles().removeIf(userRole -> userRole.getRole() == Role.TENANT_ADMIN);
        if (demotedUser.getRoles().size() < 1) {
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

    for (Tenant tenant : getTenants()) {
      if (tenant.getNamespaces() == null) {
        continue;
      }
      for (Namespace ns : tenant.getNamespaces()) {
        if (namespace.startsWith(ns.getName())) {
          return Optional.of(tenant);
        }
      }
    }

    return Optional.empty();
  }

  public Collection<Tenant> getTenants() {
    return Lists.newArrayList(tenantRepo.findAll());
  }

  public boolean updateTenantNamespaces(String tenantId, Set<String> namespaces) {
    PreConditions.notNullOrEmpty(tenantId, "tenantId");
    PreConditions.notNullOrEmpty(namespaces, "namespaces");

    Tenant tenant = tenantRepo.findByTenantId(tenantId);
    if (tenant != null) {
      // We should check if new namespaces is superset of old namespaces to make sure
      // we are not removing any namespace from the old namespaces that are already
      // being used by the models of the users of this tenant
      Set<String> tenantNamespaces = checkIfSuperset(namespaces, tenant);

      // Make sure namespaces are unique across tenants
      tenantNamespaces = checkForConflict(tenantNamespaces, namespaceOwnedByAnotherTenant(tenant));

      tenant.getNamespaces().clear();
      tenant.getNamespaces().addAll(Namespace.toNamespace(tenantNamespaces, tenant));

      tenant = tenantRepo.save(tenant);

      return true;
    }

    return false;
  }

  public boolean addNamespacesToTenant(String tenantId, Set<String> namespaces) {
    PreConditions.notNullOrEmpty(tenantId, "tenantId");
    PreConditions.notNullOrEmpty(namespaces, "namespaces");

    Tenant tenant = tenantRepo.findByTenantId(tenantId);
    if (tenant != null) {
      Set<String> newNamespaces =
          checkForConflict(namespaces, namespaceOwnedByAnotherTenant(tenant));
      tenant.getNamespaces().addAll(Namespace.toNamespace(newNamespaces, tenant));
      tenant = tenantRepo.save(tenant);
    }

    return false;
  }

  public boolean deleteTenant(Tenant tenant, IUserContext userContext) {
    PreConditions.notNull(tenant, "Tenant should not be null");

    tenant.removeUsers();
    
    tenant.unsetOwner();

    tenantRepo.delete(tenant);

    eventPublisher.publishEvent(new AppEvent(this, tenant, EventType.TENANT_DELETED));

    return true;
  }

  private Tenant updateTenant(Tenant tenant, String defaultNamespace,
      Optional<Set<String>> namespaces, String authenticationProvider,
      String authorizationProvider) {

    Set<String> tenantNamespaces = combine(namespaces, defaultNamespace);

    // We should check if new namespaces is superset of old namespaces to make sure
    // we are not removing any namespace from the old namespaces that are already
    // being used by the models of the users of this tenant
    tenantNamespaces = checkIfSuperset(tenantNamespaces, tenant);

    // Make sure namespaces are unique across tenants
    tenantNamespaces = checkForConflict(tenantNamespaces, namespaceOwnedByAnotherTenant(tenant));

    for (String ns : tenantNamespaces) {
      // add namespace if we don't have it
      if (!tenant.getNamespaces().stream().anyMatch(_ns -> _ns.getName().equals(ns))) {
        tenant.getNamespaces().add(Namespace.newNamespace(ns));
      }
    }

    tenant.setDefaultNamespace(defaultNamespace);
    tenant.setAuthenticationProvider(
        getAuthenticationProvider(authenticationProvider, tenant.getAuthenticationProvider()));
    tenant.setAuthorizationProvider(
        getAuthorizationProvider(authorizationProvider, tenant.getAuthorizationProvider()));
    return tenant;
  }

  private Predicate<String> namespaceOwnedByAnotherTenant(Tenant tenant) {
    return namespace -> !namespaceOwnedByTenant(tenant, namespace)
        && conflictsWithExistingNamespace(namespace);
  }

  private boolean namespaceOwnedByTenant(Tenant tenant, String namespace) {
    return tenant.getNamespaces().stream().map(ns -> ns.getName()).anyMatch(namespace::equals);
  }

  private Tenant newTenant(String tenantId, String defaultNamespace,
      Optional<Set<String>> namespaces, String authenticationProvider,
      String authorizationProvider) {

    Set<String> tenantNamespaces = checkForConflict(combine(namespaces, defaultNamespace),
        this::conflictsWithExistingNamespace);

    Tenant tenant = Tenant.newTenant(tenantId, defaultNamespace, tenantNamespaces);
    tenant.setAuthenticationProvider(
        getAuthenticationProvider(authenticationProvider, AuthenticationProvider.GITHUB));
    tenant.setAuthorizationProvider(
        getAuthorizationProvider(authorizationProvider, AuthorizationProvider.DB));
    return tenant;
  }

  private Set<String> checkIfSuperset(Set<String> newNamespaces, Tenant tenant) {
    List<String> oldNamespaces =
        tenant.getNamespaces().stream().map(ns -> ns.getName()).collect(Collectors.toList());
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
