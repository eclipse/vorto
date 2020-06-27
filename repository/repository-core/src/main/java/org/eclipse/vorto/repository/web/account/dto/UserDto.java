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
import java.util.Map;
import java.util.Set;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.User;

public class UserDto {
  private String username;

  private Map<String, Set<Role>> roles;

  private Timestamp dateCreated;

  private Timestamp lastUpdated;

  private String email;

  public static UserDto fromUser(User user) {
    UserDto dto = new UserDto(user.getUsername(), user.getDateCreated(), user.getLastUpdated());
    dto.setRoles(user.getTenantRoles());
    dto.setEmail(user.getEmailAddress());
    return dto;
  }

  public UserDto() {
    super();
  }

  private UserDto(String username, Timestamp dateCreated, Timestamp lastUpdated) {
    this.username = username;
    this.dateCreated = dateCreated;
    this.lastUpdated = lastUpdated;
  }

  public Map<String, Set<Role>> getRoles() {
    return roles;
  }

  public void setRoles(Map<String, Set<Role>> roles) {
    this.roles = roles;
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


}
