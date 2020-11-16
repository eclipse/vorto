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

import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
   * Defines the legacy notation for Bosch OAuth providers. <br/>
   * Used to convert old-notation requests (e.g. coming from software integrating with Vorto), so
   * that values are automatically converted to the new notation during validation, thus supporting
   * edge-case backwards compatibility.
   */
  public static final Map<String[], String> LEGACY_BOSCH_OAUTH_IDS_CONVERSION = new HashMap<>();
  static {
    LEGACY_BOSCH_OAUTH_IDS_CONVERSION.put(new String[]{"BOSCH-ID", "BOSCH-IOT-SUITE-AUTH"}, "BOSCH");
  };

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
   * A subject can be null or empty, but if not, it has to be valid.
   *
   * @param subject
   * @throws InvalidUserException if the subject {@link String} is not matched by the pattern.
   */
  public void validateSubject(String subject) throws InvalidUserException {
    if (null == subject || subject.trim().isEmpty()) {
      return;
    }
    if (!subject.matches(AUTHENTICATION_SUBJECT_VALIDATION_PATTERN)) {
      throw new InvalidUserException(
          "Invalid subject for user. The subject should consist in a sequence of 4 or more alphanumeric characters.");
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
    ServiceValidationUtil.validateEmpties(authenticationProviderID);
    Set<String> supportedAuthenticationProviders = registry.list().stream()
        .map(IOAuthProvider::getId).collect(Collectors.toSet());
    if (!supportedAuthenticationProviders.contains(authenticationProviderID)) {
      throw new InvalidUserException(String
          .format("Invalid authentication provider ID for user.\nSupported values are: %s",
              supportedAuthenticationProviders));
    }
  }

  /**
   * Validates the given {@link User} object to ensure all properties are fit to consider it a valid
   * user representation.<br/>
   * Invoking this method implies the user is new and requires creating, therefore its id is not
   * validated.
   *
   * @param user
   * @throws InvalidUserException
   */
  public void validateNewUser(User user) throws InvalidUserException {
    // boilerplate null validation
    ServiceValidationUtil.validateNulls(user);
    ServiceValidationUtil.validateEmpties(user.getUsername(),
        user.getAuthenticationProviderId(), user.getSubject());
    validateSubject(user.getSubject());
    // converts the legacy-notation authentication provider ID to the new notation if necessary
    user.setAuthenticationProviderId(
        convertLegacyBoschOAuthProviderID(user.getAuthenticationProviderId())
    );
    validateAuthenticationProviderID(user.getAuthenticationProviderId());
  }

  /**
   * Validates the given {@link User} object to ensure all properties are fit to consider it a valid
   * user representation.<br/>
   * Invoking this method implies the user exists already, therefore its id is also  validated.
   *
   * @param user
   * @throws InvalidUserException
   */
  public void validateExistingUser(User user) throws InvalidUserException {
    validateNewUser(user);
    ServiceValidationUtil.validateEmpties(user.getId());
  }

  public void validateUsername(String username) throws InvalidUserException {
    if (Objects.isNull(username) || username.trim().isEmpty()) {
      throw new InvalidUserException("Username is empty.");
    }
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

  /**
   * Converts legacy-notation Bosch OAuth provider ID to new notation if necessary. <br/>
   * Used in user pre-validation.
   *
   * @param oauthID
   * @return
   */
  public static String convertLegacyBoschOAuthProviderID(String oauthID) {
    if (Strings.isNullOrEmpty(oauthID)) return oauthID;
    return Optional.ofNullable(LEGACY_BOSCH_OAUTH_IDS_CONVERSION.get(oauthID)).orElse(oauthID);
  }

}
