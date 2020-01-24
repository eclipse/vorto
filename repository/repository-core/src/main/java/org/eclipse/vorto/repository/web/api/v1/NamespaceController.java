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
package org.eclipse.vorto.repository.web.api.v1;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiParam;
import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.oauth.IOAuthProvider;
import org.eclipse.vorto.repository.oauth.IOAuthProviderRegistry;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.tenant.NamespaceExistException;
import org.eclipse.vorto.repository.tenant.NewNamespaceNotPrivateException;
import org.eclipse.vorto.repository.tenant.NewNamespacesNotSupersetException;
import org.eclipse.vorto.repository.tenant.RestrictTenantPerOwnerException;
import org.eclipse.vorto.repository.tenant.TenantAdminDoesntExistException;
import org.eclipse.vorto.repository.tenant.TenantDoesntExistException;
import org.eclipse.vorto.repository.tenant.UpdateNotAllowedException;
import org.eclipse.vorto.repository.web.ControllerUtils;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceDto;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceOperationResult;
import org.eclipse.vorto.repository.web.tenant.TenantManagementController;
import org.eclipse.vorto.repository.web.tenant.dto.CreateTenantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller gathers all controller functionality initially designed to work with tenant
 * objects (namely through passing a tenant id), from the following controllers:
 * <ul>
 *   <li>{@link org.eclipse.vorto.repository.web.account.AccountController}</li>
 *   <li>{@link org.eclipse.vorto.repository.web.tenant.TenantManagementController}</li>
 *   <li>{@link org.eclipse.vorto.repository.web.backup.BackupController}</li>
 * </ul>
 * The ultimate goal is full deprecation of "tenant"-based notations, model and DB table design
 * in favor of a simplified namespace-based architecture that is already partially in place.<br/>
 * In the meantime, this controller can act as a proxy for the UI, tests and servlet filters, while
 * delegating its core functionality to the existing controllers until the full replacement is in
 * place.
 */
@RestController
@RequestMapping(value = "/api/v1/namespaces")
public class NamespaceController {

  private static final Logger LOGGER = Logger.getLogger(NamespaceController.class);
  
  @Autowired
  private ITenantService tenantService;
  
  @Autowired
  private IUserAccountService accountService;
  
  @Autowired
  private IOAuthProviderRegistry providerRegistry;
  
  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<Collection<NamespaceDto>> getNamespaces(Principal user) {
    Collection<NamespaceDto> namespaces = tenantService.getTenants().stream()
      .filter(tenant -> tenant.hasUser(user.getName()))
      .map(NamespaceDto::fromTenant)
      .collect(Collectors.toList());
    
    return new ResponseEntity<>(namespaces, HttpStatus.OK);
  }
  
  @RequestMapping(method = RequestMethod.GET, value="/{namespace:[a-zA-Z0-9_\\.]+}")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<NamespaceDto> getNamespace(
      @ApiParam(value = "The namespace you want to retrieve", required = true) final @PathVariable String namespace,
      Principal user) {
    
    Tenant tenant = tenantService.getTenantFromNamespace(ControllerUtils.sanitize(namespace))
        .orElseThrow(() -> TenantDoesntExistException.missingForNamespace(namespace));
    
    if (!tenant.hasUser(user.getName())) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    return new ResponseEntity<>(NamespaceDto.fromTenant(tenant), HttpStatus.OK);
  }

