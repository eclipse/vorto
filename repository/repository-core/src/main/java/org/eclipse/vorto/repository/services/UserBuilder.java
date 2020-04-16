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

import java.sql.Timestamp;
import java.util.Objects;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;

/**
 * Utility class to build {@link User} objects.<br/>
 * Validates, but does not persist users on its own by default. <br/>
 * Can be initialized to not validate by using the appropriate constructor.
 */
public class UserBuilder {
  protected User user = new User();
  protected boolean validate = true;

  /**
   * Use to override the default flag to validate relevant properties of the {@link User}.
   *
   * @param validate
   */
  public UserBuilder(boolean validate) {
    this.validate = validate;
  }

  public UserBuilder() {
  }

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
    user.setAuthenticationProviderId(authenticationProviderID);
    return this;
  }

  public UserBuilder withAuthenticationSubject(String subject) throws InvalidUserException {
    user.setSubject(subject);
    return this;
  }

  public UserBuilder withEmailAddress(String email) {
    user.setEmailAddress(email);
    return this;
  }

  public UserBuilder setTechnicalUser(boolean technicalUser) {
    user.setTechnicalUser(technicalUser);
    return this;
  }

  /**
   * Does not validate the user fields, as they are eagerly validated by the {@literal with...}
   * methods.<br/>
   * Invokes the {@link User#setDateCreated(Timestamp)}, {@link User#setLastUpdated(Timestamp)}
   * and {@link User#setAckOfTermsAndCondTimestamp(Timestamp)} with a value of "now" before
   * returning the {@link User}.
   *
   * @return
   */
  public User build() {
    user.setDateCreated(new Timestamp(System.currentTimeMillis()));
    user.setLastUpdated(new Timestamp(System.currentTimeMillis()));
    user.setAckOfTermsAndCondTimestamp(new Timestamp(System.currentTimeMillis()));
    return user;
  }

}
