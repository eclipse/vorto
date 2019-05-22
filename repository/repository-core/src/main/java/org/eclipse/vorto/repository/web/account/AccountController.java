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
package org.eclipse.vorto.repository.web.account;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.TenantUser;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserRole;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.upgrade.IUpgradeService;
import org.eclipse.vorto.repository.web.account.dto.TenantUserDto;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiParam;

@RestController
public class AccountController {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Autowired
  private IUserAccountService accountService;

  @Autowired
  private IUpgradeService updateService;

  @Autowired
  private ITenantService tenantService;

  @RequestMapping(method = RequestMethod.PUT, value = "/rest/tenants/{tenantId}/users/{userId}")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasPermission(#tenantId, 'org.eclipse.vorto.repository.domain.Tenant', 'ROLE_TENANT_ADMIN')")
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
        //.anyMatch(role -> role.equals(UserRole.ROLE_SYS_ADMIN) || role.equals(UserRole.ROLE_TENANT_ADMIN))) {
    	.anyMatch(role -> role.equals(UserRole.ROLE_SYS_ADMIN))) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    try {
      LOGGER.info("Adding user [" + userId + "] to tenant [" + tenantId + "] with Role [" + Arrays.toString(user.getRoles().toArray()) + "]");
      return new ResponseEntity<>(accountService.addUserToTenant(tenantId, userId, toRoles(user)),
          HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    } catch (Exception e) {
      LOGGER.error("error at addOrUpdateUsersForTenant()", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private Role[] toRoles(TenantUserDto user) {
    Set<Role> roles = user.getRoles().stream()
        //.filter(role -> !(role.equals(UserRole.ROLE_SYS_ADMIN) || role.equals(UserRole.ROLE_TENANT_ADMIN)))
    	.filter(role -> !(role.equals(UserRole.ROLE_SYS_ADMIN)))
        .map(strRole -> Role.valueOf(strRole.replace(Role.rolePrefix, ""))).collect(Collectors.toSet());
    return roles.toArray(new Role[roles.size()]);
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/rest/tenants/{tenantId}/users/{userId}")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasPermission(#tenantId, 'org.eclipse.vorto.repository.domain.Tenant', 'ROLE_TENANT_ADMIN')")
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

      LOGGER.info("Removing user [" + userId + "] from tenant [" + tenantId + "]");
      return new ResponseEntity<>(accountService.removeUserFromTenant(tenantId, userId),
          HttpStatus.OK);

    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    } catch (Exception e) {
      LOGGER.error("Error in deleteUserFromTenant()", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @RequestMapping(method = RequestMethod.GET, value = "/rest/tenants/{tenantId}/users")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasPermission(#tenantId, 'org.eclipse.vorto.repository.domain.Tenant', 'ROLE_TENANT_ADMIN')")
  public ResponseEntity<Collection<TenantUserDto>> getUsersForTenant(
      @ApiParam(value = "tenantId", required = true) @PathVariable String tenantId) {

    if (Strings.nullToEmpty(tenantId).trim().isEmpty()) {
      return new ResponseEntity<>(Collections.emptyList(), HttpStatus.PRECONDITION_FAILED);
    }

    try {
      Optional<Tenant> _tenant = tenantService.getTenant(tenantId);
      if (_tenant.isPresent()) {
    	  Set<TenantUser> tenantUserSet= _tenant.get().getUsers();
    	  Set<TenantUserDto> tenantUserDtoSet = new HashSet<>();
    	  for(TenantUser tenantUser : tenantUserSet){
    		  TenantUserDto tenantUserDto = TenantUserDto.fromTenantUser(tenantUser);
    		  tenantUserDtoSet.add(tenantUserDto);
    		}
    	  return new ResponseEntity<>(tenantUserDtoSet,HttpStatus.OK);
       /* return new ResponseEntity<>(_tenant.get().getUsers().stream()
            .map(TenantUserDto::fromTenantUser).collect(Collectors.toList()), HttpStatus.OK);*/
        
        
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      LOGGER.error("Error in getUsersForTenant()", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(method = RequestMethod.GET, value = "/rest/accounts/{username:.+}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDto> getUser(
      @ApiParam(value = "Username", required = true) @PathVariable String username) {
    User user = accountService.getUser(username);
    if (user != null) {
      return new ResponseEntity<>(UserDto.fromUser(user), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(method = RequestMethod.POST, consumes = "application/json",
      value = "/rest/{tenantId}/accounts")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or #user.name == authentication.name")
  public ResponseEntity<Boolean> createUserAccount(Principal user) {
    OAuth2Authentication oauth2User = (OAuth2Authentication) user;

    if (accountService.getUser(oauth2User.getName()) != null) {
      return new ResponseEntity<>(false, HttpStatus.CREATED);
    }
    LOGGER.info("User: '{}' accepted the terms and conditions.", oauth2User.getName());

    User createdUser = accountService.create(oauth2User.getName());
    SpringUserUtils.refreshSpringSecurityUser(createdUser);

    return new ResponseEntity<>(true, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.POST,
      value = "/rest/accounts/{username:.+}/updateTask")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or #username == authentication.name")
  public ResponseEntity<Boolean> upgradeUserAccount(Principal user,
      @ApiParam(value = "Username", required = true) @PathVariable String username) {

    User userAccount = accountService.getUser(username);
    if (userAccount == null) {
      return new ResponseEntity<Boolean>(true, HttpStatus.NOT_FOUND);
    }

    updateService.installUserUpgrade(userAccount, () -> user);

    return new ResponseEntity<Boolean>(true, HttpStatus.CREATED);
  }

  @RequestMapping(value = "/rest/accounts/{username:.+}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or #username == authentication.name")
  public ResponseEntity<UserDto> updateAccount(@PathVariable("username") final String username,
      HttpEntity<String> httpEntity) {
    User account = accountService.getUser(username);
    if (account == null) {
      return new ResponseEntity<UserDto>((UserDto) null, HttpStatus.NOT_FOUND);
    }
    account.setEmailAddress(httpEntity.getBody());
    accountService.saveUser(account);

    return new ResponseEntity<UserDto>(UserDto.fromUser(account), HttpStatus.OK);
  }

  @RequestMapping(value = "/rest/accounts/{username:.+}", method = RequestMethod.DELETE)
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasPermission(#username,'user:delete')")
  public ResponseEntity<Void> deleteUserAccount(@PathVariable("username") final String username) {
    accountService.delete(username);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
