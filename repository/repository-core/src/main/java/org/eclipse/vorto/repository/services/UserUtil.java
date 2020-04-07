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

import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.text.normalizer.UBiDiProps;

/**
 * Provides various utility functionalities including validation for {@link org.eclipse.vorto.repository.domain.User}
 * objects.
 * TODO #2655 merge utility methods from User and UserUtils classes
 */
@Service
public class UserUtil {

  public static class UserBuilder {

    private User user = new User();

    public static UserBuilder newInstance() {
      return new UserBuilder();
    }

    public UserBuilder withID(long id) {
      user.setId(id);
      return this;
    }

    public UserBuilder withName(String name) {
      user.setUsername(name);
      return this;
    }

    public UserBuilder withAuthenticationProviderID(String authenticationProviderID) {
      user.setAuthenticationProviderId(authenticationProviderID);
      return this;
    }

    /**
     * Validation wraps the checked exception {@link InvalidUserException} into a runtime exception
     * here.
     * @param subject
     * @return
     */
    public UserBuilder withAuthenticationSubject(String subject) {
      try {
        validateSubject(subject);
        user.setSubject(subject);
      }
      catch (InvalidUserException iue) {
        throw new RuntimeException(iue);
      }
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
  public static void validateSubject(String subject) throws InvalidUserException {
    if (!AUTHENTICATION_SUBJECT_VALIDATION_PATTERN.matches(subject)) {
      throw new InvalidUserException("Invalid subject for technical user.");
    }
  }

  /**
   * Validates the given {@link User} object to ensure all properties are fit to consider it a valid
   * technical user representation.
   *
   * @param user
   * @throws InvalidUserException
   */
  public void validateTechnicalUser(User user) throws InvalidUserException {
    // boilerplate null validation
    validator.validateNulls(user);
    validator.validateEmpties(user.getId(), user.getSubject(), user.getUsername(),
        user.getAuthenticationProviderId());
    validateSubject(user.getSubject());
  }
}
