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

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.AuthenticationProvider;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.tenant.TenantDoesntExistException;
import org.eclipse.vorto.repository.web.ControllerUtils;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/api/v1/namespaces")
public class NamespaceController {
  
  @Autowired
  private ITenantService tenantService;
  
  @Autowired
  private IUserAccountService accountService;
  
  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasRole('ROLE_TENANT_ADMIN')")
  public Collection<NamespaceDto> getUserNamespaces(Principal user) {
    
    return tenantService.getTenants().stream()
      .filter(tenant -> tenant.hasUserWithRole(user.getName(), Role.TENANT_ADMIN))
      .map(NamespaceDto::fromTenant)
      .collect(Collectors.toList());
  }
  
  @RequestMapping(method = RequestMethod.GET, value="/{namespace:[a-zA-Z0-9_\\.]+}")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasRole('ROLE_TENANT_ADMIN')")
  public ResponseEntity<NamespaceDto> getNamespace(
      @ApiParam(value = "The namespace you want to retrieve", required = true) final @PathVariable String namespace,
      Principal user) {
    
    Tenant tenant = tenantService.getTenantFromNamespace(ControllerUtils.sanitize(namespace))
        .orElseThrow(() -> TenantDoesntExistException.missingForNamespace(namespace));
    
    if (!tenant.hasTenantAdmin(user.getName())) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    return new ResponseEntity<>(NamespaceDto.fromTenant(tenant), HttpStatus.OK);
  }
  
  @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", value="/{namespace:[a-zA-Z0-9_\\.]+}")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasRole('ROLE_TENANT_ADMIN')")
  public ResponseEntity<Void> modifyNamespace(
      @ApiParam(value = "The namespace you want to retrieve", required = true) 
      final @PathVariable String namespace,
      @RequestBody @ApiParam(value = "The new admins of the namespace", required = true) 
      Collection<String> newTenantAdmins,
      Principal user) {
    
    if (newTenantAdmins.size() <= 0) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    Tenant tenant = tenantService.getTenantFromNamespace(ControllerUtils.sanitize(namespace))
        .orElseThrow(() -> TenantDoesntExistException.missingForNamespace(namespace));
    
    if (!tenant.hasTenantAdmin(user.getName())) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    tenantService.changeTenantAdmins(tenant, new HashSet<String>(newTenantAdmins));
    
    return new ResponseEntity<>(HttpStatus.OK);
  }
  
  @RequestMapping(method = RequestMethod.GET, value="/{namespace:[a-zA-Z0-9_\\.]+}/collaborators")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasRole('ROLE_TENANT_ADMIN')")
  public ResponseEntity<Collection<Collaborator>> getCollaborators(
      @ApiParam(value = "The namespace whose collaborators you want to retrieve", required = true) 
      final @PathVariable String namespace,
      Principal user) {
    
    Tenant tenant = tenantService.getTenantFromNamespace(ControllerUtils.sanitize(namespace))
        .orElseThrow(() -> TenantDoesntExistException.missingForNamespace(namespace));
    
    if (!tenant.hasTenantAdmin(user.getName())) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    return new ResponseEntity<>(getCollaborators(tenant), HttpStatus.OK);
  }

  private Collection<Collaborator> getCollaborators(Tenant tenant) {
    return tenant.getUsers().stream().map(Collaborator::fromUser).collect(Collectors.toList());
  }
  
  @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", value="/{namespace:[a-zA-Z0-9_\\.]+}/collaborators/{userId}")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasRole('ROLE_TENANT_ADMIN')")
  public ResponseEntity<Void> updateCollaborator(
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
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    Optional<AuthenticationProvider> authProvider = getProvider(collaboratorInfo);
    if (!authProvider.isPresent()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    if (authProvider.get() != AuthenticationProvider.BOSCH_IOT_SUITE_AUTH && 
        !accountService.exists(collaboratorInfo.getUserId())) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    if (collaboratorInfo.getRoles().isEmpty()) {
      accountService.removeUserFromTenant(tenant.getTenantId(), collaboratorInfo.getUserId());
    } else {
      accountService.create(collaboratorInfo.getUserId(), tenant.getTenantId(), 
          authProvider.get(), toRoles(collaboratorInfo.getRoles()));
    }
    
    return new ResponseEntity<>(HttpStatus.OK);
  }
  
  private Optional<AuthenticationProvider> getProvider(Collaborator user) {
    try {
      return Optional.of(AuthenticationProvider.valueOf(user.getProviderId()));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  private Role[] toRoles(Collection<String> rolesStr) {
    Collection<Role> roles = rolesStr.stream().map(roleStr -> Role.of(roleStr)).collect(Collectors.toList());
    return roles.toArray(new Role[roles.size()]);
  }
}
