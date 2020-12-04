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
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
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
import org.springframework.security.core.Authentication;
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

  /**
   * Users should now be identified by both username and authentication provider ID. <br/>
   * This legacy endpoint remains for some edge cases where standalone forms that can take a
   * single username query parameter, etc.
   *
   * @param username
   * @return
   */
  @Deprecated
  @GetMapping("/rest/accounts/{username:.+}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDto> getUser(
      @ApiParam(value = "Username", required = true) @PathVariable String username) {
    return accountService.getUser(ControllerUtils.sanitize(username))
        .map(
            u -> {
              User actor = accountService.getUser(
                  SecurityContextHolder.getContext().getAuthentication()
              );
              return new ResponseEntity<>(
                  // suppresses sensitive data if caller not the same user as target
                  UserDto.fromUser(u, !actor.equals(u)),
                  HttpStatus.OK
              );
            }
        )
        .orElse(
            new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
  }

  /**
   * Fetches user information now based on both username and authentication provider. <br/>
   * @param username
   * @param authenticationProvider
   * @return
   */
  @GetMapping("/rest/accounts")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDto> getUser(
      @ApiParam(value = "username", required = true) @RequestParam String username,
      @ApiParam(value = "authenticationProvider", required = true) @RequestParam String authenticationProvider
  ) {
    User result = accountService.getUser(UserDto.of(username, authenticationProvider));
    if (Objects.isNull(result)) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    else {
      User actor = accountService.getUser(
          SecurityContextHolder.getContext().getAuthentication()
      );
      return new ResponseEntity<>(
          // suppresses sensitive data if caller not the same user as target
          UserDto.fromUser(result, !actor.equals(result)),
          HttpStatus.OK
      );
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

    if (accountService.getUser(oauth2User) != null) {
      return new ResponseEntity<>(false, HttpStatus.CREATED);
    }
    User createdUser = null;
    try {
      createdUser = accountService
          .createNonTechnicalUser(
              UserDto.of(
                  oauth2User.getName(), getAuthenticationProvider(oauth2User)
              ),
              null
          );
    } catch (InvalidUserException iue) {
      return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }
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
   */
  @PostMapping(consumes = "application/json", value = "/rest/accounts/createTechnicalUser")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<OperationResult> createTechnicalUser(
      @RequestBody @ApiParam(value = "The technical user to be created", required = true) final UserDto technicalUser) {
    // user exists - do nothing and return false / conflict
    User existingUser = accountService.getUser(
        UserDto.of(
          technicalUser.getUsername(),
          technicalUser.getAuthenticationProvider()
        )
    );
    if (existingUser != null) {
      return new ResponseEntity<>(OperationResult.failure("Technical user exists already"), HttpStatus.CONFLICT);
    }
    // user does not exist
    // getting calling user
    User actor = accountService.getUser(SecurityContextHolder.getContext().getAuthentication());
    try {
      // adding date fields
      technicalUser.setDateCreated(Timestamp.from(Instant.now()));
      technicalUser.setLastUpdated(Timestamp.from(Instant.now()));
      // UI will inform end-user that by creating the technical user, the terms and conditions are
      // considered to be approved
      userService.createOrUpdateTechnicalUser(actor, technicalUser.toUser());
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
    // TODO #2529 this will only find unique users whose name is unique
    return accountService.getUser(username).map(
        u -> {
          updateService.installUserUpgrade(u, () -> user);

          return new ResponseEntity<>(true, HttpStatus.CREATED);
        }
    )
        .orElse(
            new ResponseEntity<>(true, HttpStatus.NOT_FOUND)
        );

  }

  /**
   * This essentially updates a user's e-mail address. <br/>
   * As long as the user is updating their own e-mail address (which constitutes the totality of
   * the cases in the field at the time of writing), this requires no changes.<br/>
   * However, as this resolves the user by their username only, it cannot infer the right
   * authentication provider ID if it is called by a sysadmin user on behalf of another user
   * (again, no documented field cases for that). <br/>
   * The latter implies that if the target username is not unique, the operation will fail when
   * initiated by a sysadmin targeting another user.<br/>
   *
   * @param username
   * @param httpEntity
   * @return
   * @see AccountController#updateAccount(UserDto) for a safer implementation
   */
  @PutMapping("/rest/accounts/{username:.+}")
  @PreAuthorize("hasAuthority('sysadmin') or #username == authentication.name")
  public ResponseEntity<UserDto> updateAccount(@PathVariable("username") final String username,
      HttpEntity<String> httpEntity) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User account = null;
    if (username.equals(authentication.getName())) {
      account = accountService.getUser(authentication);
    } else {
      LOGGER.warn(
          "Invoking with sysadmin targeting another user - this can fail if the target user's name is not unique");
      account = accountService.getUser(username).orElse(null);
    }
    if (account == null) {
      return new ResponseEntity<>((UserDto) null, HttpStatus.NOT_FOUND);
    }
    account.setEmailAddress(httpEntity.getBody());
    accountService.updateUser(account);

    return new ResponseEntity<>(UserDto.fromUser(account), HttpStatus.OK);
  }

  @PutMapping("/rest/accounts")
  @PreAuthorize("hasAuthority('sysadmin') or #username == authentication.name")
  public ResponseEntity<UserDto> updateAccount(@RequestBody UserDto user) {
    User account = accountService.getUser(user);
    if (account == null) {
      return new ResponseEntity<>((UserDto) null, HttpStatus.NOT_FOUND);
    }
    account.setEmailAddress(user.getEmail());
    accountService.updateUser(account);

    return new ResponseEntity<>(UserDto.fromUser(account), HttpStatus.OK);
  }

  @DeleteMapping("/rest/accounts")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> deleteUserAccount(
      @ApiParam(value = "username", required = true) @RequestParam String username,
      @ApiParam(value = "authenticationProvider", required = true) @RequestParam String authenticationProvider
  ) {
    try {
      UserDto user = UserDto.of(username, authenticationProvider);
      userService.delete(user);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (DoesNotExistException dnee) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

  }
}
