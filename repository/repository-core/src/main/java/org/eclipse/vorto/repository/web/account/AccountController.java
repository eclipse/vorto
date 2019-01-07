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
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.account.User;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.eclipse.vorto.repository.upgrade.IUpgradeService;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/rest/{tenant}/accounts")
public class AccountController {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Autowired
  private IUserAccountService accountService;

  @Autowired
  private IUpgradeService updateService;

  @RequestMapping(method = RequestMethod.GET, value = "/{username:.+}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.name")
  public ResponseEntity<UserDto> getUser(
      @ApiParam(value = "Username", required = true) @PathVariable String username) {
    User user = accountService.getUser(username);
    if (user!=null){
      return new ResponseEntity<>(UserDto.fromUser(user), HttpStatus.OK);
    }else{

      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
  @PreAuthorize("hasRole('ROLE_ADMIN') or #user.name == authentication.name")
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

  @RequestMapping(method = RequestMethod.POST, value = "/{username:.+}/updateTask")
  @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.name")
  public ResponseEntity<Boolean> upgradeUserAccount(Principal user,
      @ApiParam(value = "Username", required = true) @PathVariable String username) {

    User userAccount = accountService.getUser(username);
    if (userAccount == null) {
      return new ResponseEntity<Boolean>(true, HttpStatus.NOT_FOUND);
    }

    updateService.installUserUpgrade(userAccount, () -> user);

    return new ResponseEntity<Boolean>(true, HttpStatus.CREATED);
  }

  @RequestMapping(value = "/{username:.+}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.name")
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

  @RequestMapping(value = "/{username:.+}", method = RequestMethod.DELETE)
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#username,'user:delete')")
  public ResponseEntity<Void> deleteUserAccount(@PathVariable("username") final String username) {
    accountService.delete(username);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
