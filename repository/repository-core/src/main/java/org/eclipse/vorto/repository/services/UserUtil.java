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
package org.eclipse.vorto.repository.services;

import java.util.Objects;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.oauth.IOAuthProvider;
import org.eclipse.vorto.repository.oauth.IOAuthProviderRegistry;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides various utility functionalities including validation for {@link org.eclipse.vorto.repository.domain.User}
 * objects.<br/>
 * Validation is fail-fast, and occurs in setters throwing checked {@link InvalidUserException}.<br/>
 * The intended usage is to wrap the creation of a {@link User} around a {@code try} / {@code catch}
 * block intercepting that exception.
 * TODO #2655 merge utility methods from User and UserUtils classes
 */
@Service
public class UserUtil {

  /**
   * Utility class to build {@link User} objects.<br/>
   * Validates, but does not persist users on its own.
   */
  public class UserBuilder {

    private User user = new User();

    public UserBuilder withID(long id) {
      user.setId(id);
      return this;
    }

    public UserBuilder withName(String name) throws InvalidUserException {
      if (Objects.isNull(name) || name.trim().isEmpty()) {
        throw new InvalidUserException("Username is empty.");
      }
      user.setUsername(name);
      return this;
    }

    public UserBuilder withAuthenticationProviderID(String authenticationProviderID)
        throws InvalidUserException {
      validateAuthenticationProviderID(authenticationProviderID);
      user.setAuthenticationProviderId(authenticationProviderID);
      return this;
    }

    public UserBuilder withAuthenticationSubject(String subject) throws InvalidUserException {
      validateSubject(subject);
      user.setSubject(subject);
      return this;
    }

    public UserBuilder setTechnicalUser(boolean technicalUser) {
      user.setTechnicalUser(technicalUser);
      return this;
    }

    public User build() {
      return user;
    }

  }

  @Autowired
  private ServiceValidationUtil validator;

  @Autowired
  private IOAuthProviderRegistry registry;

  @Autowired
  private UserRepositoryRoleService userRepositoryRoleService;

  /**
   * Defines the minimum validation requirement for a subject string. <br/>
   * Set arbitrarily to 4+ alphanumeric characters for now. <br/>
   * This is and should be reflected in the front-end validation - see resource
   * {@literal createTechnicalUser.html}.
   */
  public static final String AUTHENTICATION_SUBJECT_VALIDATION_PATTERN = "^[a-zA-Z0-9]{4,}$";

  /**
   * Validates a {@link User}'s authentication subject by pattern
   * {@link UserUtil#AUTHENTICATION_SUBJECT_VALIDATION_PATTERN}.<br/>
   *
   * @param subject
   * @throws InvalidUserException if the subject {@link String} is not matched by the pattern.
   */
  public void validateSubject(String subject) throws InvalidUserException {
    validator.validateEmpties(subject);
    if (!AUTHENTICATION_SUBJECT_VALIDATION_PATTERN.matches(subject)) {
      throw new InvalidUserException("Invalid subject for user.");
    }
  }

  /**
   * Validates the given authentication provider ID by comparing it with the list of supported
   * {@link IOAuthProvider}s' IDs.
   *
   * @param authenticationProviderID
   * @throws InvalidUserException
   */
  public void validateAuthenticationProviderID(String authenticationProviderID)
      throws InvalidUserException {
    validator.validateEmpties(authenticationProviderID);
    if (!registry.list().stream()
        .map(
            IOAuthProvider::getId).collect(Collectors.toSet()).contains(authenticationProviderID)) {
      throw new InvalidUserException("Invalid authentication provider ID for user.");
    }
  }

  /**
   * Validates the given {@link User} object to ensure all properties are fit to consider it a valid
   * technical user representation.
   *
   * @param user
   * @throws InvalidUserException
   */
  public void validateUser(User user) throws InvalidUserException {
    // boilerplate null validation
    validator.validateNulls(user);
    validator.validateEmpties(user.getId(), user.getSubject(), user.getUsername(),
        user.getAuthenticationProviderId());
    validateSubject(user.getSubject());
    validateAuthenticationProviderID(user.getAuthenticationProviderId());
  }

  /**
   * Verifies whether the given acting {@link User} is the same user as the given target {@link User},
   * or whether they have the repository {@literal sysadmin} role.<br/>
   * Used when retrieving information about which {@link Namespace}s a {@link User} has visibility on.
   *
   * @param actor
   * @param target
   * @throws OperationForbiddenException if none of the conditions above applies.
   */
  public void authorizeActorAsTargetOrSysadmin(User actor, User target)
      throws OperationForbiddenException {
    if (!userRepositoryRoleService.isSysadmin(actor) && !actor.equals(target)) {
      throw new OperationForbiddenException(
          "Acting user is neither target user nor sysadmin - aborting operation");
    }
  }
}
