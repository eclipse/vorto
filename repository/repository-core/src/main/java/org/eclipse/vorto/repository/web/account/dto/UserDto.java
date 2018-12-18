/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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
import java.util.ArrayList;
import java.util.List;
import org.eclipse.vorto.repository.account.Role;
import org.eclipse.vorto.repository.account.User;
import org.eclipse.vorto.repository.account.UserRole;

public class UserDto {
  private String username;

  private List<Role> roles;

  private Timestamp dateCreated;

  private Timestamp lastUpdated;

  private String email;

  public static UserDto fromUser(User user) {
    UserDto dto = new UserDto(user.getUsername(), user.getDateCreated(), user.getLastUpdated());
    dto.addRoles(new ArrayList<>(user.getRoles()));
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

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  public void addRoles(List<UserRole> userRoles) {
    userRoles.forEach(e -> {
      if (roles == null) {
        roles = new ArrayList<>();
      }
      roles.add(e.getRole());
    });
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
