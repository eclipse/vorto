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

import java.util.Collection;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.domain.TenantUser;

public class TenantUserDto {
  
  private String username;
  private Collection<String> roles;
  
  public static TenantUserDto fromTenantUser(TenantUser user) {
    TenantUserDto tenantUser = new TenantUserDto();
    tenantUser.setUsername(user.getUser().getUsername());
    tenantUser.setRoles(user.getRoles().stream()
        .map(userRole -> "ROLE_" + userRole.getRole().toString())
        .collect(Collectors.toList()));
    return tenantUser;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Collection<String> getRoles() {
    return roles;
  }

  public void setRoles(Collection<String> roles) {
    this.roles = roles;
  }
}
