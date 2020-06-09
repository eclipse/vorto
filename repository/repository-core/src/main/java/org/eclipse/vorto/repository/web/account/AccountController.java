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
package org.eclipse.vorto.repository.web.account;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiParam;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.account.impl.AccountDeletionNotAllowed;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.TenantUser;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserRole;
import org.eclipse.vorto.repository.oauth.IOAuthProviderRegistry;
import org.eclipse.vorto.repository.oauth.internal.SpringUserUtils;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.upgrade.IUpgradeService;
import org.eclipse.vorto.repository.web.ControllerUtils;
import org.eclipse.vorto.repository.web.account.dto.TenantTechnicalUserDto;
import org.eclipse.vorto.repository.web.account.dto.TenantUserDto;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

  @Autowired
  private IUserAccountService accountService;

  @Autowired
  private IUpgradeService updateService;

  @Autowired
  private ITenantService tenantService;
  
  @Autowired
  private IOAuthProviderRegistry oauthProviderRegistry;

  @Autowired
  private UserNamespaceRoleService userNamespaceRoleService;

  @PutMapping("/rest/tenants/{tenantId}/users/{userId}")
  @PreAuthorize("hasAuthority('sysadmin') or hasPermission(#tenantId, 'org.eclipse.vorto.repository.domain.Namespace', 'namespace_admin')")
  public ResponseEntity<Boolean> addOrUpdateUsersForTenant(
      @ApiParam(value = "tenantId", required = true) @PathVariable String tenantId,
      @ApiParam(value = "userId", required = true) @PathVariable String userId,
      @RequestBody @ApiParam(value = "The user to be added to the tenant",
          required = true) final TenantUserDto user) {

    if (Strings.nullToEmpty(userId).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (Strings.nullToEmpty(tenantId).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }
    
    if (user.getRoles().stream()
        .anyMatch(role -> role.equals(UserRole.ROLE_SYS_ADMIN))) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    try {
      LOGGER.info("Adding user [" + userId + "] to tenant [" + tenantId + "] with Role [" + Arrays.toString(user.getRoles().toArray()) + "]");
      if (userId.equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<>(accountService.addUserToTenant(tenantId, userId, toRoles(user)),
          HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    } catch (Exception e) {
      LOGGER.error("error at addOrUpdateUsersForTenant()", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/rest/tenants/{tenantId}/users/{userId}")
  @PreAuthorize("hasAuthority('sysadmin') or hasPermission(#tenantId, 'org.eclipse.vorto.repository.domain.Namespace', 'namespace_admin')")
  public ResponseEntity<Boolean> createTechnicalUserForTenant(
      @ApiParam(value = "tenantId", required = true) @PathVariable String tenantId,
      @ApiParam(value = "userId", required = true) @PathVariable String userId,
      @RequestBody @ApiParam(value = "The user to be added to the tenant",
          required = true) final TenantTechnicalUserDto user) {

    if (Strings.nullToEmpty(userId).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (Strings.nullToEmpty(tenantId).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (Strings.nullToEmpty(user.getAuthenticationProviderId()).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (Strings.nullToEmpty(user.getSubject()).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (user.getRoles().stream()
        .anyMatch(role -> role.equals(UserRole.ROLE_SYS_ADMIN))) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    try {
      LOGGER.info(
        String.format(
          "Creating technical user [%s] and adding to tenant [%s] with role(s) [%s]",
          userId,
          tenantId,
          user.getRoles()
        )
      );
      if (userId.equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<>(
        accountService.createTechnicalUserAndAddToTenant(tenantId, userId, user, toRoles(user)),
        HttpStatus.OK
      );
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    } catch (Exception e) {
      LOGGER.error("error at addOrUpdateUsersForTenant()", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private Role[] toRoles(TenantUserDto user) {
    Set<Role> roles = user.getRoles().stream()
        .filter(role -> !(role.equals(UserRole.ROLE_SYS_ADMIN)))
        .map(strRole -> Role.valueOf(strRole.replace(Role.rolePrefix, ""))).collect(Collectors.toSet());
    return roles.toArray(new Role[roles.size()]);
  }

  @DeleteMapping("/rest/tenants/{tenantId}/users/{userId}")
  @PreAuthorize("hasAuthority('sysadmin') or hasPermission(#tenantId, 'org.eclipse.vorto.repository.domain.Namespace', 'namespace_admin')")
  public ResponseEntity<Boolean> deleteUserFromTenant(
      @ApiParam(value = "tenantId", required = true) @PathVariable String tenantId,
      @ApiParam(value = "userId", required = true) @PathVariable String userId) {

    if (Strings.nullToEmpty(userId).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (Strings.nullToEmpty(tenantId).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }
    
    // You cannot delete yourself if you are tenant admin
    Authentication user = SecurityContextHolder.getContext().getAuthentication();
    if (user.getName().equals(userId)) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    try {

      LOGGER.info("Removing user [" + ControllerUtils.sanitize(userId) + "] from tenant [" + ControllerUtils.sanitize(tenantId) + "]");
      return new ResponseEntity<>(accountService.removeUserFromTenant(tenantId, userId),
          HttpStatus.OK);

    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    } catch (Exception e) {
      LOGGER.error("Error in deleteUserFromTenant()", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @GetMapping("/rest/tenants/{tenantId}/users")
  @PreAuthorize("hasAuthority('sysadmin') or hasPermission(#tenantId, 'org.eclipse.vorto.repository.domain.Namespace', 'namespace_admin')")
  public ResponseEntity<Collection<TenantUserDto>> getUsersForTenant(
      @ApiParam(value = "tenantId", required = true) @PathVariable String tenantId) {

    if (Strings.nullToEmpty(tenantId).trim().isEmpty()) {
      return new ResponseEntity<>(Collections.emptyList(), HttpStatus.PRECONDITION_FAILED);
    }

    try {
      Optional<Tenant> maybeTenant = tenantService.getTenant(tenantId);
      if (maybeTenant.isPresent()) {
    	  Set<TenantUser> tenantUserSet= maybeTenant.get().getUsers();
    	  Set<TenantUserDto> tenantUserDtoSet = new HashSet<>();
    	  for(TenantUser tenantUser : tenantUserSet) {
    	    TenantUserDto tenantUserDto = TenantUserDto.fromTenantUser(tenantUser);
    	    tenantUserDtoSet.add(tenantUserDto);
    	  }
    	  return new ResponseEntity<>(tenantUserDtoSet,HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      LOGGER.error("Error in getUsersForTenant()", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @GetMapping("/rest/tenants/{tenantId}/users/{userId}")
  @PreAuthorize("hasAuthority('sysadmin') or hasPermission(#tenantId, 'org.eclipse.vorto.repository.domain.Namespace', 'namespace_admin')")
  public ResponseEntity<TenantUserDto> getUserForTenant(
      @ApiParam(value = "tenantId", required = true) @PathVariable String tenantId,
      @ApiParam(value = "userId", required = true) @PathVariable String userId) {
    
    try {
      Optional<Tenant> maybeTenant = tenantService.getTenant(tenantId);
      if (maybeTenant.isPresent()) {
        for(TenantUser tenantUser : maybeTenant.get().getUsers()) {
          if (tenantUser.getUser().getUsername().equals(userId)) {
            return new ResponseEntity<>(TenantUserDto.fromTenantUser(tenantUser), HttpStatus.OK);
          }
        }
      }
      
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      LOGGER.error("Error in getUsersForTenant()", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @DeleteMapping("/rest/tenants/{tenantId}/users/{username}/roles")
  @PreAuthorize("hasAuthority('sysadmin')")
  public ResponseEntity<UserDto> removeUserRole(@PathVariable("username") final String userName,
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @RequestBody List<Role> roles) {

    User user = accountService.removeUserRole(userName, tenantId, roles);

    return new ResponseEntity<>(UserDto.fromUser(user), HttpStatus.OK);
  }

  // TODO : check if we really need this and if so, make this correct. This should return all the 
  // roles of the user in this tenant
  @GetMapping("/rest/tenants/{tenantId}/users/{username}/roles")
  @PreAuthorize("hasAuthority('sysadmin')")
  public ResponseEntity<UserDto> getUserRoles(@PathVariable("username") final String userName) {
    
    User user = accountService.getUser(ControllerUtils.sanitize(userName));

    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException("User Not Found: " + userName);
    }
    
    return new ResponseEntity<>(UserDto.fromUser(user), HttpStatus.OK);
  }

  @GetMapping("/rest/accounts/{username:.+}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDto> getUser(
      @ApiParam(value = "Username", required = true) @PathVariable String username) {
    
    User user = accountService.getUser(ControllerUtils.sanitize(username));
    if (user != null) {
      return new ResponseEntity<>(UserDto.fromUser(user), HttpStatus.OK);
    }
    else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/rest/accounts/search/{partial:.+}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Collection<Collaborator>> findUsers(
      @ApiParam(value = "Username", required = true) @PathVariable String partial,
      @ApiParam(value = "Filter technical users only", required = false) @RequestParam(value = "onlyTechnicalUsers", required = false) boolean onlyTechnicalUsers) {

    Collection<User> users = accountService.findUsers(ControllerUtils.sanitize(partial.toLowerCase()));
    if (users != null) {
      Predicate<User> filter = onlyTechnicalUsers ? User::isTechnicalUser : u -> true;
      return new ResponseEntity<>(
          users.stream().filter(filter).map(Collaborator::fromUser).collect(Collectors.toList()),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping(consumes = "application/json", value = "/rest/accounts")
  @PreAuthorize("hasAuthority('sysadmin') or #user.name == authentication.name")
  public ResponseEntity<Boolean> createUserAccount(Principal user) {
    OAuth2Authentication oauth2User = (OAuth2Authentication) user;

    if (accountService.getUser(oauth2User.getName()) != null) {
      return new ResponseEntity<>(false, HttpStatus.CREATED);
    }
    LOGGER.info("User: '{}' accepted the terms and conditions.", oauth2User.getName());

    User createdUser = accountService.create(oauth2User.getName(), getAuthenticationProvider(oauth2User), null);
    SpringUserUtils.refreshSpringSecurityUser(createdUser, userNamespaceRoleService);

    return new ResponseEntity<>(true, HttpStatus.CREATED);
  }

  private String getAuthenticationProvider(OAuth2Authentication oauth2User) {
    return oauthProviderRegistry.getByAuthentication(oauth2User).getId();
  }

  @PostMapping("/rest/accounts/{username:.+}/updateTask")
  @PreAuthorize("hasAuthority('sysadmin') or #username == authentication.name")
  public ResponseEntity<Boolean> upgradeUserAccount(Principal user,
      @ApiParam(value = "Username", required = true) @PathVariable String username) {

    User userAccount = accountService.getUser(username);
    if (userAccount == null) {
      return new ResponseEntity<>(true, HttpStatus.NOT_FOUND);
    }

    updateService.installUserUpgrade(userAccount, () -> user);

    return new ResponseEntity<>(true, HttpStatus.CREATED);
  }

  @PutMapping("/rest/accounts/{username:.+}")
  @PreAuthorize("hasAuthority('sysadmin') or #username == authentication.name")
  public ResponseEntity<UserDto> updateAccount(@PathVariable("username") final String username,
      HttpEntity<String> httpEntity) {
    User account = accountService.getUser(username);
    if (account == null) {
      return new ResponseEntity<>((UserDto) null, HttpStatus.NOT_FOUND);
    }
    account.setEmailAddress(httpEntity.getBody());
    accountService.saveUser(account);

    return new ResponseEntity<>(UserDto.fromUser(account), HttpStatus.OK);
  }

  @DeleteMapping("/rest/accounts/{username:.+}")
  @PreAuthorize("hasAuthority('sysadmin') or hasPermission(#username,'user:delete')")
  public ResponseEntity<Void> deleteUserAccount(@PathVariable("username") final String username) {
    try {
      accountService.delete(username);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch(AccountDeletionNotAllowed e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
  }
}
