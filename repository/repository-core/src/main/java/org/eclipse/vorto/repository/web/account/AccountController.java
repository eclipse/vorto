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

import io.swagger.annotations.ApiParam;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.oauth.IOAuthProviderRegistry;
import org.eclipse.vorto.repository.oauth.internal.SpringUserUtils;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.UserService;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.eclipse.vorto.repository.upgrade.IUpgradeService;
import org.eclipse.vorto.repository.web.ControllerUtils;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.eclipse.vorto.repository.web.api.v1.dto.OperationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

  @Autowired
  private DefaultUserAccountService accountService;

  @Autowired
  private UserService userService;

  @Autowired
  private IUpgradeService updateService;

  @Autowired
  private IOAuthProviderRegistry oauthProviderRegistry;

  @Autowired
  private UserNamespaceRoleService userNamespaceRoleService;

  @GetMapping("/rest/accounts/{username:.+}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDto> getUser(
      @ApiParam(value = "Username", required = true) @PathVariable String username) {

    IUserContext userContext = UserContext
        .user(SecurityContextHolder.getContext().getAuthentication());
    User user = accountService.getUser(ControllerUtils.sanitize(username));
    if (user != null) {
      // suppresses sensitive data e.g. e-mail if the queried username is not identical to the
      // logged-on user's name
      return new ResponseEntity<>(
          UserDto.fromUser(
              user, !userContext.getUsername().equals(username)
          ),
          HttpStatus.OK
      );
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/rest/accounts/search/{partial:.+}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Collection<Collaborator>> findUsers(
      @ApiParam(value = "Username", required = true) @PathVariable String partial,
      @ApiParam(value = "Filter technical users only", required = false) @RequestParam(value = "onlyTechnicalUsers", required = false) boolean onlyTechnicalUsers) {

    Collection<User> users = accountService
        .findUsers(ControllerUtils.sanitize(partial.toLowerCase()));
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

    User createdUser = accountService
        .create(oauth2User.getName(), getAuthenticationProvider(oauth2User), null);
    SpringUserUtils.refreshSpringSecurityUser(createdUser, userNamespaceRoleService);

    return new ResponseEntity<>(true, HttpStatus.CREATED);
  }

  private String getAuthenticationProvider(OAuth2Authentication oauth2User) {
    return oauthProviderRegistry.getByAuthentication(oauth2User).getId();
  }

  /**
   * This endpoint is added for suite integration. <br/>
   * It allows any authenticated user to create a technical user <b>if</b> that user does not exist
   * already, without the need to immediately associate that user to an existing namespace owned
   * by the requestor. <br/>
   * This is currently used by the "request access to namespace" form in its standalone version,
   * when the form is also parametrized with at least a {@code userId} whose value is the name of
   * the technical user to create (or not). <br/>
   * Returns HTTP {@literal 200} if the technical user already exists, {@literal 201} if created
   * successfully or {@literal 400} if the user cannot be created due to bad parameter values.<br/>
   * Parameter sanitization is mostly done through Spring security, and at service level.<br/>
   * Note that in the current implementation, the parametrized standalone form will first ask the
   * back-end whether the given technical use exists anyway. <br/>
   * While doubling the amount of networking, this is conductive to a workflow where the end user
   * opening the parametrized form has to click a button explicitly when creating the technical
   * user, instead of the form doing so automatically when loading parametrized. <br/>
   * In other words, the duplicate networking call (first ask if user exists, then if not allow
   * creating it) restricts possible automated abuse of technical user creation, by means of
   * a forced UI interaction.
   *
   * @param technicalUser
   * @return
   * @see AccountController#getUser(String)
   */
  @PostMapping(consumes = "application/json", value = "/rest/accounts/createTechnicalUser")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<OperationResult> createTechnicalUser(
      @RequestBody @ApiParam(value = "The technical user to be created",
          required = true) final UserDto technicalUser) {
    // user exists - do nothing and return false / 200
    User existingUser = accountService.getUser(technicalUser.getUsername());
    if (existingUser != null) {
      return new ResponseEntity<>(OperationResult.success("User already exists"), HttpStatus.OK);
    }
    // user does not exist
    try {
      // adding date fields
      technicalUser.setDateCreated(Timestamp.from(Instant.now()));
      technicalUser.setLastUpdated(Timestamp.from(Instant.now()));
      // UI will inform end-user that by creating the technical user, the terms and conditions are
      // considered to be approved
      userService.createOrUpdateTechnicalUser(technicalUser.toUser());
      return new ResponseEntity<>(OperationResult.success(), HttpStatus.CREATED);
    } catch (InvalidUserException iue) {
      LOGGER.warn("Invalid technical user creation request.", iue);
      return new ResponseEntity<>(OperationResult.failure(iue.getMessage()),
          HttpStatus.BAD_REQUEST);
    }

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
      IUserContext userContext = UserContext
          .user(SecurityContextHolder.getContext().getAuthentication());
      userService.delete(userContext.getUsername(), username);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    } catch (DoesNotExistException dnee) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