  /**
   * This is a transitory endpoint to create namespaces that will ultimately replace
   * {@link TenantManagementController#createTenant(String, CreateTenantRequest)}. <br/>
   * The endpoint takes an empty body and a namespace path variable, then tries to create it by
   * associating the necessary user/tenant information before handing over to the autowired
   * {@link ITenantService}.<br/>
   * Ultimately everything "tenant" should be refactored and simplified, so the tenant service
   * should be gone and replaced with a namespace service eventually.
   * @param namespace
   * @return
   */
  @RequestMapping(method = RequestMethod.PUT, value="/{namespace:[a-zA-Z0-9_\\.]+}", produces = "application/json")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<NamespaceOperationResult> createNamespace(
      @ApiParam(value = "The name of the namespace to be created", required = true)
      final @PathVariable String namespace
  ) {
    IUserContext userContext = UserContext.user(SecurityContextHolder.getContext().getAuthentication());
    if (Strings.nullToEmpty(namespace).trim().isEmpty()) {
      return new ResponseEntity<>(NamespaceOperationResult.failure("Empty namespace"), HttpStatus.BAD_REQUEST);
    }

    try {
      /*
      This creates a fake "tenant ID" to feed the tenant service and pointlessly populate the
      tenant table. Once everything "tenant-wise" is replaced with a simpler namespace-oriented
      architecture, the UUID itself should be removed, i.e. a "namespace service" would not care
      to use an additional unique ID since the namespace names are unique.
      */
      String fakeTenantId = UUID.randomUUID().toString().replace("-", "");
      /*
      This wraps the only admin (i.e. the given user for this request) in a set, as the service
      API allows multiple admins (but it makes no sense upon creation of a new namespace - only
      one admin and that is the user who initiated the request).
       */
      Set<String> fakeAdmins = new HashSet<>();
      fakeAdmins.add(userContext.getUsername());
      /*
      This wraps the only namespace in a set, according to service API .
       */
      Set<String> fakeNamespaces = new HashSet<>();
      fakeNamespaces.add(namespace);
      tenantService.createOrUpdateTenant(
          fakeTenantId,
          namespace,
          fakeAdmins,
          Optional.of(fakeNamespaces),
          // no authentication or authorization provider necessary
          Optional.ofNullable(null),
          Optional.ofNullable(null),
          userContext
      );

      return new ResponseEntity<>(NamespaceOperationResult.success(), HttpStatus.OK);
    }
    catch (NamespaceExistException e) {
      return new ResponseEntity<>(NamespaceOperationResult.failure("Namespace already exist"),
          HttpStatus.CONFLICT);
    }
    catch (RestrictTenantPerOwnerException e) {
      return new ResponseEntity<>(NamespaceOperationResult.failure("Namespace Quota of 1 exceeded."),
          HttpStatus.CONFLICT);
    }
    catch (IllegalArgumentException | TenantAdminDoesntExistException | UpdateNotAllowedException
        | NewNamespacesNotSupersetException | NewNamespaceNotPrivateException e) {
      return new ResponseEntity<>(NamespaceOperationResult.failure(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    catch (Exception e) {
      LOGGER.error(e);
      return new ResponseEntity<>(
          NamespaceOperationResult.failure("Internal error. Consult the vorto administrators!"),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping(value = "/{namespace:[a-zA-Z0-9_\\.]+}", produces = "application/json")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<NamespaceOperationResult> deleteNamespace(
      @ApiParam(value = "The name of the namespace to be deleted", required = true)
      final @PathVariable String namespace
  ) {
    // validates given namespace
    if (Strings.nullToEmpty(namespace).trim().isEmpty()) {
      return new ResponseEntity<>(NamespaceOperationResult.failure("Empty namespace"), HttpStatus.BAD_REQUEST);
    }

    // gets tenant for namespace
    Tenant tenant = tenantService.getTenantFromNamespace(namespace).orElse(null);
    if (tenant == null) {
      return new ResponseEntity<>(NamespaceOperationResult.failure("Namespace does not exist"), HttpStatus.NOT_FOUND);
    }

    // gets user (tenant ID required here, otherwise the service will fail to delete even with the given tenant)
    IUserContext userContext = UserContext.user(SecurityContextHolder.getContext().getAuthentication(), tenant.getTenantId());

    // validates user permissions on this namespace
    if (!(userContext.isSysAdmin() || isOwner(userContext.getUsername()).test(tenant))) {
      return new ResponseEntity<>(NamespaceOperationResult.failure("Operation forbidden for this user"), HttpStatus.FORBIDDEN);
    }

    return new ResponseEntity<>(
      NamespaceOperationResult.generate(
          tenantService.deleteTenant(tenant, userContext),
          Optional.of("Operation failed for unknown reasons")
      ),
      HttpStatus.OK
    );
  }


  @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", value="/{namespace:[a-zA-Z0-9_\\.]+}/collaborators/{userId}")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasRole('ROLE_TENANT_ADMIN')")
  public ResponseEntity<String> updateCollaborator(
      @ApiParam(value = "The namespace you want to add a collaborator to.", required = true) 
      final @PathVariable String namespace,
      @ApiParam(value = "The collaborator you want to add to this namespace.", required = true) 
      final @PathVariable String userId,
      @RequestBody @ApiParam(value = "Collaborator information", required = true) 
      final Collaborator collaboratorInfo,
      Principal user) {
    
    collaboratorInfo.setUserId(userId);
    
    Tenant tenant = tenantService.getTenantFromNamespace(ControllerUtils.sanitize(namespace))
        .orElseThrow(() -> TenantDoesntExistException.missingForNamespace(namespace));
    
    if (!tenant.hasTenantAdmin(user.getName())) {
      return new ResponseEntity<>("User is not admin", HttpStatus.FORBIDDEN);
    }
    
    Optional<IOAuthProvider> authProvider = providerRegistry.getById(collaboratorInfo.getAuthenticationProviderId());
    if (!authProvider.isPresent()) {
      return new ResponseEntity<>("AuthenticationProviderId is not found.", HttpStatus.BAD_REQUEST);
    }
    
    if (!collaboratorInfo.isTechnicalUser() && !accountService.exists(collaboratorInfo.getUserId())) {
      return new ResponseEntity<>("Account doesn't exist.", HttpStatus.BAD_REQUEST);
    }
    
    if (collaboratorInfo.isTechnicalUser() && Strings.nullToEmpty(collaboratorInfo.getSubject()).trim().isEmpty()) {
      return new ResponseEntity<>("Subject is empty.", HttpStatus.BAD_REQUEST);
    }
    
    if (collaboratorInfo.getRoles().isEmpty()) {
      accountService.removeUserFromTenant(tenant.getTenantId(), collaboratorInfo.getUserId());
    } else {
      accountService.createOrUpdate(collaboratorInfo.getUserId(), authProvider.get().getId(), collaboratorInfo.getSubject(), 
          collaboratorInfo.isTechnicalUser(), tenant.getTenantId(), toRoles(collaboratorInfo.getRoles()));
    }
    
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private Role[] toRoles(Collection<String> rolesStr) {
    Collection<Role> roles = rolesStr.stream().map(roleStr -> Role.of(roleStr)).collect(Collectors.toList());
    return roles.toArray(new Role[roles.size()]);
  }

  private static Predicate<Tenant> isOwner(String username) {
    return tenant -> tenant.hasTenantAdmin(username);
  }
}
