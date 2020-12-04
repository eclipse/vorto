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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import org.eclipse.vorto.repository.domain.User;

public class UserDto {

  public static final UserDto ANONYMOUS = UserDto.of("anonymous", "n/a");
  public static final String UNAMBIGUOUS_USERNAME_FORMAT = "%s (%s)";

  private String username;

  private Timestamp dateCreated;

  private Timestamp lastUpdated;

  private String email;

  private String authenticationProvider;

  private String subject;

  private boolean isTechnicalUser;

  /**
   * Minimal {@link UserDto} with given name and OAuth provider ID.
   * @param username
   * @param authenticationProviderID
   * @return
   */
  public static UserDto of(String username, String authenticationProviderID) {
    UserDto result = new UserDto();
    result.setUsername(username);
    result.setAuthenticationProvider(authenticationProviderID);
    return result;
  }

  /**
   * Compares username and authentication provider ID with the given {@link User}'s.
   * @param user
   * @return
   */
  public boolean is(User user) {
    if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(authenticationProvider)) {
      return false;
    }
    if (Objects.isNull(user)) {
      return false;
    }
    if (
        Strings.isNullOrEmpty(user.getUsername()) ||
        Strings.isNullOrEmpty(user.getAuthenticationProviderId())
    ) {
      return false;
    }
    return
        username.equals(user.getUsername()) &&
        authenticationProvider.equals(user.getAuthenticationProviderId());
  }

  /**
   * Converts a {@link User} entity to a payload for the UI. <br/>
   * If the given {@link User} is {@code null}, returns a {@link UserDto} representing an
   * anonymous user.
   *
   * @param user
   * @param suppressSensitiveData only leaves the username, authentication provider, subject and technical user flag, removing the other properties from the DTO.
   * @return
   */
  public static UserDto fromUser(User user, boolean suppressSensitiveData) {
    if (Objects.isNull(user)) {
      return UserDto.ANONYMOUS;
    }
    UserDto dto = new UserDto();
    dto.setAuthenticationProvider(user.getAuthenticationProviderId());
    dto.setSubject(user.getSubject());
    dto.setTechnicalUser(user.isTechnicalUser());
    dto.setUsername(user.getUsername());
    if (!suppressSensitiveData) {
      dto.setDateCreated(user.getDateCreated());
      dto.setLastUpdated(user.getLastUpdated());
      dto.setEmail(user.getEmailAddress());
    }
    return dto;
  }

  /**
   * Does not suppress any sensitive data from the converted DTO.
   *
   * @param user
   * @return
   * @see UserDto#fromUser(User, boolean)
   */
  public static UserDto fromUser(User user) {
    return fromUser(user, false);
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

  public UserDto() {
  }

  public String getUsername() {
    return username;
  }

  /**
   * For broader compatibility with userId vs username in front-end script payloads
   * @return
   */
  public String getUserId() {
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

  public void setUserId(String userId) {
    setUsername(userId);
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

  /**
   * For broader compatibility with various non-homogeneous payloads
   * @return
   */
  public String getAuthenticationProviderId() {
    return authenticationProvider;
  }

  public void setAuthenticationProvider(String authenticationProvider) {
    this.authenticationProvider = authenticationProvider;
  }

  public void setAuthenticationProviderId(String authenticationProviderId) {
    this.authenticationProvider = authenticationProviderId;
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

  @JsonIgnore
  public String toUnambiguousUsernameFormat() {
    return String.format(UNAMBIGUOUS_USERNAME_FORMAT, username, authenticationProvider);
  }

  @JsonIgnore
  public boolean isBasicUserInfoValid() {
    return !Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(authenticationProvider);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserDto userDto = (UserDto) o;

    if (isTechnicalUser != userDto.isTechnicalUser) {
      return false;
    }
    if (!username.equals(userDto.username)) {
      return false;
    }
    if (dateCreated != null ? !dateCreated.equals(userDto.dateCreated)
        : userDto.dateCreated != null) {
      return false;
    }
    if (lastUpdated != null ? !lastUpdated.equals(userDto.lastUpdated)
        : userDto.lastUpdated != null) {
      return false;
    }
    if (email != null ? !email.equals(userDto.email) : userDto.email != null) {
      return false;
    }
    if (!authenticationProvider.equals(userDto.authenticationProvider)) {
      return false;
    }
    return subject != null ? subject.equals(userDto.subject) : userDto.subject == null;
  }

  @Override
  public int hashCode() {
    int result = username.hashCode();
    result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
    result = 31 * result + (lastUpdated != null ? lastUpdated.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + authenticationProvider.hashCode();
    result = 31 * result + (subject != null ? subject.hashCode() : 0);
    result = 31 * result + (isTechnicalUser ? 1 : 0);
    return result;
  }
}
