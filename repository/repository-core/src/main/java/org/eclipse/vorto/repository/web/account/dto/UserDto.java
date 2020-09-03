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
package org.eclipse.vorto.repository.web.account.dto;

import java.sql.Timestamp;
import java.time.Instant;
import org.eclipse.vorto.repository.domain.User;

public class UserDto {

  private String username;

  private Timestamp dateCreated;

  private Timestamp lastUpdated;

  private String email;

  private String authenticationProvider;

  private String subject;

  private boolean isTechnicalUser;

  /**
   * Converts a {@link User} entity to a payload for the UI. <br/>
   *
   * @param user
   * @param suppressSensitiveData only leaves the username, authentication provider, subject and technical user flag, removing the other properties from the DTO.
   * @return
   */
  public static UserDto fromUser(User user, boolean suppressSensitiveData) {
    if (!suppressSensitiveData) {
      return fromUser(user);
    } else {
      UserDto dto = new UserDto();
      dto.setAuthenticationProvider(user.getAuthenticationProviderId());
      dto.setSubject(user.getSubject());
      dto.setTechnicalUser(user.isTechnicalUser());
      dto.setUsername(user.getUsername());
      return dto;
    }
  }

  /**
   * Does not suppress any sensitive data from the converted DTO.
   *
   * @param user
   * @return
   * @see UserDto#fromUser(User, boolean)
   */
  public static UserDto fromUser(User user) {
    UserDto dto = new UserDto();
    dto.setAuthenticationProvider(user.getAuthenticationProviderId());
    dto.setDateCreated(user.getDateCreated());
    dto.setEmail(user.getEmailAddress());
    dto.setLastUpdated(user.getLastUpdated());
    dto.setSubject(user.getSubject());
    dto.setTechnicalUser(user.isTechnicalUser());
    dto.setUsername(user.getUsername());
    return dto;
  }


  public User toUser() {
    User user = new User();
    user.setAuthenticationProviderId(getAuthenticationProvider());
    user.setDateCreated(getDateCreated());
    user.setEmailAddress(getEmail());
    user.setLastUpdated(getLastUpdated());
    user.setSubject(getSubject());
    user.setTechnicalUser(isTechnicalUser());
    user.setUsername(getUsername());
    user.setAckOfTermsAndCondTimestamp(Timestamp.from(Instant.now()));
    return user;
  }

  private UserDto() {
  }

  public String getUsername() {
    return username;
  }

  public Timestamp getDateCreated() {
    return dateCreated;
  }

  public Timestamp getLastUpdated() {
    return lastUpdated;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setDateCreated(Timestamp dateCreated) {
    this.dateCreated = dateCreated;
  }

  public void setLastUpdated(Timestamp lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAuthenticationProvider() {
    return authenticationProvider;
  }

  public void setAuthenticationProvider(String authenticationProvider) {
    this.authenticationProvider = authenticationProvider;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public boolean isTechnicalUser() {
    return isTechnicalUser;
  }

  public void setTechnicalUser(boolean technicalUser) {
    isTechnicalUser = technicalUser;
  }

  public void setIsTechnicalUser(boolean value) {
    isTechnicalUser = value;
  }

}
