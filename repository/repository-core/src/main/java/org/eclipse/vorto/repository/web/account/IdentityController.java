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
import java.util.List;
import java.util.Objects;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.eclipse.vorto.repository.upgrade.IUpgradeService;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiParam;

/**
 * Manages identities and their memberships/roles
 *
 */
@RestController
@RequestMapping(value = "/rest/{tenantId}/users")
public class IdentityController {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Autowired
  private IUserRepository userRepository;

  @Autowired
  private IUserAccountService accountService;

  @Autowired
  private IUpgradeService updateService;

  @Deprecated
  @RequestMapping(method = RequestMethod.GET, value = "/{username:.+}")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or #username == authentication.name")
  public ResponseEntity<UserDto> getUser(
      @ApiParam(value = "Username", required = true) @PathVariable String username) {

    LOGGER.debug("User {} - {} ", username, userRepository.findByUsername(username));

    User user = userRepository.findByUsername(username);

    return new ResponseEntity<UserDto>(UserDto.fromUser(user), HttpStatus.OK);
  }

  @Deprecated
  @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or #user.name == authentication.name")
  public ResponseEntity<Boolean> createUserAccount(Principal user) {

    OAuth2Authentication oauth2User = (OAuth2Authentication) user;

    if (userRepository.findByUsername(oauth2User.getName()) != null) {
      return new ResponseEntity<Boolean>(false, HttpStatus.CREATED);
    }

    LOGGER.info("User: '{}' accepted the terms and conditions.", oauth2User.getName());

    User createdUser = accountService.create(oauth2User.getName());

    SpringUserUtils.refreshSpringSecurityUser(createdUser); // change the spring oauth context with
                                                            // the updated user and its roles

    return new ResponseEntity<Boolean>(true, HttpStatus.CREATED);
  }

  @Deprecated
  @RequestMapping(method = RequestMethod.POST, value = "/{username:.+}/updateTask")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or #username == authentication.name")
  public ResponseEntity<Boolean> upgradeUserAccount(Principal user,
      @ApiParam(value = "Username", required = true) @PathVariable String username) {

    User userAccount = userRepository.findByUsername(username);
    if (userAccount == null) {
      return new ResponseEntity<Boolean>(true, HttpStatus.BAD_REQUEST);
    }

    updateService.installUserUpgrade(userAccount, () -> user);

    return new ResponseEntity<Boolean>(true, HttpStatus.CREATED);
  }

  @Deprecated
  @RequestMapping(value = "/{username:.+}", method = RequestMethod.DELETE)
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasPermission(#username,'user:delete')")
  public ResponseEntity<Void> deleteUserAccount(@PathVariable("username") final String username) {
    accountService.delete(username);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "/{username:.+}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  public ResponseEntity<UserDto> updateUserAccount(@PathVariable("username") final String userName,
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @RequestBody List<Role> roles) {

    User user = accountService.create(userName, tenantId, roles.toArray(new Role[roles.size()]));

    return new ResponseEntity<UserDto>(UserDto.fromUser(user), HttpStatus.CREATED);
  }

  @RequestMapping(value = "/{username:.+}/roles", method = RequestMethod.DELETE)
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  public ResponseEntity<UserDto> removeUserRole(@PathVariable("username") final String userName,
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @RequestBody List<Role> roles) {

    User user = accountService.removeUserRole(userName, tenantId, roles);

    return new ResponseEntity<UserDto>(UserDto.fromUser(user), HttpStatus.OK);
  }

  @RequestMapping(value = "/{username:.+}/roles", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  public ResponseEntity<UserDto> getUserRoles(@PathVariable("username") final String userName) {
    User user = this.userRepository.findByUsername(userName);

    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException("User Not Found: " + userName);
    }
    return new ResponseEntity<UserDto>(UserDto.fromUser(user), HttpStatus.OK);
  }


}
