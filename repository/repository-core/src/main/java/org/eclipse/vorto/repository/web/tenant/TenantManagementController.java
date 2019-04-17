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
package org.eclipse.vorto.repository.web.tenant;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.UserRole;
import org.eclipse.vorto.repository.tenant.NamespaceExistException;
import org.eclipse.vorto.repository.tenant.NewNamespacesNotSupersetException;
import org.eclipse.vorto.repository.tenant.TenantAdminDoesntExistException;
import org.eclipse.vorto.repository.tenant.TenantService;
import org.eclipse.vorto.repository.web.tenant.dto.CreateTenantRequest;
import org.eclipse.vorto.repository.web.tenant.dto.NamespacesRequest;
import org.eclipse.vorto.repository.web.tenant.dto.TenantDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/rest")
public class TenantManagementController {

  private static Logger logger = Logger.getLogger(TenantManagementController.class);

  private TenantService tenantService;

  public TenantManagementController(@Autowired TenantService tenantService) {
    this.tenantService = tenantService;
  }

  @PreAuthorize("isAuthenticated()")
  @PutMapping(value = "/tenants/{tenantId:.+}", produces = "application/json")
  public ResponseEntity<Boolean> createTenant(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @RequestBody @ApiParam(value = "The information needed for the tenant creation request",
          required = true) final CreateTenantRequest tenantRequest) {
    
    IUserContext userContext =
        UserContext.user(SecurityContextHolder.getContext().getAuthentication(), tenantId);
    
    if (Strings.nullToEmpty(tenantId).trim().isEmpty()) {
      return new ResponseEntity<>(false, HttpStatus.PRECONDITION_FAILED);
    }

    if (Strings.nullToEmpty(tenantRequest.getDefaultNamespace()).trim().isEmpty()) {
      return new ResponseEntity<>(false, HttpStatus.PRECONDITION_FAILED);
    }

    if (tenantRequest.getTenantAdmins() == null || tenantRequest.getTenantAdmins().size() < 1) {
      return new ResponseEntity<>(false, HttpStatus.PRECONDITION_FAILED);
    }

    try {

      tenantService.createOrUpdateTenant(tenantId, tenantRequest.getDefaultNamespace(),
          tenantRequest.getTenantAdmins(),
          Optional.ofNullable(tenantRequest.getNamespaces()),
          Optional.ofNullable(tenantRequest.getAuthenticationProvider()),
          Optional.ofNullable(tenantRequest.getAuthorizationProvider()),
          userContext);

      return new ResponseEntity<>(true, HttpStatus.OK);

    } catch (NamespaceExistException e) {
      return new ResponseEntity<>(false, HttpStatus.CONFLICT);
    } catch (IllegalArgumentException | TenantAdminDoesntExistException e) {
      return new ResponseEntity<>(false, HttpStatus.PRECONDITION_FAILED);
    } catch (Exception e) {
      logger.error(e);
      return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @PreAuthorize("isAuthenticated()")
  @DeleteMapping(value = "/tenants/{tenantId:.+}", produces = "application/json")
  public ResponseEntity<Boolean> removeTenant(@ApiParam(value = "The id of the tenant",
      required = true) final @PathVariable String tenantId) {
    
    if (Strings.nullToEmpty(tenantId).trim().isEmpty()) {
      return new ResponseEntity<>(false, HttpStatus.PRECONDITION_FAILED);
    }
    
    IUserContext userContext =
        UserContext.user(SecurityContextHolder.getContext().getAuthentication(), tenantId);
    
    try {
      return new ResponseEntity<>(tenantService.deleteTenant(tenantId, userContext), HttpStatus.OK);
    } catch(Exception e) {
      e.printStackTrace();
      logger.error(e);
      return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping(value="/tenants", produces = "application/json")
  public ResponseEntity<Collection<TenantDto>> getTenants(
      @ApiParam(value = "If set, tenants are filtered based on the role of the user", required = false) 
      final @RequestParam(value = "role", required = false) String role,
      Principal user) {
    
    OAuth2Authentication oauth2User = (OAuth2Authentication) user;
    boolean isSysAdmin = oauth2User.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(UserRole.ROLE_SYS_ADMIN));
    
    Collection<Tenant> tenants = tenantService.getTenants();
    
    if (!isSysAdmin) {
      Predicate<Tenant> filter = isOwner(oauth2User.getName());
      if (role != null) {
        Role roleFilter = Role.valueOf(role.replace(Role.rolePrefix, ""));
        filter = hasMemberWithRole(oauth2User.getName(), roleFilter);
      }
      tenants = tenants.stream().filter(filter).collect(Collectors.toList());
    }
    
    return new ResponseEntity<>(tenants.stream().map(TenantDto::fromTenant)
        .collect(Collectors.toList()), HttpStatus.OK);
  }
  
  private Predicate<Tenant> isOwner(String username) {
    return tenant -> tenant.getOwner().getUsername().equals(username);
  }
  
  private Predicate<Tenant> hasMemberWithRole(String username, Role role) {
    return tenant -> tenant.getUsers().stream()
        .anyMatch(user -> 
          user.getUser().getUsername().equals(username) && 
          user.getRoles().size() > 0 &&
          user.hasRole(role));
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/tenants/{tenantId:.+}", produces = "application/json")
  public ResponseEntity<Tenant> getTenant(
      @ApiParam(value = "The id of the tenant", required = true) 
      final @PathVariable String tenantId,
      final Principal user) {

    if (Strings.nullToEmpty(tenantId).trim().isEmpty()) {
      return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
    }
    
    OAuth2Authentication oauth2User = (OAuth2Authentication) user;

    return tenantService.getTenant(tenantId)
        .flatMap(tenant -> {
          if (tenant.getOwner().getUsername().equals(oauth2User.getName())) {
            return Optional.of(tenant);
          }
          return Optional.empty();
        })
        .map(tenant -> new ResponseEntity<>(tenant, HttpStatus.OK))
        .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
  }

  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  @PutMapping(value = "/tenants/{tenantId}/namespaces", produces = "application/json")
  public ResponseEntity<Boolean> updateNamespace(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @ApiParam(value = "The information needed to update the namespaces of tenant",
          required = true) final @RequestBody NamespacesRequest namespacesRequest) {

    if (Strings.nullToEmpty(tenantId).trim().isEmpty()) {
      return new ResponseEntity<>(false, HttpStatus.PRECONDITION_FAILED);
    }

    if (namespacesRequest.getNamespaces() == null || namespacesRequest.getNamespaces().isEmpty()) {
      return new ResponseEntity<>(false, HttpStatus.PRECONDITION_FAILED);
    }

    try {
      tenantService.updateTenantNamespaces(tenantId, namespacesRequest.getNamespaces());

      return new ResponseEntity<>(true, HttpStatus.OK);
    } catch (NewNamespacesNotSupersetException e) {
      return new ResponseEntity<>(false, HttpStatus.PRECONDITION_FAILED);
    } catch (NamespaceExistException e) {
      return new ResponseEntity<>(false, HttpStatus.CONFLICT);
    }
  }

  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  @PostMapping(value = "/tenants/{tenantId}/namespaces", produces = "application/json")
  public ResponseEntity<Boolean> addNamespaces(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @RequestBody @ApiParam(value = "The information needed to update the namespaces of tenant",
          required = true) final NamespacesRequest namespacesRequest) {

    if (Strings.nullToEmpty(tenantId).trim().isEmpty()) {
      return new ResponseEntity<>(false, HttpStatus.PRECONDITION_FAILED);
    }

    if (namespacesRequest.getNamespaces() == null || namespacesRequest.getNamespaces().isEmpty()) {
      return new ResponseEntity<>(false, HttpStatus.PRECONDITION_FAILED);
    }

    try {
      tenantService.addNamespacesToTenant(tenantId, namespacesRequest.getNamespaces());

      return new ResponseEntity<>(true, HttpStatus.OK);
    } catch (NamespaceExistException e) {
      return new ResponseEntity<>(false, HttpStatus.CONFLICT);
    }
  }
  
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/namespaces/{namespace}/valid", produces = "application/json")
  public ResponseEntity<Boolean> validNamespace(
      @ApiParam(value = "The namespace to validate", required = true) final @PathVariable String namespace) {
    
    if (Strings.nullToEmpty(namespace).trim().isEmpty()) {
      return new ResponseEntity<>(false, HttpStatus.PRECONDITION_FAILED);
    }
    
    return new ResponseEntity<>(!tenantService.namespaceExist(namespace), HttpStatus.OK);
  }

}
