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
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;

/**
 * Utility class to build {@link User} objects.<br/>
 * Validates, but does not persist users on its own by default. <br/>
 * Can be initialized to not validate by using the appropriate constructor.
 */
public class UserBuilder {

  private UserUtil userUtil;

  protected User user = new User();

  /**
   * Provides a service to validate the {@link User} whilst building.
   *
   * @param userUtil
   */
  public UserBuilder(UserUtil userUtil) {
    this.userUtil = userUtil;
  }

  public UserBuilder() {
  }

  public UserBuilder withID(long id) {
    user.setId(id);
    return this;
  }

  public UserBuilder withName(String name) throws InvalidUserException {
    if (userUtil != null) {
      userUtil.validateUsername(name);
    }
    user.setUsername(name);
    return this;
  }

  public UserBuilder withAuthenticationProviderID(String authenticationProviderID)
      throws InvalidUserException {
    if (userUtil != null) {
      userUtil.validateAuthenticationProviderID(authenticationProviderID);
    }
    user.setAuthenticationProviderId(authenticationProviderID);
    return this;
  }

  public UserBuilder withAuthenticationSubject(String subject) throws InvalidUserException {
    if (userUtil != null) {
      userUtil.validateSubject(subject);
    }
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
